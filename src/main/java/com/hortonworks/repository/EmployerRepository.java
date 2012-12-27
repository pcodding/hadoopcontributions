package com.hortonworks.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.hortonworks.domain.Employer;

public interface EmployerRepository extends GraphRepository<Employer> {
}
