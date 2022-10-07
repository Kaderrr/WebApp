package org.naklaken.app.restful;

import org.naklaken.app.daos.GuestLoginDAO;
import org.naklaken.app.databaseconnection.CustomDataSource;
import org.naklaken.app.restful.resources.CodeSurname;
import org.naklaken.app.restful.resources.Guest;
import org.naklaken.app.restful.resources.status.ReturnStatus;
import org.naklaken.app.restful.resources.status.StatusCode;
import org.naklaken.app.utilities.GuestSessionManagement;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/auth/guest")
public class GuestLoginApi {


    @Context
    private HttpServletRequest request;

    @Path("/login")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public ReturnStatus<Guest> loginGuest(CodeSurname codeSurname) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource("jdbc/naklaken_db_test");
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try (Connection connection = dataSource.getConnection()) {
            ReturnStatus<Guest> returnStatus = new GuestLoginDAO(connection).checkCredentials(codeSurname.code(), codeSurname.surname());
            if (returnStatus.status()) {
                GuestSessionManagement.createSession(request, returnStatus.data());
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
    public ReturnStatus<Object> logoutGuest() {
        if (GuestSessionManagement.destroySession(request)) {
            return ReturnStatus.ok(null);
        }
        return ReturnStatus.create(StatusCode.NOT_LOGGED_IN, null);
    }


}
