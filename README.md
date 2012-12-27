## Checking Out Hadoop Projects

Hadoop Common

	git clone https://github.com/apache/hadoop-common.git
	cd hadoop-common && git log --numstat > hadoop-common.numstat

Ambari

	git clone https://github.com/apache/ambari.git
	git log --numstat > ambari.numstat

Oozie

	git clone https://github.com/apache/oozie.git
	git log --numstat > ambari.numstat

Flume

	git clone https://github.com/apache/flume.git
	git log --numstat > flume.numstat

Pig

	git clone https://github.com/apache/pig.git
	git log --numstat > pig.numstat
	
HCatalog

	git clone https://github.com/apache/hcatalog.git
	git log --numstat > hcatalog.numstat

## Running the Application

	mvn clean package
	./run.sh "Project Name" <path to numstat> <path to contributor metadata> <optional path to CHANGES.txt>
	
The application will parse the data in the numstat output and cross reference contributors and committers with the metadata CSV file.  Optionally it will parse the CHANGES.txt and update the graph accordingly.  The graph database will be created in the target/data/graph.db folder.  This can be opened up with Neoclipse to browse and query.

## Using [Neoclipse](https://github.com/neo4j/neoclipse/downloads)

Increase the Heap size for Neoclipse by editing the: `eclipse/neoclipse.app/Contents/MacOS/neoclipse.ini` file, and appending the following line to the file

	-Xmx2048m

Once that has been done open up neoclipse.