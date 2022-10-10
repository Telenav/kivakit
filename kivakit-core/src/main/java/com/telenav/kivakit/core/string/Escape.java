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

package com.telenav.kivakit.core.string;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Escapes different kinds of strings for JavaScript, SQL and XML.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramString.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Escape
{
    /**
     * Returns the given text with single quotes escaped
     */
    public static String escapeJavaScript(String text)
    {
        return Strings.replaceAll(text, "'", "\\'");
    }

    /**
     * Returns the given string with single quotes escaped
     */
    public static String escapeSql(String text)
    {
        return Strings.replaceAll(text, "'", "''");
    }

    /**
     * Returns the given XML text with special characters escaped
     */
    public static String escapeXml(String xml)
    {
        var u1 = Strings.replaceAll(unescapeXml(xml), "\"", "&quot;");
        var u2 = Strings.replaceAll(u1, "&", "&amp;");
        var u3 = Strings.replaceAll(u2, "'", "&apos;");
        var u4 = Strings.replaceAll(u3, "<", "&lt;");
        return Strings.replaceAll(u4, ">", "&gt;");
    }

    /**
     * Returns the given xml string with escaping removed
     */
    public static String unescapeXml(String xml)
    {
        var u1 = Strings.replaceAll(xml, "&quot;", "\"");
        var u2 = Strings.replaceAll(u1, "&amp;", "&");
        var u3 = Strings.replaceAll(u2, "&apos;", "'");
        var u4 = Strings.replaceAll(u3, "&lt;", "<");
        return Strings.replaceAll(u4, "&gt;", ">");
    }
}
