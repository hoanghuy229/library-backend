package com.huy2209.library_backend.dao;

import com.huy2209.library_backend.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History,Long> {
    Page<History> findByUserEmail(String userEmail, Pageable pageable);
}
