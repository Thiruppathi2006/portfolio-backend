package com.thiru.portfoliobackend.repository;

import com.thiru.portfoliobackend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}