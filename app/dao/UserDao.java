package dao;

import models.entities.User;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by mik on 10.09.2016.
 */
public class UserDao extends GenericDaoImpl<User, Serializable> {
    public UserDao(EntityManager em){
        super(em, User.class);
    }
}
