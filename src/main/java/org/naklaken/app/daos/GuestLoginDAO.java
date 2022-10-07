package org.naklaken.app.daos;

import org.naklaken.app.restful.resources.Booking;
import org.naklaken.app.restful.resources.Guest;
import org.naklaken.app.restful.resources.status.ReturnStatus;
import org.naklaken.app.restful.resources.status.StatusCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.naklaken.app.restful.resources.status.StatusCode.*;

public class GuestLoginDAO extends AbstractDaoClass {


    private static final String STATEMENT_GET_GUEST_FROM_CODE_SURNAME = "SELECT * FROM naklaken.guest WHERE surname=?";// merr guest id
    private static final String STATEMENT_GET_BOOKING_FROM_GUEST = "SELECT * FROM naklaken.booking WHERE guest_id=? AND now() BETWEEN check_in and check_out ORDER BY id DESC LIMIT 1";
    private static final String STATEMENT_GET_MATCH_FROM_BOOKING = "SELECT * FROM naklaken.match WHERE booking_id=? AND code_rfid=? AND status=true";

    public GuestLoginDAO(Connection connection) {
        super(connection);
    }

    public ReturnStatus<Guest> checkCredentials(String code, String surname) {
        if (connection == null) {
            return ReturnStatus.create(CONNECTION_IS_NULL, null);
        }
        StatusCode error;
        try {
            PreparedStatement preparedStatement1 = connection.prepareStatement(STATEMENT_GET_GUEST_FROM_CODE_SURNAME);
            preparedStatement1.setString(1, surname);
            ResultSet resultSet1 = preparedStatement1.executeQuery();

            if (resultSet1.next()) {
                Guest guest = GuestDAO.guestFromRow(resultSet1);// guest

                PreparedStatement preparedStatement2 = connection.prepareStatement(STATEMENT_GET_BOOKING_FROM_GUEST);
                String guestid = guest.id();
                preparedStatement2.setString(1, guestid);
                ResultSet resultSet2 = preparedStatement2.executeQuery(); //booking
                if (resultSet2.next()) {
                    Booking booking = BookingDAO.bookingFromRow(resultSet2);
                    PreparedStatement preparedStatement3 = connection.prepareStatement(STATEMENT_GET_MATCH_FROM_BOOKING);
                    preparedStatement3.setInt(1, booking.id());
                    preparedStatement3.setString(2, code);
                    ResultSet resultSet3 = preparedStatement3.executeQuery(); // match
                    if (resultSet3.next()) {
                        return ReturnStatus.ok(guest);
                    }
                }

            }
            error = WRONG_CREDENTIALS;
        } catch (SQLException e) {
            e.printStackTrace();
            error = QUERY_ERROR;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                error = CLOSE_CONNECTION_ERROR;
            }
        }
        return ReturnStatus.create(error, null);
    }

}
