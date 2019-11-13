package com.haopn.demo.repository;

import com.haopn.demo.entity.Count;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountRepository extends JpaRepository<Count, Integer> {
    Count findById(int id);
}
