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

package com.telenav.kivakit.network.email;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.network.core.EmailAddress;
import com.telenav.kivakit.network.email.internal.lexakai.DiagramEmail;
import com.telenav.kivakit.validation.BaseValidator;
import com.telenav.kivakit.validation.Validatable;
import com.telenav.kivakit.validation.ValidationType;
import com.telenav.kivakit.validation.Validator;
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

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.illegalState;

/**
 * Models an email.
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #addRecipient(EmailAddress)}</li>
 *     <li>{@link #body(EmailBody)}</li>
 *     <li>{@link #from(EmailAddress)}</li>
 *     <li>{@link #sentAt(Time)}</li>
 *     <li>{@link #subject(String)}</li>
 *     <li>{@link #to(Set)}</li>
 *     <li>{@link #waitForRetry(Duration)} </li>
 * </ul>
 *
 * <p><b>Validation</b></p>
 *
 * <ul>
 *     <li>{@link #validator(ValidationType)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramEmail.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
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

    public Email addRecipient(EmailAddress address)
    {
        to.add(address);
        return this;
    }

    public Email attach(EmailAttachment attachment)
    {
        attachments.add(attachment);
        return this;
    }

    public Email body(EmailBody body)
    {
        this.body = body;
        return this;
    }

    public Email from(EmailAddress from)
    {
        this.from = from;
        return this;
    }

    public Email sentAt(Time sentAt)
    {
        this.sentAt = sentAt;
        return this;
    }

    public Email subject(String subject)
    {
        this.subject = subject;
        return this;
    }

    public Email to(Set<EmailAddress> to)
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
    public Validator validator(ValidationType type)
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

    public void waitForRetry(Duration durationBetweenRetries)
    {
        if (lastRetry != null)
        {
            lastRetry.plus(durationBetweenRetries).untilNow().sleep();
        }
        lastRetry = Time.now();
        tries++;
    }

    void composeMessage(MimeMessage message) throws Exception
    {

        message.setSubject(subject);
        message.setFrom(resolve(from));
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(body.bodyPart);
        for (var attachment : attachments)
        {
            multipart.addBodyPart(attachment.bodyPart());
        }
        message.setContent(multipart);
        for (var address : to)
        {
            message.addRecipient(Message.RecipientType.TO, resolve(address));
        }
    }

    Count tries()
    {
        return Count.count(tries);
    }

    private InternetAddress resolve(EmailAddress email)
    {
        try
        {
            var addresses = InternetAddress.parse(email.email(), true);
            if (addresses.length == 1)
            {
                return addresses[0];
            }
            else
            {
                return illegalState("Cannot parse email address: $", email);
            }
        }
        catch (AddressException e)
        {
            return illegalState(e, "Cannot parse email address: $", email);
        }
    }
}
