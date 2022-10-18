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

package com.telenav.kivakit.internal.tests.core.string;

import com.telenav.kivakit.core.collections.iteration.Iterables;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.core.string.CaseFormat;
import com.telenav.kivakit.core.string.Paths;
import com.telenav.kivakit.core.string.Split;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.collections.list.StringList.split;
import static com.telenav.kivakit.core.string.Align.rightAlign;
import static com.telenav.kivakit.core.string.AsciiArt.repeat;
import static com.telenav.kivakit.core.string.CaseFormat.camelCaseToHyphenated;
import static com.telenav.kivakit.core.string.CaseFormat.decapitalize;
import static com.telenav.kivakit.core.string.Escape.escapeXml;
import static com.telenav.kivakit.core.string.Escape.unescapeXml;
import static com.telenav.kivakit.core.string.Indent.indentBy;
import static com.telenav.kivakit.core.string.Paths.pathTail;
import static com.telenav.kivakit.core.string.Paths.pathWithoutSuffix;
import static com.telenav.kivakit.core.string.Split.splitOnFirst;
import static com.telenav.kivakit.core.string.StringConversions.toBinaryString;
import static com.telenav.kivakit.core.string.StringConversions.toNonNullString;
import static com.telenav.kivakit.core.string.StringSimilarity.levenshteinDistance;
import static com.telenav.kivakit.core.string.Strings.equalsAllowNull;
import static com.telenav.kivakit.core.string.Strings.isNaturalNumber;
import static com.telenav.kivakit.core.string.Strings.isNullOrBlank;
import static com.telenav.kivakit.core.string.Strings.occurrences;
import static com.telenav.kivakit.core.string.Strings.trailing;

