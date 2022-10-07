package org.naklaken.app.restful.filters;

import org.naklaken.app.utilities.GuestSessionManagement;
import org.naklaken.app.utilities.UserSessionManagement;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@UserAdmin
@Provider
public class GuestAccountFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest webRequest;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        if (UserSessionManagement.isUserInSession(webRequest)) {
            return;
        }
        if (!GuestSessionManagement.isGuestInSession(webRequest)) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

}
