package org.naklaken.app.daos;

import org.naklaken.app.restful.resources.Booking;
import org.naklaken.app.restful.resources.status.ReturnStatus;
import org.naklaken.app.restful.resources.status.StatusCode;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.naklaken.app.restful.resources.status.StatusCode.*;

public class BookingDAO extends AbstractDaoClass {
    private static final String STATEMENT_GET_ALL = "SELECT * FROM naklaken.booking ORDER BY check_in DESC";
    private static final String STATEMENT_GET_ONE = "SELECT * FROM naklaken.booking WHERE id=?";
    private static final String STATEMENT_DELETE_ONE = "DELETE FROM naklaken.booking WHERE id=? RETURNING *";
    private static final String STATEMENT_GET_ONE_BY_GUEST_ID = "SELECT * FROM naklaken.booking WHERE \"guest_id\"=? AND now() BETWEEN check_in and check_out ORDER BY id DESC LIMIT 1";
    private static final String STATEMENT_INSERT_ONE = "INSERT INTO naklaken.booking (check_in, check_out, room, balance,\"guest_id\",\"user_id\") VALUES (?,?,?,?,?,?) RETURNING *";
    private static final String STATEMENT_UPDATE_BALANCE = "UPDATE naklaken.booking SET balance=? WHERE id=? RETURNING *";
    private static final String STATEMENT_GET_BETWEEN_GIVEN_DATES = "SELECT * FROM naklaken.booking WHERE check_in >= ? AND check_out <= ? ORDER BY check_in DESC";

    public BookingDAO(Connection connection) {
        super(connection);
    }

    public ReturnStatus<Booking>  getOneByGuestId(String guest_id) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ONE_BY_GUEST_ID)) {
            preparedStatement.setString(1, guest_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Booking booking = bookingFromRow(resultSet);
                return ReturnStatus.ok(booking);
            }
            error = BOOKING_ID_DOES_NOT_EXIST;
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

    public ReturnStatus<Booking> getOne(int id) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ONE)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Booking booking = bookingFromRow(resultSet);
                return ReturnStatus.ok(booking);
            }
            error = BOOKING_ID_DOES_NOT_EXIST;
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

    public ReturnStatus<List<Booking>> getAll() {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        List<Booking> bookingList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Booking booking = bookingFromRow(resultSet);
                bookingList.add(booking);
            }
            return ReturnStatus.ok(bookingList);
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

    public ReturnStatus<Booking> insertOne(Booking booking) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_INSERT_ONE)) {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(booking.check_in());
            Timestamp timestamp_checkin = new Timestamp(date.getTime());
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(booking.check_out());
            Timestamp timestamp_checkout = new Timestamp(date.getTime());
            if (timestamp_checkin.compareTo(timestamp_checkout) >= 0) {
                return ReturnStatus.create(VALUE_ERROR, null);
            }
            preparedStatement.setTimestamp(1, timestamp_checkin);
            preparedStatement.setTimestamp(2, timestamp_checkout);
            preparedStatement.setString(3, booking.room());
            preparedStatement.setDouble(4, booking.balance());
            preparedStatement.setString(5, booking.guest_id());
            preparedStatement.setString(6, booking.user_id());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Booking returnBooking = bookingFromRow(resultSet);
                return ReturnStatus.ok(returnBooking);
            }
            error = INSERT_DID_NOT_RETURN;
        } catch (SQLException e) {
            e.printStackTrace();
            error = QUERY_ERROR;
        } catch (ParseException e) {
            e.printStackTrace();
            error = VALUE_ERROR;
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

    public ReturnStatus<Boolean> deleteOne(int id) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_DELETE_ONE)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ReturnStatus.ok(true);
            }
            error = BOOKING_ID_DOES_NOT_EXIST;
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

    public ReturnStatus<Booking> updateBalance(Integer id, double balance) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_UPDATE_BALANCE)) {
            preparedStatement.setDouble(1, balance);
            preparedStatement.setInt(2, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Booking booking = bookingFromRow(resultSet);
                return ReturnStatus.ok(booking);
            }
            error = UPDATE_BALANCE_DID_NOT_RETURN;
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

    public ReturnStatus<List<Booking>> getBetweenDates(String fromDate, String toDate) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        List<Booking> bookingList = new ArrayList<>();
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
                Booking booking = bookingFromRow(resultSet);
                bookingList.add(booking);
            }
            return ReturnStatus.ok(bookingList);
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

    public static Booking bookingFromRow(ResultSet resultSet) throws SQLException {
        int bookingId = resultSet.getInt("id");
        String check_in = resultSet.getString("check_in");
        String check_out = resultSet.getString("check_out");
        String room = resultSet.getString("room");
        double balance = resultSet.getDouble("balance");
        String guestId = resultSet.getString("guest_id");
        String userId = resultSet.getString("user_id");

        return new Booking(bookingId, check_in, check_out, room, balance, guestId, userId);
    }
}
