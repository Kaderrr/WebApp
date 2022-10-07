package org.naklaken.app.restful.resources;


public record Match(
        int id,
        boolean status,
        String datetime,
        int booking_id,
        String code_rfid) {

}
