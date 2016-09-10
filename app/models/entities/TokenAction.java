package models.entities;

import models.entities.User;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "token_actions")
public class TokenAction {
    public String type;


	/**
	 * Verification time frame (until the user clicks on the link in the email)
	 * in seconds
	 * Defaults to one week
	 */
	private final static long VERIFICATION_TIME = 7 * 24 * 3600;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true)
	public String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
	public models.entities.User targetUser;

	public Date created;

	public Date expires;

	public static TokenAction findByToken(final String token, final String type) {
		Session session = (Session) JPA.em().getCriteriaBuilder();
		return (TokenAction)session.createCriteria(TokenAction.class)
                .add(Restrictions.eq("token", token))
                .add(Restrictions.eq("type", type))
                .list().get(0);
	}

	public static void deleteByUser(final models.entities.User u, final String type) {
		Session session = (Session) JPA.em().getCriteriaBuilder();
		List<TokenAction> tokens = session.createCriteria(TokenAction.class)
                .createAlias("users.id", "us")
                .add(Restrictions.eq("us.id", u.id))
                .add(Restrictions.eq("type", type))
                .list();
		for(TokenAction token : tokens) {
			session.delete(token);
		}
	}

	public boolean isValid() {
		return this.expires.after(new Date());
	}

	public static TokenAction create(final String type, final String token,
                                     final User targetUser) {
		final TokenAction ua = new TokenAction();
		ua.targetUser = targetUser;
		ua.token = token;
		ua.type = type;
		final Date created = new Date();
		ua.created = created;
		ua.expires = new Date(created.getTime() + VERIFICATION_TIME * 1000);
		Session session = (Session) JPA.em().getCriteriaBuilder();
		session.save(ua);
		return ua;
	}
}
