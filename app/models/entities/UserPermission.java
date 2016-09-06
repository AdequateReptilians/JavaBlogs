package models.entities;

import be.objectify.deadbolt.java.models.Permission;
import models.AppModel;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPA;

import javax.persistence.*;

/**
 * Initial version based on work by Steve Chaloner (steve@objectify.be) for
 * Deadbolt2
 */
@Entity
public class UserPermission extends AppModel implements Permission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long id;

	@Column
	@org.hibernate.annotations.Type(type = "text")
	public String value;

    public static final Session session = (Session) JPA.em().getCriteriaBuilder();

	public String getValue() {
		return value;
	}

	public static UserPermission findByValue(String value) {

	    return (UserPermission)session.createCriteria(UserPermission.class)
                .add(Restrictions.eq("value", value))
                .list().get(0);
	}
}
