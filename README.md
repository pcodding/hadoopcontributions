## Checking out projects

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

## Running

Increase the Heap size for Neoclipse by editing the: `eclipse/neoclipse.app/Contents/MacOS/neoclipse.ini` file.

	-Xmx2048m
