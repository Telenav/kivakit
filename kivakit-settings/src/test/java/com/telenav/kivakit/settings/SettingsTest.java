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
import com.telenav.kivakit.test.UnitTest;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.resource.packages.PackageTrait;
import com.telenav.kivakit.serialization.gson.GsonSerializationProject;
import com.telenav.kivakit.serialization.gson.factory.CoreGsonFactory;
import com.telenav.kivakit.serialization.properties.PropertiesSerializationProject;
import com.telenav.kivakit.settings.stores.ResourceFolderSettingsStore;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

public class SettingsTest extends UnitTest implements PackageTrait
{
    private static final InstanceIdentifier SERVER1 = InstanceIdentifier.of("SERVER1");

    private static final InstanceIdentifier SERVER2 = InstanceIdentifier.of("SERVER2");

    @Before
    public void setup()
    {
        register(new CoreGsonFactory(this));

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
            server.timeout(Duration.ONE_MINUTE);
            server.port(9000);

            // and adds it to global configuration set
            Settings.of(this).registerSettingsObject(server);
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
    public void testJson()
    {
        var settings = globalSettings();

        // Configure
        {
            // Add all properties files in this package to the global set
            settings.registerSettingsIn(new ResourceFolderSettingsStore(this, thisPackage()));
        }

        // Get configuration
        {
            // Client code can then retrieve both settings
            var server1 = settings.requireSettings(ClientSettings.class, "banana");
            ensureEqual(Duration.seconds(6), server1.timeout());
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
            server1.timeout(Duration.ONE_MINUTE);
            server1.port(8080);

            // and adds it to the global settings set with the given enum key
            settings.registerSettingsObject(server1, SERVER1);

            // Script can register a second settings of the same class
            var server2 = new ServerSettings();
            server2.timeout(Duration.ONE_MINUTE);
            server2.port(80);

            // under a different key
            settings.registerSettingsObject(server2, SERVER2);
        }

        // Get settings
        {
            // Client code can then retrieve both settings
            var server1 = Settings.of(this).requireSettings(ServerSettings.class, SERVER1);
            ensureEqual(Duration.ONE_MINUTE, server1.timeout());
            ensureEqual(8080, server1.port());

            var server2 = Settings.of(this).requireSettings(ServerSettings.class, SERVER2);
            ensureEqual(Duration.ONE_MINUTE, server2.timeout());
            ensureEqual(80, server2.port());
        }
    }

    @Test
    public void testProperties()
    {
        var settings = globalSettings();

        // Configure
        {
            // Add all properties files in this package to the global set
            settings.registerSettingsIn(new ResourceFolderSettingsStore(this, thisPackage()));
        }

        // Get settings
        {
            // Client code, possibly in a library class, later retrieves the settings
            var serverSettings = Settings.of(this).requireSettings(ServerSettings.class);
            ensureEqual(Duration.ONE_MINUTE, serverSettings.timeout());
            ensureEqual(7000, serverSettings.port());
        }
    }

    @NotNull
    private Settings globalSettings()
    {
        var global = Settings.of(this);
        global.unload();
        global.clearListeners();
        global.addListener(this);
        return global;
    }
}
