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

package com.telenav.kivakit.core.commandline;

import com.telenav.kivakit.core.commandline.project.lexakai.diagrams.DiagramCommandLine;
import com.telenav.kivakit.core.commandline.project.lexakai.diagrams.DiagramSwitch;
import com.telenav.kivakit.core.kernel.data.conversion.Converter;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.StringConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.collection.BaseListConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.collection.BaseSetConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.enumeration.EnumConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.language.IdentityConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.language.PatternConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.language.VersionConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.primitive.BooleanConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.primitive.DoubleConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.primitive.IntegerConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.primitive.LongConverter;
import com.telenav.kivakit.core.kernel.data.validation.Validatable;
import com.telenav.kivakit.core.kernel.data.validation.Validation;
import com.telenav.kivakit.core.kernel.data.validation.Validator;
import com.telenav.kivakit.core.kernel.data.validation.listeners.ValidationIssues;
import com.telenav.kivakit.core.kernel.data.validation.validators.BaseValidator;
import com.telenav.kivakit.core.kernel.interfaces.factory.MapFactory;
import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.reflection.Type;
import com.telenav.kivakit.core.kernel.language.strings.CaseFormat;
import com.telenav.kivakit.core.kernel.language.time.LocalTime;
import com.telenav.kivakit.core.kernel.language.time.conversion.converters.LocalDateConverter;
import com.telenav.kivakit.core.kernel.language.time.conversion.converters.LocalDateTimeConverter;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.language.values.count.Minimum;
import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.kivakit.core.kernel.language.values.version.Version;
import com.telenav.kivakit.core.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.util.Set;
import java.util.regex.Pattern;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

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
 *     <li>{@link Builder#converter(Converter)} - A converter to convert the string value of the switch to an object</li>
 * </ul>
 * <p>
 * <b>Example</b>
 * <p>
 * This example provides a builder to create switch parsers for {@link Version} objects. The object specified on the
 * command line will be converted from a string to a {@link Version} object with {@link VersionConverter}. Many classes
 * in KivaKit provide string converters, which makes it an easy job to construct switch parsers.
 * <pre>
 * public static Builder&lt;Version&gt; switchParser(String name, String description)
 * {
 *     return builder(Version.class)
 *         .name(name)
 *         .converter(new Version.Converter(LOGGER))
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
public class SwitchParser<T> implements Named, Validatable
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static Builder<Boolean> booleanSwitch(final String name, final String description)
    {
        return builder(Boolean.class)
                .name(name)
                .converter(new BooleanConverter(LOGGER))
                .description(description);
    }

    public static <T> Builder<T> builder(final Class<T> type)
    {
        return new Builder<T>().type(type);
    }

    public static Builder<Bytes> bytesSwitch(final String name, final String description)
    {
        return builder(Bytes.class)
                .name(name)
                .converter(new Bytes.Converter(LOGGER))
                .description(description);
    }

    public static Builder<Count> countSwitch(final String name, final String description)
    {
        return builder(Count.class)
                .name(name)
                .converter(new Count.Converter(LOGGER))
                .description(description);
    }

    public static Builder<Double> doubleSwitch(final String name, final String description)
    {
        return builder(Double.class)
                .name(name)
                .converter(new DoubleConverter(LOGGER))
                .description(description);
    }

    public static <E extends Enum<E>> Builder<E> enumSwitch(final String name,
                                                            final String description,
                                                            final Class<E> type)
    {
        if (type.isEnum())
        {
            final var options = new StringList();
            for (final var option : type.getEnumConstants())
            {
                options.add(CaseFormat.upperUnderscoreToLowerHyphen(option.name()));
            }
            final var help = description + "\n\n" + options.bulleted(4) + "\n";
            return builder(type)
                    .name(name)
                    .converter(new EnumConverter<>(LOGGER, type))
                    .description(help);
        }
        return fail("TimeFormat is not an enum");
    }

    public static Builder<Integer> integerSwitch(final String name, final String description)
    {
        return builder(Integer.class)
                .name(name)
                .converter(new IntegerConverter(LOGGER))
                .description(description);
    }

    public static <E> Builder<ObjectList<E>> listSwitch(
            final String name,
            final String description,
            final StringConverter<E> elementConverter,
            final Class<E> elementType,
            final String delimiter)
    {
        final var builder = new Builder<ObjectList<E>>();
        builder.type = Type.of(elementType);
        return builder
                .name(name)
                .converter(new BaseListConverter<>(LOGGER, elementConverter, delimiter) {})
                .description(description);
    }

    public static Builder<LocalTime> localDateSwitch(final String name, final String description)
    {
        return builder(LocalTime.class)
                .name(name)
                .description(description)
                .converter(new LocalDateConverter(LOGGER));
    }

    public static Builder<LocalTime> localDateTimeSwitch(final String name, final String description)
    {
        return builder(LocalTime.class)
                .name(name)
                .description(description)
                .converter(new LocalDateTimeConverter(LOGGER));
    }

    public static Builder<Long> longSwitch(final String name, final String description)
    {
        return builder(Long.class)
                .name(name)
                .converter(new LongConverter(LOGGER))
                .description(description);
    }

    public static Builder<Maximum> maximumSwitch(final String name, final String description)
    {
        return builder(Maximum.class)
                .name(name)
                .converter(new Maximum.Converter(LOGGER))
                .description(description);
    }

    public static Builder<Minimum> minimumSwitch(final String name, final String description)
    {
        return builder(Minimum.class)
                .name(name)
                .converter(new Minimum.Converter(LOGGER))
                .description(description);
    }

    public static Builder<Pattern> patternSwitch(final String name, final String description)
    {
        return builder(Pattern.class)
                .name(name)
                .converter(new PatternConverter(LOGGER))
                .description(description);
    }

    public static Builder<Percent> percentageSwitch(final String name, final String description)
    {
        return builder(Percent.class)
                .name(name)
                .converter(new Percent.Converter(LOGGER))
                .description(description);
    }

    public static <T extends Quantizable> Builder<T> quantizableSwitch(final String name,
                                                                       final String description,
                                                                       final Class<T> type,
                                                                       final MapFactory<Long, T> factory)
    {
        return builder(type)
                .name(name)
                .description(description)
                .converter(new Quantizable.Converter<>(LOGGER, factory));
    }

    public static <E> Builder<Set<E>> setSwitch(
            final String name,
            final String description,
            final StringConverter<E> elementConverter,
            final Class<E> elementType,
            final String delimiter)
    {
        final var builder = new Builder<Set<E>>();
        builder.type = Type.of(elementType);
        return builder
                .name(name)
                .converter(new BaseSetConverter<>(LOGGER, elementConverter, delimiter) {})
                .description(description);
    }

    public static Builder<String> stringSwitch(final String name, final String description)
    {
        return builder(String.class)
                .name(name)
                .converter(new IdentityConverter(LOGGER))
                .description(description);
    }

    public static SwitchParser<Count> threadCountSwitch(final Count maximum)
    {
        final var defaultThreads = maximum.minimum(JavaVirtualMachine.local().processors());
        return countSwitch("threads", "Number of threads to use (default is " + defaultThreads + ")")
                .optional()
                .defaultValue(defaultThreads)
                .build();
    }

    public static Builder<Version> versionSwitch(final String name, final String description)
    {
        return builder(Version.class)
                .name(name)
                .converter(new VersionConverter(LOGGER))
                .description(description);
    }

    public static class Builder<T>
    {
        private Quantifier quantifier;

        private Type<T> type;

        private String description;

        private Converter<String, T> converter;

        private String name;

        private T defaultValue;

        private Set<T> validValues;

        private Builder()
        {
        }

        @UmlRelation(label = "creates")
        public SwitchParser<T> build()
        {
            if (quantifier == null)
            {
                return fail("Must provide quantifier");
            }
            if (name == null)
            {
                return fail("Must provide name");
            }
            if (type == null)
            {
                return fail("Must provide type");
            }
            if (converter == null)
            {
                return fail("Must provide converter");
            }
            if (description == null)
            {
                return fail("Must provide description");
            }
            return new SwitchParser<>(quantifier, name, type, defaultValue, validValues,
                    converter, description);
        }

        public Builder<T> converter(final Converter<String, T> converter)
        {
            this.converter = converter;
            return this;
        }

        public Builder<T> defaultValue(final T defaultValue)
        {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder<T> description(final String description)
        {
            this.description = description;
            return this;
        }

        public Builder<T> name(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder<T> optional()
        {
            quantifier = Quantifier.OPTIONAL;
            return this;
        }

        public Builder<T> required()
        {
            quantifier = Quantifier.REQUIRED;
            return this;
        }

        public Builder<T> type(final Class<T> type)
        {
            this.type = Type.forClass(type);
            return this;
        }

        public Builder<T> validValues(final Set<T> validValues)
        {
            this.validValues = validValues;
            return this;
        }
    }

    @UmlAggregation
    private final Quantifier quantifier;

    private final String name;

    private final Type<T> type;

    private final String description;

    @UmlAggregation(label = "converts values with")
    private final Converter<String, T> converter;

    @UmlAggregation(label = "default value")
    private final T defaultValue;

    private final Set<T> validValues;

    private CommandLineParser parent;

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
            final Quantifier quantifier,
            final String name,
            final Type<T> type,
            final T defaultValue,
            final Set<T> validValues,
            final Converter<String, T> converter,
            final String description)
    {
        this.name = name;
        this.quantifier = quantifier;
        this.defaultValue = defaultValue;
        this.validValues = validValues;
        this.converter = converter;
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
    public T get(final Switch _switch)
    {
        final var messages = new ValidationIssues();
        messages.listenTo(converter);
        final var value = converter.convert(_switch.value());
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
        final var specifics = new StringList();
        specifics.add(quantifier.name().toLowerCase());
        if (defaultValue != null)
        {
            specifics.add("default: " + defaultValue);
        }
        return this + "=" + type.simpleName()
                + " (" + specifics.join() + ") : " + description
                + (validValues != null ? "\n\n" + new ObjectList<>().appendAll(validValues).bulleted(4) + "\n"
                : "");
    }

    /**
     * @return True if this switch is required
     */
    public boolean isRequired()
    {
        return quantifier == Quantifier.REQUIRED;
    }

    /**
     * @return The name of the switch
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
     * {@inheritDoc}
     */
    @Override
    public Validator validator(final Validation type)
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
    void parent(final CommandLineParser parent)
    {
        this.parent = parent;
    }

    /**
     * @return The set of valid values for switches parsed by this parser
     */
    Set<T> validValues()
    {
        return validValues;
    }
}
