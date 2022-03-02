package com.telenav.kivakit.core.messaging.alarms;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Rate;

/**
 * A message alarm is a listener that triggers via {@link #trigger(Rate)} when {@link #shouldTrigger()} returns true.
 *
 * @author jonathanl (shibo)
 */
public interface MessageAlarm extends Listener
{
    /**
     * @return True if the alarm should be triggered
     */
    boolean shouldTrigger();

    /**
     * Executes the action for this message alarm. The action that takes place depends on the implementation. For
     * example, the alarm might send an email or text message, or it might write information to an incident log.
     *
     * @param rate The rate of errors that triggered this alarm
     */
    void trigger(Rate rate);
}
