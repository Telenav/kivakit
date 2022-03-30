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

package com.telenav.kivakit.commandline;

import com.telenav.kivakit.commandline.lexakai.DiagramCommandLine;
import com.telenav.kivakit.commandline.lexakai.DiagramSwitch;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.Converter;
import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.conversion.core.value.VersionConverter;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.validation.BaseValidator;
import com.telenav.kivakit.validation.Validatable;
import com.telenav.kivakit.validation.ValidationIssues;
import com.telenav.kivakit.validation.ValidationType;
import com.telenav.kivakit.validation.Validator;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.util.Set;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * A switch parser that can be passed to {@link CommandLine#get(SwitchParser)} to retrieve a switch value.
 *
 * <p><b>Built-In Parsers</b></p>
 *
 * <p>
 * Numerous switch parser builders are provided by this class as static methods.
 * </p>
 *
 * <p><b>Parser Builders</b></p>
 *
 * <p>
 * New switches can be created with the switch parser {@link Builder}, which can be accessed through {@link
 * #builder(Class)}. The type parameter is the type of the switch being built. For example, a float switch would be of
 * type Float.class. The builder then allows attributes of the switch parser to be specified:
 * <ul>
 *     <li>{@link Builder#name(String)} - The name of the switch on the command line, like "input"</li>
 *     <li>{@link Builder#description(String)} - A description of what the switch does</li>
 *     <li>{@link Builder#required()} - The user must provide the switch or it is an error</li>
 *     <li>{@link Builder#optional()} - The user can omit the switch and a null or default value will result</li>
 *     <li>{@link Builder#defaultValue(Object)} - A default value if the switch is optional and omitted</li>
 *     <li>{@link Builder#validValues(Set)} - A set of allowable values for the switch</li>
 *     <li>{@link Builder#converter(StringConverter)} - A converter to convert the string value of the switch to an object</li>
 * </ul>
 * <p>
 * <b>Example</b>
 * <p>
 * This example provides a builder to create switch parsers for {@link Version} objects. The object specified on the
 * command line will be converted from a string to a {@link Version} object with {@link VersionConverter}. Many classes
 * in KivaKit provide string converters, which makes it an easy job to construct switch parsers.
 * <pre>
 * public static Builder&lt;Version&gt; switchParser(Listener listener, String name, String description)
 * {
 *     return builder(Version.class)
 *         .name(name)
 *         .converter(new Version.Converter(listener))
 *         .description(description);
 * }
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see BaseStringConverter
 * @see StringConverter
 * @see Converter
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramSwitch.class)
@UmlClassDiagram(diagram = DiagramCommandLine.class, includeMembers = false)
@UmlExcludeSuperTypes
public class SwitchParser<T> implements
        Named,
        Validatable
{
    public static <T> Builder<T> builder(Class<T> type)
    {
        return new Builder<T>().type(type);
    }

    /**
     * A fluent builder for argument parsers. Switches have:
     *
     * <ul>
     *     <li>{@link Type} - The type of the switch</li>
     *     <li>{@link Quantifier} - The number of times the switch can appear</li>
     *     <li>Name - The name of the switch</li>
     *     <li>Description - The switch description when giving command line help</li>
     *     <li>{@link Converter} - Converter between switch string value and {@link Type}</li>
     *     <li>Default value - A default value if the switch is optional</li>
     *     <li>Set of value values - Set of values that are allowed</li>
     * </ul>
     *
     * @author jonathanl (shibo)
     */
    @SuppressWarnings("DuplicatedCode") @LexakaiJavadoc(complete = true)
    public static class Builder<T>
    {
        private StringConverter<T> converter;

        private T defaultValue;

        private String description;

        private String name;

        private Quantifier quantifier;

        private Type<T> type;

        private Set<T> validValues;

        private String delimiter;

        private Builder()
        {
        }

        @UmlRelation(label = "creates")
        public SwitchParser<T> build()
        {
            if (quantifier == null)
            {
                fail("Must provide quantifier");
                return null;
            }
            if (name == null)
            {
                fail("Must provide name");
                return null;
            }
            if (type == null)
            {
                fail("Must provide type");
                return null;
            }
            if (converter == null)
            {
                fail("Must provide converter");
                return null;
            }
            if (description == null)
            {
                fail("Must provide description");
                return null;
            }
            return new SwitchParser<>(quantifier,
                    name,
                    type,
                    defaultValue,
                    validValues,
                    converter,
                    delimiter,
                    description);
        }

        public Builder<T> converter(StringConverter<T> converter)
        {
            this.converter = converter;
            return this;
        }

        public Builder<T> defaultValue(T defaultValue)
        {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder<T> description(String description)
        {
            this.description = description;
            return this;
        }

        public Builder<T> listConverter(StringConverter<T> converter, String delimiter)
        {
            this.converter = converter;
            this.delimiter = delimiter;
            return this;
        }

        public Builder<T> name(String name)
        {
            this.name = name;
            return this;
        }

        public Builder<T> optional()
        {
            quantifier = Quantifier.OPTIONAL;
            return this;
        }

        public Builder<T> quantifier(Quantifier quantifier)
        {
            this.quantifier = quantifier;
            return this;
        }

        public Builder<T> required()
        {
            quantifier = Quantifier.REQUIRED;
            return this;
        }

        public Builder<T> type(Class<T> type)
        {
            this.type = Type.forClass(type);
            return this;
        }

        public Builder<T> validValues(Set<T> validValues)
        {
            this.validValues = validValues;
            return this;
        }
    }

    @UmlAggregation(label = "converts values with")
    private final StringConverter<T> converter;

    private final String delimiter;

    @UmlAggregation(label = "default value")
    private final T defaultValue;

    private final String description;

    private final String name;

    private CommandLineParser parent;

    @UmlAggregation
    private final Quantifier quantifier;

    private final Type<T> type;

    private final Set<T> validValues;

    /**
     * Construct.
     *
     * @param quantifier The optionality of the switch
     * @param name The name of the switch
     * @param type The type of the switch
     * @param defaultValue The default value if the switch is optional and omitted
     * @param validValues A set of valid values
     * @param converter String converter for the given type
     * @param description Description of what the switch does
     */
    private SwitchParser(
            Quantifier quantifier,
            String name,
            Type<T> type,
            T defaultValue,
            Set<T> validValues,
            StringConverter<T> converter,
            String delimiter,
            String description)
    {
        this.name = name;
        this.quantifier = quantifier;
        this.defaultValue = defaultValue;
        this.validValues = validValues;
        this.converter = converter;
        this.delimiter = delimiter;
        this.type = type;
        this.description = description;
    }

    /**
     * @return The default value to use if there is no switch present
     */
    public T defaultValue()
    {
        return defaultValue;
    }

    /**
     * @return The value of the given switch
     */
    @UmlNotPublicApi
    @UmlRelation(label = "gets")
    public T get(Switch _switch)
    {
        var messages = new ValidationIssues();
        messages.listenTo(converter);
        var value = converter.convert(_switch.value());
        if (messages.isEmpty())
        {
            return value;
        }
        parent.exit("Invalid value $ for switch -$ ", _switch.value(), _switch.name());
        return null;
    }

    /**
     * @return A help string for this switch
     */
    public String help()
    {
        var specifics = new StringList();
        specifics.add(quantifier.name().toLowerCase());
        if (defaultValue != null)
        {
            specifics.add("default: " + defaultValue);
        }
        return this + "=" + type.simpleName()
                + " (" + specifics.join() + ") : " + description
                + (validValues != null ? "\n\n" + new ObjectList<>().appendAll(validValues).bulleted(4) : "");
    }

    /**
     * @return True if this switch is required
     */
    public boolean isRequired()
    {
        return quantifier == Quantifier.REQUIRED;
    }

    /**
     * Returns a list of values for the given switch
     */
    @UmlNotPublicApi
    @UmlRelation(label = "gets")
    public ObjectList<T> list(Switch _switch)
    {
        var messages = new ValidationIssues();
        messages.listenTo(converter);
        var value = converter.convertToList(_switch.value(), ensureNotNull(delimiter));
        if (messages.isEmpty())
        {
            return value;
        }
        parent.exit("Invalid value $ for switch -$ ", _switch.value(), _switch.name());
        return null;
    }

    /**
     * @return The name of the switch
     */
    @Override
    public String name()
    {
        return name;
    }

    /**
     * Returns a list of values for the given switch
     */
    @UmlNotPublicApi
    @UmlRelation(label = "gets")
    public ObjectSet<T> set(Switch _switch, String delimiter)
    {
        var messages = new ValidationIssues();
        messages.listenTo(converter);
        var value = converter.convertToSet(_switch.value(), delimiter);
        if (messages.isEmpty())
        {
            return value;
        }
        parent.exit("Invalid value $ for switch -$ ", _switch.value(), _switch.name());
        return null;
    }

    @Override
    public String toString()
    {
        return "-" + name;
    }

    /**
     * @return The set of valid values for switches parsed by this parser
     */
    public Set<T> validValues()
    {
        return validValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Validator validator(ValidationType type)
    {
        return new BaseValidator()
        {
            @Override
            protected void onValidate()
            {
            }
        };
    }

    /**
     * @return The command line parser that owns this switch parser
     */
    CommandLineParser parent()
    {
        return parent;
    }

    /**
     * @param parent The parent command-line parser that owns this switch parser
     */
    void parent(CommandLineParser parent)
    {
        this.parent = parent;
    }
}
