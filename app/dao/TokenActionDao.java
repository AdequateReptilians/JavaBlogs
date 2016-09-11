package dao;

import models.entities.TokenAction;
import models.entities.User;
import org.hibernate.criterion.Restrictions;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by mik on 11.09.2016.
 */
public class TokenActionDao extends GenericDaoImpl<TokenAction, Serializable> {
    /**
     * Verification time frame (until the user clicks on the link in the email)
     * in seconds
     * Defaults to one week
     */
    private final static long VERIFICATION_TIME = 7 * 24 * 3600;

    public TokenActionDao(EntityManager em){
        super(em, TokenAction.class);
    }

    public TokenAction findByToken(final String token, final String type) {
        return (TokenAction)getSession().createCriteria(TokenAction.class)
                .add(Restrictions.eq("token", token))
                .add(Restrictions.eq("type", type))
                .list().get(0);
    }

    public void deleteByUser(final models.entities.User u, final String type) {
        List<TokenAction> tokens =  getSession()
                                    .createCriteria(TokenAction.class)
                                    .createAlias("users.id", "us")
                                    .add(Restrictions.eq("us.id", u.id))
                                    .add(Restrictions.eq("type", type))
                                    .list();
        for(TokenAction token : tokens) {
            getSession().delete(token);
        }
    }

    public TokenAction create(final String type, final String token, final User targetUser) {
        final TokenAction ua = new TokenAction();
        ua.targetUser = targetUser;
        ua.token = token;
        ua.type = type;
        final Date created = new Date();
        ua.created = created;
        ua.expires = new Date(created.getTime() + VERIFICATION_TIME  * 1000);
        getSession().save(ua);
        return ua;
    }
}
