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

package kernel.language.string;

import com.telenav.kivakit.core.kernel.data.validation.reporters.ValidationFailure;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.iteration.Iterables;
import com.telenav.kivakit.core.kernel.language.strings.*;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureFalse;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

public class StringsTest
{
    @Test
    public void testCamelToHyphenated()
    {
        ensureEqual("camel-case", CaseFormat.camelCaseToHyphenated("camelCase"));
        ensureEqual("vertex", CaseFormat.camelCaseToHyphenated("vertex"));
    }

    @Test
    public void testCapitalization()
    {
        ensureEqual("A", CaseFormat.capitalizeOnlyFirstLetter("a"));
        ensureEqual("A", CaseFormat.capitalize("a"));
        ensureEqual("Auto", CaseFormat.capitalizeOnlyFirstLetter("auto"));
        ensureEqual("Auto", CaseFormat.capitalize("auto"));
        ensureEqual("Automobile", CaseFormat.capitalizeOnlyFirstLetter("autoMobile"));
        ensureEqual("AutoMobile", CaseFormat.capitalize("autoMobile"));
    }

    @Test
    public void testContainsIgnoreCase()
    {
        ensure(Strings.containsIgnoreCase("billyBobThornton", "bob"));
        ensureFalse(Strings.containsIgnoreCase("billyJoeThornton", "bob"));
    }

    @Test
    public void testCount()
    {
        ensureEqual(0, Strings.occurrences("", 'a'));
        ensureEqual(1, Strings.occurrences("a", 'a'));
        ensureEqual(2, Strings.occurrences("oaoao", 'a'));
        ensureEqual(3, Strings.occurrences("xxxaaxxax", 'a'));
    }

