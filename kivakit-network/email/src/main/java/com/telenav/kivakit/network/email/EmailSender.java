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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.thread.RepeatingThread;
import com.telenav.kivakit.core.thread.latches.CompletionLatch;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Rate;
import com.telenav.kivakit.core.time.RateCalculator;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.io.Closeable;
import com.telenav.kivakit.interfaces.io.Flushable;
import com.telenav.kivakit.interfaces.lifecycle.Startable;
import com.telenav.kivakit.interfaces.lifecycle.Stoppable;
import com.telenav.kivakit.network.email.internal.lexakai.DiagramEmail;
import com.telenav.kivakit.network.email.senders.SmtpEmailSender;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.Classes.simpleName;
import static com.telenav.kivakit.core.time.Duration.FOREVER;
import static com.telenav.kivakit.core.time.Duration.ONE_MINUTE;
import static com.telenav.kivakit.core.time.Duration.seconds;
import static com.telenav.kivakit.core.time.Frequency.CONTINUOUSLY;
import static com.telenav.kivakit.core.value.count.Maximum.maximum;
import static javax.mail.Message.RecipientType.TO;

/**
 * An email sender. Emails can be added with {@link #enqueue(Email)} and they will be sent as soon as possible. Emails
 * are not persisted, so if the process terminates, enqueued emails will be lost. The {@link #close()} method will shut
 * down the queue so that no further emails can be enqueued and then attempt to send any remaining emails before
 * stopping. For testing purposes {@link #enableSending(boolean)} can be called with false and emails will be processed
 * but not actually sent to their destination.
 * <p>
 * SMTP is currently the only protocol supported. In the future, IMAP may be added when the need arises.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see SmtpEmailSender
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramEmail.class)
@UmlRelation(label = "sends", referent = Email.class)
@UmlRelation(label = "configured by", referent = EmailSender.Configuration.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public abstract class EmailSender extends BaseRepeater implements
        Startable,
        Stoppable<Duration>,
        Flushable<Duration>,
        Closeable
{
    /**
     * Base configuration for all {@link EmailSender}s.
     *
     * @author jonathanl (shibo)
     */
    @UmlClassDiagram(diagram = DiagramEmail.class)
    @TypeQuality(stability = STABLE_EXTENSIBLE,
                 testing = UNTESTED,
                 documentation = DOCUMENTED)
    public static class Configuration
    {
        private Rate maximumSendRate;

        public Rate maximumSendRate()
        {
            return maximumSendRate;
        }

        public void maximumSendRate(Rate maximumSendRate)
        {
            this.maximumSendRate = maximumSendRate;
        }
    }

    private volatile boolean closed;

    private final Configuration configuration;

    private Maximum maximumRetries = maximum(16);

    @UmlAggregation
    private final EmailQueue queue = new EmailQueue();

    private final CompletionLatch queueEmpty = new CompletionLatch();

    private final RateCalculator rate = new RateCalculator(ONE_MINUTE);

    private Duration retryPeriod = seconds(30);

    private volatile boolean running;

    private boolean enabled = true;

    private final RepeatingThread thread = new RepeatingThread(this, simpleName(EmailSender.class), CONTINUOUSLY)
    {
        @Override
        protected void onRun()
        {
            var email = queue().take();
            if (email != null)
            {
                email.waitForRetry(retryPeriod);
                if (!send(email))
                {
                    if (email.tries().isLessThan(maximumRetries))
                    {
                        if (!queue().enqueue(email, seconds(5)))
                        {
                            warning("Unable to re-queue email");
                        }
                    }
                }
                else
                {
                    queue().markSent(email);
                }
            }
            if (queue().isEmpty())
            {
                queueEmpty.threadCompleted();
            }
        }

        {
            addListener(this);
        }
    };

    protected EmailSender(Configuration configuration)
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

    public EmailSender enableSending(boolean on)
    {
        enabled = on;
        return this;
    }

    public void enqueue(Email email)
    {
        if (!closed)
        {
            if (!queue().enqueue(email, seconds(5)))
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
    public void flush(Duration maximumWaitTime)
    {
        trace("Flushing queue within ${debug}", maximumWaitTime);
        queueEmpty.waitForAllThreadsToComplete();
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

    @Override
    public Duration maximumFlushTime()
    {
        return FOREVER;
    }

    public EmailSender maximumRetries(Maximum maximumRetries)
    {
        this.maximumRetries = maximumRetries;
        return this;
    }

    @Override
    public Duration maximumStopTime()
    {
        return FOREVER;
    }

    public EmailSender retryPeriod(Duration durationBetweenRetries)
    {
        retryPeriod = durationBetweenRetries;
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
    public void stop(Duration maximumWaitTime)
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

    private boolean send(Email email)
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
                if (enabled)
                {
                    var session = Session.getDefaultInstance(getMailSessionProperties(), authenticator());
                    session.setDebug(debug().isDebugOn());
                    var transport = session.getTransport();
                    var message = new MimeMessage(session);
                    email.composeMessage(message);
                    transport.connect();
                    transport.sendMessage(message, message.getRecipients(TO));
                    transport.close();
                }
                return true;
            }
            catch (Exception e)
            {
                problem(e, "Cannot send email");
                return false;
            }
        }
    }
}
