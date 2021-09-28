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

package com.telenav.kivakit.kernel.language.collections.map.string;

import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashSet;
import java.util.Map;

/**
 * A bounded map from string to value which supports variable interpolation into a string via {@link #expand(String)}.
 * For example, a {@link VariableMap} of {@link Integer}s might have the entries "x=9" and "y=3" in it. In this case,
 * interpolate("${x} = ${y}") would yield the string "9 = 3". An example use of this class can be found in
 * File.withVariables(final VariableMap&lt;?&gt; variables), which substitutes the name of the file with values from the
 * variable map.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unchecked")
@UmlClassDiagram(diagram = DiagramLanguageCollectionsMap.class)
public class VariableMap<Value> extends BaseStringMap<Value>
{
    /**
     * @return A string variable map for the given string-to-string map
     */
    public static VariableMap<String> of(final Map<String, String> that)
    {
        final var copy = new VariableMap<String>();
        for (final var key : that.keySet())
        {
            copy.put(key, that.get(key));
        }
        return copy;
    }

    /**
     * An unbounded variable map
     */
    public VariableMap()
    {
        super(Maximum.MAXIMUM);
    }

    /**
     * A bounded variable map
     */
    public VariableMap(final Maximum maximum)
    {
        super(maximum);
    }

    public VariableMap<Value> add(final String name, final Value object)
    {
        super.put(name, object);
        return this;
    }

    public VariableMap<Value> addAll(final Object object, final PropertyFilter filter)
    {
        addAll((VariableMap<Value>) Type.of(object).variables(object, filter));
        return this;
    }

    public VariableMap<Value> addAll(final Object object, final PropertyFilter filter, final Value nullValue)
    {
        addAll((VariableMap<Value>) Type.of(object).variables(object, filter, nullValue));
        return this;
    }

    public VariableMap<Value> addAll(final VariableMap<Value> variables)
    {
        super.putAll(variables);
        return this;
    }

    public VariableMap<Value> copy()
    {
        final var copy = new VariableMap<Value>();
        copy.addAll(this);
        return copy;
    }

    /**
     * @return This variable map with all the keys and values as double-quoted strings.
     */
    public VariableMap<String> doubleQuoted()
    {
        final var quoted = new VariableMap<String>();
        for (final var key : keySet())
        {
            quoted.add("\"" + key + "\"", "\"" + get(key) + "\"");
        }
        return quoted;
    }

    /**
     * Expands the given text, leaving any ${x} markers for which there is no value in place.
     *
     * @param text The string to interpolate values into
     * @return The interpolated string
     */
    public String expand(final String text)
    {
        return expand(text, null);
    }

    /**
     * Interpolates the values in this map into the given string. Substitutions occur when the names of variables appear
     * inside curly braces after a '$', like "${x}". Any such substitution markers that do not correspond to a variable
     * in the map are substituted with the given default value, or if that value is null, they are left unchanged.
     *
     * @param text The string to interpolate values into
     * @param defaultValue The value to use for missing variables, or null to leave the ${x} marker unexpanded
     * @return The interpolated string
     */
    public String expand(final String text, final String defaultValue)
    {
        if (text.contains("${"))
        {
            final var builder = new StringBuilder();
            var pos = 0;
            while (true)
            {
                final var next = text.indexOf("${", pos);
                if (next < 0)
                {
                    break;
                }
                builder.append(text, pos, next);
                pos = next + 2;
                final var start = pos;
                while (pos < text.length() && isVariableCharacter(text.charAt(pos)))
                {
                    pos++;
                }
                if (pos > start && text.charAt(pos) == '}')
                {
                    final var variable = text.substring(start, pos);
                    final var value = get(variable);
                    if (value != null)
                    {
                        builder.append(value);
                    }
                    else
                    {
                        if (defaultValue != null)
                        {
                            builder.append(defaultValue);
                        }
                        else
                        {
                            builder.append("${").append(variable).append("}");
                        }
                    }
                    pos++;
                }
            }
            builder.append(text.substring(pos));
            return builder.toString();
        }
        return text;
    }

    /**
     * @return This variable map with all string values expanded by interpolating values for other keys in the map. For
     * example, the entry for key "coordinate" might be the value "${location-x}, ${location-y}". If the value for the
     * key "location-x" is "9" and the value for "location-y" is "81", then the expanded variable map will have the
     * value "9, 81" for the key "coordinate"
     */
    public VariableMap<String> expanded()
    {
        final var expanded = new VariableMap<String>();
        for (final var key : new HashSet<>(keySet()))
        {
            final var value = get(key);
            expanded.put(key, expand(value.toString()));
        }
        return expanded;
    }

    /**
     * @return This variable map with all the values as quoted strings.
     */
    public VariableMap<String> withQuotedValues()
    {
        final var quoted = new VariableMap<String>();
        for (final var key : keySet())
        {
            quoted.add(key, "'" + get(key) + "'");
        }
        return quoted;
    }

    private boolean isVariableCharacter(final char character)
    {
        return Character.isLetterOrDigit(character) || character == '.' || character == '_' || character == '-';
    }
}
