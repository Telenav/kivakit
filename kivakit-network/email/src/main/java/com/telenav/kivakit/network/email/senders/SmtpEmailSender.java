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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.core.language.object.KivaKitConverted;
import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.network.core.authentication.Password;
import com.telenav.kivakit.network.core.authentication.UserName;
import com.telenav.kivakit.network.core.authentication.passwords.PlainTextPassword;
import com.telenav.kivakit.network.email.EmailSender;
import com.telenav.kivakit.network.email.internal.lexakai.DiagramEmail;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Properties;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Sends emails via SMTP using a host, username and password.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramEmail.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class SmtpEmailSender extends EmailSender
{
    /**
     * Specifies the host, username and password to send emails via SMTP
     *
     * @author jonathanl (shibo)
     */
    @ApiQuality(stability = API_STABLE_EXTENSIBLE,
                testing = TESTING_NONE,
                documentation = DOCUMENTATION_COMPLETE)
    public static class Configuration extends EmailSender.Configuration
    {
        private Host host;

        private UserName username;

        private Password password;

        public Host host()
        {
            return host;
        }

        @KivaKitConverted(Host.Converter.class)
        public Configuration host(Host host)
        {
            this.host = host;
            return this;
        }

        public Password password()
        {
            return password;
        }

        @KivaKitConverted(PlainTextPassword.Converter.class)
        public Configuration password(Password password)
        {
            this.password = password;
            return this;
        }

        public UserName username()
        {
            return username;
        }

        @KivaKitConverted(UserName.Converter.class)
        public Configuration username(UserName username)
        {
            this.username = username;
            return this;
        }
    }

    private final Configuration configuration;

    public SmtpEmailSender(Configuration configuration)
    {
        super(configuration);
        this.configuration = configuration;
    }

    @Override
    protected Properties getMailSessionProperties()
    {
        var properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.host", configuration.host().name());
        var username = configuration.username();
        if (username != null)
        {
            properties.setProperty("mail.user", username.toString());
        }
        var password = configuration.password();
        if (password != null)
        {
            properties.setProperty("mail.password", password.toString());
        }
        return properties;
    }
}
