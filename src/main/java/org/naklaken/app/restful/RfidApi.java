package org.naklaken.app.restful;

import org.naklaken.app.daos.GuestDAO;
import org.naklaken.app.daos.RfidDAO;
import org.naklaken.app.databaseconnection.CustomDataSource;
import org.naklaken.app.restful.filters.UserReceptionist;
import org.naklaken.app.restful.filters.UserServiceProvider;
import org.naklaken.app.restful.resources.status.ReturnStatus;
import org.naklaken.app.restful.resources.Rfid;
import org.naklaken.app.restful.resources.status.StatusCode;
import org.naklaken.app.utilities.Constants;

import javax.sql.DataSource;
import javax.ws.rs.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/rfid")
public class RfidApi {

    @Path("/{code}")
    @GET
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<Rfid> getOneRfid(@PathParam("code") String code) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new RfidDAO(connection).getOne(code);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/all")
    @GET
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<List<Rfid>> getAllRfid() {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new RfidDAO(connection).getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/all-unused")
    @GET
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<List<Rfid>> getAllUnusedRfid() {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new RfidDAO(connection).getAllUnused();
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
    public ReturnStatus<Rfid> insertOneApplicationJson(Rfid rfid) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try (Connection connection = dataSource.getConnection()) {
            return new RfidDAO(connection).insertOne(rfid);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/insert-one")
    @POST
    @Consumes("text/json")
    @Produces("application/json")
    @UserReceptionist
    public ReturnStatus<Rfid> insertOneTextJson(Rfid rfid) {
        return insertOneApplicationJson(rfid);
    }

    @Path("/delete/{code}")
    @DELETE
    @Produces("application/json")
    @UserReceptionist
    public ReturnStatus<Boolean> deleteRfid(@PathParam("code") String code) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource("jdbc/naklaken_db_test");
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new RfidDAO(connection).deleteRfid(code);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }
}
