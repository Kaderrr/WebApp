package org.naklaken.app.daos;

import org.naklaken.app.restful.BookingApi;
import org.naklaken.app.databaseconnection.CustomDataSource;
import org.naklaken.app.restful.resources.Booking;
import org.naklaken.app.restful.resources.Transaction;
import org.naklaken.app.restful.resources.status.ReturnStatus;
import org.naklaken.app.restful.resources.status.StatusCode;
import org.naklaken.app.utilities.Constants;

import javax.sql.DataSource;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static org.naklaken.app.restful.resources.status.StatusCode.*;

public class TransactionDAO extends AbstractDaoClass {
    private static final String STATEMENT_GET_ALL = "SELECT * FROM naklaken.transactions ORDER BY datetime DESC";
    private static final String STATEMENT_GET_ONE = "SELECT * FROM naklaken.transactions WHERE id=?";
    private static final String STATEMENT_GET_BETWEEN_GIVEN_DATES = "SELECT * FROM naklaken.transactions WHERE datetime BETWEEN ? and ? ORDER BY datetime DESC"; // BETWEEN work in postgresql
    private static final String STATEMENT_GET_ALL_BOOKING_TRANSACTIONS = "SELECT * FROM naklaken.transactions WHERE booking_id=? ORDER BY datetime DESC";
    //HACK solveD error on userId and bookingId by using \" escape character
    private static final String STATEMENT_INSERT_ONE = "INSERT INTO naklaken.transactions (state, amount, description, user_id, booking_id, type, datetime) VALUES (?, ?, ?, ?, ?, ?::transaction_type, now()) RETURNING *";
    private static final String STATEMENT_DELETE_ONE = "UPDATE naklaken.transactions SET state=false WHERE id=? and state=true RETURNING *";
    private static final String STATEMENT_DELETE_LAST_BY_USER_ID = "UPDATE naklaken.transactions SET state=false WHERE id=(SELECT id FROM naklaken.transactions WHERE user_id=? ORDER BY id DESC LIMIT 1) and state=true RETURNING *";

    public TransactionDAO(Connection connection) {
        super(connection);
    }

