package com.telenav.kivakit.settings;////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.resource.packages.PackageTrait;
import com.telenav.kivakit.serialization.gson.GsonSerializationProject;
import com.telenav.kivakit.serialization.gson.KivaKitCoreGsonFactory;
import com.telenav.kivakit.serialization.properties.PropertiesSerializationProject;
import com.telenav.kivakit.settings.stores.ResourceFolderSettingsStore;
import com.telenav.kivakit.testing.UnitTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import static com.telenav.kivakit.core.registry.InstanceIdentifier.instanceIdentifier;
import static com.telenav.kivakit.core.time.Duration.ONE_MINUTE;
import static com.telenav.kivakit.core.time.Duration.seconds;
import static com.telenav.kivakit.settings.SettingsRegistry.settingsFor;

public class SettingsRegistryTest extends UnitTest implements PackageTrait
{
    private static final InstanceIdentifier SERVER1 = instanceIdentifier(WhichServer.SERVER1);

    private static final InstanceIdentifier SERVER2 = instanceIdentifier(WhichServer.SERVER2);

    enum WhichServer
    {
        SERVER1,
        SERVER2
    }

    @Before
    public void setup()
    {
        register(new KivaKitCoreGsonFactory());

        initializeProject(PropertiesSerializationProject.class);
        initializeProject(GsonSerializationProject.class);
    }

    @Test
    public void test()
    {
        // Configure
        {
            // Script or startup code creates configuration
            var server = new ServerSettings();
            server.timeout(ONE_MINUTE);
            server.port(9000);

            // and adds it to global configuration set
            settingsFor(this).registerSettings(server);
        }

        // Get configuration
        {
            // Client code, possibly in a library class, later retrieves the configuration
            var server = settingsFor(this).requireSettings(ServerSettings.class);
            ensureEqual(ONE_MINUTE, server.timeout());
            ensureEqual(9000, server.port());
        }
    }

    @Test
    public void testJson()
    {
        var settings = globalSettings();

        // Configure
        {
            // Add all properties files in this package to the global set
            settings.registerSettingsIn(new ResourceFolderSettingsStore(this, packageForThis()));
        }

        // Get configuration
        {
            // Client code can then retrieve both settings
            var server1 = settings.requireSettings(ClientSettings.class, WhichServer.SERVER1);
            ensureEqual(seconds(6), server1.timeout());
            ensureEqual(9999, server1.port());
        }
    }

    @Test
    public void testMultipleInstances()
    {
        var settings = globalSettings();

        // Configure
        {
            // Script or startup code creates settings
            var server1 = new ServerSettings();
            server1.timeout(ONE_MINUTE);
            server1.port(8080);

            // and adds it to the global settings set with the given enum key
            settings.registerSettings(server1, SERVER1);

            // Script can register a second settings of the same class
            var server2 = new ServerSettings();
            server2.timeout(ONE_MINUTE);
            server2.port(80);

            // under a different key
            settings.registerSettings(server2, SERVER2);
        }

        // Get settings
        {
            // Client code can then retrieve both settings
            var server1 = settingsFor(this).requireSettings(ServerSettings.class, SERVER1);
            ensureEqual(ONE_MINUTE, server1.timeout());
            ensureEqual(8080, server1.port());

            var server2 = settingsFor(this).requireSettings(ServerSettings.class, SERVER2);
            ensureEqual(ONE_MINUTE, server2.timeout());
            ensureEqual(80, server2.port());
        }
    }

    @Test
    public void testProperties()
    {
        // Configure
        {
            // Add all properties files in this package to the global set
            globalSettings().registerSettingsIn(packageForThis());
        }

        // Get settings
        {
            // Client code, possibly in a library class, later retrieves the settings
            var serverSettings = settingsFor(this).requireSettings(ServerSettings.class);
            ensureEqual(ONE_MINUTE, serverSettings.timeout());
            ensureEqual(7000, serverSettings.port());
        }
    }

    @NotNull
    private SettingsRegistry globalSettings()
    {
        var global = settingsFor(this);
        global.clear();
        global.clearListeners();
        global.addListener(this);
        return global;
    }
}
