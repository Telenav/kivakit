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

package com.telenav.kivakit.configuration;

import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

public class ConfigurationSetTest extends UnitTest
{
    private static final InstanceIdentifier SERVER1 = new InstanceIdentifier("SERVER1");

    private static final InstanceIdentifier SERVER2 = new InstanceIdentifier("SERVER2");

    @Test
    public void testMultipleConfigurations()
    {
        final var global = ConfigurationSet.global();
        global.addListener(this);

        // Configure
        {
            // Script or startup code creates configuration
            final var server1 = new ServerConfiguration();
            server1.timeout(Duration.ONE_MINUTE);
            server1.port(8080);

            // and adds it to the global configuration set with the given enum key
            global.add(server1, SERVER1);

            // Script can register a second configuration of the same class
            final var server2 = new ServerConfiguration();
            server2.timeout(Duration.ONE_MINUTE);
            server2.port(80);

            // under a different key
            global.add(server2, SERVER2);

            // Dump the global configuration set to the console
            trace("testMultipleConfigurations: " + global);
        }

        // Get configuration
        {
            // Client code can then retrieve both configurations
            final var server1 = global.require(ServerConfiguration.class, SERVER1);
            ensureEqual(Duration.ONE_MINUTE, server1.timeout());
            ensureEqual(8080, server1.port());

            final var server2 = global.require(ServerConfiguration.class, SERVER2);
            ensureEqual(Duration.ONE_MINUTE, server2.timeout());
            ensureEqual(80, server2.port());
        }
    }

    @Test
    public void testPropertiesFileConfiguration()
    {
        final var global = ConfigurationSet.global();
        global.clear();
        global.addListener(this);

        // Configure
        {
            // Add all properties files in this package to the global set
            global.addPackage(getClass());

            // Dump the set to the console
            trace("testPropertiesFileConfiguration: " + global);
        }

        // Get configuration
        {
            // Client code, possibly in a library class, later retrieves the configuration
            final var configuration = global.require(ServerConfiguration.class);
            ensureEqual(Duration.ONE_MINUTE, configuration.timeout());
            ensureEqual(7000, configuration.port());
        }
    }

    @Test
    public void testSingleConfiguration()
    {
        final var global = ConfigurationSet.global();
        global.addListener(this);

        // Configure
        {
            // Script or startup code creates configuration
            final var configuration = new ServerConfiguration();
            configuration.timeout(Duration.ONE_MINUTE);
            configuration.port(9000);

            // and adds it to global configuration set
            global.add(configuration);

            // Dump the set to the console
            trace("testSingleConfiguration: " + global);
        }

        // Get configuration
        {
            // Client code, possibly in a library class, later retrieves the configuration
            final var configuration = global.require(ServerConfiguration.class);
            ensureEqual(Duration.ONE_MINUTE, configuration.timeout());
            ensureEqual(9000, configuration.port());
        }
    }
}
