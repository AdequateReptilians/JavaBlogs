package models.entities;

import javax.persistence.*;


@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column private String name;

    @Column private int age;

    public Person(final String name, final int age) {
        this.name = name;
        this.age = age;
    }

    public Person() {
    }

    public Long getPersonId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}