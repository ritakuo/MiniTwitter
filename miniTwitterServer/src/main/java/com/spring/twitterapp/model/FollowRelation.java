package com.spring.twitterapp.model;

import com.spring.twitterapp.model.audit.DateAudit;

import javax.persistence.*;

/**
 * Created by ritakuo on 6/21/19.
 */
@Entity
public class FollowRelation extends DateAudit {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="from_user_fk")
    private User from;

    @ManyToOne
    @JoinColumn(name="to_user_fk")
    private User to;

    public FollowRelation() {};

    public FollowRelation(User from, User to) {
        this.from = from;
        this.to = to;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }
}