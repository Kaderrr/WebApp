package org.naklaken.app.databaseconnection;

import org.naklaken.app.restful.TestsApi;
import org.naklaken.app.restful.resources.status.ReturnStatus;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;

import static org.naklaken.app.restful.resources.status.StatusCode.DATASOURCE_CANNOT_CONNECT;
import static org.naklaken.app.restful.resources.status.StatusCode.DATASOURCE_NAME_INCORRECT;

/**
 * Use this class anywhere to access the resource declared in context file.
 * Check if DataSource is present after call. See below for example usage.
 *
 * @see TestsApi
 */
public class CustomDataSource {

    private static final HashMap<String, DataSource> cache = new HashMap<>();

    /**
     * Get declared data source by name.
     * Or get the error message.
     *
     * @param name Name of resource (see what you put in context.xml)
     * @return Either data source or error.
     */
    public static ReturnStatus<DataSource> getDataSource(String name) {
        DataSource cachedDataSource = cache.get(name);
        if (cachedDataSource != null) {
            return ReturnStatus.ok(cachedDataSource);
        }
        try {
            InitialContext initialContext = new InitialContext();
            DataSource lookup = (DataSource) initialContext.lookup("java:/comp/env/" + name);
            lookup.getConnection();
            cache.put(name, lookup);
            return ReturnStatus.ok(lookup);
        } catch (NamingException e) {
            e.printStackTrace();
            return ReturnStatus.create(DATASOURCE_NAME_INCORRECT, null);
        } catch (SQLException e) {
            e.printStackTrace();
            return ReturnStatus.create(DATASOURCE_CANNOT_CONNECT, null);
        }
    }
}
