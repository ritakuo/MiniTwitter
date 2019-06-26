package com.spring.twitterapp.repository;

import com.spring.twitterapp.model.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


/**
 * Created by ritakuo on 6/19/19.
 */
@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    Optional<Tweet> findById(Long tweetId);

    Page<Tweet> findByCreatedBy(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);

    List<Tweet> findByIdIn(List<Long> tweetIds);

    List<Tweet> findByIdIn(List<Long> tweetIds, Sort sort);

    Page<Tweet> findByCreatedByIn(List<Long> userIds, Pageable pageable);
}
