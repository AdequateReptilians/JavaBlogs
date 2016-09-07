package service;

import models.entities.SecurityRole;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import javax.inject.Inject;
import java.util.Arrays;

/**
 * Data initializer class.
 */
public class DataInitializer {

    public DataInitializer() {
        // TODO: remove
//        Session session = (Session) JPAApi.api.em().getCriteriaBuilder();
//        if ((Integer)session.createCriteria(SecurityRole.class).setProjection(Projections.rowCount()).uniqueResult() == 0) {
//            for (final String roleName : Arrays.asList(controllers.Application.USER_ROLE)) {
//                final SecurityRole role = new SecurityRole();
//                role.roleName = roleName;
//                session.save(role);
//            }
//        }
    }
}
