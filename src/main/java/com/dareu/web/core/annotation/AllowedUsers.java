package com.dareu.web.core.annotation;

import com.dareu.web.dto.security.SecurityRole;
import java.lang.annotation.*;

/**
 *
 * @author jose.rubalcaba
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface AllowedUsers {

    /**
     * Allowed roles to be allowed on the application resources
     *
     * @return
     */
    SecurityRole[] securityRoles() default SecurityRole.ALL;

}
