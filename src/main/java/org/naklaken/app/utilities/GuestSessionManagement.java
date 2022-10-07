package org.naklaken.app.utilities;

import org.naklaken.app.restful.resources.Guest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class GuestSessionManagement {

    public static void createSession(HttpServletRequest request, Guest guest) {
        HttpSession session = request.getSession();
        Object attribute = session.getAttribute(Constants.SESSION_USER_ID);
        if(attribute != null){
            session.removeAttribute(Constants.SESSION_USER_ID);
            session.removeAttribute(Constants.SESSION_USER_PERMISSION_LEVEL);
        }
        session.setAttribute(Constants.SESSION_GUEST_ID, guest.id());
    }

    public static boolean destroySession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object attribute = session.getAttribute(Constants.SESSION_GUEST_ID);
        if (attribute == null) {
            return false;
        }
        session.invalidate();
        return true;
    }

    public static boolean isGuestInSession(HttpServletRequest webRequest) {
        HttpSession session = webRequest.getSession(false);
        if (session == null) {
            return false;
        }
        String guestId = (String) session.getAttribute(Constants.SESSION_GUEST_ID);

        return guestId != null && !guestId.isBlank();

    }

}
