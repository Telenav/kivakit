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

package com.telenav.kivakit.service.registry.client;

import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.core.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.network.core.NetworkLocation;
import com.telenav.kivakit.core.network.http.HttpNetworkLocation;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.Resourceful;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * {@link ServiceRegistryClient} settings, including an {@link #accessTimeout(Duration)} and the location of the
 * executable server jar, provided with {@link #serverJar(Resourceful)}. Note that all {@link Resource}s are {@link
 * Resourceful}, but {@link NetworkLocation} is also resourceful and can be used directly with {@link
 * #serverJar(Resourceful)} to launch a JAR from a network location. The default settings for {@link
 * ServiceRegistryClient} will launch the server directly from GitHub with a timeout of one minute. It will thereafter
 * be cached in the KivaKit cache folder, as provided by {@link KivaKit#cacheFolderPath()}. See the
 * ServiceRegistryClientSettings.properties file in this package.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class ServiceRegistryClientSettings
{
    private Duration accessTimeout;

    private Resourceful serverJar;

    @KivaKitPropertyConverter(Duration.Converter.class)
    public ServiceRegistryClientSettings accessTimeout(final Duration timeout)
    {
        accessTimeout = timeout;
        return this;
    }

    @KivaKitIncludeProperty
    public Duration accessTimeout()
    {
        return accessTimeout;
    }

    @KivaKitPropertyConverter(HttpNetworkLocation.Converter.class)
    public ServiceRegistryClientSettings serverJar(final Resourceful location)
    {
        serverJar = location;
        return this;
    }

    @KivaKitIncludeProperty
    public Resourceful serverJar()
    {
        return serverJar;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}
