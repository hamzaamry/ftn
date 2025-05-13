package com.esprit.ftn.Repositories;

import com.esprit.ftn.entities.FileDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDetailRepository extends JpaRepository<FileDetail, Long> {
    boolean existsByFilename(String filename);
}
