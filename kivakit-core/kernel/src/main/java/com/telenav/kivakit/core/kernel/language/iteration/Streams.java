////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.iteration;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageIteration;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Simple utility methods that hide the mechanics of Java's stream support and spliterator.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageIteration.class)
public class Streams
{
    public static <T> Stream<T> parallelStream(final Iterable<T> iterable)
    {
        return StreamSupport.stream(iterable.spliterator(), true);
    }

    public static <T> Stream<T> stream(final Iterable<T> iterable)
    {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T> Stream<T> stream(final Processing processing, final Iterable<T> iterable)
    {
        switch (processing)
        {
            case PARALLEL:
                return parallelStream(iterable);

            case SEQUENTIAL:
            default:
                return stream(iterable);
        }
    }

    public enum Processing
    {
        PARALLEL,
        SEQUENTIAL
    }
}
