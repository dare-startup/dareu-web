/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.factory;

import com.dareu.web.core.security.DareuPrincipal;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

/**
 *
 * @author MACARENA
 */

public class BeanFactory {
    
    private static final Logger log = Logger.getAnonymousLogger();
    
    @Produces
    public Logger loggerBean(InjectionPoint ip){
        return Logger.getLogger(ip.getMember().getDeclaringClass().getName());
    }
}
