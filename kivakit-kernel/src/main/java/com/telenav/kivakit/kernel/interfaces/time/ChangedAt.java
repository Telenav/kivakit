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

package com.telenav.kivakit.kernel.interfaces.time;

import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public interface ChangedAt
{
    default Time lastModified()
    {
        return unsupported("Cannot retrieve last modified time from: ${class}", getClass());
    }

    default boolean wasChangedAfter(final ChangedAt that)
    {
        return lastModified().isAfter(that.lastModified());
    }

    default boolean wasChangedBefore(final ChangedAt that)
    {
        return lastModified().isBefore(that.lastModified());
    }
}
