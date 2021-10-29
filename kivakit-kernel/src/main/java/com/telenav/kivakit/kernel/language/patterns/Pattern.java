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

package com.telenav.kivakit.kernel.language.patterns;

import com.telenav.kivakit.kernel.data.conversion.Converter;
import com.telenav.kivakit.kernel.data.conversion.string.language.IdentityConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.BooleanConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.FloatConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.IntegerConverter;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.patterns.character.Character;
import com.telenav.kivakit.kernel.language.patterns.character.CharacterClass;
import com.telenav.kivakit.kernel.language.patterns.character.LiteralCharacter;
import com.telenav.kivakit.kernel.language.patterns.closure.OneOrMore;
import com.telenav.kivakit.kernel.language.patterns.closure.Optional;
import com.telenav.kivakit.kernel.language.patterns.closure.ZeroOrMore;
import com.telenav.kivakit.kernel.language.patterns.group.Group;
import com.telenav.kivakit.kernel.language.patterns.logical.Or;
import com.telenav.kivakit.kernel.language.patterns.logical.Then;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguagePattern;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.regex.Matcher;

/**
 * Useful class for constructing readable and reusable regular expressions.
 * <p>
 * Patterns can be constructed from simple regular expressions and chained together with {@link #then(Pattern)}, {@link
 * #or(Pattern)}, {@link #optional()} and other operators. In this way, it is easy to build up large, complex patterns
 * from simple, readable building blocks. The pattern classes in this package also automatically track the capture group
 * numbers of any {@link Group}s for easy access after a match.
 * <p>
 * A variety of static constants are provided for use in subclasses and in constructing compound patterns.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "StaticInitializerReferencesSubClass" })
@UmlClassDiagram(diagram = DiagramLanguagePattern.class)
public abstract class Pattern
{
    /**
     * Common characters
     */
    public static final Character COMMA = character(',');

    public static final Character COLON = character(':');

    public static final Character SEMICOLON = character(';');

    public static final Character SLASH = character('/');

    public static final Character UNDERSCORE = character('_');

    public static final Character AMPERSAND = character('&');

    public static final Character PERCENT = character('%');

    public static final Character POUND_SIGN = character('#');

    public static final Character AT_SIGN = character('@');

    public static final Character EXCLAMATION_POINT = character('!');

    public static final Character EQUALS = character('=');

    public static final Character SPACE = character(' ');

    /**
     * Literal characters
     */
    public static final LiteralCharacter BACKSLASH = literal('\\');

    public static final LiteralCharacter DOT = literal('.');

    public static final LiteralCharacter PLUS = literal('+');

    public static final LiteralCharacter QUOTE = literal('\"');

    public static final LiteralCharacter SINGLE_QUOTE = literal('\'');

    public static final LiteralCharacter MINUS = literal('-');

    public static final LiteralCharacter DASH = literal('-');

    public static final LiteralCharacter DOLLAR_SIGN = literal('$');

    public static final LiteralCharacter TILDE = literal('~');

    public static final LiteralCharacter STAR = literal('*');

    public static final LiteralCharacter PIPE = literal('|');

    public static final LiteralCharacter LEFT_PAREN = literal('(');

    public static final LiteralCharacter RIGHT_PAREN = literal(')');

    public static final LiteralCharacter LEFT_CURLY = literal('{');

    public static final LiteralCharacter RIGHT_CURLY = literal('}');

    public static final LiteralCharacter LEFT_SQUARE = literal('[');

    public static final LiteralCharacter RIGHT_SQUARE = literal(']');

    /**
     * Predefined character classes
     */
    public static final Expression ANY_CHARACTER = expression(".");

    public static final Expression WHITESPACE_CHARACTER = expression("\\s");

    public static final Expression WORD_CHARACTER = expression("\\w");

    public static final Expression NON_WORD_CHARACTER = expression("\\W");

    public static final Expression DIGIT = expression("\\d");

    /**
     * Custom character classes
     */
    public static final CharacterClass ALPHABETIC_CHARACTER = characterClass().withAlphabetic();

    public static final CharacterClass ALPHANUMERIC_CHARACTER = characterClass().withAlphanumeric();

    public static final CharacterClass STRING_CHARACTER = characterClass(WORD_CHARACTER, MINUS, DOT);

    public static final CharacterClass HEXADECIMAL_CHARACTER = characterClass(DIGIT, range('a', 'f'), range('A', 'F'));

    public static final Pattern HEXADECIMAL_NUMBER = HEXADECIMAL_CHARACTER.oneOrMore();

    /**
     * Space
     */
    public static final Pattern WHITESPACE = WHITESPACE_CHARACTER.oneOrMore();

    public static final Pattern OPTIONAL_WHITESPACE = WHITESPACE_CHARACTER.zeroOrMore();

    /**
     * Separators
     */
    public static final Pattern COMMA_SEPARATOR = COMMA.withOptionalWhiteSpace();

    public static final Pattern COLON_SEPARATOR = COLON.withOptionalWhiteSpace();

    public static final Pattern SEMICOLON_SEPARATOR = SEMICOLON.withOptionalWhiteSpace();

    /**
     * Words
     */
    public static final Pattern WORD = WORD_CHARACTER.oneOrMore();

    public static final Pattern NON_WORD = NON_WORD_CHARACTER.oneOrMore();

    public static final Pattern OPTIONAL_WORD = WORD_CHARACTER.zeroOrMore();

    /**
     * Numbers
     */
    public static final Pattern BOOLEAN = expression("true").or("false");

    public static final Pattern DIGITS = DIGIT.oneOrMore();

    public static final Pattern POSITIVE_INTEGER = DIGITS;

    public static final Pattern INTEGER = MINUS.optional().then(DIGITS);

    public static final Pattern FLOATING_POINT_NUMBER = MINUS.optional().then(DIGITS.optional())
            .then(DOT.then(DIGITS).optional());

    /**
     * Anything
     */
    public static final Pattern ANYTHING = ANY_CHARACTER.zeroOrMore();

    public static final Pattern ANYTHING_NON_EMPTY = ANY_CHARACTER.oneOrMore();

    public static final Pattern ANYTHING_BUT_A_QUOTE = characterClass(QUOTE).inverted();

    /**
     * Strings
     */
    public static final Pattern DOUBLE_QUOTED_STRING = QUOTE.then(ANYTHING_BUT_A_QUOTE.zeroOrMore()).then(QUOTE);

    public static final Pattern ANYTHING_BUT_A_SINGLE_QUOTE = characterClass(SINGLE_QUOTE).inverted();

    public static final Pattern SINGLE_QUOTED_STRING = SINGLE_QUOTE.then(ANYTHING_BUT_A_SINGLE_QUOTE.zeroOrMore())
            .then(SINGLE_QUOTE);

    public static final Pattern STRING = STRING_CHARACTER.oneOrMore().or(DOUBLE_QUOTED_STRING).or(SINGLE_QUOTED_STRING)
            .parenthesized();

    public static final Pattern OPTIONAL_STRING = STRING.optional();

    /**
     * XML
     */
    public static final Pattern XML_ELEMENT_NAME = expression("[A-Za-z_][A-Za-z0-9_.-]*");

    public static final Pattern XML_ATTRIBUTE_NAME = XML_ELEMENT_NAME;

    /**
     * Perl-style variable interpolations
     */
    public static final Pattern VARIABLE_NAME = ALPHABETIC_CHARACTER.with(UNDERSCORE)
            .then(ALPHANUMERIC_CHARACTER.with(UNDERSCORE).zeroOrMore());

    public static final Pattern PERL_INTERPOLATION = expression("\\$\\{" + VARIABLE_NAME + "\\}");

    public static CharacterClass anyOf(String characters)
    {
        return new CharacterClass(characters);
    }

    public static <T> Expression anyOf(T[] options)
    {
        return new Expression(new ObjectList<>(Maximum.maximum(options)).appendAll(options).join("|"));
    }

    public static Character character(char character)
    {
        return new Character(character);
    }

    public static CharacterClass characterClass(Object... objects)
    {
        return new CharacterClass(objects);
    }

    public static Expression constant(String expression)
    {
        return expression(java.util.regex.Pattern.quote(expression));
    }

    public static Expression expression(String expression)
    {
        return new Expression(expression);
    }

    public static LiteralCharacter literal(char character)
    {
        return new LiteralCharacter(character);
    }

    public static CharacterClass range(char first, char last)
    {
        return characterClass().withRange(first, last);
    }

    /**
     * The compiled pattern
     */
    private java.util.regex.Pattern pattern;

    public String afterMatch(String input)
    {
        var matcher = matcher(input);
        if (matcher.lookingAt())
        {
            return input.substring(matcher.end());
        }
        return null;
    }

    /**
     * Binds this Pattern to one or more capturing groups. Since Patterns can nest, the binding process can recurse.
     *
     * @param group The capture group number to bind to
     */
    public abstract int bind(int group);

    public Group<Boolean> booleanGroup(Listener listener)
    {
        return group(new BooleanConverter(listener));
    }

    public Pattern caseInsensitive()
    {
        return expression("(?i)").then(this).then(expression("(?-i)"));
    }

    public boolean find(String input)
    {
        return matcher(input).find();
    }

    public Group<Float> floatGroup(Listener listener)
    {
        return group(new FloatConverter(listener));
    }

    public <T> Group<T> group(Converter<String, T> converter)
    {
        return new Group<>(this, converter);
    }

    public Group<String> group(Listener listener)
    {
        return group(new IdentityConverter(listener));
    }

    public Group<Integer> integerGroup(Listener listener)
    {
        return group(new IntegerConverter(listener));
    }

    public boolean lookingAt(String input)
    {
        return matcher(input).lookingAt();
    }

    /**
     * Creates a matcher against a given input character sequence.
     *
     * @param input The input to match against
     * @return The matcher
     */
    public final Matcher matcher(CharSequence input)
    {
        return matcher(input, 0);
    }

    /**
     * Creates a matcher with the given regexp compile flags. Once you call this method with a given regexp compile flag
     * value, the pattern will be compiled. Calling it again with a different value for flags will not recompile the
     * pattern.
     *
     * @param input The input to match
     * @param flags One or more of the standard Java regular expression compile flags (see {@link
     * java.util.regex.Pattern#compile(String, int)})
     * @return The matcher
     */
    public final Matcher matcher(CharSequence input, int flags)
    {
        return compile(flags).matcher(input);
    }

    public final Matcher matcherCaseInsensitive(CharSequence input)
    {
        return matcher(input, java.util.regex.Pattern.CASE_INSENSITIVE);
    }

    public boolean matches(CharSequence input)
    {
        return matcher(input).matches();
    }

    public boolean matchesIgnoreCase(CharSequence input)
    {
        return matcherCaseInsensitive(input).matches();
    }

    public Pattern oneOrMore()
    {
        return new OneOrMore(this);
    }

    public Pattern optional()
    {
        return new Optional(this);
    }

    public Pattern or(Pattern that)
    {
        return new Or(this, that);
    }

    public Pattern or(String that)
    {
        return or(expression(that));
    }

    public Pattern parenthesized()
    {
        return new Parenthesized(this);
    }

    public Pattern then(Pattern that)
    {
        return new Then(this, that);
    }

    public Pattern then(String that)
    {
        return then(expression(that));
    }

    public abstract String toExpression();

    @Override
    public String toString()
    {
        return toExpression();
    }

    public Pattern withOptionalWhiteSpace()
    {
        return OPTIONAL_WHITESPACE.then(this).then(OPTIONAL_WHITESPACE);
    }

    public ZeroOrMore zeroOrMore()
    {
        return new ZeroOrMore(this);
    }

    /**
     * Compiles this Pattern with the given Java regular expression flags.
     *
     * @param flags One or more of the standard Java regular expression compile flags (see {@link
     * java.util.regex.Pattern#compile(String, int)})
     */
    private synchronized java.util.regex.Pattern compile(int flags)
    {
        if (pattern == null)
        {
            bind(0);
            pattern = java.util.regex.Pattern.compile(toExpression(), flags);
        }
        return pattern;
    }
}
