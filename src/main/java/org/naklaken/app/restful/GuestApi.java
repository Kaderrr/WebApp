package org.naklaken.app.restful;

import org.naklaken.app.daos.GuestDAO;
import org.naklaken.app.databaseconnection.CustomDataSource;
import org.naklaken.app.restful.filters.GuestAccount;
import org.naklaken.app.restful.filters.UserReceptionist;
import org.naklaken.app.restful.resources.Guest;
import org.naklaken.app.restful.resources.status.ReturnStatus;
import org.naklaken.app.restful.resources.status.StatusCode;
import org.naklaken.app.utilities.Constants;

import javax.sql.DataSource;
import javax.ws.rs.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@Path("/guest")
public class GuestApi {

    @Path("/{id}")
    @GET
    @Produces("application/json")
    @GuestAccount
    public ReturnStatus<Guest> getGuest(@PathParam("id") String id) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new GuestDAO(connection).getOne(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/all")
    @GET
    @Produces("application/json")
    @UserReceptionist
    public ReturnStatus<List<Guest>> getAllGuests() {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new GuestDAO(connection).getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/all-ordered-by-surname")
    @GET
    @Produces("application/json")
    @UserReceptionist
    public ReturnStatus<List<Guest>> getAllGuestsOrderedBySurname() {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new GuestDAO(connection).getAllOrderedBySurname();
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/insert-one")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @UserReceptionist
    public ReturnStatus<Guest> insertOneApplicationJson(Guest guest) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try (Connection connection = dataSource.getConnection()) {
            return new GuestDAO(connection).insertOne(guest);
        } catch (SQLException e) {
            Logger.getAnonymousLogger().info("INFO dddddddd " + e.getErrorCode());
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/insert-one")
    @POST
    @Consumes("text/json")
    @Produces("application/json")
    @UserReceptionist
    public ReturnStatus<Guest> insertOneTextJson(Guest guest) {
        return insertOneApplicationJson(guest);
    }

    @Path("/delete/{id}")
    @DELETE
    @Produces("application/json")
    @UserReceptionist
    public ReturnStatus<Boolean> deleteGuest(@PathParam("id") String id) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource("jdbc/naklaken_db_test");
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new GuestDAO(connection).deleteGuest(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }


}
