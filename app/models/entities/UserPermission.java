package models.entities;

import be.objectify.deadbolt.java.models.Permission;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPA;

import javax.persistence.*;

/**
 * Initial version based on work by Steve Chaloner (steve@objectify.be) for
 * Deadbolt2
 */
@Entity
public class UserPermission implements Permission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long id;

	public String value;

	public String getValue() {
		return value;
	}

	public static UserPermission findByValue(String value) {
		Session session = (Session) JPA.em().getCriteriaBuilder();
	    return (UserPermission)session.createCriteria(UserPermission.class)
                .add(Restrictions.eq("value", value))
                .list().get(0);
	}
}
