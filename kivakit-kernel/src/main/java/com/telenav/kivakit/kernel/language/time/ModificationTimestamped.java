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

package com.telenav.kivakit.kernel.language.time;

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public interface ModificationTimestamped
{
    default Duration age()
    {
        return lastModified().elapsedSince();
    }

    default boolean isNewerThan(final ModificationTimestamped that)
    {
        return lastModified().isAfter(that.lastModified());
    }

    default boolean isOlderThan(final ModificationTimestamped that)
    {
        return lastModified().isBefore(that.lastModified());
    }

    default boolean lastModified(final Time modified)
    {
        return false;
    }

    default Time lastModified()
    {
        return Time.now();
    }
}
