////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.extraction;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.messaging.Broadcaster;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.Transceiver;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.extraction.internal.lexakai.DiagramExtraction;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * An extractor extracts a value from a source object of a given type.
 *
 * <p><b>Object Extraction</b></p>
 *
 * <p>
 * An extractor implements {@link BaseExtractor#onExtract(Object)} to extract a value from the source. In the following
 * code, a {@link Count} is extracted from a PbfWay:
 * </p>
 * <pre>
 * public class LaneCountExtractor extends BaseExtractor&lt;Count, PbfWay&gt;
 * {
 *     public LaneCountExtractor( Listener listener)
 *     {
 *         super(listener);
 *         this(listener, "lanes");
 *     }
 *
 *     public Count onExtract( PbfWay way)
 *     {
 *         Count laneCount;
 *
 *             [...]
 *
 *         return laneCount;
 *     }
 * }</pre>
 * <p>
 * Usage of the extractor is then like this (where 'this' implements {@link Repeater} or {@link Listener}):
 * </p>
 *
 * <pre>
 * var lanes = new LaneCountExtractor(this).extract(way);
 * </pre>
 *
 * @param <Value> The value type
 * @param <From> The object type
 * @author jonathanl (shibo)
 * @see Listener
 * @see Broadcaster
 * @see Transceiver
 */
@UmlClassDiagram(diagram = DiagramExtraction.class)
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public interface Extractor<Value, From> extends Broadcaster
{
    /**
     * @param object The object to extract from
     * @return The value extracted from the given object, if any
     */
    Value extract(From object);
}
