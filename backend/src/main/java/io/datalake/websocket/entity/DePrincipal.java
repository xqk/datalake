package io.datalake.websocket.entity;

import java.security.Principal;

public class DePrincipal implements Principal {

    public DePrincipal(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public String getName() {
        return name;
    }
}
