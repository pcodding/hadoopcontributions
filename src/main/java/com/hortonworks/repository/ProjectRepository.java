package com.hortonworks.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.hortonworks.domain.Project;

public interface ProjectRepository extends GraphRepository<Project> {
}
