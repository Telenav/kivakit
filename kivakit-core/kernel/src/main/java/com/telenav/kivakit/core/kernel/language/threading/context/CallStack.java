////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.threading.context;

import com.telenav.kivakit.core.kernel.language.reflection.Method;
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThread;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.List;

/**
 * A stack of KivaKit {@link Method} objects for a given thread ({@link #stack(Thread)} or the current thread {@link
 * #stack()}.
 * <p>
 * The caller of a given class on the stack (the "callee") can be determined with {@link #callerOf(Proximity, Matching,
 * Class, Matching, Class[])}, which takes the callee type and a variable number of classes to ignore. Matching of the
 * caller and its distance from the callee are specified by the first and second parameters respectively.
 * <p>
 * For example, the class <b>A</b> might want to determine who called it (for a concrete example see {@link Debug},
 * which finds its caller in order to switch on/off debugging for that class). This makes class <b>A</b> the callee. It
 * might also wish to ignore another class in the same package, class <b>B</b>, which might be on the stack between the
 * caller and the callee. In this case, CallStack.callerOf(..., A.class, B.class) would return the code which is calling
 * method(s) in <b>A</b> whether it called through method(s) in <b>B</b> or not.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThread.class)
public class CallStack
{
    public static Method callerOf(final Proximity proximity, final Matching matching, final Class<?> calleeType)
    {
        return callerOf(proximity, matching, calleeType, Matching.EXACT);
    }

    /**
     * Finds the (KivaKit) {@link Method} that called the given callee class. The {@link Matching} parameter is used to
     * specify whether classes should exactly match the given class parameters or if subclasses are acceptable as well.
     * Because intervening frames may be uninteresting, the {@link Proximity} type can be used to determine if the
     * caller must be the immediate caller or if a more distant caller is acceptable.  It is possible to specify a
     * variable-length list of classes to ignore when determining the calling method after finding the callee. This
     * makes it easy to skip over intervening frames that don't matter.
     *
     * @param proximity Either {@link Proximity#IMMEDIATE} if the caller must be immediately before the callee on the
     * stack or {@link Proximity#DISTANT} if the caller can be anywhere on the stack
     * @param matching Either {@link Matching#EXACT} or {@link Matching#SUBCLASS} to determine if matching of the type
     * parameters should be exact or if any subclass will do
     * @param calleeType The class that we want the caller of
     * @param ignoreMatching Matching for the classes to be ignored
     * @param ignores Any intermediate classes to ignore
     * @return The class that most recently called the given callee on this thread's execution stack
     */
    public static Method callerOf(final Proximity proximity, final Matching matching, final Class<?> calleeType,
                                  final Matching ignoreMatching, final Class<?>... ignores)
    {
        // Get call stack
        final var stack = stack();

        // Find the index of the callee on the stack using the matching rules
        final var callee = calleeType == null ? 0 : findCallee(matching, proximity, stack, calleeType);

        // If we found the callee
        if (callee != -1)
        {
            // the caller is the next index
            var caller = callee + 1;

            // except that we may need to ignore some methods
            while (caller < stack.size() && shouldIgnore(stack.get(caller), ignoreMatching, ignores))
            {
                caller++;
            }
            return caller < stack.size() ? stack.get(caller) : null;
        }
        return null;
    }

    public static List<Method> stack()
    {
        return stack(Thread.currentThread());
    }

    public static List<Method> stack(final Thread thread)
    {
        final List<Method> stack = new ArrayList<>();
        for (final var frame : Thread.currentThread().getStackTrace())
        {
            final var method = Method.of(frame);
            if (method != null)
            {
                stack.add(method);
            }
        }
        return stack;
    }

    public enum Matching
    {
        /** The class must exactly match */
        EXACT,

        /** The class must be a subclass of the specified class */
        SUBCLASS
    }

    public enum Proximity
    {
        /** The class can be anywhere on the call stack */
        DISTANT,

        /** The class must be at the top of the call stack */
        IMMEDIATE
    }

    private static int findCallee(final Matching matching, final Proximity proximity, final List<Method> stack,
                                  final Class<?> calleeType)
    {
        var callee = -1;

        var index = 0;
        for (final var method : stack)
        {
            final var matches = matching == Matching.EXACT ? calleeType.equals(method.type()) : calleeType.isAssignableFrom(method.type());

            switch (proximity)
            {
                case DISTANT:
                    if (matches)
                    {
                        return index;
                    }
                    break;

                case IMMEDIATE:
                    if (matches)
                    {
                        callee = index;
                    }
                    else
                    {
                        if (callee > -1)
                        {
                            return callee;
                        }
                    }
                    break;
            }

            index++;
        }

        return callee;
    }

    private static boolean shouldIgnore(final Method caller, final Matching matching, final Class<?>... ignores)
    {
        var ignored = false;
        final var exact = matching == Matching.EXACT;
        if (caller != null)
        {
            for (final var ignore : ignores)
            {
                if ((exact && ignore == caller.type()) || (!exact && ignore.isAssignableFrom(caller.type())))
                {
                    ignored = true;
                }
            }
        }
        return ignored;
    }
}
