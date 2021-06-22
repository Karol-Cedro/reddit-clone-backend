package com.kcedro.redditclone.repository;
import com.kcedro.redditclone.model.Post;
import com.kcedro.redditclone.model.User;
import com.kcedro.redditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
