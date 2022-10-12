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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.core.language.reflection.property.Property;
import com.telenav.kivakit.core.language.reflection.property.PropertyValue;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashSet;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;

/**
 * A bounded map from string to value which supports variable interpolation into a string via {@link #expand(String)}.
 * For example, a {@link VariableMap} of {@link Integer}s might have the entries "x=9" and "y=3" in it. In this case,
 * interpolate("${x} = ${y}") would yield the string "9 = 3". An example use of this class can be found in
 * File.withVariables(VariableMap&lt;?&gt; variables), which substitutes the name of the file with values from the
 * variable map.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCollections.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTATION_COMPLETE)
public class VariableMap<Value> extends StringMap<Value> implements PropertyValue
{
    /**
     * Returns a string variable map for the given string-to-string map
     */
    public static VariableMap<String> variableMap(Map<String, String> that)
    {
        var copy = new VariableMap<String>();
        for (var key : that.keySet())
        {
            copy.put(key, that.get(key));
        }
        return copy;
    }

    /**
     * Returns this list of strings as a variable map where the even elements are keys and the odd elements are values.
     */
    public static VariableMap<String> variableMap(StringList list)
    {
        var variables = new VariableMap<String>();
        for (var i = 0; i < list.size(); i += 2)
        {
            if (i + 1 < list.size())
            {
                variables.add(list.get(i), list.get(i + 1));
            }
        }
        return variables;
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
    public VariableMap(Maximum maximum)
    {
        super(maximum);
    }

    /**
     * Add the given variable to this variable map
     */
    public VariableMap<Value> add(String name, Value value)
    {
        put(name, value);
        return this;
    }

    /**
     * Add the given variables to this variable map
     */
    public VariableMap<Value> addAll(VariableMap<Value> variables)
    {
        putAll(variables);
        return this;
    }

    /**
     * Returns a copy of this variable map
     */
    @Override
    public VariableMap<Value> copy()
    {
        return (VariableMap<Value>) super.copy();
    }

    /**
     * Returns this variable map with all the keys and values as double-quoted strings.
     */
    public VariableMap<String> doubleQuoted()
    {
        var quoted = newStringMap();
        for (var key : keySet())
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
    public String expand(String text)
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
    public String expand(String text, String defaultValue)
    {
        if (text.contains("${"))
        {
            var builder = new StringBuilder();
            var pos = 0;
            while (true)
            {
                var next = text.indexOf("${", pos);
                if (next < 0)
                {
                    break;
                }
                builder.append(text, pos, next);
                pos = next + 2;
                var start = pos;
                while (pos < text.length() && isVariableCharacter(text.charAt(pos)))
                {
                    pos++;
                }
                if (pos > start && text.charAt(pos) == '}')
                {
                    var variable = text.substring(start, pos);
                    var value = get(variable);
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
     * Returns this variable map with all string values expanded by interpolating values for other keys in the map. For
     * example, the entry for key "coordinate" might be the value "${location-x}, ${location-y}". If the value for the
     * key "location-x" is "9" and the value for "location-y" is "81", then the expanded variable map will have the
     * value "9, 81" for the key "coordinate"
     */
    public VariableMap<String> expanded()
    {
        var expanded = newStringMap();
        for (var key : new HashSet<>(keySet()))
        {
            var value = get(key);
            if (value != null)
            {
                expanded.put(key, expand(value.toString()));
            }
        }
        return expanded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object propertyValue(Property property)
    {
        return get(property.name());
    }

    /**
     * Returns this variable map with all the values as quoted strings.
     */
    public VariableMap<String> withQuotedValues()
    {
        var quoted = newStringMap();
        for (var key : keySet())
        {
            quoted.add(key, "'" + get(key) + "'");
        }
        return quoted;
    }

    protected VariableMap<String> newStringMap()
    {
        return new VariableMap<>();
    }

    private boolean isVariableCharacter(char character)
    {
        return Character.isLetterOrDigit(character) || character == '.' || character == '_' || character == '-';
    }
}
