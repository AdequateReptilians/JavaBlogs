package service;

import models.entities.SecurityRole;
import org.hibernate.criterion.Projections;

import java.util.Arrays;

/**
 * Data initializer class.
 */
public class DataInitializer {
    public DataInitializer() {
        if ((Integer)SecurityRole.session.createCriteria(SecurityRole.class).setProjection(Projections.rowCount()).uniqueResult() == 0) {
            for (final String roleName : Arrays
                    .asList(controllers.Application.USER_ROLE)) {
                final SecurityRole role = new SecurityRole();
                role.roleName = roleName;
                SecurityRole.session.save(role);
            }
        }
    }
}
