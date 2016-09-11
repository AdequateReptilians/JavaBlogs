package dao;

import models.entities.SecurityRole;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by mik on 11.09.2016.
 *
 */
public class SecurityRoleDao extends GenericDaoImpl<SecurityRole, Serializable> {
    public SecurityRoleDao(EntityManager em) {
        super(em, SecurityRole.class);
    }

    public SecurityRole findByRoleName(String roleName) {
        return (SecurityRole)getSession().createCriteria(SecurityRole.class).add(Restrictions.eq("role_name", roleName)).list().get(0);
    }

    public Long count() {
        return (Long)getSession().createCriteria(SecurityRole.class).setProjection(Projections.rowCount()).uniqueResult();
    }
}
