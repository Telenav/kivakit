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

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Calculates a {@link Rate} over time as a count is increased by {@link #add(int)} or {@link #increment()}. The rate
 * can be retrieved with {@link #rate()}. The calculator can be reset with {@link #reset()}.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE_EXPANDABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class RateCalculator
{
    private double count;

    private double lastCount;

    private Time start;

    private Time lastStart;

    private final Duration resetInterval;

    /**
     * Creates a rate calculator that resets every so often
     */
    public RateCalculator(Duration resetInterval)
    {
        this.resetInterval = resetInterval;
        reset();
    }

    /**
     * Add to the number of cycles
     */
    public void add(int count)
    {
        this.count += count;
    }

    /**
     * Increment the number of cycles
     */
    public void increment()
    {
        count++;
    }

    /**
     * Returns the calculated rate based on how many cycles have occurred since the rate calculator was created
     */
    public Rate rate()
    {
        var elapsed = start.elapsedSince();
        Rate rate;
        if (lastStart != null)
        {
            rate = new Rate(lastCount, resetInterval);
        }
        else
        {
            rate = new Rate(count, elapsed);
        }
        if (elapsed.isGreaterThan(resetInterval))
        {
            reset();
        }
        return rate;
    }

    /**
     * Resets rate calculation
     */
    public void reset()
    {
        lastCount = count;
        lastStart = start;
        count = 0;
        start = Time.now();
    }
}
