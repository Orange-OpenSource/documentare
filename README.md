[![Build Status](https://travis-ci.org/Orange-OpenSource/documentare-simdoc.svg?branch=master)](https://travis-ci.org/Orange-OpenSource/documentare-simdoc)
[![License](https://img.shields.io/aur/license/yaourt.svg?maxAge=2592000)](https://github.com/Orange-OpenSource/documentare-simdoc/blob/master/LICENSE)

# Documentare, SimDoc library & tools
Library and tools for similarity measurement, classification and clustering of digital content and segmentation images from digitized document.

## Build

Prerequisites:
 - java 8
 - you need to install a rebuilt version of graphviz, provided here: https://gitlab.com/Orange-OpenSource/documentare/documentare-graphviz
 - you need to install convert (imagemagick)

More information about graphviz are provided under `doc/graphviz/README.md`

To build the core library: ```cd simdoc/core/java && ./mvnw clean install```

To build tools (***LineDetection***,***NCD***, etc): ```cd simdoc/apps/ && ./mvnw clean install```

## Introduction

This software bundle is aimed for computer-aided transcription of digitised document produced with scanners or digital cameras. But other uses are allowed for NCD and SimClustering. Following tools are implemented:

1)	***LineDetection***: picture segmentation and potential glyphs extraction. OpenCV is needed.

2)	[***NCD***](./doc/text/NCD_theory.md): similarity distance matrix computing of content or parts of content (i.e. glyphs) with an unsupervised and agnostic way of measurement.

3)	[***SimClustering***](./doc/text/clustering.md): unsupervised and agnostic clustering of similar pattern. For visualisation and post-treatments, GraphViz (including gvmap) is needed.

## General syntax

### used notation

	$: shell prompt.
	{fic}: file, directory or program name.
	[param n]: optional computing parameter, 
	if present, n is the value of the current parameter.

### general syntax

command launching is done with a classical way in a shell window.
	
	$ java –jar /{jar_directory}/{prog_name} {fic1} {fic2}… [param1 [value]] [param2 [value]]…
	prog_name: {LineDetection-1.0.0.jar|Ncd-1.0.0.jar|Simclustering-1.0.0.jar}
	
All results are stored in json format (and csv for short analysing with Excel in NCD) which names are beginning with the name of the program that produces this json file.

Following processes must be done for obtaining a visualization graph :
	
	- For image of documents : LineDetection, NCD, PrepClustering, SimClustering, Graph 
	and your GraphViz script
	
	- For content in directories : NCD, PrepClustering, SimClustering, Graph
	and your GraphViz script

## Programs functionalities

### LineDetection
#### Option for LinDetection

	{file_name}

#### Goal
This tool is used for digitised document segmentation and glyphs (characters or symbols) extraction. Segmentation is done in a very classical way by using adaptive thresholding and connected component extraction in a digital picture using OpenCV algorithms. Identification and extraction of glyphs use a statistical approach on pattern size and topological neighbourhood for computing
	
- Text lines in the document,
- Glyphs and character size in a text line.

Results are stored in a JSON format file named **ld\_page\_geom\_ready\_for\_ncd.json.gz** , pattern representing glyphs and characters are stored in a directory named ld_out, and a layered picture showing obtained segmentation is produced, named bin.png. Produced data are stored in current directory.

### Computing a matrix distance of similarity and preparing clustering

Since version 1.13, This function offers two modes in order to allow computing on a big set of data and to reduce the size of results : mode "Regular" applyied on files directories and "SimDoc" applyied to digitized pictures of documents.

### Global goal

The goal of this set of tools consists in computing a distance lattice between pairs of files in a directory or pairs of segmented pictures containing glyphs and obtained with LineDetection in a digitized document.

The regular or SimDoc modes are invoked through a parameter in NCD command line (-simDocJsonGz <file path>) : by using this parameter NCD will work in SimDoc mode (need of a json file generated by LineDetection. Otherwise, NCD will work in regular mode.

### NCD

#### SimDoc mode

This mode is dedicated to segmented image of documents produced with LineDetection with the aim to apply our experimental OCR, using Zenobie interface. 

It is NOT available for general content analysis.

In order to produce ready for clustering data, one have just to apply NCD program before clustering With program SimClustering.


#### Regular mode 

In this mode we use 2 programs NCD and PrepClustering 

**NCD** computes a distance matrix and stores results in a json file with a generic presentation of array and named **ncd\_regular\_files\_model.json.gz**

**PrepClustering** builds an index of each files and computes a kind of topology based on distance triangles computed from the matrix generated by NCD. This matrix can be filtered by specifying a value of neighborhood for each element. This parameter samples a pannel of relevant distances : we take each element with its k nearest neigbours.   

json file name generated by PrepClustering is **prep\_clustering\_ready.json.gz** 


 
#### Options for NCD

 	-file1 <file path>          first file (must be a directory)
 	-file2 <file path>          second file (must be a directory), not
                                mandatory (we will assume file2 = file1)
 	-help                       print this message
 	-simDocJsonGz <file path>   SimDoc model json gzip file
 	
#### Options for PrepClustering

	-f <file path>   NCD input file
 	-help            print this message
 	-k <knn>         k nearest neighbours
 	-writeCSV        write WRITE_CSV files (matrix, nearest)


### SimClustering
#### Option for SimClustering

 	-ccut                                              enable clusters scalpel cut post treatments
	-h                                                 print this message
 	-i <distances json gzip file path>                 path to json gzip file containing items distances
	-scut                                              enable subgraphs scalpel cut post treatments
	-sdArea <graph scissor Area SD factor>             graph scissor area's standard deviation factor
 	-sdQ <graph scissor Q SD factor>                   graph scissor equilaterality's standard deviation factor
	-sdScut <subgraph scalpel SD factor>               subgraph scalpel standard deviation factor
	-simDocJsonGz <SimDoc json gzip file path>         path to json gzip file containing SimDoc model ready for clustering
	-tileCcut <cluster scalpel percentile threshold>   cluster scalpel percentile threshold
 	-wcut                                              enable subgraphs wonder cut post treatments

#### Goal
Cluster computing of pattern where similarities are evaluated with NCD. This tool includes different steps corresponding with a statistical and progressive refinement of parameters. At each step, patterns are excluded from a subgraph or cluster and all excluded patterns are coded as “singletons”, representing a rejecting method in cluster computing. We plan a recall strategy for these “singletons” in a next version, based on using NCD in classifier mode on multisets assembled from consistent, and indexed by values, clusters.

-	**Step 1**: this step uses a global view of distance matrix produced with NCD:
A triangulation is processed on matrix distance; each triangle is build from the current element. The second summit is the first neighbour of current element and the third summit is the first neighbour of second summit. Consequently, edges of a triangle are given with: distance between first and second summit, second and third summit, first and third summit. For each triangle, area and an equilaterality factor are computed and stored in a histogram. This allows computing averages and standard deviation that are used to prune triangles and build subgraphs representing a rough segmentation of similar patterns. The sensitivity of the pruning can be adjusted by a factor applied on standard deviation on triangles surface and equilaterality (default value 2).
This algorithm induces redundancy in edges, matching to adjacent triangles. This redundancy is used to evaluate the best representative pattern in a cluster.

-	**Step 2**: this step is focused on edges of subgraphs obtained in step 1.
The computing method deals no more with triangles but with edges of subgraphs.
The goal consists of obtaining homogenous and isotropic clusters of similar patterns. The method consists of pruning from edges lengths using statistical factors based on averages and standard deviation. We have implemented two modes of pruning: a fast, but less accurate, mode, and an iterative mode.

	-	The fast mode preserves only minimal distances edges, connected on centroids in subgraphs. Centroids are determined from redundant edges matching to adjacent triangles computed in step 1.
	-	The iterative mode cuts edges until all edges lengths in a cluster are included in a threshold computed from average and standard deviation of each remaining subgraphs. The sensitivity of pruning can be adjusted with a factor applied on standard deviation (default value 1).
Obtaining cluster from subgraphs uses a near Voronoï network algorithm, as implemented in GraphViz (gvmap package). This method computes cluster boundaries inside a subgraph, depending with local minimal distances on centroids.

-	**Step 3**: pruning clusters.
This step is optional. It consists on a edge pruning using a quantile parameter to eliminate isolated extrema (default value, the third quartile). 

Results are stored with a json mode file in sc_page_geom.json.gz to be used with Zenobie. For using with GraphViz, the file is **sc\_graph\_input.json.gz**.

## Sequences of processes
Depending of using regular or SimDoc mode, these sequences of programs must be applyed :

**regular mode** : NCD-PrepClustering-SimClustering

**SimDoc mode** : NCD-SimClustering

## About thumbnails

NCD computes by default thumbnails, using "convert" program from ImageMagick package (ensure that this distribution is installed on your computer) of documents coded in ".png", ".jpg", ".jpeg", ".tif", ".tiff" and ".pdf" format. Pictures are stored in "**./thumbnails**" subdirectory of current NCD launch directory.


## Other utilities

### Exporting graphs to GraphViz

GraphViz use a dedicated file format named “dot”. Its an ASCII-like format whose specification can be found in GraphViz website. To build a dot file from documentare, use the “Graph” utility (Graph-1.0.0.jar). It produces a file named graph.dot wich is fully manageable with GraphViz. The Graph utility syntax allows you to add thumbnails of pictures (the content in ld\_out) ore reduced pictures from ld\_out to a specific directory.
Option of graph utility

 	-d <image directory>             directory containing images of vertices
 	-h                               print this message
 	-i <graph json gzip file path>   path to json gzip file containing graph
 	-simDocJsonGz                    simDocJson mode, will add .png extension automatically

### Viewing graphs with GraphViz
A complete documentation is stored on GraphViz Website. Here is an example of syntax usable to view a graph in svg format.

	$ sfdp -Goverlap=prism -Gcharset=latin1 graph.dot | gvmap -e | neato -n2 -Tsvg > g.svg
	
## Helpful java options
	-Xmx<n>                Specifies the maximum size, in bytes, of the memory
                           allocation  pool.  This value must be a multiple of
                           1024 greater than 2 MB.  Append the letter k  or  K
                           to  indicate  kilobytes, the letter m or M to indi-
                           cate megabytes, the letter g or G to indicate giga-
                           bytes,  or the letter t or T to indicate terabytes.
                           The default value is 64MB. (useful on regular mode 
                           with a big set of data)
	
	-XX:+HeapDumpOnOutOfMemoryError
						   As named : will dump memory content when program crashes
						   in a ".hprof" file 
						   
# License
@Copyright 2016 Orange

This software is distributed under the terms of the GPL v2 license, please see license file.

# Authors
Joël Gardes and Christophe Maldivi
