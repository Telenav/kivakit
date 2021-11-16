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

import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.settings.stores.resource.PackageSettingsStore;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.test.UnitTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class SettingsTest extends UnitTest
{
    private static final InstanceIdentifier SERVER1 = InstanceIdentifier.instanceIdentifier("SERVER1");

    private static final InstanceIdentifier SERVER2 = InstanceIdentifier.instanceIdentifier("SERVER2");

    @Test
    public void test()
    {
        Settings settings = globalSettings();

        // Configure
        {
            // Script or startup code creates configuration
            var server = new ServerSettings();
            server.timeout(Duration.ONE_MINUTE);
            server.port(9000);

            // and adds it to global configuration set
            Settings.of(this).registerSettingsObject(server);

            // Dump the set to the console
            trace("test: " + settings);
        }

        // Get configuration
        {
            // Client code, possibly in a library class, later retrieves the configuration
            var server = Settings.of(this).requireSettings(ServerSettings.class);
            ensureEqual(Duration.ONE_MINUTE, server.timeout());
            ensureEqual(9000, server.port());
        }
    }

    @Test
    public void testMultipleInstances()
    {
        var settings = globalSettings();

        // Configure
        {
            // Script or startup code creates configuration
            var server1 = new ServerSettings();
            server1.timeout(Duration.ONE_MINUTE);
            server1.port(8080);

            // and adds it to the global configuration set with the given enum key
            settings.registerSettingsObject(server1, SERVER1);

            // Script can register a second configuration of the same class
            var server2 = new ServerSettings();
            server2.timeout(Duration.ONE_MINUTE);
            server2.port(80);

            // under a different key
            settings.registerSettingsObject(server2, SERVER2);

            // Dump the global configuration set to the console
            trace("testMultipleInstances: " + settings);
        }

        // Get configuration
        {
            // Client code can then retrieve both configurations
            var server1 = Settings.of(this).requireSettings(ServerSettings.class, SERVER1);
            ensureEqual(Duration.ONE_MINUTE, server1.timeout());
            ensureEqual(8080, server1.port());

            var server2 = Settings.of(this).requireSettings(ServerSettings.class, SERVER2);
            ensureEqual(Duration.ONE_MINUTE, server2.timeout());
            ensureEqual(80, server2.port());
        }
    }

    @Test
    public void testPropertiesFile()
    {
        var settings = globalSettings();

        // Configure
        {
            // Add all properties files in this package to the global set
            settings.registerSettingsIn(PackageSettingsStore.of(this, PackagePath.packagePath(getClass())));

            // Dump the set to the console
            trace("testPropertiesFile: " + settings);
        }

        // Get configuration
        {
            // Client code, possibly in a library class, later retrieves the configuration
            var configuration = Settings.of(this).requireSettings(ServerSettings.class);
            ensureEqual(Duration.ONE_MINUTE, configuration.timeout());
            ensureEqual(7000, configuration.port());
        }
    }

    @NotNull
    private Settings globalSettings()
    {
        var global = Settings.of(this);
        global.clear();
        global.clearListeners();
        global.addListener(this);
        return global;
    }
}
