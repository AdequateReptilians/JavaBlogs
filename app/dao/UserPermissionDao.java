package dao;

import models.entities.UserPermission;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by mik on 11.09.2016.
 */
public class UserPermissionDao extends GenericDaoImpl<UserPermission, Serializable> {
    public UserPermissionDao(EntityManager em) {
        super(em,UserPermission.class);
    }

    public UserPermission findByValue(String value) {
        return (UserPermission)getSession().createCriteria(UserPermission.class).add(Restrictions.eq("value", value)).list().get(0);
    }
}
