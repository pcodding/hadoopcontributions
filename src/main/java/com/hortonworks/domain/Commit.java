package com.hortonworks.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Commit {
	@GraphId
	Long nodeId;
	@Indexed
	private String commitId;
	private Committer author;
	private Date commitDate;
	private String description;
	private String gitSvnId;
	@RelatedTo(type = "contributed")
	private Set<Contributor> contributors = new HashSet<Contributor>();
	@RelatedTo(type = "addresses")
	private Jira jira;

	@RelatedTo(type = "changed")
	public Set<Change> changes = new HashSet<Change>();

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	public Committer getAuthor() {
		return author;
	}

	public void setAuthor(Committer author) {
		this.author = author;
	}

	public Date getCommitDate() {
		return commitDate;
	}

	public void setCommitDate(Date commitDate) {
		this.commitDate = commitDate;
	}

	public void setCommitDate(String commitDate) throws ParseException {
		// Tue Dec 18 19:49:14 2012 +0000
		SimpleDateFormat sdf = new SimpleDateFormat(
				"  EEE MMM d HH:mm:ss yyyy Z");
		this.commitDate = sdf.parse(commitDate);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGitSvnId() {
		return gitSvnId;
	}

	public void setGitSvnId(String gitSvnId) {
		this.gitSvnId = gitSvnId;
	}

	public Set<Contributor> getContributors() {
		return contributors;
	}

	public void setContributors(Set<Contributor> contributors) {
		this.contributors = contributors;
	}

	public Set<Change> getChanges() {
		return changes;
	}

	public void setChanges(Set<Change> changes) {
		this.changes = changes;
	}

	public Jira getJira() {
		return jira;
	}

	public void setJira(Jira jira) {
		this.jira = jira;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
