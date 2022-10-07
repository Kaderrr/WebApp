package org.naklaken.app.restful.resources;

public record Booking(
        int id,
        String check_in,
        String check_out,
        String room,
        double balance,
        String guest_id,
        String user_id
) {
}
