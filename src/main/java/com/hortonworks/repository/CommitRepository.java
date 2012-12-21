package com.hortonworks.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.hortonworks.domain.Commit;
import com.hortonworks.domain.Contributor;

public interface CommitRepository extends GraphRepository<Commit> {
	@Query("start commit={0} match m<-[rating:RATED]-user return rating")
	Iterable<Contributor> getContributors(Commit commit);
}