    @Test
    public void testDecapitalize()
    {
        ensureEqual("foo", CaseFormat.decapitalize("Foo"));
        ensureEqual("kangaRoo", CaseFormat.decapitalize("KangaRoo"));
        ensureEqual("f", CaseFormat.decapitalize("F"));
        ensureEqual("", CaseFormat.decapitalize(""));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testEqual()
    {
        ensure(Strings.equals("foo", "foo"));
        ensureFalse(Strings.equals("foo", "fooe"));
        ensureFalse(Strings.equals(null, "foo"));
        ensureFalse(Strings.equals("foo", null));
        ensure(Strings.equals(null, null));
    }

    @Test
    public void testEscapeXml()
    {
        ensureEqual("a &lt; b", Escape.xml("a < b"));
        ensureEqual("a &lt; b", Escape.xml("a &lt; b"));
    }

    @Test
    public void testFirstPathComponent()
    {
        ensureEqual("foo", PathStrings.optionalHead("foo/bar/baz", '/'));
    }

    @Test
    public void testFrench()
    {
        ensure(Strings.isFrench("Avenue des Églises"));
        ensure(Strings.isFrench("Boulevard de l'Ange-Gardien"));
        ensureFalse(Strings.isFrench("Will's Ave"));
    }

    @Test
    public void testIndented()
    {
        ensureEqual("  a\n  b", Indent.by(2, "a\nb"));
        ensureEqual("\n  a\n  b", Indent.by(2, "\na\nb"));
    }

    @Test
    public void testIsEmpty()
    {
        ensure(Strings.isEmpty(null));
        ensure(Strings.isEmpty(""));
        ensure(Strings.isEmpty(" "));
        ensureFalse(Strings.isEmpty("a"));
    }

    @Test
    public void testIsInteger()
    {
        ensureFalse(AsciiArt.isNaturalNumber(""));
        ensureFalse(AsciiArt.isNaturalNumber(null));
        ensureFalse(AsciiArt.isNaturalNumber("banana"));
        ensure(AsciiArt.isNaturalNumber("1"));
        ensure(AsciiArt.isNaturalNumber("1129831928379128739128739182739182739182793127"));
    }

    @Test
    public void testLeftPad()
    {
        ensureEqual("....4", Align.right("4", 5, '.'));
        ensureEqual("4", Align.right("4", 1, '.'));
        ensureEqual("45", Align.right("45", 1, '.'));
    }

    @Test
    public void testLevenshteinDistance()
    {
        ensureEqual(3, Comparison.levenshteinDistance("kitten", "sitting"));
        ensureEqual(0, Comparison.levenshteinDistance("kitten", "kitten"));
        ensureEqual(1, Comparison.levenshteinDistance("kitten", "kitton"));
        ensureEqual(6, Comparison.levenshteinDistance("kitten", "flobpa"));
    }

    @Test
    public void testLineCount()
    {
        ensureEqual(1, AsciiArt.lineCount("a"));
        ensureEqual(2, AsciiArt.lineCount("a\nb"));
        ensureEqual(3, AsciiArt.lineCount("a\nb\nc"));
    }

    @Test
    public void testNormalizeSymbolsAndAccents()
    {
        ensureEqual("a", Normalize.normalizeSymbolsAndAccents("a"));
        ensureEqual("oo", Normalize.normalizeSymbolsAndAccents("o\u00B0"));
        ensureEqual("oss", Normalize.normalizeSymbolsAndAccents("o\u00DF"));
        ensureEqual("a", Normalize.normalizeSymbolsAndAccents("\u00E1"));
        ensureEqual("o", Normalize.normalizeSymbolsAndAccents("o"));
    }

    @Test
    public void testOptional()
    {
        ensureEqual("", StringTo.nonNullString(null));
        ensureEqual("foo", StringTo.nonNullString("foo"));
        ensureEqual("1", StringTo.nonNullString(1));
    }

    @Test
    public void testOptionalHead()
    {
        ensureEqual("abc", PathStrings.optionalHead("abc-x-def", "-x-"));
        ensureEqual("abc", PathStrings.optionalHead("abc", "x"));
        ensureEqual("abc", PathStrings.optionalHead("abc", "a"));
    }

    @Test
    public void testOptionalSuffix()
    {
        ensureEqual("baz", PathStrings.optionalSuffix("foo/bar/baz", '/'));
        ensureEqual("foobaz", PathStrings.optionalSuffix("foobaz", '/'));
        ensureEqual(null, PathStrings.optionalSuffix(null, '/'));
    }

    @Test
    public void testRemove()
    {
        ensureEqual("abcghi", Strings.remove("abcdefghi", 3, 6));
        ensureEqual("abcdef", Strings.remove("abcdefghi", 6, 9));
        ensureEqual("defghi", Strings.remove("abcdefghi", 0, 3));
    }

    @Test
    public void testRepeat()
    {
        ensureEqual("**********", AsciiArt.repeat(10, "*"));
        ensureEqual("", AsciiArt.repeat(0, "*"));
        try
        {
            ensureEqual("", AsciiArt.repeat(-1, "*"));
            fail("Should have thrown AssertionError");
        }
        catch (final ValidationFailure e)
        {
            // expected
        }
        ensureEqual("hellohellohellohellohello", AsciiArt.repeat(5, "hello"));
        ensureEqual("", AsciiArt.repeat(0, "hello"));
        try
        {
            ensureEqual("", AsciiArt.repeat(-1, "hello"));
            fail("Should have thrown AssertionError");
        }
        catch (final ValidationFailure e)
        {
            // expected
        }
    }

    @Test
    public void testSplit()
    {
        ensure(Iterables.equals(StringList.split("a,b,c", ","), Split.split("a,b,c", ",")));
        ensure(Iterables.equals(StringList.split(",b,c", ","), Split.split(",b,c", ",")));
        ensure(Iterables.equals(StringList.split("a,b,", ","), Split.split("a,b,", ",")));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testSplitOnFirst()
    {
        final String[] split = Split.splitOnFirst("foo/bar/baz", '/');
        ensure(split != null);
        ensureEqual("foo", split[0]);
        ensureEqual("bar/baz", split[1]);
    }

    @Test
    public void testTail()
    {
        ensureEqual("def", PathStrings.tail("abc-x-def", "-x-"));
        ensureEqual(null, PathStrings.tail("abc", "x"));
        ensureEqual(null, PathStrings.tail("abc", "c"));
    }

    @Test
    public void testToBinaryString()
    {
        ensureEqual("001", StringTo.binary(1, 3));
        ensureEqual("010", StringTo.binary(2, 3));
        ensureEqual("111", StringTo.binary(7, 3));
        ensureEqual("111", StringTo.binary(15, 3));
        ensureEqual("11", StringTo.binary(7, 2));
    }

    @Test
    public void testTrailing()
    {
        ensureEqual("", Strings.trailing("", 0));
        ensureEqual("", Strings.trailing("", 1));
        ensureEqual("c", Strings.trailing("abc", 1));
        ensureEqual("bc", Strings.trailing("abc", 2));
        ensureEqual("abc", Strings.trailing("abc", 3));
        ensureEqual("f", Strings.trailing("abcdef", 1));
        ensureEqual("ef", Strings.trailing("abcdef", 2));
        ensureEqual("def", Strings.trailing("abcdef", 3));
        ensureEqual("abc", Strings.trailing("abc", 9));
    }

    @Test
    public void testUnescapeXml()
    {
        ensureEqual("a < b", Escape.unescapeXml("a &lt; b"));
        ensureEqual("\"a\" > \"b\"", Escape.unescapeXml("&quot;a&quot; &gt; &quot;b&quot;"));
    }

    @Test
    public void testUpperCamelToHyphenated()
    {
        ensureEqual("vertex", CaseFormat.camelCaseToHyphenated("Vertex"));
        ensureEqual("b2b-industry", CaseFormat.camelCaseToHyphenated("B2BIndustry"));
    }

    @Test
    public void testWithoutSuffix()
    {
        ensureEqual("a.b", PathStrings.withoutSuffix("a.b.c", '.'));
        ensureEqual(null, PathStrings.withoutSuffix("a.b.c", 'x'));
        ensureEqual(null, PathStrings.withoutSuffix(null, '.'));
    }
}
