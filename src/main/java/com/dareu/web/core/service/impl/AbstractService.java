package com.dareu.web.core.service.impl;

import com.dareu.web.core.security.DareuPrincipal;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
public abstract class AbstractService {
    protected DareuPrincipal getPrincipal(){
        DareuPrincipal principal = ResteasyProviderFactory.getContextData(DareuPrincipal.class);
        return principal; 
    }
}
