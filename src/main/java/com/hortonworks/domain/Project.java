package com.hortonworks.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Project {
	@GraphId
	Long nodeId;
	@Indexed
	private String name;
	@RelatedTo(type = "commits")
	private Set<Commit> commits = new HashSet<Commit>();

	public Project() {
	}

	public Project(String name) {
		this.name = name;
	}

	public Set<Commit> getCommits() {
		return commits;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCommits(Set<Commit> commits) {
		this.commits = commits;
	}

	public Long getNodeId() {
		return nodeId;
	}
}
