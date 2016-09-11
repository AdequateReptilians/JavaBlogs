package service;

import dao.SecurityRoleDao;
import models.entities.SecurityRole;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.Arrays;

/**
 * Data initializer class.
 */
public class DataInitializer {
    @Inject
    private JPAApi jpaApi;

    public DataInitializer() {
        SecurityRoleDao securityRoleDao = new SecurityRoleDao(jpaApi.em());
        if (securityRoleDao.count() == 0) {
            for (final String roleName : Arrays.asList(controllers.Application.USER_ROLE)) {
                final SecurityRole role = new SecurityRole();
                role.roleName = roleName;
                securityRoleDao.create(role);
            }
        }
    }
}
