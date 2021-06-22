package com.kcedro.redditclone.repository;
import com.kcedro.redditclone.model.Post;
import com.kcedro.redditclone.model.Subreddit;
import com.kcedro.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
