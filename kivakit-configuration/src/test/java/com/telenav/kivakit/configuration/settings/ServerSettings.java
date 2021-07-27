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

package com.telenav.kivakit.configuration.settings;

import com.telenav.kivakit.kernel.data.conversion.string.primitive.IntegerConverter;
import com.telenav.kivakit.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.time.Duration;

/**
 * Configuration Java Bean with string conversion methods for each property to allow storage of this configuration in a
 * property file.
 *
 * @author jonathanl (shibo)
 */
public class ServerSettings
{
    private int port;

    private Duration timeout;

    @KivaKitIncludeProperty
    public int port()
    {
        return port;
    }

    @KivaKitPropertyConverter(IntegerConverter.class)
    @KivaKitIncludeProperty
    public void port(final int port)
    {
        this.port = port;
    }

    @KivaKitIncludeProperty
    public Duration timeout()
    {
        return timeout;
    }

    @KivaKitPropertyConverter(Duration.Converter.class)
    @KivaKitIncludeProperty
    public void timeout(final Duration timeout)
    {
        this.timeout = timeout;
    }

    @Override
    public String toString()
    {
        return "[Configuration timeout = " + timeout + ", port = " + port + "]";
    }
}
