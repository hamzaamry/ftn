package com.esprit.ftn.Repositories;

import com.esprit.ftn.entities.Athlete;
import com.esprit.ftn.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {
}