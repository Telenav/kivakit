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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.core.language.primitive.Booleans;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.broadcasters.GlobalRepeater;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
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

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;

/**
 * A bounded map from string to value. Because KivaKit string maps support type conversion convenience methods, they
 * implement {@link RepeaterMixin} and broadcast {@link Problem}s when conversions fail.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCollections.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTED)
public abstract class BaseStringMap<Value> extends BaseMap<String, Value> implements
        GlobalRepeater,
        TryTrait
{
    /**
     * Constructs a map with the given maximum size
     */
    protected BaseStringMap(Maximum maximumSize)
    {
        super(maximumSize);
    }

    /**
     * Constructs a map with the given maximum size and initial values
     */
    protected BaseStringMap(Maximum maximumSize, Map<String, Value> that)
    {
        super(maximumSize, that);
    }

    /**
     * Retrieves the value for the given key as a boolean. If the conversion from String to object fails, a problem is
     * transmitted.
     *
     * @return The boolean value for the given key, or the default value if there is no value for the key
     */
    public Boolean asBoolean(String key, boolean defaultValue)
    {
        return keyMissing(key) ? defaultValue : asBoolean(key);
    }

    /**
     * Retrieves the value for the given key as a {@link Boolean}. If the conversion from String to object fails, a
     * problem is transmitted.
     *
     * @return The {@link Boolean} value for the given key, or null if there is no value for the key
     */
    public Boolean asBoolean(String key)
    {
        return convert(key, Booleans::isTrue, Boolean.class);
    }

    /**
     * Retrieves the value for the given key as a {@link Bytes} object. If the conversion from String to object fails, a
     * problem is transmitted.
     *
     * @return The {@link Bytes} value for the given key, or null if there is no value for the key
     */
    public Bytes asBytes(String key)
    {
        return convert(this, key, Bytes::parseBytes, Bytes.class);
    }

    /**
     * Retrieves the value for the given key as a {@link Count} object. If the conversion from String to object fails, a
     * problem is transmitted.
     *
     * @return The {@link Count} value for the given key, or null if there is no value for the key
     */
    public Count asCount(String key)
    {
        return convert(this, key, Count::parseCount, Count.class);
    }

    /**
     * Returns the value of the given key as a double. If the conversion from String to object fails, a problem is
     * transmitted.
     */
    public Double asDouble(String key)
    {
        return convert(key, Double::parseDouble, Double.class);
    }

    /**
     * Retrieves the value for the given key as an {@link Estimate} object. If the conversion from String to object
     * fails, a problem is transmitted.
     *
     * @return The {@link Estimate} value for the given key, or null if there is no value for the key
     */
    public Estimate asEstimate(String key)
    {
        return convert(this, key, Estimate::parseEstimate, Estimate.class);
    }

    /**
     * Retrieves the value for the given key as an {@link Identifier} object. If the conversion from String to object
     * fails, a problem is transmitted.
     *
     * @return The {@link Estimate} value for the given key, or null if there is no value for the key
     */
    public Identifier asIdentifier(String key)
    {
        return convert(key, value -> new Identifier(Long.parseLong(value)), Identifier.class);
    }

    /**
     * Retrieves the value for the given key as an {@link IntegerIdentifier} object. If the conversion from String to
     * object fails, a problem is transmitted.
     *
     * @return The {@link IntegerIdentifier} value for the given key, or null if there is no value for the key
     */
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
     * Returns the value of the given key as a {@link Long} object, or broadcasts a {@link Problem} and returns null if
     * the value is invalid or missing
     */
    public Long asLong(String key)
    {
        return convert(key, Long::parseLong, Long.class);
    }

    /**
     * Returns the value of the given key as a {@link Maximum} object, or broadcasts a {@link Problem} and returns null
     * if the value is invalid or missing
     */
    public Maximum asMaximum(String key)
    {
        return convert(this, key, Maximum::parseMaximum, Maximum.class);
    }

    /**
     * Returns the value of the given key as a {@link Minimum} object, or broadcasts a {@link Problem} and returns null
     * if the value is invalid or missing
     */
    public Minimum asMinimum(String key)
    {
        return convert(this, key, Minimum::parseMinimum, Minimum.class);
    }

    /**
     * Returns the value of the given key as a {@link String} path, with no trailing slash, or broadcasts a
     * {@link Problem} and returns null if the value is invalid or missing
     *
     * @return The given key as a path with no trailing slash
     */
    public String asPathString(String key)
    {
        var value = asString(key);
        return value == null ? null : Strip.stripTrailing(value, "/");
    }

    /**
     * Returns the value of the given key as a {@link Percent} object, or broadcasts a {@link Problem} and returns null
     * if the value is invalid or missing
     *
     * @return The given value as a {@link Percent}
     */
    public Percent asPercent(String key)
    {
        return convert(this, key, Percent::parsePercent, Percent.class);
    }

    /**
     * Returns the value of the given key as string
     */
    public String asString(String key)
    {
        return (String) super.get(key);
    }

    /**
     * Returns the value of the given key as a {@link StringIdentifier} object, or broadcasts a {@link Problem} and
     * returns null if the value is invalid or missing
     */
    public StringIdentifier asStringIdentifier(String key)
    {
        var value = get(key).toString();
        return new StringIdentifier(value);
    }

    /**
     * Returns the value of the given key as a sorted {@link StringList} object, or broadcasts a {@link Problem} and
     * returns null if the value is invalid or missing
     */
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
     * Returns the value of the given key as a {@link URI} object, or broadcasts a {@link Problem} and returns null if
     * the value is invalid or missing
     *
     * @return The given value as a {@link URI}
     */
    public URI asUri(String key)
    {
        return convert(key, URI::create, URI.class);
    }

    /**
     * Returns the value of the given key as a {@link Version} object, or broadcasts a {@link Problem} and returns null
     * if the value is invalid or missing
     *
     * @return The given value as a {@link Version}
     */
    public Version asVersion(String key)
    {
        return convert(key, Version::version, Version.class);
    }

    /**
     * Converts the value of the given key using the given function to the given type, or broadcasts a {@link Problem}
     * and returns null if the value is invalid or missing
     */
    public <T> T convert(String key, Function<String, T> converter, Class<T> type)
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

    /**
     * Converts the value of the given key using the given function to the given type, or broadcasts a {@link Problem}
     * and returns null if the value is invalid or missing
     */
    public <T> T convert(Listener listener, String key, BiFunction<Listener, String, T> converter, Class<T> type)
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

    /**
     * Returns the keys and values of this map separated by the given separator
     */
    public String join(String separator)
    {
        return asStringList().join(separator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return join("\n");
    }

    private boolean keyMissing(String key)
    {
        return get(key) == null;
    }
}
