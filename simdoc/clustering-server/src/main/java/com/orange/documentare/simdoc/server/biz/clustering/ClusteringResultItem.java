package com.orange.documentare.simdoc.server.biz.clustering;

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
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.system.inputfilesconverter.FilesMap;
import com.orange.documentare.simdoc.server.biz.FileIO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@EqualsAndHashCode
public class ClusteringResultItem {
  @ApiModelProperty(example = "january/ticket-beijing.pdf", required = true)
  public final String id;
  @ApiModelProperty(example = "3", required = true)
  public final int clusterId;


  static ClusteringResultItem[] buildItemsInBytesDataModeWithFilesPreparation(BytesData[] bytesData, SimClusteringItem[] simClusteringItems, FileIO fileIO) {
    ClusteringResultItem[] items = new ClusteringResultItem[simClusteringItems.length];

    return buildItemsInBytesDataMode(items, bytesData, simClusteringItems);
  }



  private static ClusteringResultItem[] buildItemsInBytesDataMode(ClusteringResultItem[] items, BytesData[] bytesData, SimClusteringItem[] simClusteringItems) {
    for (int i = 0; i < items.length; i++) {
      items[i] = new ClusteringResultItem(bytesData[i].id, simClusteringItems[i].getClusterId());
    }
    return items;
  }

}
