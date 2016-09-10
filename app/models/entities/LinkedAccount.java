package models.entities;

import com.feth.play.module.pa.user.AuthUser;
import models.entities.User;
import org.hibernate.annotations.Type;
import play.db.jpa.JPA;
import javax.persistence.*;

@Entity
@Table(name="linked_accounts")
public class LinkedAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
	public User user;

	public String providerUserId;

	public String providerKey;

	public static LinkedAccount findByProviderKey(final User user, String key) {
        // Example of working with hibernate creteria
//        Session s = (Session)JPA.em().getCriteriaBuilder();
//        CriteriaBuilder builder = JPA.em().getCriteriaBuilder();
//        CriteriaQuery<LinkedAccount> criteria = builder.createQuery( LinkedAccount.class );
//        Root<LinkedAccount> root = criteria.from( LinkedAccount.class );
//        ParameterExpression<String> keyParameter = builder.parameter( String.class );
//        criteria.where( builder.equal( root.get( LinkedAccount.providerKey ), keyParameter ) );
//        TypedQuery<Person> query = entityManager.createQuery( criteria );
//        query.setParameter( keyParameter, "JD" );

        String sql_query = String.format("select * from linked_accounts where user_id = %d and provider_key = %s", user.getIdentifier(), key);
        Query query = JPA.em().createQuery(sql_query);
        return (LinkedAccount)query.setMaxResults(1).getResultList().get(0);
	}

	public static LinkedAccount create(final AuthUser authUser) {
		final LinkedAccount ret = new LinkedAccount();
		ret.update(authUser);
		return ret;
	}
	
	public void update(final AuthUser authUser) {
		this.providerKey = authUser.getProvider();
		this.providerUserId = authUser.getId();
	}

	public static LinkedAccount create(final LinkedAccount acc) {
		final LinkedAccount ret = new LinkedAccount();
		ret.providerKey = acc.providerKey;
		ret.providerUserId = acc.providerUserId;

		return ret;
	}
}