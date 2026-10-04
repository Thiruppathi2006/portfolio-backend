package com.thiru.portfoliobackend.service;

import com.thiru.portfoliobackend.entity.Project;
import com.thiru.portfoliobackend.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public Project createProject(Project project) {
    return projectRepository.save(project);
}

    public Project updateProject(Long id, Project project) {
        Project existingProject = getProjectById(id);

        existingProject.setTitle(project.getTitle());
        existingProject.setDescription(project.getDescription());
        existingProject.setTechnologies(project.getTechnologies());
        existingProject.setGithubUrl(project.getGithubUrl());
        existingProject.setLiveUrl(project.getLiveUrl());
        existingProject.setImageUrl(project.getImageUrl());
        existingProject.setStatus(project.getStatus());

        return projectRepository.save(existingProject);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}