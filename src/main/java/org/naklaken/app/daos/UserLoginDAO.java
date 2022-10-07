package org.naklaken.app.daos;

import org.naklaken.app.restful.resources.User;
import org.naklaken.app.restful.resources.status.ReturnStatus;
import org.naklaken.app.restful.resources.status.StatusCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import static org.naklaken.app.restful.resources.status.StatusCode.*;

public class UserLoginDAO extends AbstractDaoClass {

    private static final String STATEMENT_GET_USER_FROM_EMAIL_PASSWORD = "SELECT * FROM naklaken.user WHERE email=? AND password=? AND active=true";

    public UserLoginDAO(Connection connection) {
        super(connection);
    }

    public ReturnStatus<User> checkCredentials(String email, String password) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try (PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_USER_FROM_EMAIL_PASSWORD)) {
            preparedStatement.setString(1, email);
            Base64.Encoder encoder = Base64.getEncoder();
            String encodedPassword = encoder.encodeToString(password.getBytes());
            preparedStatement.setString(2, encodedPassword);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = UserDAO.rowToUser(resultSet);
                return ReturnStatus.ok(user);
            }
            error = WRONG_CREDENTIALS;
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
