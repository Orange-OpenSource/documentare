package com.orange.documentare.core.comp.distance.filesdistances;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orange.documentare.core.model.ref.comp.DistanceItem;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
public class FileDistanceItem implements DistanceItem {
  /** file name with directory hierarchy under provided root directory */
  public final String relativeFilename;

  /** transient bytes array, released after the distance computation */
  @JsonIgnore
  private byte[] bytes;

  FileDistanceItem(File file, String rootDirectoryPath) throws IOException {
    relativeFilename = file.getAbsolutePath().replaceFirst(rootDirectoryPath, "");
    bytes = readBytes(file);
  }

  /**
   * cunning trick, use file path to detect equality, to speed up NCD
   * NB: if files are equal but duplicated with distinct file names, NCD will have to
   *     compute the distance anyway...
   */
  @Override
  public boolean equals(Object obj) {
    FileDistanceItem otherItem = (FileDistanceItem) obj;
    return relativeFilename.equals(otherItem.relativeFilename);
  }

  private byte[] readBytes(File file) throws IOException {
    // FIXME: using direct buffer mapped on File could avoid keeping all data in memory...
    return FileUtils.readFileToByteArray(file);
  }

  void releaseBytes() {
    bytes = null;
  }

  @Override
  @JsonIgnore
  public String getHumanReadableId() {
    // FIXME: to remove after simdoc mode review?
    return null;
  }

  @Override
  public byte[] getBytes() {
    return bytes;
  }
}
