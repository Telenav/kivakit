package com.telenav.kivakit.network.email;

import com.telenav.kivakit.configuration.lookup.RegistryTrait;
import com.telenav.kivakit.kernel.language.time.Rate;
import com.telenav.kivakit.kernel.messaging.alarms.BaseMessageAlarm;

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
