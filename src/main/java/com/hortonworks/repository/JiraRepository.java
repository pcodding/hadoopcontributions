package com.hortonworks.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.hortonworks.domain.Jira;

public interface JiraRepository extends GraphRepository<Jira> {
}
