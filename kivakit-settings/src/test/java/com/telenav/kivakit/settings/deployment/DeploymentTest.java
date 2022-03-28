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

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.test.UnitTest;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.settings.Deployment;
import com.telenav.kivakit.settings.ServerSettings;
import com.telenav.kivakit.settings.Settings;
import com.telenav.kivakit.settings.SettingsTrait;
import org.junit.Test;

public class DeploymentTest extends UnitTest implements SettingsTrait
{
    public static class China extends Deployment implements SettingsTrait
    {
        public China()
        {
            super(Listener.throwing(),"china", "test china deployment");

            var settings = new ServerSettings();
            settings.timeout(Duration.ONE_MINUTE);
            settings.port(9090);

            registerSettingsObject(settings);
        }
    }

    public static class Development extends Deployment implements SettingsTrait
    {
        public Development()
        {
            super(Listener.throwing(),"development", "test development deployment");

            var settings = new ServerSettings();
            settings.timeout(Duration.ONE_MINUTE);
            settings.port(8080);

            registerSettingsObject(settings);
        }
    }

    public static class Production extends Deployment implements SettingsTrait
    {
        public Production()
        {
            super(Listener.throwing(),"production", "test production deployment");

            var settings = new ServerSettings();
            settings.timeout(Duration.ONE_MINUTE);
            settings.port(80);

            registerSettingsObject(settings);
        }
    }

    @Test
    public void testChina()
    {
        registerSettingsIn(new China());

        var settings = Settings.of(this).requireSettings(ServerSettings.class);
        ensureEqual(settings.port(), 9090);
        ensureEqual(settings.timeout(), Duration.ONE_MINUTE);
    }

    @Test
    public void testDevelopment()
    {
        registerSettingsIn(new Development());

        var settings = Settings.of(this).requireSettings(ServerSettings.class);
        ensureEqual(8080, settings.port());
        ensureEqual(Duration.ONE_MINUTE, settings.timeout());
    }

    @Test
    public void testProduction()
    {
        registerSettingsIn(new Production());

        var settings = Settings.of(this).requireSettings(ServerSettings.class);
        ensureEqual(80, settings.port());
        ensureEqual(Duration.ONE_MINUTE, settings.timeout());
    }
}
