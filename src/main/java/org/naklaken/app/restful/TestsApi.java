package org.naklaken.app.restful;

import org.naklaken.app.databaseconnection.CustomDataSource;
import org.naklaken.app.restful.resources.status.ReturnStatus;
import org.naklaken.app.utilities.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

@Path("/test")
public class TestsApi {

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    @Path("/postgre-connection")
    @GET
    @Produces("application/json")
    public ReturnStatus<Object> testConnection() {
        ReturnStatus<DataSource> dataSourceReturnStatus = CustomDataSource.getDataSource(Constants.DATA_SOURCE_TEST);
        return dataSourceReturnStatus.changeDataAndType(null);
    }
}
