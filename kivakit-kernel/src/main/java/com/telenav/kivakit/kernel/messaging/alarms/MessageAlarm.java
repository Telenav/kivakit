package com.telenav.kivakit.kernel.messaging.alarms;

import com.telenav.kivakit.kernel.language.time.Rate;
import com.telenav.kivakit.kernel.messaging.Listener;

/**
 * A message alarm is a listener that triggers via {@link #trigger(Rate)} when {@link #shouldTrigger()} returns true.
 *
 * @author jonathanl (shibo)
 */
public interface MessageAlarm extends Listener
{
    /**
     * Executes the action for this message alarm. The action that takes place depends on the implementation. For
     * example, the alarm might send an email or text message, or it might write information to an incident log.
     *
     * @param rate The rate of errors that triggered this alarm
     */
    void trigger(Rate rate);

    /**
     * @return True if the alarm should be triggered
     */
    boolean shouldTrigger();
}
