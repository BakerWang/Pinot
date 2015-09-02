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
package com.linkedin.pinot.core.indexsegment.columnar;

import java.io.File;

import com.linkedin.pinot.common.metadata.segment.IndexLoadingConfigMetadata;
import com.linkedin.pinot.common.segment.ReadMode;
import com.linkedin.pinot.common.segment.SegmentMetadata;
import com.linkedin.pinot.core.indexsegment.IndexSegment;
import com.linkedin.pinot.core.segment.index.loader.Loaders;


/**
 *
 * July 19, 2014
 */
public class ColumnarSegmentLoader {
  public static IndexSegment load(File indexDir, ReadMode mode) throws Exception {
    return load(indexDir, mode, null);
  }

  public static IndexSegment load(File indexDir, ReadMode mode, IndexLoadingConfigMetadata indexLoadingConfigMetadata)
      throws Exception {
    return loadSegment(indexDir, mode, indexLoadingConfigMetadata);
  }

  public static IndexSegment loadSegment(SegmentMetadata segmentMetadata, ReadMode readMode,
      IndexLoadingConfigMetadata indexLoadingConfigMetadata) throws Exception {
    return loadSegment(new File(segmentMetadata.getIndexDir()), readMode, indexLoadingConfigMetadata);
  }

  public static IndexSegment loadSegment(File indexDir, ReadMode readMode,
      IndexLoadingConfigMetadata indexLoadingConfigMetadata) throws Exception {
    return Loaders.IndexSegment.load(indexDir, readMode, indexLoadingConfigMetadata);
  }

}
