package org.naklaken.app.restful;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.naklaken.app.daos.BookingDAO;
import org.naklaken.app.daos.GuestDAO;
import org.naklaken.app.daos.TransactionDAO;
import org.naklaken.app.databaseconnection.CustomDataSource;
import org.naklaken.app.restful.filters.GuestAccount;
import org.naklaken.app.restful.filters.UserReceptionist;
import org.naklaken.app.restful.filters.UserServiceProvider;
import org.naklaken.app.restful.resources.Booking;
import org.naklaken.app.restful.resources.Transaction;
import org.naklaken.app.restful.resources.status.ReturnStatus;
import org.naklaken.app.restful.resources.status.StatusCode;
import org.naklaken.app.utilities.Constants;
import org.naklaken.app.utilities.Constants;

import javax.sql.DataSource;
import javax.ws.rs.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/booking")

public class BookingApi {
    @Path("/{id}")
    @GET
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<Booking> getBooking(@PathParam("id") int id) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new BookingDAO(connection).getOne(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/last-booking/{guest_id}")
    @GET
    @Produces("application/json")
    @GuestAccount
    public ReturnStatus<Booking> getBookingByGuest(@PathParam("guest_id") String guest_id) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new BookingDAO(connection).getOneByGuestId(guest_id);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }


    @Path("/all")
    @GET
    @Produces("application/json")
    @UserReceptionist
    public ReturnStatus<List<Booking>> getAllBookings() {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();

        try {
            Connection connection = dataSource.getConnection();
            return new BookingDAO(connection).getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    // Check if we need another operation in booking
    @Path("/insert-one")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @UserReceptionist
    public ReturnStatus<Booking> insertOneApplicationJson(Booking booking) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }

        DataSource dataSource = dataSourceReturnStatus.data();
        try (Connection connection = dataSource.getConnection()) {
            return new BookingDAO(connection).insertOne(booking);
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
    public ReturnStatus<Booking> insertOneTextJson(Booking booking) {
        return insertOneApplicationJson(booking);
    }

    @Path("/delete/{id}")
    @DELETE
    @Produces("application/json")
    public ReturnStatus<Boolean> deleteGuest(@PathParam("id") int id) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource("jdbc/naklaken_db_test");
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new BookingDAO(connection).deleteOne(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/between-dates")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<List<Booking>> getBetweenDates(@FormParam("fromDate") String fromDate, @FormParam("toDate") String toDate) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new BookingDAO(connection).getBetweenDates(fromDate, toDate);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }
}