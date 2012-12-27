package com.hortonworks.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.QueryType;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.hortonworks.domain.Commit;

public interface CommitRepository extends GraphRepository<Commit> {
	@Query(value = "START jira = node:jiraId(jiraId={0}) MATCH commit-[:addresses]->jira RETURN commit", type = QueryType.Cypher)
	public Commit findByJiraId(String jiraId);
	
	@Query(value = "START jira = node:jiraId(jiraId={0}) MATCH commit-[:addresses]->jira RETURN commit", type = QueryType.Cypher)
	public Iterable<Commit> findAllByJiraId(String jiraId);
}