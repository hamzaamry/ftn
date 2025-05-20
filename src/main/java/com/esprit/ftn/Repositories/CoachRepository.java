package com.esprit.ftn.Repositories;

import com.esprit.ftn.entities.Coach;
import com.esprit.ftn.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
    boolean existsByUser(User user);
}