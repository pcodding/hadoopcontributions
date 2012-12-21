package com.hortonworks.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.hortonworks.domain.Committer;

public interface CommitterRepository extends GraphRepository<Committer> {
}
