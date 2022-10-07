package org.naklaken.app.restful;

import org.naklaken.app.daos.UserDAO;
import org.naklaken.app.databaseconnection.CustomDataSource;
import org.naklaken.app.restful.filters.UserReceptionist;
import org.naklaken.app.restful.filters.UserServiceProvider;
import org.naklaken.app.restful.resources.User;
import org.naklaken.app.restful.filters.UserAdmin;
import org.naklaken.app.restful.resources.status.ReturnStatus;
import org.naklaken.app.restful.resources.status.StatusCode;
import org.naklaken.app.utilities.Constants;

import javax.sql.DataSource;
import javax.ws.rs.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/user")
public class UserApi {
    @Path("/{id}")
    @GET
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<User> getOneUser(@PathParam("id") String id) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new UserDAO(connection).getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/all")
    @GET
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<List<User>> getAllUsers() {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new UserDAO(connection).getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/insert-one")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @UserAdmin
    public ReturnStatus<User> insertOneApplicationJson(User user) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try (Connection connection = dataSource.getConnection()) {
            return new UserDAO(connection).insertOne(user);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/insert-one")
    @POST
    @Consumes("text/json")
    @Produces("application/json")
    @UserAdmin
    public ReturnStatus<User> insertOneTextJson(User user) {
        return insertOneApplicationJson(user);
    }

    @Path("/delete/{id}")
    @DELETE
    @Produces("application/json")
    @UserAdmin
    public ReturnStatus<Boolean> deleteUser(@PathParam("id") String id) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource("jdbc/naklaken_db_test");
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new UserDAO(connection).deleteUser(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }
}
