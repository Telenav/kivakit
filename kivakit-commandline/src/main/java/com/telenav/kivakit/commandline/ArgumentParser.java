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
import com.telenav.kivakit.commandline.internal.lexakai.DiagramArgument;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramCommandLine;
import com.telenav.kivakit.conversion.Converter;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.messaging.messages.status.Glitch;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.validation.Validatable;
import com.telenav.kivakit.validation.ValidationIssues;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.commandline.Quantifier.ONE_OR_MORE;
import static com.telenav.kivakit.commandline.Quantifier.OPTIONAL;
import static com.telenav.kivakit.commandline.Quantifier.REQUIRED;
import static com.telenav.kivakit.commandline.Quantifier.ZERO_OR_MORE;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.language.reflection.Type.typeForClass;

/**
 * Parses a command-line {@link ArgumentValue}, as defined by a {@link Quantifier}, a type, a string converter for that
 * type and a human-readable description. An argument parser can be passed to {@link ArgumentValue#get(ArgumentParser)}
 * to retrieve the value of an argument.
 *
 * <p><b>Argument Parser Builders</b></p>
 *
 * <p>
 * New argument parsers can be created with the argument parser {@link Builder}, which can be accessed through
 * {@link #argumentParser(Class)}. The type parameter is the type of the argument parser being built. For example, a
 * float switch would be of type Float.class. The builder then allows attributes of the argument parser to be
 * specified.
 * </p>
 *
 * <ul>
 *     <li>{@link #argumentParser(Class)}</li>
 *     <li>{@link Builder#description(String)} - A description of what the argument is for</li>
 *     <li>{@link Builder#required()} - The user must provide the argument or it is an error</li>
 *     <li>{@link Builder#optional()} - The user can omit the argument</li>
 *     <li>{@link Builder#oneOrMore()} - The user must provide one or more arguments of this type</li>
 *     <li>{@link Builder#zeroOrMore()} - The user can provide zero or more arguments of this type</li>
 *     <li>{@link Builder#converter(Converter)} - Converts the argument string to an object</li>
 * </ul>
 *
 * <p><b>Example</b></p>
 *
 * <p>
 * This example builds an argument parser that parses {@link Version} objects:
 * </p>
 *
 * <pre>
 * ArgumentParser&lt;Version&gt; VERSION = argumentParser(Version.class)
 *     .description("The input file version")
 *     .required()
 *     .build();</pre>
 *
 * <p><b>Arguments</b></p>
 *
 * <ul>
 *     <li>{@link #asObject(ArgumentValue)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #description()}</li>
 *     <li>{@link #help()}</li>
 *     <li>{@link #isAllowedMultipleTimes()}</li>
 *     <li>{@link #isOptional()}</li>
 *     <li>{@link #isRequired()}</li>
 *     <li>{@link #quantifier()}</li>
 *     <li>{@link #parent()}</li>
 *     <li>{@link #parent(CommandLineParser)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "DuplicatedCode" })
@UmlClassDiagram(diagram = DiagramArgument.class)
@UmlClassDiagram(diagram = DiagramCommandLine.class, includeMembers = false)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class ArgumentParser<T>
{
    /**
     * Returns an argument parser builder for the given type
     *
     * @param type The type of object the argument parser will produce
     * @return The parser builder
     */
    public static <T> Builder<T> argumentParser(Class<T> type)
    {
        return new Builder<T>().type(type);
    }

    /**
     * A fluent builder for argument parsers.
     *
     * <ul>
     *     <li>{@link Type} - The type of the argument</li>
     *     <li>{@link Quantifier} - The number of times the argument can appear</li>
     *     <li>Description - The argument description when giving command line help</li>
     *     <li>{@link Converter} - Converter between argument string value and {@link Type}</li>
     * </ul>
     *
     * @author jonathanl (shibo)
     */
    @SuppressWarnings("DuplicatedCode")
    @UmlClassDiagram(diagram = DiagramArgument.class)
    @CodeQuality(stability = STABLE_EXTENSIBLE,
                 testing = UNTESTED,
                 documentation = DOCUMENTATION_COMPLETE)
    public static class Builder<T>
    {
        /** The parser that we're building */
        private final ArgumentParser<T> parser = new ArgumentParser<>();

        @UmlRelation(label = "creates")
        public ArgumentParser<T> build()
        {
            ensureNotNull(parser.quantifier, "Must provide quantifier");
            ensureNotNull(parser.type, "Must provide type");
            ensureNotNull(parser.converter, "Must provide converter");
            ensureNotNull(parser.description, "Must provide description");

            return parser;
        }

        public Builder<T> converter(Converter<String, T> converter)
        {
            parser.converter = converter;
            return this;
        }

        public Builder<T> description(String description)
        {
            parser.description = description;
            return this;
        }

        public Builder<T> oneOrMore()
        {
            parser.quantifier = ONE_OR_MORE;
            return this;
        }

        public Builder<T> optional()
        {
            parser.quantifier = OPTIONAL;
            return this;
        }

        public Builder<T> required()
        {
            parser.quantifier = REQUIRED;
            return this;
        }

        public Builder<T> type(Class<T> type)
        {
            parser.type = typeForClass(type);
            return this;
        }

        public Builder<T> zeroOrMore()
        {
            parser.quantifier = ZERO_OR_MORE;
            return this;
        }
    }

    /**
     * The quantifier for the argument
     */
    @UmlAggregation
    private Quantifier quantifier;

    /**
     * The type of the argument
     */
    private Type<T> type;

    /**
     * A human-readable description of the argument for use in creating help messages
     */
    private String description;

    /**
     * Converter from String to argument type
     */
    @UmlAggregation(label = "converts values with")
    private Converter<String, T> converter;

    /** The command line parser that owns this argument parser */
    private CommandLineParser parent;

    /**
     * Construct.
     *
     * @param quantifier The allowable quantities of the argument
     * @param type The type of the argument
     * @param converter String converter for the argument
     * @param description The human-readable description of the argument
     */
    private ArgumentParser(
            Quantifier quantifier,
            Type<T> type,
            Converter<String, T> converter,
            String description)
    {
        this.quantifier = quantifier;
        this.converter = converter;
        this.type = type;
        this.description = description;
    }

    private ArgumentParser()
    {
        this(null, null, null, null);
    }

    /**
     * Returns the object for the given argument value
     */
    @UmlRelation(label = "gets")
    public T asObject(ArgumentValue argumentValue)
    {
        var issues = new ValidationIssues();
        issues.listenTo(converter);
        var value = converter.convert(argumentValue.value());
        if (value instanceof Validatable)
        {
            ((Validatable) value).isValid(issues);
        }
        if (issues.countWorseThanOrEqualTo(Glitch.class).isZero())
        {
            return value;
        }
        parent().exit("Invalid value for argument: $", argumentValue.value());
        return null;
    }

    /**
     * Returns true if this parser can parser the given argument
     *
     * @param argumentValue The argument value
     * @return True if the argument can be parsed correctly
     */
    public boolean canParse(String argumentValue)
    {
        return converter.withoutTransmitting(() -> converter.convert(argumentValue) != null);
    }

    /**
     * Returns the description of this argument parser
     */
    public String description()
    {
        return description;
    }

    /**
     * Returns a help message for the argument
     */
    public String help()
    {
        return type.simpleName() + " (" + quantifier.name().toLowerCase().replace('_', ' ') + ") - "
                + description;
    }

    /**
     * Returns true if the argument can be present multiple times
     */
    public boolean isAllowedMultipleTimes()
    {
        return quantifier == ONE_OR_MORE
                || quantifier == ZERO_OR_MORE;
    }

    /**
     * Returns true if the argument may be omitted
     */
    public boolean isOptional()
    {
        return !isRequired();
    }

    /**
     * Returns true if the argument must be present
     */
    public boolean isRequired()
    {
        return quantifier == REQUIRED || quantifier == ONE_OR_MORE;
    }

    /**
     * Returns the allowed quantities of this argument
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

    CommandLineParser parent()
    {
        return parent;
    }

    void parent(CommandLineParser parent)
    {
        this.parent = parent;
    }
}
