package com.hortonworks.parser;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hortonworks.domain.Commit;
import com.hortonworks.domain.Contributor;
import com.hortonworks.domain.Jira;
import com.hortonworks.service.CommitService;
import com.hortonworks.service.CommitterService;
import com.hortonworks.service.ContributorService;
import com.hortonworks.service.JiraService;

@Service
public class ChangesParser {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	CommitService commitService;
	@Autowired
	JiraService jiraService;
	@Autowired
	CommitterService committerService;
	@Autowired
	CommitParser commitParser;
	@Autowired
	ContributorService contributorService;

	@Transactional
	public void parseChange(String change) {
		Commit currentCommit = null;
		Pattern descriptionPattern = Pattern
				.compile("^([\\p{Upper}]{3,}-[0-9]{1,})(.*)");
		Matcher matcher = descriptionPattern.matcher(change);
		Iterable<Commit> associatedCommits = null;
		if (matcher.find()) {
			Jira jira = jiraService.findByJiraId(matcher.group(1));
			if (jira == null) {
				jira = new Jira(matcher.group(1));
				jiraService.save(jira);
			} else
				try {
					currentCommit = commitService
							.findByJiraId(jira.getJiraId());
				} catch (NoSuchElementException e) {
					associatedCommits = commitService.findAllByJiraId(jira
							.getJiraId());
				}
			Collection<String> contributorNames = commitParser
					.identifyContributors(matcher.group(2), jira);
			if (contributorNames != null && contributorNames.size() > 0) {
				for (String name : contributorNames) {
					Contributor contributor = contributorService
							.findByname(name.trim());
					// if !full name direct match, then
					// check alias
					if (contributor == null) {
						contributor = contributorService.findByAlias(name);
					}
					if (contributor == null) {
						contributor = new Contributor(name);
						contributorService.save(contributor);
						logger.warn("I found a contributor I don't know about: '"
								+ name
								+ "'"
								+ " CHANGES jira: "
								+ jira.getJiraId());
					} else
						logger.debug("Matched contributor for name: " + name);
					if (currentCommit != null) {
						currentCommit.getContributors().add(contributor);
						commitService.save(currentCommit);
					} else if (associatedCommits != null) {
						for (Commit commit : associatedCommits) {
							commit.getContributors().add(contributor);
							commitService.save(commit);
						}
					} else
						logger.warn("Could not find any jira with jiraId:" + jira.getJiraId());
				}
			}
		}
	}
}