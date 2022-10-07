package org.naklaken.app.restful.filters;

import org.naklaken.app.utilities.Constants;
import org.naklaken.app.utilities.UserSessionManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@UserAdmin
@Provider
public class UserAdminFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest webRequest;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        if (!UserSessionManagement.isUserInSession(webRequest) || !isUserAdmin()) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    public boolean isUserAdmin() {
        HttpSession session = webRequest.getSession(false);
        String userPermissionLevel = (String) session.getAttribute(Constants.SESSION_USER_PERMISSION_LEVEL);
        return Constants.USER_PERMISSION_LEVEL_ADMIN.equals(userPermissionLevel);
    }

}
