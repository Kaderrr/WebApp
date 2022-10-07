package org.naklaken.app.daos;

import org.naklaken.app.restful.resources.Rfid;
import org.naklaken.app.restful.resources.status.ReturnStatus;
import org.naklaken.app.restful.resources.status.StatusCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.naklaken.app.restful.resources.status.StatusCode.*;

public class RfidDAO extends AbstractDaoClass {

    private static final String STATEMENT_GET_ALL = "SELECT * FROM naklaken.rfid";

    private static final String STATEMENT_GET_ALL_UNUSED = "SELECT DISTINCT rfid.* FROM naklaken.rfid WHERE rfid.status = 'valid' AND rfid.code NOT IN (SELECT match.code_rfid FROM match JOIN naklaken.booking b ON b.id = match.booking_id WHERE now() BETWEEN b.check_in AND b.check_out and match.status=true)";
    private static final String STATEMENT_GET_ONE = "SELECT * FROM naklaken.rfid WHERE code=?";
    private static final String STATEMENT_INSERT_ONE = "INSERT INTO naklaken.rfid (code, type, status) VALUES (?, ?::rfid_type, ?::rfid_status) RETURNING *";
    private static final String STATEMENT_DELETE_ONE = "DELETE FROM naklaken.rfid WHERE code=? RETURNING *";


    public RfidDAO(Connection connection) {
        super(connection);
    }

    public ReturnStatus<Rfid> getOne(String code) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ONE)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Rfid rfid = rfidFromRow(resultSet);
                return ReturnStatus.ok(rfid);
            }
            error = RFID_CODE_DOES_NOT_EXIST;
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

    public ReturnStatus<List<Rfid>> getAll() {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        List<Rfid> rfidList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Rfid rfid = rfidFromRow(resultSet);
                rfidList.add(rfid);
            }
            return ReturnStatus.ok(rfidList);
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

    public ReturnStatus<List<Rfid>> getAllUnused() {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        List<Rfid> rfidList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ALL_UNUSED)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Rfid rfid = rfidFromRow(resultSet);
                rfidList.add(rfid);
            }
            return ReturnStatus.ok(rfidList);
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

    public ReturnStatus<Rfid> insertOne(Rfid rfid) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_INSERT_ONE)) {
            preparedStatement.setString(1, rfid.code());
            preparedStatement.setObject(2, rfid.type().toLowerCase(Locale.ROOT));
            preparedStatement.setString(3, rfid.status().toLowerCase(Locale.ROOT));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Rfid returnRfid = rfidFromRow(resultSet);
                return ReturnStatus.ok(returnRfid);
            }
            error = INSERT_RFID_DID_NOT_RETURN;
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

    private Rfid rfidFromRow(ResultSet resultSet) throws SQLException {
        String rfidCode = resultSet.getString("code");
        String rfidType = resultSet.getString("type");
        String rfidStatus = resultSet.getString("status");
        return new Rfid(rfidCode, rfidType, rfidStatus);
    }

    public ReturnStatus<Boolean> deleteRfid(String code) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_DELETE_ONE)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ReturnStatus.ok(true);
            }
            error = RFID_CODE_DOES_NOT_EXIST;
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

