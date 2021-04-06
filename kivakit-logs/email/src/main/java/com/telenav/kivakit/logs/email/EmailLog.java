////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.logs.email;

import com.telenav.kivakit.core.configuration.lookup.Lookup;
import com.telenav.kivakit.core.kernel.logging.LogEntry;
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
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;
import static com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter.Format.WITH_EXCEPTION;

@UmlClassDiagram(diagram = DiagramLogsEmail.class)
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
            configuration.host(new Host(host));
            configuration.username(new UserName(username));
            configuration.password(new PlainTextPassword(password));
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
