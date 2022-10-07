package org.naklaken.app.daos;

import org.naklaken.app.restful.resources.User;
import org.naklaken.app.restful.resources.status.ReturnStatus;
import org.naklaken.app.restful.resources.status.StatusCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

import static org.naklaken.app.restful.resources.status.StatusCode.*;

public class UserDAO extends AbstractDaoClass {
    private static final String STATEMENT_GET_ALL = "SELECT * FROM naklaken.user";
    private static final String STATEMENT_GET_BY_ID = "SELECT * FROM naklaken.user WHERE id=?";
    private static final String STATEMENT_INSERT_ONE = "INSERT INTO naklaken.user (id, email, password,active,permission_level) VALUES (?, ?, ?,?, ?::permission_level) RETURNING *";
    private static final String STATEMENT_DELETE_ONE = "DELETE FROM naklaken.user WHERE id=? RETURNING *";


    public UserDAO(Connection connection) {
        super(connection);
    }

    public ReturnStatus<User> getById(String id) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_BY_ID)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = rowToUser(resultSet);
                return ReturnStatus.ok(user);
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

    public ReturnStatus<List<User>> getAll() {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        List<User> userList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = rowToUser(resultSet);
                userList.add(user);
            }
            return ReturnStatus.ok(userList);
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

    public ReturnStatus<User> insertOne(User user) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_INSERT_ONE)) {
            preparedStatement.setString(1, user.id());
            preparedStatement.setString(2, user.email());
            // encode given password
            String originalPassword = user.password();
            Base64.Encoder encoder = Base64.getEncoder();
            String encodedPassword = encoder.encodeToString(originalPassword.getBytes());

            preparedStatement.setString(3, encodedPassword);
            preparedStatement.setObject(4, user.isActive());
            preparedStatement.setObject(5, user.permission_level().toLowerCase(Locale.ROOT));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User returnUser = rowToUser(resultSet);
                return ReturnStatus.ok(returnUser);
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

    public static User rowToUser(ResultSet resultSet) throws SQLException {
        String userId = resultSet.getString("id");
        String userEmail = resultSet.getString("email");
        String userPassword = resultSet.getString("password");
        boolean isUserActive = resultSet.getBoolean("active");
        String userPermissionLevel = resultSet.getString("permission_level");
        return new User(userId, userEmail, userPassword, isUserActive, userPermissionLevel);
    }

    public ReturnStatus<Boolean> deleteUser(String id) {
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
            error = USER_ID_DOES_NOT_EXIST;
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
