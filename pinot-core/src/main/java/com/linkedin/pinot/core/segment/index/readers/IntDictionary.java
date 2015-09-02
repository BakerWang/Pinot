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
import com.linkedin.pinot.core.segment.index.ColumnMetadata;


/**
 * Nov 14, 2014
 */

public class IntDictionary extends ImmutableDictionaryReader {

  public IntDictionary(File dictFile, ColumnMetadata metadata, ReadMode readMode) throws IOException {
    super(dictFile, metadata.getCardinality(), Integer.SIZE / 8, readMode);
  }

  @Override
  public int indexOf(Object rawValue) {
    Integer lookup;
    if (rawValue instanceof String) {
      lookup = Integer.parseInt((String) rawValue);
    } else {
      lookup = (Integer) rawValue;
    }

    return intIndexOf(lookup.intValue());
  }

  @Override
  public Integer get(int dictionaryId) {
    return new Integer(getInt(dictionaryId));
  }

  @Override
  public long getLongValue(int dictionaryId) {
    return new Long(getInt(dictionaryId));
  }

  @Override
  public double getDoubleValue(int dictionaryId) {
    return new Double(getInt(dictionaryId));
  }
  
  @Override
  public String getStringValue(int dictionaryId) {
    return new Integer(getInt(dictionaryId)).toString();
  }

  @Override
  public String toString(int dictionaryId) {
    return new Integer(getInt(dictionaryId)).toString();
  }

  private int getInt(int dictionaryId) {
    return dataFileReader.getInt(dictionaryId, 0);
  }

}
