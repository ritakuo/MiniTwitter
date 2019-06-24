package com.spring.twitterapp.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

/**
 * Created by ritakuo on 6/18/19.
 */

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length=50)
    private RoleName name;
    public Role() {

    }

    public Role(RoleName  name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName  getName() {
        return name;
    }

    public void setName(RoleName  name) {
        this.name = name;
    }




}
