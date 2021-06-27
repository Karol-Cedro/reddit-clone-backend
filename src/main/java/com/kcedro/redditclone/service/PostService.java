package com.kcedro.redditclone.service;

import com.kcedro.redditclone.dto.PostRequest;
import com.kcedro.redditclone.dto.PostResponse;
import com.kcedro.redditclone.exceptions.PostNotFoundException;
import com.kcedro.redditclone.exceptions.SubredditNotFoundException;
import com.kcedro.redditclone.mapper.PostMapper;
import com.kcedro.redditclone.model.Post;
import com.kcedro.redditclone.model.Subreddit;
import com.kcedro.redditclone.model.User;
import com.kcedro.redditclone.repository.PostRepository;
import com.kcedro.redditclone.repository.SubredditRepository;
import com.kcedro.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
               .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        Post savedPost = postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
        subreddit.getPosts().add(savedPost);
        subredditRepository.save(subreddit);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
}
