package com.hortonworks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hortonworks.domain.Project;
import com.hortonworks.repository.ProjectRepository;

@Service
public class ProjectService {
	@Autowired
	ProjectRepository repo;

	public Iterable<Project> findAll() {
		Iterable<Project> projects = repo.findAll();
		return projects;
	}

	public void save(Project project) {
		repo.save(project);
	}
}
