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

package com.telenav.kivakit.core.messaging.context;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramContext;
import com.telenav.kivakit.core.language.reflection.Method;
import com.telenav.kivakit.core.messaging.Debug;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.reflection.Method.method;
import static java.lang.Thread.currentThread;

/**
 * A stack of KivaKit {@link Method} objects for a given thread ({@link #callstack(Thread)} or the current thread
 * {@link #callstack()}.
 * <p>
 * The caller of a given class on the stack (the "callee") can be determined with
 * {@link #callerOf(Proximity, Matching, Class, Matching, Class[])}, which takes the callee type and a variable number
 * of classes to ignore. Matching of the caller and its distance from the callee are specified by the first and second
 * parameters respectively.
 * <p>
 * For example, the class <b>A</b> might want to determine who called it (for a concrete example see {@link Debug},
 * which finds its caller in order to switch on/off debugging for that class). This makes class <b>A</b> the callee. It
 * might also wish to ignore another class in the same package, class <b>B</b>, which might be on the stack between the
 * caller and the callee. In this case, CallStack.callerOf(..., A.class, B.class) would return the code which is calling
 * method(s) in <b>A</b> whether it called through method(s) in <b>B</b> or not.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramContext.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class CallStack
{
    public static Method callerOf(Proximity proximity, Matching matching, Class<?> calleeType)
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
    public static Method callerOf(Proximity proximity,
                                  Matching matching,
                                  Class<?> calleeType,
                                  Matching ignoreMatching,
                                  Class<?>... ignores)
    {
        // Get call stack
        var stack = callstack();

        // Find the index of the callee on the stack using the matching rules
        var callee = calleeType == null ? 0 : findCallee(matching, proximity, stack, calleeType);

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

    public static List<Method> callstack()
    {
        return callstack(currentThread());
    }

    @SuppressWarnings("unused")
    public static List<Method> callstack(Thread thread)
    {
        var stack = new ArrayList<Method>();
        for (var frame : currentThread().getStackTrace())
        {
            var method = method(frame);
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

    private static int findCallee(Matching matching,
                                  Proximity proximity,
                                  List<Method> stack,
                                  Class<?> calleeType)
    {
        var callee = -1;

        var index = 0;
        for (var method : stack)
        {
            var matches = matching == Matching.EXACT
                    ? calleeType.equals(method.parentType().asJavaType())
                    : calleeType.isAssignableFrom(method.parentType().asJavaType());

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

    private static boolean shouldIgnore(Method caller, Matching matching, Class<?>... ignores)
    {
        var ignored = false;
        var exact = matching == Matching.EXACT;
        if (caller != null)
        {
            for (var ignore : ignores)
            {
                if ((exact && ignore == caller.parentType().asJavaType()) || (!exact && ignore.isAssignableFrom(caller.parentType().asJavaType())))
                {
                    ignored = true;
                }
            }
        }
        return ignored;
    }
}
