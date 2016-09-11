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

	public boolean isValid() {
		return this.expires.after(new Date());
	}
}
