package com.bozo.issuetracker.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('ROLE_ADMIN') "
        + "or T(com.bozo.issuetracker.details.user.ApplicationUser).cast(principal).getUser().getId() == #userId")
public @interface PreAuthorizeRoleAdminOrUserWithSameId {
}
