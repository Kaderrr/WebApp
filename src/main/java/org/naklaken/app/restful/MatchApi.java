package org.naklaken.app.restful;

import org.naklaken.app.daos.MatchDAO;
import org.naklaken.app.databaseconnection.CustomDataSource;
import org.naklaken.app.restful.filters.UserReceptionist;
import org.naklaken.app.restful.filters.UserServiceProvider;
import org.naklaken.app.restful.resources.Match;
import org.naklaken.app.restful.resources.status.ReturnStatus;
import org.naklaken.app.restful.resources.status.StatusCode;
import org.naklaken.app.utilities.Constants;

import javax.sql.DataSource;
import javax.ws.rs.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/match")
public class MatchApi {

    @Path("/{id}")
    @GET
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<Match> getOneMatch(@PathParam("id") Integer id) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new MatchDAO(connection).getOne(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/all")
    @GET
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<List<Match>> getAllMatches() {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new MatchDAO(connection).getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/all-of-booking/{booking_id}")
    @GET
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<List<Match>> getAllMatchesOfBooking(@PathParam("booking_id") Integer booking_id) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new MatchDAO(connection).getAllOfBooking(booking_id);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/all-valid-of-booking/{booking_id}")
    @GET
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<List<Match>> getAllValidMatchesOfBooking(@PathParam("booking_id") Integer booking_id) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new MatchDAO(connection).getAllValidOfBooking(booking_id);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/last-booking-of-rfid/{code_rfid}")
    @GET
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<Match> getLastBookingIdFromRfid(@PathParam("code_rfid") String code_rfid) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new MatchDAO(connection).getLastBookingIdFromRfid(code_rfid);
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
    public ReturnStatus<Match> insertOneApplicationJson(Match match) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try (Connection connection = dataSource.getConnection()) {
            return new MatchDAO(connection).insertOne(match);
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
    public ReturnStatus<Match> insertOneTextJson(Match match) {
        return insertOneApplicationJson(match);
    }

    @Path("/delete-one")
    @DELETE
    @Produces("application/json")
    @UserReceptionist
    public ReturnStatus<Match> deleteOneApplicationJson(@QueryParam("id") int id) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource("jdbc/naklaken_db_test");
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try (Connection connection = dataSource.getConnection()) {
            return new MatchDAO(connection).deleteOne(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }
}
