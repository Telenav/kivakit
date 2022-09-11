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

import com.telenav.kivakit.core.internal.lexakai.DiagramLanguage;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Simple utility methods that hide the mechanics of Java's stream support and spliterator.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguage.class)
@LexakaiJavadoc(complete = true)
public class Streams
{
    public static <T> Stream<T> parallelStream(Iterable<T> iterable)
    {
        return StreamSupport.stream(iterable.spliterator(), true);
    }

    public static <T> Stream<T> stream(Iterable<T> iterable)
    {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T> Stream<T> stream(Processing processing, Iterable<T> iterable)
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

    /**
     * Type of processing to apply to a stream
     */
    public enum Processing
    {
        PARALLEL,
        SEQUENTIAL
    }
}
