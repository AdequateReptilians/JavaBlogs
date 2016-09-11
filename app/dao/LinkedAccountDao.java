package dao;

import models.entities.LinkedAccount;
import models.entities.User;
import org.hibernate.Query;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by mik on 11.09.2016.
 */
public class LinkedAccountDao extends GenericDaoImpl<LinkedAccount, Serializable> {
    public LinkedAccountDao(EntityManager em){
        super(em, LinkedAccount.class);
    }

    public LinkedAccount findByProviderKey(final User user, String key) {
        String sql_query = String.format("select * from linked_accounts where user_id = %d and provider_key = %s", user.getIdentifier(), key);
        Query query = getSession().createQuery(sql_query);
        return (LinkedAccount)query.setMaxResults(1).list().get(0);
    }
}
