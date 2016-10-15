package com.dareu.web.core.security;

import java.io.Serializable;

/**
 *
 * @author jose.rubalcaba
 */
public class DareuPrincipal implements Serializable{
    private String id; 

    public DareuPrincipal(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
