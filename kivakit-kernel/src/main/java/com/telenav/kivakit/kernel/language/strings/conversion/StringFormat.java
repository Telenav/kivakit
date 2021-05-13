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

package com.telenav.kivakit.kernel.language.strings.conversion;

import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.values.identifier.StringIdentifier;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramInterfaceString;
import com.telenav.kivakit.kernel.language.collections.map.string.NameMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

/**
 * The type of format for a string returned by {@link AsString#asString(StringFormat)}
 */
@UmlClassDiagram(diagram = DiagramInterfaceString.class)
@UmlExcludeSuperTypes
public class StringFormat extends StringIdentifier implements Named
{
    public static final NameMap<StringFormat> identifiers = new NameMap<>();

    // String versions of the format objects for use in switch statements as case values
    // and as annotation parameter values, as with @KivaKitFormatProperty

    public static final String DEBUGGER_IDENTIFIER = "debugger";

    public static final String FILESYSTEM_IDENTIFIER = "filesystem";

    public static final String HTML_IDENTIFIER = "html";

    public static final String PROGRAMMATIC_IDENTIFIER = "programmatic";

    public static final String TEXT_IDENTIFIER = "text";

    public static final String USER_MULTILINE_IDENTIFIER = "user-multiline";

    public static final String USER_SINGLE_LINE_IDENTIFIER = "user-single-line";

    public static final String USER_LABEL_IDENTIFIER = "user-label";

    public static final String LOG_IDENTIFIER = "log";

    /** Suitable for display in an IDE debugger, like "x = 9" */
    public static final StringFormat DEBUGGER = new StringFormat(DEBUGGER_IDENTIFIER);

    /** Suitable for use in a filesystem path, having no invalid filename characters */
    public static StringFormat FILESYSTEM = new StringFormat(FILESYSTEM_IDENTIFIER);

    /** Suitable for display in a browser */
    public static final StringFormat HTML = new StringFormat(HTML_IDENTIFIER);

    /** Suitable for programmatic use (parse-able), like "9" */
    public static final StringFormat PROGRAMMATIC = new StringFormat(PROGRAMMATIC_IDENTIFIER);

    /** Text suitable for any kind of use */
    public static final StringFormat TEXT = new StringFormat(TEXT_IDENTIFIER);

    /** Suitable as a (potentially multi-line) detailed display to an end-user */
    public static final StringFormat USER_MULTILINE = new StringFormat(USER_MULTILINE_IDENTIFIER);

    /** Suitable as single line display to an end-user, like "the variable x has the value 9." */
    public static StringFormat USER_SINGLE_LINE = new StringFormat(USER_SINGLE_LINE_IDENTIFIER);

    /** Suitable as a very short display to an end-user, like "x = 9" */
    public static final StringFormat USER_LABEL = new StringFormat(USER_LABEL_IDENTIFIER);

    /** Suitable for output to a log, like "[Object x = 9, y = 10]" */
    public static StringFormat LOG = new StringFormat(LOG_IDENTIFIER);

    public static StringFormat of(final String identifier)
    {
        return identifiers.get(identifier);
    }

    public StringFormat(final String identifier)
    {
        super(identifier);
        identifiers.add(this);
    }

    public boolean isHtml()
    {
        return equals(HTML);
    }

    public boolean isText()
    {
        return equals(TEXT);
    }

    @Override
    public String name()
    {
        return identifier();
    }
}
