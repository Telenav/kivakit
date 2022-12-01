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

package com.telenav.kivakit.settings;

import com.telenav.kivakit.conversion.core.language.object.ConvertedProperty;
import com.telenav.kivakit.conversion.core.language.primitive.IntegerConverter;
import com.telenav.kivakit.conversion.core.time.DurationConverter;
import com.telenav.kivakit.core.time.Duration;

/**
 * Java Bean with string conversion methods for each property to allow storage of this settings object in a JSON file
 *
 * @author jonathanl (shibo)
 */
public class ServerSettings
{
    private int port;

    private Duration timeout;

    public int port()
    {
        return port;
    }

    @ConvertedProperty(IntegerConverter.class)
    public void port(int port)
    {
        this.port = port;
    }

    public Duration timeout()
    {
        return timeout;
    }

    @ConvertedProperty(DurationConverter.class)
    public void timeout(Duration timeout)
    {
        this.timeout = timeout;
    }

    @Override
    public String toString()
    {
        return "[ServerSettings timeout = " + timeout + ", port = " + port + "]";
    }
}
