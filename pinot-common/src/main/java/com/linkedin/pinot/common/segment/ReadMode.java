/**
 * Copyright (C) 2014-2015 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.common.segment;

public enum ReadMode {
  MMAP, //mmaps the data, os manages the paging. 
  DIRECT_MEMORY, //Direct memory ByteBuffer.allocateDirect
  HEAP, //allocate byte array and loads data into byte array. Wraps the byteArray into a byteBuffer
  HEAP_UNCOMPRESSED, //uncompresses the data into its data type on load, for example a fixed bit compressed data will become short/int/long 
  NATIVE; //uses UNSAFE mode for loading, this will bypass java layer completely and does raw memory access.

  public static ReadMode getEnum(String strVal) {
    if (strVal.equalsIgnoreCase("heap")) {
      return HEAP;
    }
    if (strVal.equalsIgnoreCase("mmap") || strVal.equalsIgnoreCase("memorymapped")
        || strVal.equalsIgnoreCase("memorymap")) {
      return MMAP;
    }
    if (strVal.equalsIgnoreCase("direct_memory")) {
      return DIRECT_MEMORY;
    }
    if (strVal.equalsIgnoreCase("heap_uncompressed")) {
      return HEAP_UNCOMPRESSED;
    }
    if (strVal.equalsIgnoreCase("native")) {
      return NATIVE;
    }

    throw new IllegalArgumentException("Unknown String Value: " + strVal);
  }
}
