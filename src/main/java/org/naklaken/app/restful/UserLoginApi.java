package org.naklaken.app.restful;

import org.naklaken.app.daos.UserLoginDAO;
import org.naklaken.app.databaseconnection.CustomDataSource;
import org.naklaken.app.restful.resources.EmailPassword;
import org.naklaken.app.restful.resources.User;
import org.naklaken.app.restful.resources.status.ReturnStatus;
import org.naklaken.app.restful.resources.status.StatusCode;
import org.naklaken.app.utilities.UserSessionManagement;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/auth/user")
public class UserLoginApi {


    @Context
    private HttpServletRequest request;

    @Path("/login")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public ReturnStatus<User> loginUser(EmailPassword emailPassword) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource("jdbc/naklaken_db_test");
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try (Connection connection = dataSource.getConnection()) {
            ReturnStatus<User> returnStatus = new UserLoginDAO(connection).checkCredentials(emailPassword.email(), emailPassword.password());
            if (returnStatus.status()) {
                UserSessionManagement.createSession(request, returnStatus.data());
            }
            return returnStatus;
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/logout")
    @POST
    @Produces("application/json")
    public ReturnStatus<Object> logoutUser() {
        if (UserSessionManagement.destroySession(request)) {
            return ReturnStatus.ok(null);
        }
        return ReturnStatus.create(StatusCode.NOT_LOGGED_IN, null);
    }


}
