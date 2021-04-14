////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.logs.email;

import com.telenav.kivakit.core.configuration.lookup.Lookup;
import com.telenav.kivakit.core.kernel.logging.Log;
import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.logging.loggers.LogServiceLogger;
import com.telenav.kivakit.core.kernel.logging.logs.text.BaseTextLog;
import com.telenav.kivakit.core.network.core.EmailAddress;
import com.telenav.kivakit.core.network.core.Host;
import com.telenav.kivakit.core.network.email.Email;
import com.telenav.kivakit.core.network.email.EmailBody;
import com.telenav.kivakit.core.network.email.EmailSender;
import com.telenav.kivakit.core.network.email.senders.SmtpEmailSender;
import com.telenav.kivakit.core.security.authentication.UserName;
import com.telenav.kivakit.core.security.authentication.passwords.PlainTextPassword;
import com.telenav.kivakit.logs.email.project.lexakai.diagrams.DiagramLogsEmail;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;
import static com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter.Format.WITH_EXCEPTION;

/**
 * A {@link Log} service provider that sends emails. Configuration occurs via the command line. See {@link
 * LogServiceLogger} for details. Further details are available in the markdown help. The options available for
 * configuration with this logger are:
 *
 * <ul>
 *     <li><i>to</i> - The set of email addresses to send to</li>
 *     <li><i>from</i> - The email address to send from</li>
 *     <li><i>subject</i> - The subject line</li>
 *     <li><i>host</i> - The SMTP host</li>
 *     <li><i>username</i> - The SMTP username</li>
 *     <li><i>password</i> - The SMTP password</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogsEmail.class)
@LexakaiJavadoc(complete = true)
public class EmailLog extends BaseTextLog
{
    @UmlAggregation
    private EmailSender sender;

    @UmlAggregation(label = "to")
    private final Set<EmailAddress> to = new HashSet<>();

    @UmlAggregation(label = "from")
    private EmailAddress from;

    private String subject;

    @Override
    public void configure(final Map<String, String> properties)
    {
        // Subject
        subject = properties.get("subject");
        if (subject == null)
        {
            fail("EmailLog missing subject property");
        }

        // To email addresses
        final var to = properties.get("to");
        if (to != null)
        {
            for (final var at : to.split(","))
            {
                this.to.add(EmailAddress.parse(at));
            }
        }
        else
        {
            fail("EmailLog missing to property");
        }

        // From email address
        final var from = properties.get("from");
        if (from != null)
        {
            this.from = EmailAddress.parse(from);
        }
        else
        {
            fail("EmailLog missing to property");
        }

        // Sender
        final var host = properties.get("host");
        final var username = properties.get("username");
        final var password = properties.get("password");
        if (host != null && username != null && password != null)
        {
            final var configuration = new SmtpEmailSender.Configuration();
            configuration.host(Host.parse(host));
            configuration.username(UserName.parse(username));
            configuration.password(PlainTextPassword.parse(password));
            sender = new SmtpEmailSender(configuration);
        }
    }

    @Override
    public String name()
    {
        return "Email";
    }

    @Override
    protected void onLog(final LogEntry entry)
    {
        final var email = new Email();
        email.to(to);
        email.from(from);
        email.body(new EmailBody(format(entry, WITH_EXCEPTION)));
        email.subject(subject);
        if (sender == null)
        {
            sender = Lookup.global().locate(EmailSender.class);
        }
        sender.enqueue(email);
    }
}
