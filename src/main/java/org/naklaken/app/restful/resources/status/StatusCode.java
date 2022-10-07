package org.naklaken.app.restful.resources.status;

import java.util.Optional;

public enum StatusCode {

    ALL_OK(0, "No errors occured."),
    // 100 Data base related
    DATASOURCE_NAME_INCORRECT(101, "The specified name for data source does not exists."),
    DATASOURCE_CANNOT_CONNECT(102, "The data source cannot connect to the database. Check name corresponding in context.xml."),
    CONNECTION_IS_NULL(103, "Connection of DAO is null."),
    QUERY_ERROR(104, "SQL statement gone wrong. Probably duplicate key."),
    CLOSE_CONNECTION_ERROR(105, "Cannot close the connection to database."),
    VALUE_ERROR(106, "Wrong query values."),
    QUERY_ERROR_INSERT_MATCH(107, "Error inser match"),
    NOT_LOGGED_IN(108, "You are not logged in."),


    // 200 GuestAccount API related
    GUEST_ID_DOES_NOT_EXIST(201, "GuestAccount not found."),
    INSERT_DID_NOT_RETURN(203, "Something went wrong with the insertion"),
    GUEST_DELETED_SUCCESSFULLY(204, "Successful deletion"),


    USER_ID_DOES_NOT_EXIST(801, "User not found"),

    // 900 Match API related
    MATCH_ID_DOES_NOT_EXIST(901, "This rfid-booking match does not exist."),
    DELETE_MATCH_DID_NOT_RETURN(903, "Something went wrong with the deletion"),
    INSERT_MATCH_ERROR_ON_GET_VALID_MATCHES(904, "Error on get list of valid matches."),
    DELETE_MATCH_DID_NOT_RETURN_1(905, "ERROR"),
    // 300 Transaction API related
    TRANSACTION_ID_DOES_NOT_EXIST(301, "Transaction ID does not exists."),
    INSERT_TRANSACTION_DID_NOT_RETURN(303, "Something went wrong with the insertion."),
    DELETE_TRANSACTION_DID_NOT_RETURN(304, "Last transaction has already been deleted."),
    INSERT_TRANSACTION_FAILED_LOW_BALANCE(305, "Booking's balance lower than transaction's amount."),


    // 600 Booking API related
    BOOKING_ID_DOES_NOT_EXIST(601, "Booking not found"),
    UPDATE_BALANCE_DID_NOT_RETURN(602, "Something went wrong with balance's update."),


    //700 RFID API related
    RFID_CODE_DOES_NOT_EXIST(701, "GuestAccount ID does not exists."),
    INSERT_RFID_DID_NOT_RETURN(703, "Something went wrong with the insertion."),

    // 800 User related
    WRONG_CREDENTIALS(801, "Credentials are wrong.");


    private final int code;
    private final String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Optional<StatusCode> getFromCode(int code) {
        StatusCode[] values = StatusCode.values();
        for (StatusCode statusCode : values) {
            if (statusCode.code == code) {
                return Optional.of(statusCode);
            }
        }
        return Optional.empty();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

