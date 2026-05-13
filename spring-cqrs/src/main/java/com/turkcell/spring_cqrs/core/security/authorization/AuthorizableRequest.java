package com.turkcell.spring_cqrs.core.security.authorization;

import java.util.Collections;
import java.util.List;

public interface AuthorizableRequest {
    default List<String> getRequiredRoles() {
        return Collections.emptyList();
    }
}
