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

package com.telenav.kivakit.core.project;

import com.telenav.cactus.metadata.BuildMetadata;
import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.interfaces.naming.Named;

import java.time.LocalDate;

import static com.telenav.cactus.metadata.BuildMetadata.buildMetaData;
import static com.telenav.cactus.metadata.BuildName.TELENAV_EPOCH_DAY;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.map.VariableMap.variableMap;
import static com.telenav.kivakit.core.language.primitive.Ints.parseFastInt;
import static com.telenav.kivakit.core.time.LocalTime.localTime;
import static com.telenav.kivakit.core.time.LocalTime.utcTimeZone;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;

/**
 * Information about a build, from the resource classpath resource /build.properties. The build information for a
 * project can be retrieved with {@link #build(Class)}, passing in any class in that project.
 *
 * <p><b>Build Date, Number and Name</b></p>
 *
 * <p>
 * The date that the build occurred <i>in UTC time</i> can be retrieved with {@link #buildJavaUtcDate()}. A build number
 * is the number of days since the start of the KivaKit epoch, December 5, 2020. There is only one build number per day
 * (hour of day is not considered if there are multiple builds in the same day). The build number can be retrieved with
 * {@link #buildNumber()} and the build also has a human-memorable {@link #name()} that is formed from the build number
 * using combinations of adjectives and nouns, like "sparkling piglet". The build name will always be the same for a
 * given build number or date.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Build implements Named
{
    /**
     * Returns build information for the given class in the root of the project. This is typically the {@link Project}
     * or Application class.
     */
    public static Build build(Class<?> projectType)
    {
        return new Build(buildMetaData(projectType));
    }

    /** The metadata for this build */
    private BuildMetadata metadata;

    protected Build()
    {
    }

    private Build(BuildMetadata metadata)
    {
        this.metadata = metadata;
    }

    /**
     * Returns the build day in number of days since the start of the UNIX epoch
     */
    public int buildEpochDay()
    {
        return TELENAV_EPOCH_DAY + buildNumber();
    }

    /**
     * Returns the UTC date of this build in standard [year].[month].[day-of-month] format
     */
    public String buildFormattedDate()
    {
        return property("build-date");
    }

    /**
     * Returns the date of this build in UTC time
     */
    public LocalDate buildJavaUtcDate()
    {
        return LocalDate.parse(property("build-date"), ISO_INSTANT);
    }

    /**
     * Returns the KivaKit build number for the calling project in days since December 5, 2020
     */
    public int buildNumber()
    {
        return parseFastInt(property("build-number"));
    }

    /**
     * Returns the date of the build in UTC time
     */
    public LocalTime buildUtcTime()
    {
        return localTime(utcTimeZone(), LocalDate.ofEpochDay(buildEpochDay()).atTime(0, 0));
    }

    /**
     * Returns the name of this build, such as "sparkling piglet"
     */
    @Override
    public String name()
    {
        return property("build-name");
    }

    /**
     * Returns project properties
     */
    public VariableMap<String> properties()
    {
        return variableMap(metadata.buildProperties());
    }

    /**
     * Returns the project property for the given key
     */
    public String property(String key)
    {
        return properties().get(key);
    }

    @Override
    public String toString()
    {
        return "#"
                + buildNumber()
                + " on "
                + buildFormattedDate()
                + " \"" + name() + "\"";
    }
}
