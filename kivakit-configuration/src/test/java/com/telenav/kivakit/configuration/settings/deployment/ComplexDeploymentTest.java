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

import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.settings.ServerSettings;
import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

public class ComplexDeploymentTest extends UnitTest
{
    private static final InstanceIdentifier SERVER1 = InstanceIdentifier.of("SERVER1");

    private static final InstanceIdentifier SERVER2 = InstanceIdentifier.of("SERVER2");

    public static class Development extends Deployment
    {
        public Development()
        {
            super("development", "test development deployment");
            registerSettings(new Server1(), SERVER1);
            registerSettings(new Server2(), SERVER2);
        }
    }

    public static class Production extends Deployment
    {
        public Production()
        {
            super("production", "test production deployment");
            registerSettings(new Server3(), SERVER1);
            registerSettings(new Server4(), SERVER2);
        }
    }

    public static final class Server1 extends ServerSettings
    {
        public Server1()
        {
            timeout(Duration.ONE_MINUTE);
            port(9001);
        }
    }

    public static final class Server2 extends ServerSettings
    {
        public Server2()
        {
            timeout(Duration.ONE_MINUTE);
            port(9002);
        }
    }

    public static final class Server3 extends ServerSettings
    {
        public Server3()
        {
            timeout(Duration.ONE_MINUTE);
            port(9003);
        }
    }

    public static final class Server4 extends ServerSettings
    {
        public Server4()
        {
            timeout(Duration.ONE_MINUTE);
            port(9004);
        }
    }

    @Test
    public void testDevelopment()
    {
        new Development().install();

        final var server1 = Settings.of(this).requireSettings(ServerSettings.class, SERVER1);
        ensureEqual(9001, server1.port());
        ensureEqual(Duration.ONE_MINUTE, server1.timeout());

        final var server2 = Settings.of(this).requireSettings(ServerSettings.class, SERVER2);
        ensureEqual(9002, server2.port());
        ensureEqual(Duration.ONE_MINUTE, server2.timeout());
    }
}
