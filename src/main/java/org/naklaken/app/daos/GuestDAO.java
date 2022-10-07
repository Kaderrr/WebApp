package org.naklaken.app.daos;

import org.naklaken.app.restful.resources.Guest;
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

public class GuestDAO extends AbstractDaoClass {

    private static final String STATEMENT_GET_ALL = "SELECT * FROM naklaken.guest";
    private static final String STATEMENT_GET_ONE = "SELECT * FROM naklaken.guest WHERE id=?";
    private static final String STATEMENT_DELETE_ONE = "DELETE FROM naklaken.guest WHERE id=? RETURNING *";
    private static final String STATEMENT_INSERT_ONE = "INSERT INTO naklaken.guest (id, name, surname, type) VALUES (?, ?, ?, ?::guest_type) RETURNING *";
    private static final String STATEMENT_GET_ALL_ORDERED = "SELECT * FROM naklaken.guest ORDER BY surname";


    public GuestDAO(Connection connection) {
        super(connection);
    }

    public ReturnStatus<Guest> getOne(String id) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ONE)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Guest guest = guestFromRow(resultSet);
                return ReturnStatus.ok(guest);
            }
            error = GUEST_ID_DOES_NOT_EXIST;
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

    public ReturnStatus<List<Guest>> getAll() {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        List<Guest> guestList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Guest guest = guestFromRow(resultSet);
                guestList.add(guest);
            }
            return ReturnStatus.ok(guestList);
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

    public ReturnStatus<List<Guest>> getAllOrderedBySurname() {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        List<Guest> guestList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ALL_ORDERED)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Guest guest = guestFromRow(resultSet);
                guestList.add(guest);
            }
            return ReturnStatus.ok(guestList);
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

    public ReturnStatus<Guest> insertOne(Guest guest) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_INSERT_ONE)) {
            preparedStatement.setString(1, guest.id());
            preparedStatement.setString(2, guest.name());
            preparedStatement.setString(3, guest.surname());
            preparedStatement.setObject(4, guest.type().toLowerCase(Locale.ROOT));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Guest returnGuest = guestFromRow(resultSet);
                return ReturnStatus.ok(returnGuest);
            }
            error = INSERT_DID_NOT_RETURN;
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

    public static Guest guestFromRow(ResultSet resultSet) throws SQLException {
        String guestId = resultSet.getString("id");
        String guestName = resultSet.getString("name");
        String guestSurname = resultSet.getString("surname");
        String guestType = resultSet.getString("type");
        return new Guest(guestId, guestName, guestSurname, guestType);
    }


    public ReturnStatus<Boolean> deleteGuest(String id) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_DELETE_ONE)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ReturnStatus.ok(true);
            }
            error = GUEST_ID_DOES_NOT_EXIST;
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
