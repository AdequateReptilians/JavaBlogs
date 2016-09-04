package models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name="persons")
public class Person {

    @Column(name = "name")
    private String name;

    @Id
    @Column(name = "id")
    private int id;

    @Column
    private int age;

    public Person(final String name, final int id) {
        this.name = name;
        this.id = id;
    }

    public Person() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}