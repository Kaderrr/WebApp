package org.naklaken.app.daos;

import org.naklaken.app.databaseconnection.CustomDataSource;
import org.naklaken.app.restful.resources.Match;
import org.naklaken.app.restful.resources.status.ReturnStatus;
import org.naklaken.app.restful.resources.status.StatusCode;
import org.naklaken.app.utilities.Constants;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.naklaken.app.restful.resources.status.StatusCode.*;

public class MatchDAO extends AbstractDaoClass {

    private static final String STATEMENT_GET_ALL = "SELECT * FROM naklaken.match ORDER BY datetime DESC";
    private static final String STATEMENT_GET_ONE = "SELECT * FROM naklaken.match WHERE id=?";
    private static final String STATEMENT_GET_ALL_OF_BOOKING = "SELECT * FROM naklaken.match WHERE booking_id = ? ORDER BY datetime DESC";
    private static final String STATEMENT_GET_ALL_VALID_OF_BOOKING = "SELECT * FROM naklaken.match WHERE booking_id = ? AND status = true ORDER BY datetime DESC";
    private static final String STATEMENT_GET_BOOKING_FROM_RFID = "SELECT * FROM naklaken.match WHERE code_rfid = ? AND status = true ORDER BY datetime DESC LIMIT 1";
    private static final String STATEMENT_INSERT_ONE = "INSERT INTO naklaken.match (status, \"booking_id\", \"code_rfid\", datetime) VALUES (?, ?, ?, now()) RETURNING *";
    private static final String STATEMENT_DELETE_ONE = "UPDATE naklaken.match SET status=false WHERE id=? RETURNING *";


    public MatchDAO(Connection connection) {
        super(connection);
    }

    public static Match matchFromRow(ResultSet resultSet) throws SQLException {
        int matchId = resultSet.getInt("id");
        boolean matchStatus = resultSet.getBoolean("status");
        java.util.Date datetime = resultSet.getTimestamp("datetime");
        String matchDatetime = datetime.toString();
        int matchBookingId = resultSet.getInt("booking_id");
        String matchCodeRfid = resultSet.getString("code_rfid");
        return new Match(matchId, matchStatus, matchDatetime, matchBookingId, matchCodeRfid);
    }

    public ReturnStatus<Match> getOne(Integer id) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ONE)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Match match = matchFromRow(resultSet);
                return ReturnStatus.ok(match);
            }
            error = MATCH_ID_DOES_NOT_EXIST;
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

    public ReturnStatus<List<Match>> getAll() {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        List<Match> matchList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Match match = matchFromRow(resultSet);
                matchList.add(match);
            }
            return ReturnStatus.ok(matchList);
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

    public ReturnStatus<List<Match>> getAllOfBooking(Integer booking_id) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        List<Match> matchList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ALL_OF_BOOKING)) {
            preparedStatement.setInt(1, booking_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Match match = matchFromRow(resultSet);
                matchList.add(match);
            }
            return ReturnStatus.ok(matchList);
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

    public ReturnStatus<List<Match>> getAllValidOfBooking(Integer booking_id) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        List<Match> matchList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ALL_VALID_OF_BOOKING)) {
            preparedStatement.setInt(1, booking_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Match match = matchFromRow(resultSet);
                matchList.add(match);
            }
            //connection.close();
            return ReturnStatus.ok(matchList);
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

    public ReturnStatus<Match> getLastBookingIdFromRfid(String code_rfid) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        List<Match> matchList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_BOOKING_FROM_RFID)) {
            preparedStatement.setString(1, code_rfid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Match match = matchFromRow(resultSet);
                return ReturnStatus.ok(match);
            }
            error = MATCH_ID_DOES_NOT_EXIST;
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

    public ReturnStatus<Match> insertOne(Match match) throws SQLException {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        DataSource dataSource = dataSourceReturnStatus.data();
        StatusCode error;
        // UPDATE TO FALSE ALL MATCHES OF THAT BOOKING_ID
        ReturnStatus<List<Match>> returnMatchesToUpdate = getAllValidOfBooking(match.booking_id());
        if (!returnMatchesToUpdate.status()) {
            return ReturnStatus.create(INSERT_MATCH_ERROR_ON_GET_VALID_MATCHES, null);
        }
        List<Match> matchesToUpdate = returnMatchesToUpdate.data();
        for (Match match1 : matchesToUpdate) {
            if (match1.status()) { // can be removed since I already take only the valid ones
                Connection connection2 = dataSource.getConnection();
                try (PreparedStatement preparedStatement = connection2.prepareStatement(STATEMENT_DELETE_ONE)) {
                    preparedStatement.setInt(1, match1.id());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        Match match2 = matchFromRow(resultSet);
                    }
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
            }
        }
        // All matched with status true have been updated to status false.
        Connection connection3 = dataSource.getConnection();
        try (PreparedStatement preparedStatement = connection3.prepareStatement(STATEMENT_INSERT_ONE)) {
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, match.booking_id());
            preparedStatement.setString(3, match.code_rfid());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Match returnMatch = matchFromRow(resultSet);
                return ReturnStatus.ok(returnMatch);
            }
            error = INSERT_DID_NOT_RETURN;
        } catch (SQLException e) {
            e.printStackTrace();
            error = QUERY_ERROR_INSERT_MATCH;
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

    public ReturnStatus<Match> deleteOne(Integer id) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_DELETE_ONE)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Match returnMatch = matchFromRow(resultSet);
                return ReturnStatus.ok(returnMatch);
            }
            error = DELETE_MATCH_DID_NOT_RETURN;
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
}
