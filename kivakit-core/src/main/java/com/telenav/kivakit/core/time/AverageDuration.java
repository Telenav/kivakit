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

package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.math.Average;
import com.telenav.kivakit.core.test.Tested;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import static com.telenav.kivakit.core.time.Duration.milliseconds;

/**
 * Computes an average duration for a succession of samples.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@LexakaiJavadoc(complete = true)
public class AverageDuration extends Average
{
    @Tested
    public void add(Duration duration)
    {
        super.add(duration.asMilliseconds());
    }

    @Tested
    public Duration averageDuration()
    {
        return milliseconds(average());
    }

    @Tested
    public Duration maximumDuration()
    {
        return milliseconds(super.maximum());
    }

    @Tested
    public Duration minimumDuration()
    {
        return milliseconds(super.minimum());
    }

    @Override
    public String toString()
    {
        return averageDuration().toString();
    }

    @Tested
    public Duration totalDuration()
    {
        return milliseconds(super.total());
    }
}
