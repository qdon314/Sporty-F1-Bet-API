package com.example.f1simple.repo;
import com.example.f1simple.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {}
