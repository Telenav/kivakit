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

package com.telenav.kivakit.core.math;

import com.telenav.kivakit.core.string.KivaKitFormat;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.interfaces.lifecycle.Resettable;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Computes a simple average, as well as a minimum and maximum value from a series of <i>double</i> sample values.
 * Sample values are added with {@link #add(double)}  and the average is computed by {@link #average()}.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class Average implements Resettable
{
    private double total;

    private int samples;

    private double maximum = Double.MIN_VALUE;

    private double minimum = Double.MAX_VALUE;

    public void add(Average that)
    {
        minimum = Math.min(minimum, that.minimum);
        maximum = Math.max(maximum, that.maximum);
        total += that.total;
        samples += that.samples;
    }

    /**
     * Adds the given sample value to this average
     */
    public void add(double value)
    {
        if (value < minimum)
        {
            minimum = value;
        }
        if (value > maximum)
        {
            maximum = value;
        }
        total += value;
        samples += 1;
    }

    /**
     * @return The average of all samples that have been added
     */
    @KivaKitFormat
    public double average()
    {
        if (samples > 0)
        {
            return total / samples;
        }
        return 0;
    }

    /**
     * @return The largest sample
     */
    @KivaKitFormat
    public double maximum()
    {
        return maximum;
    }

    /**
     * @return The smallest sample
     */
    @KivaKitFormat
    public double minimum()
    {
        return minimum;
    }

    /**
     * Resets this average to zero samples
     */
    @Override
    public void reset()
    {
        total = 0;
        samples = 0;
    }

    /**
     * @return The number of samples in this average
     */
    @KivaKitFormat
    public int samples()
    {
        return samples;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * @return The total of all samples in this average
     */
    @KivaKitFormat
    public double total()
    {
        return total;
    }
}
