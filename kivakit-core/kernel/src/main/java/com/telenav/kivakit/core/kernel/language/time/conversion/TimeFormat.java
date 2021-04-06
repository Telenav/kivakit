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

package com.telenav.kivakit.core.kernel.language.time.conversion;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public enum TimeFormat
{
    DATE("yyyy.MM.dd"),
    TIME("h.mma"),
    DATE_TIME("yyyy.MM.dd_h.mma"),
    DATE_TIME_WITH_SECONDS("yyyy.MM.dd_h.mm.ssa"),
    DATE_TIME_WITH_MILLISECONDS("yyyy.MM.dd_h.mm.ss.SSSa");

    private final String pattern;

    TimeFormat(final String pattern)
    {
        this.pattern = pattern;
    }

    DateTimeFormatter formatter()
    {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern(pattern)
                .toFormatter();
    }
}
