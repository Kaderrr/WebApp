package org.naklaken.app.utilities;

import org.naklaken.app.restful.resources.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserSessionManagement {

    public static void createSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        Object attribute = session.getAttribute(Constants.SESSION_GUEST_ID);
        if (attribute != null) {
            session.removeAttribute(Constants.SESSION_GUEST_ID);
        }
        session.setAttribute(Constants.SESSION_USER_ID, user.id());
        session.setAttribute(Constants.SESSION_USER_PERMISSION_LEVEL, user.permission_level());
    }

    public static boolean destroySession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object attribute = session.getAttribute(Constants.SESSION_USER_ID);
        if (attribute == null) {
            return false;
        }
        session.invalidate();
        return true;
    }

    public static boolean isUserInSession(HttpServletRequest webRequest) {
        HttpSession session = webRequest.getSession(false);
        if (session == null) {
            return false;
        }
        String userId = (String) session.getAttribute(Constants.SESSION_USER_ID);
        String userPermissionLevel = (String) session.getAttribute(Constants.SESSION_USER_PERMISSION_LEVEL);
        if (userId == null || userId.isBlank()) {
            return false;
        }
        return userPermissionLevel != null && !userPermissionLevel.isBlank();
    }

}
