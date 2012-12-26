package com.hortonworks.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.hortonworks.domain.Contributor;

public interface ContributorRepository extends GraphRepository<Contributor> {
}
