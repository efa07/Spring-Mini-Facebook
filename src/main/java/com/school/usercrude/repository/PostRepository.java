package com.school.usercrude.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.school.usercrude.entity.Post;
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
