////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.extraction;

import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.messaging.Broadcaster;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Repeater;
import com.telenav.kivakit.core.kernel.messaging.Transceiver;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataExtraction;
import com.telenav.lexakai.annotations.UmlClassDiagram;

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
 *     public LaneCountExtractor(final Listener listener)
 *     {
 *         super(listener);
 *         this(listener, "lanes");
 *     }
 *
 *     public Count onExtract(final PbfWay way)
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
@UmlClassDiagram(diagram = DiagramDataExtraction.class)
public interface Extractor<Value, From> extends Broadcaster
{
    /**
     * @param object The object to extract from
     * @return The value extracted from the given object, if any
     */
    Value extract(From object);
}
