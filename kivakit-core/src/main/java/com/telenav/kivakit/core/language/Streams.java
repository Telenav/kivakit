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

package com.telenav.kivakit.core.language;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramLanguage;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Simple utility methods that hide the mechanics of Java's stream support and spliterator.
 *
 * <p><b>Streams</b></p>
 *
 * <ul>
 *     <li>{@link #stream(Iterable)}</li>
 *     <li>{@link #parallelStream(Iterable)}</li>
 *     <li>{@link #stream(Processing, Iterable)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguage.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public class Streams
{
    /**
     * Returns a parallel stream for an iterable
     */
    public static <T> Stream<T> parallelStream(Iterable<T> iterable)
    {
        return StreamSupport.stream(iterable.spliterator(), true);
    }

    /**
     * Returns a stream for an iterable
     */
    public static <T> Stream<T> stream(Iterable<T> iterable)
    {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * Returns a parallel or sequential stream for the given iterable
     */
    public static <T> Stream<T> stream(Processing processing, Iterable<T> iterable)
    {
        return switch (processing)
            {
                case PARALLEL -> parallelStream(iterable);
                default -> stream(iterable);
            };
    }

    /**
     * Type of processing to apply to a stream
     */
    public enum Processing
    {
        PARALLEL,
        SEQUENTIAL
    }
}
