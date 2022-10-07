package org.naklaken.app.restful;

import org.naklaken.app.daos.TransactionDAO;
import org.naklaken.app.databaseconnection.CustomDataSource;
import org.naklaken.app.restful.filters.UserReceptionist;
import org.naklaken.app.restful.filters.UserServiceProvider;
import org.naklaken.app.restful.resources.Transaction;
import org.naklaken.app.restful.resources.status.ReturnStatus;
import org.naklaken.app.restful.resources.status.StatusCode;
import org.naklaken.app.utilities.Constants;

import javax.sql.DataSource;
import javax.ws.rs.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Path("/transactions")
public class TransactionAPI {
    @Path("/{id}")
    @GET
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<Transaction> getOneTransaction(@PathParam("id") int id) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new TransactionDAO(connection).getOne(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/all")
    @GET
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<List<Transaction>> getAllTransactions() {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new TransactionDAO(connection).getAll();
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
    public ReturnStatus<List<Transaction>> getTransactionBetweenDates(@FormParam("fromDate") String fromDate, @FormParam("toDate") String toDate) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new TransactionDAO(connection).getBetweenDates(fromDate, toDate);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/all-booking-transactions")
    @GET
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<List<Transaction>> getAllBookingTransactions(@QueryParam("booking_id") int booking_id) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new TransactionDAO(connection).getAllBookingTransactions(booking_id);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/all-guest-booking-transactions")
    @GET
    @Produces("application/json")
    public ReturnStatus<List<Transaction>> getAllGuestBookingTransactions(@QueryParam("guest_id") String guest_id) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try {
            Connection connection = dataSource.getConnection();
            return new TransactionDAO(connection).getAllGuestBookingTransactions(guest_id);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/insert-one")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<Transaction> insertOneApplicationJson(Transaction transaction) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try (Connection connection = dataSource.getConnection()) {
            return new TransactionDAO(connection).insertOne(transaction);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/insert-one")
    @POST
    @Consumes("text/json")
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<Transaction> insertOneTextJson(Transaction transaction) {
        return insertOneApplicationJson(transaction);
    }

    @Path("/delete-one")
    @DELETE
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<Transaction> deleteOneApplicationJson(@FormParam("id") int id) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource("jdbc/naklaken_db_test");
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try (Connection connection = dataSource.getConnection()) {
            return new TransactionDAO(connection).deleteOne(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }

    @Path("/delete-last-by-user-id")
    @DELETE
    @Produces("application/json")
    @UserServiceProvider
    public ReturnStatus<Transaction> deleteLastApplicationJson(@FormParam("user_id") String user_id) {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource("jdbc/naklaken_db_test");
        if (!dataSourceReturnStatus.status()) {
            return dataSourceReturnStatus.changeDataAndType(null);
        }
        DataSource dataSource = dataSourceReturnStatus.data();
        try (Connection connection = dataSource.getConnection()) {
            return new TransactionDAO(connection).deleteLastByUserId(user_id);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(StatusCode.QUERY_ERROR, null);
        }
    }
}
