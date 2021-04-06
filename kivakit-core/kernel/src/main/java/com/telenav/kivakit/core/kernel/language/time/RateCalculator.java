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

package com.telenav.kivakit.core.kernel.language.time;

public class RateCalculator
{
    private double count;

    private double lastCount;

    private Time start;

    private Time lastStart;

    private final Duration resetInterval;

    public RateCalculator(final Duration resetInterval)
    {
        this.resetInterval = resetInterval;
        reset();
    }

    public void add(final int count)
    {
        this.count += count;
    }

    public void increment()
    {
        count++;
    }

    public Rate rate()
    {
        final var elapsed = start.elapsedSince();
        final Rate rate;
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

    private void reset()
    {
        lastCount = count;
        lastStart = start;
        count = 0;
        start = Time.now();
    }
}
