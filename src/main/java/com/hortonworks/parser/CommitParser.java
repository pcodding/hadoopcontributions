package com.hortonworks.parser;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hortonworks.domain.Change;
import com.hortonworks.domain.Commit;
import com.hortonworks.domain.Committer;
import com.hortonworks.domain.Employer;
import com.hortonworks.domain.Jira;
import com.hortonworks.domain.Project;
import com.hortonworks.service.CommitterService;
import com.hortonworks.service.JiraService;
import com.hortonworks.service.ProjectService;

@Service
public class CommitParser {
	@Autowired
	private ProjectService projectService;
	@Autowired
	private CommitterService committerService;
	@Autowired
	private JiraService jiraService;

	private Logger logger = Logger.getLogger(this.getClass());
	public Commit currentCommit;
	public LinkedList<Commit> commits = new LinkedList<Commit>();
	public Pattern commitPattern = Pattern.compile("commit (.*)");
	public Pattern authorPattern = Pattern.compile("Author: (.*) (<.*>)");
	public Pattern datePattern = Pattern.compile("Date: (.*)");
	public Pattern descriptionPattern = Pattern
			.compile("([\\p{Upper}]{3,}-[0-9]{1,})(.*)");
	public Pattern gitSvnIdPattern = Pattern.compile("^git-svn-id:\\W(http.*)");
	public Pattern changePattern = Pattern
			.compile("^(\\d)++\\W(\\d)++\\W([a-z].*)");
	Project project = new Project("Hadoop Common");
	Employer employer = new Employer("Apache");
	int rowCount = 0;

	public CommitParser() {

	}

	public LinkedList<Commit> completeParse() {
		if (currentCommit != null)
			commits.add(currentCommit);
		projectService.save(project);
		return commits;
	}

	@Transactional
	public void parseCommit(String line) {
		Matcher matcher = null;

		// Check for the commit ID
		matcher = commitPattern.matcher(line);
		if (matcher.find()) {
			if (currentCommit != null) {
				commits.add(currentCommit);
				project.getCommits().add(currentCommit);
				rowCount++;
			}
			currentCommit = new Commit();
			currentCommit.setCommitId(matcher.group(1));
			// Check for changes (as they are the most frequent)
		} else {
			matcher = changePattern.matcher(line);
			if (matcher.find()) {
				Change change = new Change(line);
				currentCommit.changes.add(change);
				// Check for author
			} else {
				matcher = authorPattern.matcher(line);
				if (matcher.find()) {
					Committer committer = committerService.findByEmail(matcher
							.group(2));
					if (committer == null) {
						committer = new Committer(matcher.group(1),
								matcher.group(2));
						committer.setEmployer(employer);
						committerService.save(committer);
						employer.getCommitters().add(committer);
					}
					currentCommit.setAuthor(committer);
					// committer.getCommits().add(currentCommit);
				}
				// Check for the date
				else {
					matcher = datePattern.matcher(line);
					if (matcher.find()) {
						try {
							currentCommit.setCommitDate(matcher.group(1));
						} catch (ParseException e) {
							logger.error(e.getMessage(), e);
						}
						// Check for the git-svn-id
					} else {
						line = line.replaceAll("^\\s+", "");
						matcher = gitSvnIdPattern.matcher(line);
						if (matcher.find())
							currentCommit.setGitSvnId(matcher.group(1));
						// check for description
						else {
							matcher = descriptionPattern.matcher(line);
							if (matcher.find()) {
								Jira jira = jiraService.findByJiraId(matcher
										.group(1));
								if (jira == null) {
									jira = new Jira(matcher.group(1));
									jiraService.save(jira);
								}
								currentCommit.getJira().add(jira);
								currentCommit.setDescription(matcher.group(0));
							}
						}
					}
				}
			}
		}
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
}
