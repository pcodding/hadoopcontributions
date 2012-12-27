package com.hortonworks.service;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Service;

import com.hortonworks.domain.Contributor;
import com.hortonworks.repository.ContributorRepository;

@Service
public class ContributorService {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	ContributorRepository repo;

	public Iterable<Contributor> findAll() {
		Iterable<Contributor> commiters = repo.findAll();
		return commiters;
	}

	public void save(Contributor contributor) {
		repo.save(contributor);
	}

	public Contributor findByname(String name) {
		Contributor contributor = repo.findByPropertyValue("name", name);
		return contributor;
	}

	public Contributor findByAlias(String alias) {
		alias = alias.replaceAll("[\\(,\\)]", "");
		String query = "|*" + alias.toUpperCase().trim() + "*|";
		Contributor matchedContributor = null;
		try {
			EndResult<Contributor> results = repo.findAllByQuery("aliases",
					query);
			Iterator<Contributor> contributorIterator = results.iterator();
			while (contributorIterator.hasNext()) {
				Contributor contributor = contributorIterator.next();
				boolean match = false;
				String[] aliases = contributor.getAliases();
				for (int aliasIndex = 0; aliasIndex < aliases.length; aliasIndex++) {
					if (aliases[aliasIndex].equals(alias.toUpperCase()))
						match = true;
				}

				if (match) {
					logger.debug("Found matching contributor: "
							+ contributor.getName());
					matchedContributor = contributor;
					System.out.println(matchedContributor);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return matchedContributor;
	}
}
