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

package com.telenav.kivakit.commandline;

import com.telenav.kivakit.kernel.language.values.version.Versioned;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Application metadata used in formulating command line usage help.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public interface ApplicationMetadata extends Versioned
{
    /**
     * @return True if command line parsing problems should result in a call to {@link System#exit(int)}. The default
     * return value for this method is *true* to ensure that the VM isn't prevented from exiting by non-daemon threads.
     */
    default boolean callSystemExitOnUnrecoverableError()
    {
        return true;
    }

    /**
     * @return The application description
     */
    String description();
}
