package org.naklaken.app.restful.resources.status;

import static org.naklaken.app.restful.resources.status.StatusCode.ALL_OK;

public record ReturnStatus<T>(
        boolean status,
        int statusCode,
        String statusMessage,
        T data
) {

    public static <T> ReturnStatus<T> create(StatusCode statusCode, T data) {
        boolean status = ALL_OK.equals(statusCode);
        return new ReturnStatus<>(status, statusCode.getCode(), statusCode.getMessage(), data);
    }

    public static <T> ReturnStatus<T> ok(T data) {
        return create(ALL_OK, data);
    }

    public <K> ReturnStatus<K> changeDataAndType(K newData) {
        return new ReturnStatus<>(status, statusCode, statusMessage, newData);
    }

}
