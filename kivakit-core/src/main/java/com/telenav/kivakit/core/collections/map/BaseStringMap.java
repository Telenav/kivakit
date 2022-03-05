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

package com.telenav.kivakit.core.collections.map;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.language.primitive.Booleans;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.core.project.lexakai.DiagramCollections;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Estimate;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.core.value.count.Minimum;
import com.telenav.kivakit.core.value.identifier.Identifier;
import com.telenav.kivakit.core.value.identifier.IntegerIdentifier;
import com.telenav.kivakit.core.value.identifier.StringIdentifier;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.core.version.Version;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A bounded map from string to value. Because KivaKit string maps support type conversion convenience methods, they
 * implement {@link RepeaterMixin} and broadcast {@link Problem}s when conversions fail.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
public abstract class BaseStringMap<Value> extends BaseMap<String, Value> implements
        RepeaterMixin,
        TryTrait
{
    protected BaseStringMap(Maximum maximumSize)
    {
        super(maximumSize);
    }

    protected BaseStringMap(Maximum maximumSize, Map<String, Value> map)
    {
        super(maximumSize, map);
    }

    public boolean asBoolean(String key)
    {
        var value = get(key).toString();
        return Booleans.isTrue(value);
    }

    public Boolean asBooleanObject(String key)
    {
        return convert(key, Booleans::isTrue, Boolean.class);
    }

    public Bytes asBytes(String key)
    {
        return convert(this, key, Bytes::parseBytes, Bytes.class);
    }

    public Count asCount(String key)
    {
        return convert(this, key, Count::parseCount, Count.class);
    }

    /**
     * @return The value of the given key as a double, or an exception is thrown if the value is invalid or missing
     */
    public double asDouble(String key)
    {
        return Double.parseDouble(key);
    }

    /**
     * Returns the value of the given key as a {@link Double} object, or broadcasts a {@link Problem} and returns null
     * if the value is invalid or missing
     */
    public Double asDoubleObject(String key)
    {
        return convert(key, Double::parseDouble, Double.class);
    }

    public Estimate asEstimate(String key)
    {
        return convert(this, key, Estimate::parseEstimate, Estimate.class);
    }

    public Identifier asIdentifier(String key)
    {
        return convert(key, value -> new Identifier(Long.parseLong(value)), Identifier.class);
    }

    /**
     * @return The value of the given key as an integer, or an exception is thrown if the value is invalid or missing
     */
    public int asInt(String key)
    {
        return Integer.parseInt(asString(key));
    }

    public IntegerIdentifier asIntegerIdentifier(String key)
    {
        return convert(key, value -> new IntegerIdentifier(Integer.parseInt(value)), IntegerIdentifier.class);
    }

    /**
     * Returns the value of the given key as a {@link Integer} object, or broadcasts a {@link Problem} and returns null
     * if the value is invalid or missing
     */
    public Integer asIntegerObject(String key)
    {
        return convert(key, Integer::parseInt, Integer.class);
    }

    /**
     * @return The value of the given key as a long, or an exception is thrown if the value is invalid or missing
     */
    public long asLong(String key)
    {
        return Long.parseLong(asString(key));
    }

    /**
     * Returns the value of the given key as a {@link Long} object, or broadcasts a {@link Problem} and returns null if
     * the value is invalid or missing
     */
    public Long asLongObject(String key)
    {
        return convert(key, Long::parseLong, Long.class);
    }

    public Maximum asMaximum(String key)
    {
        return convert(this, key, Maximum::parseMaximum, Maximum.class);
    }

    public Minimum asMinimum(String key)
    {
        return convert(this, key, Minimum::parseMinimum, Minimum.class);
    }

    /**
     * @return The given key as a path with no trailing slash
     */
    public String asPath(String key)
    {
        var value = asString(key);
        return value == null ? null : Strip.trailing(value, "/");
    }

    /**
     * @return The given value as a {@link Percent}
     */
    public Percent asPercent(String key)
    {
        return convert(this, key, Percent::parsePercent, Percent.class);
    }

    public String asString(String key)
    {
        return (String) super.get(key);
    }

    public StringIdentifier asStringIdentifier(String key)
    {
        var value = get(key).toString();
        return new StringIdentifier(value);
    }

    public StringList asStringList()
    {
        var entries = new StringList();
        var keys = new ArrayList<>(keySet());
        keys.sort(Comparator.naturalOrder());
        for (var key : keys)
        {
            entries.add(key + " = " + get(key));
        }
        return entries;
    }

    /**
     * @return The given value as a {@link URI}
     */
    public URI asUri(String key)
    {
        return convert(key, URI::create, URI.class);
    }

    /**
     * @return The given value as a {@link Version}
     */
    public Version asVersion(String key)
    {
        return convert(key, Version::version, Version.class);
    }

    /**
     * @return The keys and values of this map separated by the given separator
     */
    public String join(String separator)
    {
        return asStringList().join(separator);
    }

    @Override
    public String toString()
    {
        return join("\n");
    }

    private <T> T convert(String key, Function<String, T> converter, Class<T> type)
    {
        if (key != null)
        {
            var value = get(key);
            if (value != null)
            {
                return tryCatch(() -> converter.apply(value.toString()),
                        "Unable to convert [$] ==> $ to $", key, value, type.getSimpleName());
            }
            else
            {
                problem("Null value for key: $", key);
            }
        }
        else
        {
            problem("Null key");
        }
        return null;
    }

    private <T> T convert(Listener listener, String key, BiFunction<Listener, String, T> converter, Class<T> type)
    {
        if (key != null)
        {
            var value = get(key);
            if (value != null)
            {
                return tryCatch(() -> converter.apply(listener, value.toString()),
                        "Unable to convert [$] ==> $ to $", key, value, type.getSimpleName());
            }
            else
            {
                problem("Null value for key: $", key);
            }
        }
        else
        {
            problem("Null key");
        }
        return null;
    }
}
