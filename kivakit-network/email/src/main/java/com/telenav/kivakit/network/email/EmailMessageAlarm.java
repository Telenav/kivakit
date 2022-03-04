package com.telenav.kivakit.network.email;

import com.telenav.kivakit.core.messaging.alarms.BaseMessageAlarm;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.time.Rate;

/**
 * When triggered, sends the email composed by {@link #composeEmail(Rate)} using a registered {@link EmailSender}
 * implementation.
 *
 * @author jonathanl (shibo)
 */
public abstract class EmailMessageAlarm extends BaseMessageAlarm implements RegistryTrait
{
    /**
     * Composes the alarm email to send. For example:
     *
     * <pre>return new Email()
     * .from(shibo)
     * .subject("Alarm!")
     * .addTo(shibo)
     * .body(new EmailBody("Error rate is " + rate));</pre>
     *
     * @return The email to send
     */
    public abstract Email composeEmail(Rate rate);

    /**
     * {@inheritDoc}
     *
     * <p>
     * Composes and sends an email for this alarm
     * </p>
     */
    @Override
    protected void onTrigger(Rate rate)
    {
        require(EmailSender.class).enqueue(composeEmail(rate));
    }
}
