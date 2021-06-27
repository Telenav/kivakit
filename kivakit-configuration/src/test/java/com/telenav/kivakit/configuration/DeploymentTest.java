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

import com.telenav.kivakit.configuration.deployment.Deployment;
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
            final var configuration = new ServerConfiguration();
            configuration.timeout(Duration.ONE_MINUTE);
            configuration.port(9090);
            add(configuration);
        }
    }

    public static class Development extends Deployment
    {
        public Development()
        {
            super("development", "test development deployment");
            final var configuration = new ServerConfiguration();
            configuration.timeout(Duration.ONE_MINUTE);
            configuration.port(8080);
            add(configuration);
        }
    }

    public static class Production extends Deployment
    {
        public Production()
        {
            super("production", "test production deployment");
            final var configuration = new ServerConfiguration();
            configuration.timeout(Duration.ONE_MINUTE);
            configuration.port(80);
            add(configuration);
        }
    }

    @Test
    public void testChina()
    {
        new China().install();

        final var global = Deployment.global();
        final var configuration = global.require(ServerConfiguration.class);
        ensureEqual(9090, configuration.port());
        ensureEqual(Duration.ONE_MINUTE, configuration.timeout());
    }

    @Test
    public void testDevelopment()
    {
        new Development().install();

        final var global = Deployment.global();
        final var configuration = global.require(ServerConfiguration.class);
        ensureEqual(8080, configuration.port());
        ensureEqual(Duration.ONE_MINUTE, configuration.timeout());
    }

    @Test
    public void testProduction()
    {
        new Production().install();

        final var global = Deployment.global();
        final var configuration = global.require(ServerConfiguration.class);
        ensureEqual(80, configuration.port());
        ensureEqual(Duration.ONE_MINUTE, configuration.timeout());
    }
}
