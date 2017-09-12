package com.dareu.web.core.service.impl;

import com.dareu.web.core.security.DareuPrincipal;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

public abstract class AbstractService {
    protected DareuPrincipal getPrincipal(){
        DareuPrincipal principal = ResteasyProviderFactory.getContextData(DareuPrincipal.class);
        return principal; 
    }
}
