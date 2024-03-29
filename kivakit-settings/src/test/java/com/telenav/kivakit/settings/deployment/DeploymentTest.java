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

package com.telenav.kivakit.settings.deployment;

import com.telenav.kivakit.settings.Deployment;
import com.telenav.kivakit.settings.ServerSettings;
import com.telenav.kivakit.settings.SettingsTrait;
import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.time.Duration.ONE_MINUTE;
import static com.telenav.kivakit.settings.SettingsRegistry.settingsFor;

public class DeploymentTest extends UnitTest implements SettingsTrait
{
    public static class China extends Deployment implements SettingsTrait
    {
        public China()
        {
            super(throwingListener(),"china", "test china deployment");

            var settings = new ServerSettings();
            settings.timeout(ONE_MINUTE);
            settings.port(9090);

            registerSettings(settings);
        }
    }

    public static class Development extends Deployment implements SettingsTrait
    {
        public Development()
        {
            super(throwingListener(),"development", "test development deployment");

            var settings = new ServerSettings();
            settings.timeout(ONE_MINUTE);
            settings.port(8080);

            registerSettings(settings);
        }
    }

    public static class Production extends Deployment implements SettingsTrait
    {
        public Production()
        {
            super(throwingListener(),"production", "test production deployment");

            var settings = new ServerSettings();
            settings.timeout(ONE_MINUTE);
            settings.port(80);

            registerSettings(settings);
        }
    }

    @Test
    public void testChina()
    {
        registerSettingsIn(new China());

        var settings = settingsFor(this).requireSettings(ServerSettings.class);
        ensureEqual(settings.port(), 9090);
        ensureEqual(settings.timeout(), ONE_MINUTE);
    }

    @Test
    public void testDevelopment()
    {
        registerSettingsIn(new Development());

        var settings = settingsFor(this).requireSettings(ServerSettings.class);
        ensureEqual(8080, settings.port());
        ensureEqual(ONE_MINUTE, settings.timeout());
    }

    @Test
    public void testProduction()
    {
        registerSettingsIn(new Production());

        var settings = settingsFor(this).requireSettings(ServerSettings.class);
        ensureEqual(80, settings.port());
        ensureEqual(ONE_MINUTE, settings.timeout());
    }
}
