package com.spring.twitterapp.repository;

import com.spring.twitterapp.model.FollowRelation;
import com.spring.twitterapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ritakuo on 6/21/19.
 */
@Repository

public interface FollowRepository extends JpaRepository<FollowRelation, Long> {
    List<FollowRelation> findByFrom(User user);
    List<FollowRelation> findByTo(User user);

}
