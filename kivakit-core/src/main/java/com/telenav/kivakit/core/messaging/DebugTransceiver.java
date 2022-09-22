package com.telenav.kivakit.core.messaging;

import com.telenav.kivakit.core.messaging.context.CodeContext;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

/**
 * Adds debugging features to {@link Transceiver}.
 *
 * @author jonathanl (shibo)
 */
public interface DebugTransceiver extends Transceiver
{
    /**
     * @return Debug object for this
     */
    default Debug debug()
    {
        return Debug.of(debugClassContext(), this);
    }

    /**
     * @return The class where this transceiver is
     */
    @UmlExcludeMember
    default Class<?> debugClassContext()
    {
        return getClass();
    }

    /**
     * <b>Not public API</b>
     *
     * @return The context of this broadcaster in code
     */
    @UmlExcludeMember
    default CodeContext debugCodeContext()
    {
        return new CodeContext(debugClassContext());
    }

    /**
     * <b>Not public API</b>
     *
     * @param context The context in code
     */
    @UmlExcludeMember
    default void debugCodeContext(CodeContext context)
    {
    }

    /**
     * Runs the given code if debug is turned on for this {@link Transceiver}
     *
     * @param code The code to run if debug is off
     */
    default void ifDebug(Runnable code)
    {
        if (isDebugOn())
        {
            code.run();
        }
    }


    /**
     * @return True if debugging is on for this {@link Transceiver}
     */
    default boolean isDebugOn()
    {
        return debug().isDebugOn();
    }
}
