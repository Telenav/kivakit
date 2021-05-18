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

package com.telenav.kivakit.network.email.senders;

import com.telenav.kivakit.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.network.email.EmailSender;
import com.telenav.kivakit.network.email.project.lexakai.diagrams.DiagramEmail;
import com.telenav.kivakit.security.authentication.Password;
import com.telenav.kivakit.security.authentication.UserName;
import com.telenav.kivakit.security.authentication.passwords.PlainTextPassword;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Properties;

/**
 * Sends emails via SMTP using a host, username and password.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramEmail.class)
@LexakaiJavadoc(complete = true)
public class SmtpEmailSender extends EmailSender
{
    /**
     * Specifies the host, username and password to send emails via SMTP
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
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

        @KivaKitPropertyConverter(PlainTextPassword.Converter.class)
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
