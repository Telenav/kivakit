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

package com.telenav.kivakit.configuration.settings.deployment;

import com.telenav.kivakit.configuration.settings.ServerSettings;
import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

public class DeploymentTest extends UnitTest
{
    public static class China extends Deployment
    {
        public China()
        {
            super("china", "test china deployment");

            final var settings = new ServerSettings();
            settings.timeout(Duration.ONE_MINUTE);
            settings.port(9090);

            register(settings);
        }
    }

    public static class Development extends Deployment
    {
        public Development()
        {
            super("development", "test development deployment");

            final var settings = new ServerSettings();
            settings.timeout(Duration.ONE_MINUTE);
            settings.port(8080);

            register(settings);
        }
    }

    public static class Production extends Deployment
    {
        public Production()
        {
            super("production", "test production deployment");

            final var settings = new ServerSettings();
            settings.timeout(Duration.ONE_MINUTE);
            settings.port(80);

            register(settings);
        }
    }

    @Test
    public void testChina()
    {
        new China().install();

        final var settings = Settings.of(this).require(ServerSettings.class);
        ensureEqual(settings.port(), 9090);
        ensureEqual(settings.timeout(), Duration.ONE_MINUTE);
    }

    @Test
    public void testDevelopment()
    {
        new Development().install();

        final var settings = Settings.of(this).require(ServerSettings.class);
        ensureEqual(8080, settings.port());
        ensureEqual(Duration.ONE_MINUTE, settings.timeout());
    }

    @Test
    public void testProduction()
    {
        new Production().install();

        final var settings = Settings.of(this).require(ServerSettings.class);
        ensureEqual(80, settings.port());
        ensureEqual(Duration.ONE_MINUTE, settings.timeout());
    }
}
