package com.hortonworks.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Jira {
	@GraphId
	Long nodeId;
	@Indexed
	private String jiraId;

	public Jira() {

	}

	public Jira(String jiraId) {
		this.jiraId = jiraId;
	}

	public String getJiraId() {
		return jiraId;
	}

	public void setJiraId(String jiraId) {
		this.jiraId = jiraId;
	}
}

