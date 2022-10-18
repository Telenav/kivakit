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
import com.telenav.kivakit.core.string.IndentingStringBuilder;
import com.telenav.kivakit.interfaces.collection.Sized;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.Classes.simpleName;
import static com.telenav.kivakit.core.string.Align.rightAlign;
import static com.telenav.kivakit.core.string.IndentingStringBuilder.Indentation.indentation;
import static java.lang.Thread.currentThread;

/**
 * Holds stack trace information
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class StackTrace implements
        Sized,
        StringFormattable
{
    /**
     * Returns stack traces for all threads
     */
    public static Map<Thread, StackTrace> allThreads()
    {
        Map<Thread, StackTrace> traces = new HashMap<>();
        for (var entry : Thread.getAllStackTraces().entrySet())
        {
            traces.put(entry.getKey(), new StackTrace(null, entry.getValue()));
        }
        return traces;
    }

    /**
     * This has to exist because Josh forgot to put a no-arg constructor on StackTraceElement in the JDK. Without that,
     * it isn't serializable with Kryo.
     *
     * @author jonathanl (shibo)
     */
    @SuppressWarnings("unused") public static class StackFrame
    {
        /** The Java file */
        private String file;

        /** The line number */
        private int line;

        /** The method */
        private String method;

        /** The simple type name */
        private String type;

        public StackFrame(StackTraceElement element)
        {
            type = element.getClassName();
            method = element.getMethodName();
            line = element.getLineNumber();
            file = element.getFileName();
        }

        protected StackFrame()
        {
        }

        /**
         * Returns the full description of this frame
         */
        public String full()
        {
            return "    at " + type + "." + method
                    + "(" + file + ":" + line + ")";
        }

        /**
         * Returns a simplified description of this frame
         */
        public String simplified()
        {
            // Make type human-readable
            var type = type();

            // If the type is not stack trace itself
            if (!type.equals(StackTrace.class.getName()))
            {
                // return the frame as a simplified string
                var context = "(" + file + ":" + line + ")";
                return "  " + rightAlign(context, 40, ' ') + " " + method;
            }

            // We don't want to include this frame
            return null;
        }

        private String type()
        {
            return type.replace('$', '.');
        }
    }

    /** The stack trace that caused this one */
    private StackTrace cause;

    /** The type of exception thrown */
    private String exceptionType;

    /** The stack frames in this trace */
    private final StackFrame[] frames;

    /** The fully qualified exception type name */
    private String fullExceptionType;

    /** The message associated with this trace */
    private final String message;

    public StackTrace()
    {
        this(new Throwable());
    }

    public StackTrace(String message, StackTraceElement[] elements)
    {
        this.message = message;
        frames = new StackFrame[elements.length];
        var index = 0;
        for (var element : elements)
        {
            frames[index++] = new StackFrame(element);
        }
    }

    public StackTrace(Throwable throwable)
    {
        this(throwable.getMessage(), throwable.getStackTrace());
        if (throwable.getCause() != null)
        {
            cause = new StackTrace(throwable.getCause());
        }
        fullExceptionType = throwable.getClass().getName();
        exceptionType = simpleName(throwable.getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asString(@NotNull Format format)
    {
        switch (format)
        {
            case HTML:
                return toHtmlString();

            case TEXT:
            default:
                return toString();
        }
    }

    /**
     * Returns the stack trace that caused this one
     */
    public StackTrace cause()
    {
        return cause;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size()
    {
        return frames.length;
    }

    /**
     * Returns this trace as an HTML string
     */
    public String toHtmlString()
    {
        var builder = new StringBuilder();
        builder.append("<p><b><font color='#808080'>").append(message).append("</font></b></p>");
        for (var frame : frames)
        {
            builder.append("&nbsp;&nbsp;at ").append(frame).append("<br/>");
        }
        if (cause != null)
        {
            builder.append(cause.toHtmlString());
        }
        return builder.toString().replaceAll("\\$", ".");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        var simple = "true".equalsIgnoreCase(System.getProperty("KIVAKIT_SIMPLIFIED_STACK_TRACES"));
        return trace(!simple);
    }

    /**
     * Returns the text for the top of stack
     */
    public String top()
    {
        return frames[0].full();
    }

    private String message()
    {
        return message == null ? "" : ": " + message;
    }

    private String trace(boolean full)
    {
        var builder = new IndentingStringBuilder(indentation(full ? 4 : 2));
        if (full)
        {
            builder.appendLine("Exception in thread \"" + currentThread().getName() + "\""
                    + fullExceptionType + message());
        }
        else
        {
            builder.appendLine(rightAlign("(" + exceptionType + ")", 40, ' '));
        }
        var index = 0;
        var limit = 60;
        var include = limit / 2;
        var omitted = false;
        for (var frame : frames)
        {
            var omit = index > include && index < frames.length - include;
            if (omit)
            {
                if (!omitted)
                {
                    builder.appendLine("  ... (" + (frames.length - limit) + " frames omitted)");
                    omitted = true;
                }
            }
            else
            {
                builder.appendLine((full ? frame.full() : frame.simplified()));
            }
            index++;
        }
        if (cause != null)
        {
            builder.appendLine(cause.toString());
        }
        return builder.toString().replaceAll("\\$", ".");
    }
}
