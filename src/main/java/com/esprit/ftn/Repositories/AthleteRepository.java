package com.esprit.ftn.Repositories;

import com.esprit.ftn.entities.Athlete;
import com.esprit.ftn.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {
    boolean existsByUser(User user);
}
