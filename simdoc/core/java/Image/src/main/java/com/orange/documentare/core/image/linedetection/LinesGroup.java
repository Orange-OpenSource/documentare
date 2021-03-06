package com.orange.documentare.core.image.linedetection;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import java.util.ArrayList;

class LinesGroup extends ArrayList<Lines> {

  Lines asFlattenLines() {
    Lines lines = new Lines();
    for (Lines list : this) {
      lines.addAll(list);
    }
    return lines;
  }
}
