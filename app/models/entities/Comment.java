package models.entities;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    private Post post;

    @Column
    @Type(type = "text")
    private String content;
}
