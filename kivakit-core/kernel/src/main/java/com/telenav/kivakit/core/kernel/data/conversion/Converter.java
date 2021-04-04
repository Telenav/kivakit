////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.conversion;

import com.telenav.kivakit.core.kernel.messaging.Repeater;
import com.telenav.kivakit.core.kernel.messaging.Transceiver;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataConversion;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A converter converts from one type to another. Converters are message {@link Repeater}s, relaying information about
 * any conversion issues to listeners.
 *
 * @param <From> Source type
 * @param <To> Destination type
 * @author jonathanl (shibo)
 * @see Repeater
 * @see Transceiver
 */
@UmlClassDiagram(diagram = DiagramDataConversion.class)
public interface Converter<From, To> extends Repeater
{
    /**
     * Convert from type &lt;From&gt; to type &lt;To&gt;.
     *
     * @param from The value to convert
     * @return The converted value
     */
    To convert(From from);
}