    public ReturnStatus<Transaction> getOne(int id) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ONE)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Transaction transaction = transactionFromRow(resultSet);
                return ReturnStatus.ok(transaction);
            }
            error = TRANSACTION_ID_DOES_NOT_EXIST;
        } catch (SQLException e) {
            e.printStackTrace();
            error = QUERY_ERROR;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                error = CLOSE_CONNECTION_ERROR;
            }
        }
        return ReturnStatus.create(error, null);
    }

    public ReturnStatus<List<Transaction>> getAll() {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        List<Transaction> transactionList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = transactionFromRow(resultSet);
                transactionList.add(transaction);
            }
            return ReturnStatus.ok(transactionList);
        } catch (SQLException e) {
            e.printStackTrace();
            error = QUERY_ERROR;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                error = CLOSE_CONNECTION_ERROR;
            }
        }
        return ReturnStatus.create(error, null);
    }

    public ReturnStatus<List<Transaction>> getBetweenDates(String fromDate, String toDate) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        List<Transaction> transactionList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_BETWEEN_GIVEN_DATES)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            java.util.Date parsedDateStart = dateFormat.parse(fromDate);
            java.sql.Date sqlStartDate = new java.sql.Date(parsedDateStart.getTime());

            preparedStatement.setDate(1, sqlStartDate);

            java.util.Date parsedDateTo = dateFormat.parse(toDate);
            java.sql.Timestamp timestampToDate = new Timestamp(parsedDateTo.getTime());
            preparedStatement.setTimestamp(2, timestampToDate);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = transactionFromRow(resultSet);
                transactionList.add(transaction);
            }
            return ReturnStatus.ok(transactionList);
        } catch (SQLException e) {
            e.printStackTrace();
            error = QUERY_ERROR;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                error = CLOSE_CONNECTION_ERROR;
            }
        }
        return ReturnStatus.create(error, null);
    }

    public ReturnStatus<List<Transaction>> getAllBookingTransactions(int booking_id) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        List<Transaction> transactionList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ALL_BOOKING_TRANSACTIONS)) {
            preparedStatement.setInt(1, booking_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = transactionFromRow(resultSet);
                transactionList.add(transaction);
            }
            return ReturnStatus.ok(transactionList);
        } catch (SQLException e) {
            e.printStackTrace();
            error = QUERY_ERROR;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                error = CLOSE_CONNECTION_ERROR;
            }
        }
        return ReturnStatus.create(error, null);
    }

    public ReturnStatus<List<Transaction>> getAllGuestBookingTransactions(String guest_id) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            ReturnStatus<Booking> bookingReturnStatus = new BookingDAO(connection).getOneByGuestId(guest_id);
            Booking booking = bookingReturnStatus.data();
            List<Transaction> transactionList = new ArrayList<>();

            Connection connection2 = dataSource.getConnection();
            try (PreparedStatement preparedStatement = connection2.prepareStatement(STATEMENT_GET_ALL_BOOKING_TRANSACTIONS)) {
                preparedStatement.setInt(1, booking.id());
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Transaction transaction = transactionFromRow(resultSet);

                    transactionList.add(transaction);
                }
                return ReturnStatus.ok(transactionList);
            } catch (SQLException e) {
                e.printStackTrace();
                error = QUERY_ERROR;
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    error = CLOSE_CONNECTION_ERROR;
                }
            }
            return ReturnStatus.create(error, null);


        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    public ReturnStatus<Transaction> insertOne(Transaction transaction) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        ReturnStatus<Booking> bookingReturnStatus = new BookingApi().getBooking(transaction.booking_id());
        if (!bookingReturnStatus.status()) {
            return bookingReturnStatus.changeDataAndType(null);
        }
        Booking booking = bookingReturnStatus.data();
        if (transaction.type().equalsIgnoreCase("payment") && booking.balance() < transaction.amount()) {
            return ReturnStatus.create(INSERT_TRANSACTION_FAILED_LOW_BALANCE, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_INSERT_ONE)) {
            preparedStatement.setBoolean(1, transaction.state());
            preparedStatement.setDouble(2, transaction.amount());
            preparedStatement.setString(3, transaction.description());
            preparedStatement.setString(4, transaction.user_id());
            preparedStatement.setInt(5, transaction.booking_id());
            preparedStatement.setObject(6, transaction.type().toLowerCase(Locale.ROOT)); //Locale.ROOT
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Transaction returnTransaction = transactionFromRow(resultSet);
                double newBalance = booking.balance();
                if (transaction.state()) {
                    switch (transaction.type().toLowerCase()) {
                        case "payment" -> newBalance -= transaction.amount();
                        case "deposit" -> newBalance += transaction.amount();
                    }
                }
                ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
                DataSource dataSource = dataSourceReturnStatus.data();
                Connection connection = dataSource.getConnection();
                ReturnStatus<Booking> bookingUpdateBalance = new BookingDAO(connection).updateBalance(booking.id(), newBalance);
                if (!bookingUpdateBalance.status() || bookingUpdateBalance.data().balance() != newBalance) {
                    return bookingReturnStatus.changeDataAndType(null);
                }
                return ReturnStatus.ok(returnTransaction);
            }
            error = INSERT_TRANSACTION_DID_NOT_RETURN;
        } catch (SQLException e) {
            e.printStackTrace();
            error = QUERY_ERROR;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                error = CLOSE_CONNECTION_ERROR;
            }
        }
        return ReturnStatus.create(error, null);
    }

    public ReturnStatus<Transaction> deleteOne(Integer id) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_DELETE_ONE)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Transaction transaction = transactionFromRow(resultSet);
                ReturnStatus<Booking> bookingReturnStatus = new BookingApi().getBooking(transaction.booking_id());
                if (!bookingReturnStatus.status()) {
                    return bookingReturnStatus.changeDataAndType(null);
                }
                Booking booking = bookingReturnStatus.data();
                double newBalance = booking.balance();
                if (!transaction.state()) {
                    switch (transaction.type().toLowerCase()) {
                        case "payment" -> newBalance += transaction.amount();
                        case "deposit" -> newBalance -= transaction.amount();
                    }
                }
                ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
                DataSource dataSource = dataSourceReturnStatus.data();
                Connection connection = dataSource.getConnection();
                ReturnStatus<Booking> bookingUpdateBalance = new BookingDAO(connection).updateBalance(booking.id(), newBalance);
                if (!bookingUpdateBalance.status() || bookingUpdateBalance.data().balance() != newBalance) {
                    return bookingReturnStatus.changeDataAndType(null);
                }
                return ReturnStatus.ok(transaction);
            }
            error = DELETE_TRANSACTION_DID_NOT_RETURN;
        } catch (SQLException e) {
            e.printStackTrace();
            error = QUERY_ERROR;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                error = CLOSE_CONNECTION_ERROR;
            }
        }
        return ReturnStatus.create(error, null);
    }

    public ReturnStatus<Transaction> deleteLastByUserId(String user_id) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_DELETE_LAST_BY_USER_ID)) {
            preparedStatement.setString(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Transaction transaction = transactionFromRow(resultSet);
                ReturnStatus<Booking> bookingReturnStatus = new BookingApi().getBooking(transaction.booking_id());
                if (!bookingReturnStatus.status()) {
                    return bookingReturnStatus.changeDataAndType(null);
                }
                Booking booking = bookingReturnStatus.data();
                double newBalance = booking.balance();
                if (!transaction.state()) {
                    switch (transaction.type().toLowerCase()) {
                        case "payment" -> newBalance += transaction.amount();
                        case "deposit" -> newBalance -= transaction.amount();
                    }
                }
                ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
                DataSource dataSource = dataSourceReturnStatus.data();
                Connection connection = dataSource.getConnection();
                ReturnStatus<Booking> bookingUpdateBalance = new BookingDAO(connection).updateBalance(booking.id(), newBalance);
                if (!bookingUpdateBalance.status() || bookingUpdateBalance.data().balance() != newBalance) {
                    return bookingReturnStatus.changeDataAndType(null);
                }
                return ReturnStatus.ok(transaction);
            }
            error = DELETE_TRANSACTION_DID_NOT_RETURN;
        } catch (SQLException e) {
            e.printStackTrace();
            error = QUERY_ERROR;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                error = CLOSE_CONNECTION_ERROR;
            }
        }
        return ReturnStatus.create(error, null);
    }

    private Transaction transactionFromRow(ResultSet resultSet) throws SQLException {
        int transactionId = resultSet.getInt("id");
        String transactionType = resultSet.getString("type");
        boolean transactionState = resultSet.getBoolean("state");
        double transactionAmount = resultSet.getDouble("amount");
        String transactionDescription = resultSet.getString("description");
        java.util.Date datetime = resultSet.getTimestamp("datetime");
        String transactionDatetime = datetime.toString();
        String transactionUser_id = resultSet.getString("user_id");
        int transactionBooking_id = resultSet.getInt("booking_id");
        return new Transaction(transactionId, transactionState, transactionAmount, transactionDescription, transactionDatetime, transactionType, transactionUser_id, transactionBooking_id);
    }
}
