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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.math.Average;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.time.Duration.milliseconds;

/**
 * Computes an average duration for a succession of samples.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class AverageDuration extends Average
{
    /**
     * Adds the given duration to this average
     */
    public void add(Duration duration)
    {
        super.add(duration.asMilliseconds());
    }

    /**
     * Returns the average duration so far
     */
    public Duration averageDuration()
    {
        return milliseconds(average());
    }

    /**
     * Returns the maximum duration
     */
    public Duration maximumDuration()
    {
        return milliseconds(super.maximum());
    }

    /**
     * Returns the minimum duration
     */
    public Duration minimumDuration()
    {
        return milliseconds(super.minimum());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return averageDuration().toString();
    }

    /**
     * Returns the total duration
     */
    public Duration totalDuration()
    {
        return milliseconds(super.total());
    }
}
