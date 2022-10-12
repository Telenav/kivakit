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

import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.settings.Deployment;
import com.telenav.kivakit.settings.ServerSettings;
import com.telenav.kivakit.settings.SettingsTrait;
import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.registry.InstanceIdentifier.instanceIdentifier;
import static com.telenav.kivakit.core.time.Duration.ONE_MINUTE;
import static com.telenav.kivakit.settings.SettingsRegistry.settingsFor;

public class ComplexDeploymentTest extends UnitTest
{
    private static final InstanceIdentifier SERVER1 = instanceIdentifier(WhichServer.SERVER1);

    private static final InstanceIdentifier SERVER2 = instanceIdentifier(WhichServer.SERVER2);

    enum WhichServer
    {
        SERVER1,
        SERVER2
    }

    public static class Development extends Deployment implements SettingsTrait
    {
        public Development()
        {
            super(throwingListener(), "development", "test development deployment");
            registerSettings(new Server1(), SERVER1);
            registerSettings(new Server2(), SERVER2);
        }
    }

    @SuppressWarnings("unused")
    public static class Production extends Deployment implements SettingsTrait
    {
        public Production()
        {
            super(throwingListener(), "production", "test production deployment");
            registerSettings(new Server3(), SERVER1);
            registerSettings(new Server4(), SERVER2);
        }
    }

    public static final class Server1 extends ServerSettings
    {
        public Server1()
        {
            timeout(ONE_MINUTE);
            port(9001);
        }
    }

    public static final class Server2 extends ServerSettings
    {
        public Server2()
        {
            timeout(ONE_MINUTE);
            port(9002);
        }
    }

    public static final class Server3 extends ServerSettings
    {
        public Server3()
        {
            timeout(ONE_MINUTE);
            port(9003);
        }
    }

    public static final class Server4 extends ServerSettings
    {
        public Server4()
        {
            timeout(ONE_MINUTE);
            port(9004);
        }
    }

    @Test
    public void testDevelopment()
    {
        settingsFor(this).registerSettingsIn(new Development());

        var server1 = settingsFor(this).requireSettings(ServerSettings.class, SERVER1);
        ensureEqual(9001, server1.port());
        ensureEqual(ONE_MINUTE, server1.timeout());

        var server2 = settingsFor(this).requireSettings(ServerSettings.class, SERVER2);
        ensureEqual(9002, server2.port());
        ensureEqual(ONE_MINUTE, server2.timeout());
    }
}
