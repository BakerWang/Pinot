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
package com.linkedin.pinot.core.segment.index.readers;

import java.io.File;
import java.io.IOException;

import com.linkedin.pinot.common.segment.ReadMode;
import com.linkedin.pinot.core.index.reader.impl.FixedByteWidthRowColDataFileReader;
import com.linkedin.pinot.core.indexsegment.utils.ByteBufferBinarySearchUtil;


/**
 * Nov 13, 2014
 */

public abstract class ImmutableDictionaryReader implements Dictionary {

  protected final FixedByteWidthRowColDataFileReader dataFileReader;
  private final ByteBufferBinarySearchUtil fileSearcher;
  private final int rows;

  protected ImmutableDictionaryReader(File dictFile, int rows, int columnSize, ReadMode readMode) throws IOException {
    dataFileReader = FixedByteWidthRowColDataFileReader.createReader(dictFile, rows, 1, new int[] { columnSize }, readMode);
    this.rows = rows;
    fileSearcher = new ByteBufferBinarySearchUtil(dataFileReader);
  }

  protected int intIndexOf(int actualValue) {
    return fileSearcher.binarySearch(0, actualValue);
  }

  protected int floatIndexOf(float actualValue) {
    return fileSearcher.binarySearch(0, actualValue);
  }

  protected int longIndexOf(long actualValue) {
    return fileSearcher.binarySearch(0, actualValue);
  }

  protected int doubleIndexOf(double actualValue) {
    return fileSearcher.binarySearch(0, actualValue);
  }

  protected int stringIndexOf(String actualValue) {
    return fileSearcher.binarySearch(0, actualValue);
  }

  @Override
  public abstract int indexOf(Object rawValue);

  @Override
  public abstract Object get(int dictionaryId);

  @Override
  public abstract long getLongValue(int dictionaryId);

  @Override
  public abstract double getDoubleValue(int dictionaryId);

  @Override
  public abstract String toString(int dictionaryId);

  public void close() throws IOException {
    dataFileReader.close();
  }

  @Override
  public int length() {
    return rows;
  }
}
