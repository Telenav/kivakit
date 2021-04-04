////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.email;

import com.telenav.kivakit.core.network.email.project.lexakai.diagrams.DiagramEmail;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.kivakit.core.kernel.interfaces.io.Closeable;
import com.telenav.kivakit.core.kernel.interfaces.io.Flushable;
import com.telenav.kivakit.core.kernel.interfaces.lifecycle.Startable;
import com.telenav.kivakit.core.kernel.interfaces.lifecycle.Stoppable;
import com.telenav.kivakit.core.kernel.language.threading.RepeatingThread;
import com.telenav.kivakit.core.kernel.language.threading.locks.legacy.ConditionLock;
import com.telenav.kivakit.core.kernel.language.threading.locks.legacy.NotifyAllBooleanLock;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Rate;
import com.telenav.kivakit.core.kernel.language.time.RateCalculator;
import com.telenav.kivakit.core.kernel.language.types.Classes;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@UmlClassDiagram(diagram = DiagramEmail.class)
@UmlRelation(label = "sends", referent = Email.class)
@UmlRelation(label = "configured by", referent = EmailSender.Configuration.class)
public abstract class EmailSender extends BaseRepeater implements Startable, Stoppable, Flushable, Closeable
{
    @UmlClassDiagram(diagram = DiagramEmail.class)
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

    private final ConditionLock queueEmpty = new ConditionLock(new NotifyAllBooleanLock());

    @UmlAggregation
    private final EmailQueue queue = new EmailQueue();

    private Maximum maximumRetries = Maximum.maximum(16);

    private Duration retryPeriod = Duration.seconds(30);

    private boolean sendingOn = true;

    private volatile boolean running;

    private final RateCalculator rate = new RateCalculator(Duration.ONE_MINUTE);

    private final Configuration configuration;

    private final RepeatingThread thread = new RepeatingThread(Classes.simpleName(EmailSender.class))
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
                queueEmpty.satisfy();
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
        queueEmpty.waitFor(true, maximumWaitTime);
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
        if (rate.rate().isGreaterThan(configuration.maximumSendRate()))
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
