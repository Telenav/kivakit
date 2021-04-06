////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
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
