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

package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_REQUIRED;

/**
 * The reason why a thread completed, either it was interrupted, it timed out or it succeeded.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE,
            testing = TESTING_NOT_REQUIRED,
            documentation = FULLY_DOCUMENTED)
public enum WakeState
{
    /** Waiting was interrupted */
    INTERRUPTED,

    /** Waiting timed out */
    TIMED_OUT,

    /** The awaited operation completed */
    COMPLETED,

    /** The awaited operation failed due to an exception */
    TERMINATED;

    /**
     * @return A wake state with the given thrown exception
     */
    static WakeState terminated(Throwable thrown)
    {
        var wake = TERMINATED;
        wake.thrown = thrown;
        return wake;
    }

    /** Any exception that was thrown */
    private Throwable thrown;

    /**
     * @return True if the operation was completed
     */
    public boolean completed()
    {
        return this == COMPLETED;
    }

    /**
     * @return True if the operation did not complete
     */
    public boolean failed()
    {
        return !completed();
    }

    /**
     * @return Any exception that was thrown
     */
    public Throwable thrown()
    {
        return thrown;
    }
}
