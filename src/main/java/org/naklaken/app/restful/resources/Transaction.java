package org.naklaken.app.restful.resources;

public record Transaction(
        int id,
        boolean state,
        double amount,
        String description,
        String datetime,
        String type,
        String user_id,
        int booking_id) {
}
