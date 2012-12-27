#!/bin/bash
cd target
java -Xmx1024m -jar hadoop-contributions.jar "$@" 
