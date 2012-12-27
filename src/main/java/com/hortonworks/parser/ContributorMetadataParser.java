package com.hortonworks.parser;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hortonworks.domain.Contributor;
import com.hortonworks.domain.Employer;
import com.hortonworks.service.ContributorService;
import com.hortonworks.service.EmployerService;

@Service
public class ContributorMetadataParser {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private ContributorService contributorService;
	@Autowired
	private EmployerService employerService;

	public void parseMetaData(String contributorMetadata) {
		logger.debug(contributorMetadata);
		String alias = null;
		String standardizedName = null;
		String employerOrgName = null;
		Employer employer = null;
		if (contributorMetadata != null) {
			String pieces[] = contributorMetadata.split(",");
			alias = pieces[0];
			if (pieces.length > 1)
				standardizedName = pieces[1];
			if (pieces.length > 2) {
				employerOrgName = pieces[2];
				if (employerOrgName != null) {
					employer = employerService.findByOrgName(employerOrgName);
					if (employer == null) {
						employer = new Employer(employerOrgName);
						employerService.save(employer);
						logger.info("Creating new employer: '" + employerOrgName + "'");
					}
				}
			} else
				logger.warn("I need more information for contributor: '" + alias + "'");
			if (standardizedName != null) {
				Contributor contributor = contributorService
						.findByname(standardizedName);
				if (contributor == null) {
					contributor = new Contributor(standardizedName);
					logger.info("Creating new contributor: '" + standardizedName + "'");
				}
				if (employer != null)
					contributor.setEmployer(employer);
				contributor.addAlias(alias);
				contributorService.save(contributor);
			}
		}
	}
}
