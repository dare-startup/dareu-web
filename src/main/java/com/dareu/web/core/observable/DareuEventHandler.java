package com.dareu.web.core.observable;

import com.dareu.web.exception.event.EventHandlerException;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
public interface DareuEventHandler {
    
    /**
     * Executes an expired dares event 
     * @param event
     * @throws EventHandlerException 
     */
    public void handleExpiredDareEvent(DareEvent event)throws EventHandlerException; 
}
