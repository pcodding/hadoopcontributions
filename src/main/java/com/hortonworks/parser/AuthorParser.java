package com.hortonworks.parser;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hortonworks.domain.Committer;
import com.hortonworks.service.CommitterService;

@Service
public class AuthorParser {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private CommitterService committerService;

	public AuthorParser() {

	}

	public void completeParse() {

	}

	public void parseAuthor(String author) {
		String[] pieces = author.split(" -> ");
		String left = pieces[0];
		String right = pieces[1];
		Committer committer = committerService.findByUserId(left);
		if (committer != null)
			committer.getAliases().add(right);
		else {
			committer = committerService.findByUserId(right);
			if (committer != null)
				committer.getAliases().add(left);
		}
		if (committer != null) {
			logger.debug("Left: " + left + " -- Right: " + right);
			committerService.save(committer);
		}
	}

	public CommitterService getCommitterService() {
		return committerService;
	}

	public void setCommitterService(CommitterService committerService) {
		this.committerService = committerService;
	}
}
