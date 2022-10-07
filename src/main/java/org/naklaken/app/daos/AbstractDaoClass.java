package org.naklaken.app.daos;

import java.sql.Connection;

public abstract class AbstractDaoClass {

    protected final Connection connection;

    public AbstractDaoClass(Connection connection) {
        this.connection = connection;
    }
}