@SuppressWarnings("SpellCheckingInspection")
public class StringsTest extends CoreUnitTest
{
    @Test
    public void testCamelToHyphenated()
    {
        ensureEqual("camel-case", camelCaseToHyphenated("camelCase"));
        ensureEqual("vertex", camelCaseToHyphenated("vertex"));
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

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void testCount()
    {
        ensureEqual(0, occurrences("", 'a'));
        ensureEqual(1, occurrences("a", 'a'));
        ensureEqual(2, occurrences("oaoao", 'a'));
        ensureEqual(3, occurrences("xxxaaxxax", 'a'));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void testDecapitalize()
    {
        ensureEqual("foo", decapitalize("Foo"));
        ensureEqual("kangaRoo", decapitalize("KangaRoo"));
        ensureEqual("f", decapitalize("F"));
        ensureEqual("", decapitalize(""));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testEqual()
    {
        ensure(equalsAllowNull("foo", "foo"));
        ensureFalse(equalsAllowNull("foo", "fooe"));
        ensureFalse(equalsAllowNull(null, "foo"));
        ensureFalse(equalsAllowNull("foo", null));
        ensure(equalsAllowNull(null, null));
    }

    @Test
    public void testEscapeXml()
    {
        ensureEqual("a &lt; b", escapeXml("a < b"));
        ensureEqual("a &lt; b", escapeXml("a &lt; b"));
    }

    @Test
    public void testFirstPathComponent()
    {
        ensureEqual("foo", Paths.pathOptionalHead("foo/bar/baz", '/'));
    }

    @Test
    public void testIndented()
    {
        ensureEqual("  a\n  b", indentBy(2, "a\nb"));
        ensureEqual("\n  a\n  b", indentBy(2, "\na\nb"));
    }

    @Test
    public void testIsEmpty()
    {
        ensure(isNullOrBlank(null));
        ensure(isNullOrBlank(""));
        ensure(isNullOrBlank(" "));
        ensureFalse(isNullOrBlank("a"));
    }

    @Test
    public void testIsInteger()
    {
        ensureFalse(isNaturalNumber(""));
        ensureFalse(isNaturalNumber(null));
        ensureFalse(isNaturalNumber("banana"));
        ensure(isNaturalNumber("1"));
        ensure(isNaturalNumber("1129831928379128739128739182739182739182793127"));
    }

    @Test
    public void testLeftPad()
    {
        ensureEqual("....4", rightAlign("4", 5, '.'));
        ensureEqual("4", rightAlign("4", 1, '.'));
        ensureEqual("45", rightAlign("45", 1, '.'));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void testLevenshteinDistance()
    {
        ensureEqual(3, levenshteinDistance("kitten", "sitting"));
        ensureEqual(0, levenshteinDistance("kitten", "kitten"));
        ensureEqual(1, levenshteinDistance("kitten", "kitton"));
        ensureEqual(6, levenshteinDistance("kitten", "flobpa"));
    }

    @Test
    public void testLineCount()
    {
        ensureEqual(1, AsciiArt.lineCount("a"));
        ensureEqual(2, AsciiArt.lineCount("a\nb"));
        ensureEqual(3, AsciiArt.lineCount("a\nb\nc"));
    }

    @Test
    public void testOptional()
    {
        ensureEqual("", toNonNullString(null));
        ensureEqual("foo", toNonNullString("foo"));
        ensureEqual("1", toNonNullString(1));
    }

    @Test
    public void testOptionalHead()
    {
        ensureEqual("abc", Paths.pathOptionalHead("abc-x-def", "-x-"));
        ensureEqual("abc", Paths.pathOptionalHead("abc", "x"));
        ensureEqual("abc", Paths.pathOptionalHead("abc", "a"));
    }

    @Test
    public void testOptionalSuffix()
    {
        ensureEqual("baz", Paths.pathOptionalSuffix("foo/bar/baz", '/'));
        ensureEqual("foobaz", Paths.pathOptionalSuffix("foobaz", '/'));
        ensureEqual(null, Paths.pathOptionalSuffix(null, '/'));
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
        ensureEqual("**********", repeat(10, "*"));
        ensureEqual("", repeat(0, "*"));
        ensureThrows(() -> repeat(-1, "*"));
        //noinspection SpellCheckingInspection
        ensureEqual("hellohellohellohellohello", repeat(5, "hello"));
        ensureEqual("", repeat(0, "hello"));
        ensureThrows(() -> repeat(-1, "hello"));
    }

    @Test
    public void testSplit()
    {
        ensure(Iterables.equalIterables(split("a,b,c", ","), Split.split("a,b,c", ",")));
        ensure(Iterables.equalIterables(split(",b,c", ","), Split.split(",b,c", ",")));
        ensure(Iterables.equalIterables(split("a,b,", ","), Split.split("a,b,", ",")));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testSplitOnFirst()
    {
        String[] split = splitOnFirst("foo/bar/baz", '/');
        ensure(split != null);
        ensureEqual("foo", split[0]);
        ensureEqual("bar/baz", split[1]);
    }

    @Test
    public void testTail()
    {
        ensureEqual("def", pathTail("abc-x-def", "-x-"));
        ensureEqual(null, pathTail("abc", "x"));
        ensureEqual(null, pathTail("abc", "c"));
    }

    @Test
    public void testToBinaryString()
    {
        ensureEqual("001", toBinaryString(1, 3));
        ensureEqual("010", toBinaryString(2, 3));
        ensureEqual("111", toBinaryString(7, 3));
        ensureEqual("111", toBinaryString(15, 3));
        ensureEqual("11", toBinaryString(7, 2));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void testTrailing()
    {
        ensureEqual("", trailing("", 0));
        ensureEqual("", trailing("", 1));
        ensureEqual("c", trailing("abc", 1));
        ensureEqual("bc", trailing("abc", 2));
        ensureEqual("abc", trailing("abc", 3));
        ensureEqual("f", trailing("abcdef", 1));
        ensureEqual("ef", trailing("abcdef", 2));
        ensureEqual("def", trailing("abcdef", 3));
        ensureEqual("abc", trailing("abc", 9));
    }

    @Test
    public void testUnescapeXml()
    {
        ensureEqual("a < b", unescapeXml("a &lt; b"));
        ensureEqual("\"a\" > \"b\"", unescapeXml("&quot;a&quot; &gt; &quot;b&quot;"));
    }

    @Test
    public void testUpperCamelToHyphenated()
    {
        ensureEqual("vertex", camelCaseToHyphenated("Vertex"));
        ensureEqual("b2b-industry", camelCaseToHyphenated("B2BIndustry"));
    }

    @Test
    public void testWithoutSuffix()
    {
        ensureEqual("a.b", pathWithoutSuffix("a.b.c", '.'));
        ensureEqual(null, pathWithoutSuffix("a.b.c", 'x'));
        ensureEqual(null, pathWithoutSuffix(null, '.'));
    }
}
