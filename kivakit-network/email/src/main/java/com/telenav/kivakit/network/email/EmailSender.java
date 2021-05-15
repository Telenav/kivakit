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

import com.telenav.kivakit.kernel.interfaces.io.Closeable;
import com.telenav.kivakit.kernel.interfaces.io.Flushable;
import com.telenav.kivakit.kernel.interfaces.lifecycle.Startable;
import com.telenav.kivakit.kernel.interfaces.lifecycle.Stoppable;
import com.telenav.kivakit.kernel.language.threading.RepeatingKivaKitThread;
import com.telenav.kivakit.kernel.language.threading.latches.CompletionLatch;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Frequency;
import com.telenav.kivakit.kernel.language.time.Rate;
import com.telenav.kivakit.kernel.language.time.RateCalculator;
import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.network.email.project.lexakai.diagrams.DiagramEmail;
import com.telenav.kivakit.network.email.senders.SmtpEmailSender;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * An email sender. Emails can be added with {@link #enqueue(Email)} and they will be sent as soon as possible. Emails
 * are not persisted, so if the process terminates, enqueued emails will be lost. The {@link #close()} method will shut
 * down the queue so that no further emails can be enqueued and then attempt to send any remaining emails before
 * stopping. For testing purposes {@link #sendingOn(boolean)} can be called with false and emails will be processed but
 * not actually sent to their destination.
 * <p>
 * SMTP is currently the only protocol supported. In the future, IMAP may be added when the need arises.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see SmtpEmailSender
 */
@UmlClassDiagram(diagram = DiagramEmail.class)
@UmlRelation(label = "sends", referent = Email.class)
@UmlRelation(label = "configured by", referent = EmailSender.Configuration.class)
@LexakaiJavadoc(complete = true)
public abstract class EmailSender extends BaseRepeater implements Startable, Stoppable, Flushable, Closeable
{
    /**
     * Base configuration for all {@link EmailSender}s.
     *
     * @author jonathanl (shibo)
     */
    @UmlClassDiagram(diagram = DiagramEmail.class)
    @LexakaiJavadoc(complete = true)
    public static class Configuration
    {
        private Rate maximumSendRate;

        public Rate maximumSendRate()
        {
            return maximumSendRate;
        }

        public void maximumSendRate(final Rate maximumSendRate)
        {
            this.maximumSendRate = maximumSendRate;
        }
    }

    private volatile boolean closed;

    private final CompletionLatch queueEmpty = new CompletionLatch();

    @UmlAggregation
    private final EmailQueue queue = new EmailQueue();

    private Maximum maximumRetries = Maximum.maximum(16);

    private Duration retryPeriod = Duration.seconds(30);

    private boolean sendingOn = true;

    private volatile boolean running;

    private final RateCalculator rate = new RateCalculator(Duration.ONE_MINUTE);

    private final Configuration configuration;

    private final RepeatingKivaKitThread thread = new RepeatingKivaKitThread(this, Classes.simpleName(EmailSender.class), Frequency.CONTINUOUSLY)
    {
        @Override
        protected void onRun()
        {
            final var email = queue().take();
            if (email != null)
            {
                email.waitForRetry(retryPeriod);
                if (!send(email))
                {
                    if (email.tries().isLessThan(maximumRetries))
                    {
                        if (!queue().offer(email, Duration.seconds(5)))
                        {
                            warning("Unable to re-queue email");
                        }
                    }
                }
                else
                {
                    queue().sent(email);
                }
            }
            if (queue().isEmpty())
            {
                queueEmpty.completed();
            }
        }

        {
            addListener(this);
        }
    };

    protected EmailSender(final Configuration configuration)
    {
        this.configuration = configuration;
    }

    @SuppressWarnings("SameReturnValue")
    public Authenticator authenticator()
    {
        return null;
    }

    /**
     * Closes the queue to new entries
     */
    @Override
    public void close()
    {
        closed = true;
    }

    public void enqueue(final Email email)
    {
        if (!closed)
        {
            if (!queue().offer(email, Duration.seconds(5)))
            {
                if (!queue().isClosed())
                {
                    warning("Unable to add email to queue");
                }
            }
        }
        else
        {
            warning("Email added after EmailSender shutdown initiated");
        }
    }

    @Override
    public void flush(final Duration maximumWaitTime)
    {
        trace("Flushing queue within ${debug}", maximumWaitTime);
        queueEmpty.waitForCompletion();
        trace("Flushed");
    }

    public boolean isClosed()
    {
        return closed;
    }

    @Override
    public boolean isRunning()
    {
        return running;
    }

    public EmailSender maximumRetries(final Maximum maximumRetries)
    {
        this.maximumRetries = maximumRetries;
        return this;
    }

    public EmailSender retryPeriod(final Duration durationBetweenRetries)
    {
        retryPeriod = durationBetweenRetries;
        return this;
    }

    public EmailSender sendingOn(final boolean on)
    {
        sendingOn = on;
        return this;
    }

    @Override
    @SuppressWarnings("UnusedReturnValue")
    public boolean start()
    {
        if (!isRunning())
        {
            running = true;
            return thread.start();
        }
        return true;
    }

    @Override
    public void stop(final Duration maximumWaitTime)
    {
        // Don't accept any more entries
        close();

        // Close the queue
        queue().close();

        // Flush any remaining e-mails
        flush(maximumWaitTime);

        // Stop this thread
        thread.stop(maximumWaitTime);
    }

    protected abstract Properties getMailSessionProperties();

    private EmailQueue queue()
    {
        return queue;
    }

    private boolean send(final Email email)
    {
        rate.increment();
        if (rate.rate().isFasterThan(configuration.maximumSendRate()))
        {
            warning("Emails are being sent at a rate greater than ${debug}. Discarding email ${debug}",
                    configuration.maximumSendRate(), email);
            return true;
        }
        else
        {
            try
            {
                trace("Sending email $", email);
                if (sendingOn)
                {
                    final var session = Session.getDefaultInstance(getMailSessionProperties(), authenticator());
                    session.setDebug(debug().isDebugOn());
                    final var transport = session.getTransport();
                    final var message = new MimeMessage(session);
                    email.composeMessage(message);
                    transport.connect();
                    transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
                    transport.close();
                }
                return true;
            }
            catch (final Exception e)
            {
                problem(e, "Cannot send email");
                return false;
            }
        }
    }
}