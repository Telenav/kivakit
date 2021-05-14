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

package com.telenav.kivakit.network.email;

import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.kernel.data.validation.Validation;
import com.telenav.kivakit.kernel.data.validation.Validator;
import com.telenav.kivakit.kernel.data.validation.validators.BaseValidator;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.network.core.EmailAddress;
import com.telenav.kivakit.network.email.project.lexakai.diagrams.DiagramEmail;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.illegalState;

/**
 * Models an email with:
 *
 * <ul>
 *     <li>A 'from' email address</li>
 *     <li>One or more 'to' email addresses</li>
 *     <li>A subject</li>
 *     <li>A body</li>
 *     <li>Optional attachments</li>
 *     <li>Time sent</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramEmail.class)
@LexakaiJavadoc(complete = true)
public class Email implements Validatable
{
    @UmlAggregation(label = "to")
    Set<EmailAddress> to = new HashSet<>();

    @UmlAggregation(label = "from")
    EmailAddress from;

    String subject;

    @UmlAggregation
    EmailBody body;

    Time sentAt = Time.START_OF_UNIX_TIME;

    @UmlAggregation
    final List<EmailAttachment> attachments = new ArrayList<>();

    Time lastRetry;

    int tries;

    public Email addTo(final EmailAddress address)
    {
        to.add(address);
        return this;
    }

    public Email attach(final EmailAttachment attachment)
    {
        attachments.add(attachment);
        return this;
    }

    public Email body(final EmailBody body)
    {
        this.body = body;
        return this;
    }

    public Email from(final EmailAddress from)
    {
        this.from = from;
        return this;
    }

    public Email sentAt(final Time sentAt)
    {
        this.sentAt = sentAt;
        return this;
    }

    public Email subject(final String subject)
    {
        this.subject = subject;
        return this;
    }

    public Email to(final Set<EmailAddress> to)
    {
        this.to = to;
        return this;
    }

    @Override
    public String toString()
    {
        return "[Email from = " + from + ", to " + to + ", subject = " + subject + "]";
    }

    @Override
    public Validator validator(final Validation type)
    {
        return new BaseValidator()
        {
            @Override
            protected void onValidate()
            {
                problemIf(to == null, "Recipient (to) is missing");
                problemIf(from == null, "Sender (from) is missing");
                problemIf(isEmpty(subject), "Subject is missing or empty");
                problemIf(body == null, "Body is missing");
            }
        };
    }

    public void waitForRetry(final Duration durationBetweenRetries)
    {
        if (lastRetry != null)
        {
            lastRetry.plus(durationBetweenRetries).fromNow().sleep();
        }
        lastRetry = Time.now();
        tries++;
    }

    void composeMessage(final MimeMessage message) throws Exception
    {

        message.setSubject(subject);
        message.setFrom(resolve(from));
        final Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(body.bodyPart);
        for (final var attachment : attachments)
        {
            multipart.addBodyPart(attachment.bodyPart());
        }
        message.setContent(multipart);
        for (final var address : to)
        {
            message.addRecipient(Message.RecipientType.TO, resolve(address));
        }
    }

    Count tries()
    {
        return Count.count(tries);
    }

    private InternetAddress resolve(final EmailAddress email)
    {
        try
        {
            final var addresses = InternetAddress.parse(email.email(), true);
            if (addresses.length == 1)
            {
                return addresses[0];
            }
            else
            {
                return illegalState("Cannot parse email address: $", email);
            }
        }
        catch (final AddressException e)
        {
            return illegalState(e, "Cannot parse email address: $", email);
        }
    }
}
