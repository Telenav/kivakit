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

package com.telenav.kivakit.interfaces.string;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramString;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Interface to an object that can produce one or more different kinds of string representations. This can be useful
 * when the {@link Object#toString()} method is already being used or when other kinds of strings are needed for
 * specific purposes.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("SpellCheckingInspection")
@UmlRelation(label = "formats with", referent = StringFormattable.Format.class)
@UmlClassDiagram(diagram = DiagramString.class)
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface StringFormattable extends AsString
{
    /**
     * The type of format for a string returned by {@link StringFormattable#asString(Format)}
     */
    @UmlClassDiagram(diagram = DiagramString.class)
    @CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
                 testing = TESTING_NOT_NEEDED,
                 documentation = DOCUMENTATION_COMPLETE)
    @UmlExcludeSuperTypes enum Format
    {
        /** A format suitable for debug tracing */
        DEBUG,

        /** A format with only characters valid across filesystems */
        FILESYSTEM,

        /** A format for display in a browser */
        HTML,

        /** A format most useful for programmatic use */
        PROGRAMMATIC,

        /** A generic text format */
        TEXT,

        /** The format produced by calling toString() */
        TO_STRING,

        /** A format suitable for user presentation, allowing for multiple lines */
        USER_MULTILINE,

        /** A format suitable for user presentation, allowing only single lines */
        USER_SINGLE_LINE,

        /** A format suitable for display with a UI label */
        USER_LABEL,

        /** A compact format */
        COMPACT,

        /** A format suitable for log output */
        LOG;

        public boolean isHtml()
        {
            return equals(HTML);
        }

        public boolean isText()
        {
            return equals(TEXT);
        }
    }

    /**
     * Produces a string representation of this object in the given format
     *
     * @return A string representation of this object that is suitable for the given purpose
     */
    String asString(@NotNull Format format);

    @Override
    default String asString()
    {
        return asString(Format.TEXT);
    }
}
