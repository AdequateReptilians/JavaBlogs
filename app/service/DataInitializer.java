package service;

import dao.SecurityRoleDao;
import models.entities.SecurityRole;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collections;

/**
 * Data initializer class.
 */
public class DataInitializer {
    public DataInitializer() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory( "defaultPersistenceUnit" );
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SecurityRoleDao securityRoleDao = new SecurityRoleDao(entityManager);
        if (securityRoleDao.count() == 0) {
            for (final String roleName : Collections.singletonList(controllers.Application.USER_ROLE)) {
                final SecurityRole role = new SecurityRole();
                role.roleName = roleName;
                securityRoleDao.create(role);
            }
        }
    }
}
