////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.configuration;

import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.test.UnitTest;
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
