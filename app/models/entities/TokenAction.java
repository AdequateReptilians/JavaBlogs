package models.entities;

import models.entities.User;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class TokenAction {
	@Basic
    @Convert( converter=TokenTypeConverter.class )
    public Type type;

    public enum Type {
        EMAIL_VERIFICATION( "EV" ),
        PASSWORD_RESET( "PR" );

        private final String code;

        private Type(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static Type fromCode(String code) {
            if ( code == "EV" || code == "ev" ) {
                return EMAIL_VERIFICATION;
            }
            if ( code == "PR" || code == "pr" ) {
                return PASSWORD_RESET;
            }
            return null;
        }
    }

    @Converter
    public class TokenTypeConverter implements AttributeConverter<Type, String> {
        public String convertToDatabaseColumn(Type value) {
            if ( value == null ) {
                return null;
            }

            return value.getCode();
        }


        public Type convertToEntityAttribute(String value) {
            if ( value == null ) {
                return null;
            }

            return Type.fromCode( value );
        }
    }


	/**
	 * Verification time frame (until the user clicks on the link in the email)
	 * in seconds
	 * Defaults to one week
	 */
	private final static long VERIFICATION_TIME = 7 * 24 * 3600;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(unique = true)
    @org.hibernate.annotations.Type(type = "text")
	public String token;

	@ManyToOne
	public models.entities.User targetUser;

    @Column
    @org.hibernate.annotations.Type(type="timestamp")
	public Date created;

    @Column
    @org.hibernate.annotations.Type(type="timestamp")
	public Date expires;

    public static final Session session = (Session) JPA.em().getCriteriaBuilder();

	public static TokenAction findByToken(final String token, final Type type) {
		return (TokenAction)session.createCriteria(TokenAction.class)
                .add(Restrictions.eq("token", token))
                .add(Restrictions.eq("type", type))
                .list().get(0);
	}

	public static void deleteByUser(final models.entities.User u, final Type type) {
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

	public static TokenAction create(final Type type, final String token,
                                     final User targetUser) {
		final TokenAction ua = new TokenAction();
		ua.targetUser = targetUser;
		ua.token = token;
		ua.type = type;
		final Date created = new Date();
		ua.created = created;
		ua.expires = new Date(created.getTime() + VERIFICATION_TIME * 1000);
		session.save(ua);
		return ua;
	}
}
