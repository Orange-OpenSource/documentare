package com.orange.documentare.app.clusteringremote;

/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.File;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ClusteringRequest {
  public final String url;
  public final String inputDirectory;
  public final BytesData[] bytesData;
  public final String outputDirectory;
  public final Boolean debug;
  public final Float acutSdFactor;
  public final Float qcutSdFactor;
  public final Float scutSdFactor;
  public final Integer ccutPercentile;
  public final Boolean wcut;
  public final Integer kNearestNeighboursThreshold;
  public final Boolean sloop;

  public static ClusteringRequestBuilder builder() {
    return new ClusteringRequestBuilder();
  }

  public static class ClusteringRequestBuilder {
    public String url;
    private String inputDirectory;
    private BytesData[] bytesData;
    private String outputDirectory;
    private Boolean debug;
    private Float acutSdFactor;
    private Float qcutSdFactor;
    private Float scutSdFactor;
    private Integer ccutPercentile;
    private Boolean wcut;
    private Integer kNearestNeighboursThreshold;
    private Boolean sloop;

    private ClusteringRequestBuilder() {
    }

    public ClusteringRequest build() {
      return new ClusteringRequest(url, inputDirectory, bytesData, outputDirectory, debug, acutSdFactor, qcutSdFactor, scutSdFactor, ccutPercentile, wcut, kNearestNeighboursThreshold, sloop);
    }

    public ClusteringRequestBuilder url(String url) {
      this.url = url;
      return this;
    }

    public ClusteringRequestBuilder inputDirectory(String inputDirectory) {
      BytesData[] bytesData = BytesData.loadFromDirectory(new File(inputDirectory), File::getName);
      this.bytesData(bytesData);
      return this;
    }

    public ClusteringRequestBuilder bytesData(BytesData[] bytesData) {
      this.bytesData = bytesData;
      return this;
    }

    public ClusteringRequestBuilder outputDirectory(String outputDirectory) {
      this.outputDirectory = outputDirectory;
      return this;
    }

    public ClusteringRequestBuilder debug() {
      debug = true;
      return this;
    }

    public ClusteringRequestBuilder acut(float acutSdFactor) {
      this.acutSdFactor = acutSdFactor;
      return this;
    }

    public ClusteringRequestBuilder qcut(float qcutSdFactor) {
      this.qcutSdFactor = qcutSdFactor;
      return this;
    }

    public ClusteringRequestBuilder scut(float scutSdFactor) {
      this.scutSdFactor = scutSdFactor;
      return this;
    }

    public ClusteringRequestBuilder ccut(int ccutPercentile) {
      this.ccutPercentile = ccutPercentile;
      return this;
    }

    public ClusteringRequestBuilder wcut() {
      wcut = true;
      return this;
    }

    public ClusteringRequestBuilder kNearestNeighboursThreshold(int kNearestNeighboursThreshold) {
      this.kNearestNeighboursThreshold = kNearestNeighboursThreshold;
      return this;
    }

    public ClusteringRequestBuilder sloop() {
      sloop = true;
      return this;
    }
  }
}
