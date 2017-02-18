package com.dareu.web.core.observable.impl;

import com.dareu.web.core.observable.DareEvent;
import com.dareu.web.core.observable.DareuEventHandler;
import com.dareu.web.exception.event.EventHandlerException;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
public class DareuEventHandlerImpl implements DareuEventHandler{
    
    @Inject
    private Logger log; 

    @Override
    @Asynchronous
    public void handleExpiredDareEvent(@Observes DareEvent event) throws EventHandlerException {
        switch(event){
            case EXPIRED_DARE:
                log.info("Expired dare async event handler");
                break; 
        }
    }
    
}
