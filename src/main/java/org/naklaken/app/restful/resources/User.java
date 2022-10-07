package org.naklaken.app.restful.resources;

public record User(
        String id,
        String email,
        String password,
        boolean isActive,
        String permission_level) {
}
