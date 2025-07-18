package com.esprit.ftn.Repositories;

import com.esprit.ftn.entities.Admin;
import com.esprit.ftn.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsByUser(User user);
}
