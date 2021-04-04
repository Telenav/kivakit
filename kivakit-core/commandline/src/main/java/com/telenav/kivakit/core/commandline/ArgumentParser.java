////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.commandline;

import com.telenav.kivakit.core.commandline.project.lexakai.diagrams.DiagramArgument;
import com.telenav.kivakit.core.commandline.project.lexakai.diagrams.DiagramCommandLine;
import com.telenav.kivakit.core.kernel.data.conversion.Converter;
import com.telenav.kivakit.core.kernel.data.conversion.string.language.IdentityConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.language.VersionConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.primitive.BooleanConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.primitive.IntegerConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.primitive.LongConverter;
import com.telenav.kivakit.core.kernel.language.reflection.Type;
import com.telenav.kivakit.core.kernel.language.values.version.Version;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * Parses a command-line {@link Argument}, as defined by a {@link Quantifier}, a type, a string converter for that type
 * and a human-readable description. An argument parser can be passed to {@link Argument#get(ArgumentParser)} to
 * retrieve the value of an argument.
 *
 * <p><b>Built-In Parsers</b></p>
 *
 * <p>
 * Several argument parsers can be created with static methods:
 * <ul>
 *     <li>{@link #booleanArgument(String)} - An argument that can be true or false</li>
 *     <li>{@link #integerArgument(String)} - An integer argument</li>
 *     <li>{@link #longArgument(String)} - A long argument</li>
 *     <li>{@link #stringArgument(String)} - A string argument</li>
 *     <li>{@link #stringArgument(String)} - A string argument</li>
 * </ul>
 *
 * <p><b>Parser Builders</b></p>
 *
 * <p>
 * New argument parsers can be created with the argument parser {@link Builder}, which can be accessed through {@link #builder(Class)}.
 * The type parameter is the type of the argument parser being built. For example, a float switch would be of type Float.class.
 * The builder then allows attributes of the argument parser to be specified:
 * <ul>
 *     <li>{@link Builder#description(String)} - A description of what the argument is for</li>
 *     <li>{@link Builder#required()} - The user must provide the argument or it is an error</li>
 *     <li>{@link Builder#optional()} - The user can omit the argument</li>
 *     <li>{@link Builder#oneOrMore()} - The user must provide one or more arguments of this type</li>
 *     <li>{@link Builder#twoOrMore()} - The user must provide two or more arguments of this type</li>
 *     <li>{@link Builder#zeroOrMore()} - The user can provide zero or more arguments of this type</li>
 *     <li>{@link Builder#converter(Converter)} - Converts the argument string to an object</li>
 * </ul>
 * <p>
 * <b>Example</b>
 * <p>
 * This example provides a builder to create argument parsers for {@link Version} objects. The value specified on the
 * command line will be converted from a string to a {@link Version} object with {@link VersionConverter}. Many classes
 * in KivaKit provide string converters, which makes it an easy job to construct argument parsers.
 * <pre>
 * public static SwitchParser.Builder&lt;Version&gt; argumentParser(final String description)
 * {
 *     return SwitchParser.builder(Version.class)
 *         .converter(new Version.Converter(listener))
 *         .description(description);
 * }
 *
 *     [...]
 *
 * SwitchParser&lt;Version&gt; VERSION = argumentParser("The input file version")
 *     .required()
 *     .build();
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramArgument.class)
@UmlClassDiagram(diagram = DiagramCommandLine.class, includeMembers = false)
public class ArgumentParser<T>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static Builder<Boolean> booleanArgument(final String description)
    {
        return builder(Boolean.class)
                .converter(new BooleanConverter(LOGGER))
                .description(description);
    }

    public static <T> Builder<T> builder(final Class<T> type)
    {
        return new Builder<T>().type(type);
    }

    public static Builder<Integer> integerArgument(final String description)
    {
        return builder(Integer.class)
                .converter(new IntegerConverter(LOGGER))
                .description(description);
    }

    public static Builder<Long> longArgument(final String description)
    {
        return builder(Long.class)
                .converter(new LongConverter(LOGGER))
                .description(description);
    }

    public static Builder<String> stringArgument(final String description)
    {
        return builder(String.class)
                .converter(new IdentityConverter(LOGGER))
                .description(description);
    }

    public static Builder<Version> versionArgument(final String description)
    {
        return builder(Version.class)
                .converter(new VersionConverter(LOGGER))
                .description(description);
    }

    @UmlClassDiagram(diagram = DiagramArgument.class)
    public static class Builder<T>
    {
        /** The number of times that the argument can appear */
        private Quantifier quantifier;

        /** The type of the argument */
        private Type<T> type;

        /** A description of the argument */
        private String description;

        /** A converter from a command-line string to the type of this argument */
        private Converter<String, T> converter;

        @UmlRelation(label = "creates")
        public ArgumentParser<T> build()
        {
            if (quantifier == null)
            {
                return fail("Must provide quantifier");
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
            return new ArgumentParser<>(quantifier, type, converter, description);
        }

        public Builder<T> converter(final Converter<String, T> converter)
        {
            this.converter = converter;
            return this;
        }

        public Builder<T> description(final String description)
        {
            this.description = description;
            return this;
        }

        public Builder<T> oneOrMore()
        {
            quantifier = Quantifier.ONE_OR_MORE;
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

        public Builder<T> twoOrMore()
        {
            quantifier = Quantifier.TWO_OR_MORE;
            return this;
        }

        public Builder<T> type(final Class<T> type)
        {
            this.type = Type.forClass(type);
            return this;
        }

        public Builder<T> zeroOrMore()
        {
            quantifier = Quantifier.ZERO_OR_MORE;
            return this;
        }
    }

    /**
     * The quantifier for the argument
     */
    @UmlAggregation
    private final Quantifier quantifier;

    /**
     * The type of the argument
     */
    private final Type<T> type;

    /**
     * A human readable description of the argument for use in creating help messages
     */
    private final String description;

    /**
     * Converter from String to argument type
     */
    @UmlAggregation(label = "converts values with")
    private final Converter<String, T> converter;

    /** The command line parser that owns this argument parser */
    private CommandLineParser parent;

    /**
     * Construct.
     *
     * @param quantifier The allowable quantities of the argument
     * @param type The type of the argument
     * @param converter String converter for the argument
     * @param description The human readable description of the argument
     */
    private ArgumentParser(
            final Quantifier quantifier,
            final Type<T> type,
            final Converter<String, T> converter,
            final String description)
    {
        this.quantifier = quantifier;
        this.converter = converter;
        this.type = type;
        this.description = description;
    }

    public String description()
    {
        return description;
    }

    /**
     * @return A help message for the argument
     */
    public String help()
    {
        return type.simpleName() + " (" + quantifier.name().toLowerCase().replace('_', ' ') + ") - "
                + description;
    }

    /**
     * @return True if the argument can be present multiple times
     */
    public boolean isAllowedMultipleTimes()
    {
        return quantifier == Quantifier.ONE_OR_MORE
                || quantifier == Quantifier.ZERO_OR_MORE
                || quantifier == Quantifier.TWO_OR_MORE;
    }

    /**
     * @return True if the argument may be omitted
     */
    public boolean isOptional()
    {
        return !isRequired();
    }

    /**
     * @return True if the argument must be present
     */
    public boolean isRequired()
    {
        return quantifier == Quantifier.REQUIRED || quantifier == Quantifier.ONE_OR_MORE
                || quantifier == Quantifier.TWO_OR_MORE;
    }

    /**
     * @return The allowed quantities of this argument
     */
    public Quantifier quantifier()
    {
        return quantifier;
    }

    @Override
    public String toString()
    {
        return type.simpleName();
    }

    /**
     * @return The value of this argument
     */
    @UmlRelation(label = "gets")
    T get(final Argument argument)
    {
        return converter.convert(argument.value());
    }

    CommandLineParser parent()
    {
        return parent;
    }

    void parent(final CommandLineParser parent)
    {
        this.parent = parent;
    }
}
