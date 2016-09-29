package models.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity(name = "token_actions")
public class TokenAction {
    public String type;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true)
	public String token;

    @ManyToOne
    @JoinColumn(name = "target_user_id")
	public models.entities.User targetUser;

    @Temporal(TemporalType.TIMESTAMP)
    public Date created;

    @Temporal(TemporalType.TIMESTAMP)
    public Date expires;

	public boolean isValid() {
		return this.expires.after(new Date());
	}
}
