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

import com.telenav.cactus.build.metadata.BuildMetadata;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.interfaces.naming.Named;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Information about a build, from the resource classpath resource /build.properties. The build information for a
 * project can be retrieved with {@link #build(Class)}, passing in any class in that project.
 *
 * <p><b>Build Date, Number and Name</b></p>
 *
 * <p>
 * The date that the build occurred <i>in UTC time</i> can be retrieved with {@link #utcDate()}. A build number is the
 * number of days since the start of the KivaKit epoch, December 5, 2020. There is only one build number per day (hour
 * of day is not considered if there are multiple builds in the same day). The build number can be retrieved with {@link
 * #number()} and the build also has a human-memorable {@link #name()} that is formed from the build number using
 * combinations of adjectives and nouns, like "sparkling piglet". The build name will always be the same for a given
 * build number or date.
 * </p>
 *
 * @author jonathanl (shibo)
 */
public class Build implements Named
{
    /**
     * @return Build information for the given project
     */
    public static Build build(Class<?> project)
    {
        return new Build(BuildMetadata.of(project));
    }

    /** The metadata for this build */
    private BuildMetadata metadata;

    protected Build()
    {
    }

    private Build(final BuildMetadata metadata)
    {
        this.metadata = metadata;
    }

    /**
     * @return The date of the build in UTC time
     */
    public LocalTime date()
    {
        return LocalTime.of(LocalTime.utcTimeZone(), LocalDate.ofEpochDay(epochDay()).atTime(0, 0));
    }

    /**
     * @return The number of days since the start of the UNIX epoch
     */
    public int epochDay()
    {
        return BuildMetadata.KIVAKIT_EPOCH_DAY + number();
    }

    /**
     * @return The UTC date of this build in standard [year].[month].[day-of-month] format
     */
    public String formattedDate()
    {
        return property("build-date");
    }

    /**
     * @return The name of this build, such as "sparkling piglet"
     */
    @Override
    public String name()
    {
        return property("build-name");
    }

    /**
     * @return The KivaKit build number for the calling project in days since December 5, 2020
     */
    public int number()
    {
        return Ints.parseFast(property("build-number"));
    }

    /**
     * @return Project properties
     */
    public VariableMap<String> properties()
    {
        return VariableMap.variableMap(metadata.buildProperties());
    }

    /**
     * @return The project property for the given key
     */
    public String property(String key)
    {
        return properties().get(key);
    }

    @Override
    public String toString()
    {
        return "#"
                + number()
                + " on "
                + formattedDate()
                + " \"" + name() + "\"";
    }

    /**
     * @return The date of this build in UTC time
     */
    public LocalDate utcDate()
    {
        return LocalDate.parse(property("build-date"), DateTimeFormatter.ISO_INSTANT);
    }
}
