package com.orange.documentare.core.prepdata;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Denis Boisset & Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.system.inputfilesconverter.FilesMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Metadata {
  public final String inputDirectoryPath;
  public final FilesMap filesMap;
  public final boolean rawConversion;
}
