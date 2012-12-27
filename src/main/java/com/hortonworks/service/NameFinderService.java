package com.hortonworks.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class NameFinderService implements InitializingBean {
	private Logger logger = Logger.getLogger(this.getClass());
	private String personModelPath = new String("en-ner-person.bin");
	private String tokenModelPath = new String("en-token.bin");
	private String excludeRegex = new String(
			".*(Upgrade|FsUrlStreamHandlerFactory|Fix|Resource|Add|Remove|Setup|Change|Mac OS|Generalize|NPE|Refactor|Replace|Test|FSCK|FSData|Allow|Data|Service|NullPointer|JobTracker|Respsect|FSName|Forgot|Context|FileUtil|Improve|DFS|GridMix|Provide|DBInputFormat|DataNode|Sqoop|Updating|NameNode|FileSystem|Balancer|API|Distinguish|Marks|RPC|DistC|Removes|Datanode|InputStream|Copy|Disable|Copy|SequenceFile|TextOuputFormat|Deprecate|SocketIO|DFS|HMaster|NetworkTopology|ArrayList|ChecksumFile|Iterable|Ensure|INodeDirectory|Hadoop|CRC|FsShell|CHANGES|AlreadyBeing|MapReduce|EC2|GenericWriteable|Rename|Unknown|Maven|AppID|EditLog|JSON|BlockManager|Authentication|JobHistory|HsController|HttpServer|FileInputFormat|Text|Eclipse|Inode|Block|Wait|Respect|Protect|Slow|Input|Introduce|RAM|Also|Modify|Writable|Clients|HADOOP|JobClient|Ambari|Registration|Ruby|HBase|Hive|Uninstall|Ganglia|Nagios|JDK|32-bit|Implement|Avro|Loader|EvalFunc|Spaces|StoreFunc|hadoop|).*");
	Tokenizer tokenizer;
	NameFinderME nameFinder;

	public Collection<String> findNames(String description) {
		String tokens[] = tokenizer.tokenize(description);
		Span[] names = nameFinder.find(tokens);
		Collection<String> detectedNames = new LinkedList<String>();

		for (int spanIndex = 0; spanIndex < names.length; spanIndex++) {
			String name = new String();
			for (int arrayIndex = names[spanIndex].getStart(); arrayIndex < names[spanIndex]
					.getEnd(); arrayIndex++)
				name += tokens[arrayIndex] + " ";
			name = name.replaceAll("\\(|\\)", "");
			logger.debug("Identified name: " + name);
			if (!name.matches(excludeRegex))
				detectedNames.add(name.trim());
			else
				logger.debug("Found excluded name: " + name);
		}
		nameFinder.clearAdaptiveData();
		detectedNames = processHints(description);
		return detectedNames;
	}

	public Collection<String> processHints(String description) {
		Collection<String> hints = new LinkedList<String>();
		String[] regexes = { "Contributed by (.*)",
				"\\(([\\w*, ,\\,]*)\\Wvia (\\w*)\\)" };
		for (String regex : regexes) {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(description);
			if (matcher.find()) {
				String name = matcher.group(1).replaceAll("(\\.$|\\)$)", "");
				if (name.contains(" and ")) {
					String[] names = name.split(" and ");
					for (String cname : names) {
						logger.debug("found a contributor via hint: " + cname);
						hints.add(cname);
					}
				} else if (name.contains(",")) {
					String[] names = name.split("\\,");
					for (String cname : names) {
						logger.debug("found a contributor via hint: " + cname);
						hints.add(cname.replaceAll("^ ", ""));
					}
				} else {
					logger.debug("found a contributor via hint: " + name);
					hints.add(name);
				}
			}
		}
		return hints;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			TokenizerModel model = new TokenizerModel(new FileInputStream(
					tokenModelPath));
			tokenizer = new TokenizerME(model);

			TokenNameFinderModel personModel = new TokenNameFinderModel(
					new FileInputStream(personModelPath));
			nameFinder = new NameFinderME(personModel);
		} catch (InvalidFormatException e) {
			logger.error(e.getMessage(), e);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
}