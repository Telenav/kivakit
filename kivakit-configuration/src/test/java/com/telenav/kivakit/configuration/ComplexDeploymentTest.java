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

import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

public class ComplexDeploymentTest extends UnitTest
{
    private static final InstanceIdentifier SERVER1 = new InstanceIdentifier("SERVER1");

    private static final InstanceIdentifier SERVER2 = new InstanceIdentifier("SERVER2");

    public static class Development extends Deployment
    {
        public Development()
        {
            super("development", "test development deployment");
            add(new Server1(), SERVER1);
            add(new Server2(), SERVER2);
        }
    }

    public static class Production extends Deployment
    {
        public Production()
        {
            super("production", "test production deployment");
            add(new Server3(), SERVER1);
            add(new Server4(), SERVER2);
        }
    }

    public static final class Server1 extends ServerConfiguration
    {
        public Server1()
        {
            timeout(Duration.ONE_MINUTE);
            port(9001);
        }
    }

    public static final class Server2 extends ServerConfiguration
    {
        public Server2()
        {
            timeout(Duration.ONE_MINUTE);
            port(9002);
        }
    }

    public static final class Server3 extends ServerConfiguration
    {
        public Server3()
        {
            timeout(Duration.ONE_MINUTE);
            port(9003);
        }
    }

    public static final class Server4 extends ServerConfiguration
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

        final var global = Deployment.global();

        final var server1 = global.require(ServerConfiguration.class, SERVER1);
        ensureEqual(9001, server1.port());
        ensureEqual(Duration.ONE_MINUTE, server1.timeout());

        final var server2 = global.require(ServerConfiguration.class, SERVER2);
        ensureEqual(9002, server2.port());
        ensureEqual(Duration.ONE_MINUTE, server2.timeout());
    }
}
