#!/bin/sh

rm -rf out *.csv
java -jar target/Graph-1.0-SNAPSHOT.jar $1 $2 $3 $4 $5 $6
