////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.email.senders;

import com.telenav.kivakit.core.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.core.network.core.Host;
import com.telenav.kivakit.core.network.email.EmailSender;
import com.telenav.kivakit.core.network.email.project.lexakai.diagrams.DiagramEmail;
import com.telenav.kivakit.core.security.authentication.Password;
import com.telenav.kivakit.core.security.authentication.UserName;
import com.telenav.kivakit.core.security.authentication.converters.PlainTextPasswordConverter;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Properties;

@UmlClassDiagram(diagram = DiagramEmail.class)
public class SmtpEmailSender extends EmailSender
{
    public static class Configuration extends EmailSender.Configuration
    {
        private Host host;

        private UserName username;

        private Password password;

        public Host host()
        {
            return host;
        }

        @KivaKitPropertyConverter(Host.Converter.class)
        public Configuration host(final Host host)
        {
            this.host = host;
            return this;
        }

        public Password password()
        {
            return password;
        }

        @KivaKitPropertyConverter(PlainTextPasswordConverter.class)
        public Configuration password(final Password password)
        {
            this.password = password;
            return this;
        }

        public UserName username()
        {
            return username;
        }

        @KivaKitPropertyConverter(UserName.Converter.class)
        public Configuration username(final UserName username)
        {
            this.username = username;
            return this;
        }
    }

    private final Configuration configuration;

    public SmtpEmailSender(final Configuration configuration)
    {
        super(configuration);
        this.configuration = configuration;
    }

    @Override
    protected Properties getMailSessionProperties()
    {
        final var properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.host", configuration.host().name());
        final var username = configuration.username();
        if (username != null)
        {
            properties.setProperty("mail.user", username.toString());
        }
        final var password = configuration.password();
        if (password != null)
        {
            properties.setProperty("mail.password", password.toString());
        }
        return properties;
    }
}
