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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramCommandLine;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramSwitch;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.Converter;
import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.conversion.core.value.VersionConverter;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.messaging.messages.status.Glitch;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.validation.Validatable;
import com.telenav.kivakit.validation.ValidationIssues;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;
import static com.telenav.kivakit.commandline.Quantifier.OPTIONAL;
import static com.telenav.kivakit.commandline.Quantifier.REQUIRED;
import static com.telenav.kivakit.core.collections.set.ObjectSet.objectSet;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * A switch parser that can be passed to {@link CommandLine#get(SwitchParser)} to retrieve a switch value.
 *
 * <p><b>Built-In Parsers</b></p>
 *
 * <p>
 * Numerous switch parser builders are provided by {@link SwitchParsers}.
 * </p>
 *
 * <p><b>Parser Builders</b></p>
 *
 * <p>
 * New switches can be created with the switch parser {@link Builder}, which can be accessed through
 * {@link #switchParserBuilder(Class)}. The type parameter is the type of the switch being built. For example, a float
 * switch would be of type Float.class. The builder then allows attributes of the switch parser to be specified:
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
 * public static Builder&lt;Version&gt; switchParser(Listener listener,
 *                                             String name,
 *                                             String description)
 * {
 *     return builder(Version.class)
 *         .name(name)
 *         .converter(new Version.Converter(listener))
 *         .description(description);
 * }
 * </pre>
 *
 * <p><b>Values</b></p>
 *
 * <ul>
 *     <li>{@link #asObjectList(SwitchValue)}</li>
 *     <li>{@link #asObject(SwitchValue)}</li>
 *     <li>{@link #assignValue(SwitchValue, String)}</li>
 * </ul>
 *
 * <p><b>Validation</b></p>
 *
 * <ul>
 *     <li>{@link #validValues()}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #defaultValue()}</li>
 *     <li>{@link #help()}</li>
 *     <li>{@link #isRequired()}</li>
 *     <li>{@link #name}</li>
 *     <li>{@link #parent(CommandLineParser)}</li>
 *     <li>{@link #validValues()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see BaseStringConverter
 * @see StringConverter
 * @see Converter
 */
@SuppressWarnings({ "unused", "DuplicatedCode" })
@UmlClassDiagram(diagram = DiagramSwitch.class)
@UmlClassDiagram(diagram = DiagramCommandLine.class, includeMembers = false)
@UmlExcludeSuperTypes
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class SwitchParser<T> implements Named
{
    /**
     * Creates a switch parser builder for the given type
     *
     * @param type The type
     * @return The builder
     */
    public static <T> Builder<T> switchParserBuilder(Class<T> type)
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
    @SuppressWarnings("DuplicatedCode")
    @CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
                 testing = TESTING_NONE,
                 documentation = DOCUMENTATION_COMPLETE)
    public static class Builder<T>
    {
        /** The switch parser we're building */
        private final SwitchParser<T> parser = new SwitchParser<>();

        private Builder()
        {
        }

        /**
         * Returns the switch parser
         */
        @UmlRelation(label = "creates")
        public SwitchParser<T> build()
        {
            ensureNotNull(parser.quantifier, "Must provide quantifier");
            ensureNotNull(parser.name, "Must provide name");
            ensureNotNull(parser.type, "Must provide type");
            ensureNotNull(parser.converter, "Must provide converter");
            ensureNotNull(parser.description, "Must provide description");

            return parser;
        }

        /**
         * Sets the converter
         *
         * @param converter The converter
         * @return This for method chaining
         */
        public Builder<T> converter(@NotNull StringConverter<T> converter)
        {
            parser.converter = converter;
            return this;
        }

        /**
         * Sets the default value
         *
         * @param defaultValue The default value
         * @return This for method chaining
         */
        public Builder<T> defaultValue(@NotNull T defaultValue)
        {
            parser.defaultValue = defaultValue;
            return this;
        }

        /**
         * Sets the switch description
         *
         * @param description The description
         * @return This for method chaining
         */
        public Builder<T> description(@NotNull String description)
        {
            parser.description = description;
            return this;
        }

        /**
         * Sets the converter and delimiter to use for parsing lists
         *
         * @param converter The converter
         * @return This for method chaining
         */
        public Builder<T> listConverter(@NotNull StringConverter<T> converter, String delimiter)
        {
            parser.converter = converter;
            parser.delimiter = delimiter;
            return this;
        }

        /**
         * Sets the name of this switch
         *
         * @param name The name of this switch
         * @return This for method chaining
         */
        public Builder<T> name(@NotNull String name)
        {
            parser.name = name;
            return this;
        }

        /**
         * Makes this switch optional
         *
         * @return This for method chaining
         */
        public Builder<T> optional()
        {
            return quantifier(OPTIONAL);
        }

        /**
         * Assigns the given quantifier to this switch
         *
         * @return This for method chaining
         */
        public Builder<T> quantifier(@NotNull Quantifier quantifier)
        {
            parser.quantifier = quantifier;
            return this;
        }

        /**
         * Makes this switch required
         *
         * @return This for method chaining
         */
        public Builder<T> required()
        {
            return quantifier(REQUIRED);
        }

        /**
         * Sets the type of object for this switch
         *
         * @param type The object type
         * @return This for method chaining
         */
        public Builder<T> type(@NotNull Class<T> type)
        {
            parser.type = Type.typeForClass(type);
            return this;
        }

        /**
         * Sets the set of valid values for this switch
         *
         * @param validValues The values that are acceptable
         * @return This for method chaining
         */
        public Builder<T> validValues(@NotNull Set<T> validValues)
        {
            parser.validValues = objectSet(validValues);
            return this;
        }
    }

    /** Converts from string to object */
    @UmlAggregation(label = "converts values with")
    private StringConverter<T> converter;

    /** The default value if no value is provided */
    @UmlAggregation(label = "default value")
    private T defaultValue;

    /** Description of the switch */
    private String description;

    /** Name of the switch */
    private String name;

    /** Number of times the switch can appear */
    @UmlAggregation
    private Quantifier quantifier;

    /** The type of object produced */
    private Type<T> type;

    /** A set of value values */
    private ObjectSet<T> validValues;

    /** The delimiter for parsing multiple values */
    private String delimiter;

    /** The command line parser that owns this switch parser */
    private CommandLineParser parent;

    /**
     * Returns the value of the given switch
     */
    @UmlNotPublicApi
    @UmlRelation(label = "gets")
    public T asObject(@NotNull SwitchValue switchValue)
    {
        ensureNotNull(parent, "Switch parser was not added to command line parent");

        var issues = new ValidationIssues();
        issues.listenTo(converter);
        var value = converter.convert(switchValue.value());
        if (value instanceof Validatable)
        {
            ((Validatable) value).isValid(issues);
        }
        if (issues.countWorseThanOrEqualTo(Glitch.class).isZero())
        {
            return value;
        }
        parent.exit("Invalid value $ for switch -$ ", switchValue.value(), switchValue.name());
        return null;
    }

    /**
     * Returns a list of values for the given switch
     */
    @UmlNotPublicApi
    @UmlRelation(label = "gets")
    public ObjectList<T> asObjectList(@NotNull SwitchValue switchValue)
    {
        ensureNotNull(parent, "Switch parser was not added to command line parent");

        var messages = new ValidationIssues();
        messages.listenTo(converter);
        var value = converter.convertToList(switchValue.value(), ensureNotNull(delimiter));
        if (messages.isEmpty())
        {
            return value;
        }
        parent.exit("Invalid value $ for switch -$ ", switchValue.value(), switchValue.name());
        return null;
    }

    /**
     * Returns a list of values for the given switch
     */
    @UmlRelation(label = "gets")
    public ObjectSet<T> assignValue(@NotNull SwitchValue switchValue,
                                    @NotNull String delimiter)
    {
        ensureNotNull(parent, "Switch parser was not added to command line parent");

        var messages = new ValidationIssues();
        messages.listenTo(converter);
        var value = converter.convertToSet(switchValue.value(), delimiter);
        if (messages.isEmpty())
        {
            return value;
        }
        parent.exit("Invalid value $ for switch -$ ", switchValue.value(), switchValue.name());
        return null;
    }

    /**
     * Returns the default value to use if there is no switch present
     */
    public T defaultValue()
    {
        return defaultValue;
    }

    /**
     * Returns a help string for this switch
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
                + (validValues != null ? "\n\n" + validValues.asList().bulleted(4) : "");
    }

    /**
     * Returns true if this switch is required
     */
    public boolean isRequired()
    {
        return quantifier == Quantifier.REQUIRED;
    }

    /**
     * Returns the name of the switch
     */
    @Override
    public String name()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return "-" + name;
    }

    /**
     * Returns the set of valid values for switches parsed by this parser
     */
    public ObjectSet<T> validValues()
    {
        return validValues;
    }

    /**
     * @param parent The parent command-line parser that owns this switch parser
     */
    void parent(@NotNull CommandLineParser parent)
    {
        this.parent = parent;
    }
}
