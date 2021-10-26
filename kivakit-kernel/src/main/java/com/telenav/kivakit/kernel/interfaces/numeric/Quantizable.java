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

package com.telenav.kivakit.kernel.interfaces.numeric;

import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.interfaces.collection.Indexed;
import com.telenav.kivakit.kernel.interfaces.collection.LongKeyed;
import com.telenav.kivakit.kernel.interfaces.factory.MapFactory;
import com.telenav.kivakit.kernel.interfaces.model.Identifiable;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramInterfaceNumeric;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;

/**
 * A quantizable object can be turned into a quantum. A quantum is a discrete value and is represented by the Java
 * primitive type "long". Quanta are a unification point among classes which have a quantum representation, allowing
 * certain operations to be applied to quantizable objects uniformly (such as converting to and from string
 * representations or using the quantum value as an index or identifier in a map or data store).
 *
 * <p>
 * Examples of quantizable objects include objects that are {@link Indexed}, {@link Identifiable} or {@link LongKeyed}.
 * In addition, objects like {@link Time}, {@link Duration} and {@link Count} are quantizable. In the cases of time and
 * duration objects, the quantum is the number of milliseconds. Quantizable values can also be compared and checked for
 * zero or non-zero.
 * </p>
 *
 * <p>
 * Quantizable provides a converter which can convert between a String value and any {@link Quantizable} value. The
 * value is instantiated by passing the quantum to a {@link MapFactory}.
 * </p>
 *
 * <p>
 * Clients may extend {@link Quantizable.Converter} to quickly create a converter for any {@link Quantizable} value. For
 * example, EdgeIdentifier provides a converter between String values and EdgeIdentifiers like this:
 * </p>
 *
 * <pre>
 * public static class Converter extends Quantizable.Converter&lt;EdgeIdentifier&gt;
 * {
 *     public Converter(final Listener&lt;Message&gt; listener)
 *     {
 *         super(listener, EdgeIdentifier::new);
 *     }
 * }
 * </pre>
 *
 * <p>
 * A collection of quantizable values can be converted to an int[] with {@link #toIntArray(Collection)} or a long[] with
 * {@link #toLongArray(Collection)}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Converter
 * @see BaseStringConverter
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceNumeric.class)
public interface Quantizable extends DoubleQuantizable
{
    static Quantizable quantizable(final Integer value)
    {
        return () -> value;
    }

    static Quantizable quantizable(final Long value)
    {
        return () -> value;
    }

    static int[] toIntArray(final Collection<? extends Quantizable> values)
    {
        final var array = new int[values.size()];
        var index = 0;
        for (final Quantizable value : values)
        {
            array[index++] = (int) value.quantum();
        }
        return array;
    }

    static long[] toLongArray(final Collection<? extends Quantizable> values)
    {
        final var array = new long[values.size()];
        var index = 0;
        for (final Quantizable value : values)
        {
            array[index++] = value.quantum();
        }
        return array;
    }

    /**
     * Converter that converts between a {@link String} and any {@link Quantizable} value.
     *
     * @param <T> The type to convert to
     */
    @LexakaiJavadoc(complete = true)
    class Converter<T extends Quantizable> extends BaseStringConverter<T>
    {
        private final MapFactory<Long, T> factory;

        public Converter(final Listener listener, final MapFactory<Long, T> factory)
        {
            super(listener);
            this.factory = factory;
        }

        @Override
        protected T onToValue(final String value)
        {
            return factory.newInstance(Long.parseLong(value));
        }
    }

    @Override
    default double doubleQuantum()
    {
        return quantum();
    }

    default boolean isGreaterThan(final Quantizable that)
    {
        return quantum() > that.quantum();
    }

    default boolean isGreaterThanOrEqualTo(final Quantizable that)
    {
        return quantum() >= that.quantum();
    }

    default boolean isLessThan(final Quantizable that)
    {
        return quantum() < that.quantum();
    }

    default boolean isLessThanOrEqualTo(final Quantizable that)
    {
        return quantum() <= that.quantum();
    }

    default boolean isNonZero()
    {
        return quantum() != 0;
    }

    default boolean isZero()
    {
        return quantum() == 0;
    }

    /**
     * @return The discrete value for this object
     */
    long quantum();
}
