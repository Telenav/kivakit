////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.language.threading.context;

import com.telenav.kivakit.kernel.interfaces.numeric.Sized;
import com.telenav.kivakit.kernel.language.strings.Align;
import com.telenav.kivakit.kernel.language.strings.formatting.IndentingStringBuilder;
import com.telenav.kivakit.kernel.language.types.Classes;

import java.util.HashMap;
import java.util.Map;

public class StackTrace implements Sized
{
    public static Map<Thread, StackTrace> allThreads()
    {
        final Map<Thread, StackTrace> traces = new HashMap<>();
        for (final var entry : Thread.getAllStackTraces().entrySet())
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
    public static class Frame
    {
        private String type;

        private String method;

        private int line;

        private String file;

        public Frame(final StackTraceElement element)
        {
            type = element.getClassName();
            method = element.getMethodName();
            line = element.getLineNumber();
            file = element.getFileName();
        }

        protected Frame()
        {
        }

        public String full()
        {
            return "    at " + type + "." + method
                    + "(" + file + ":" + line + ")";
        }

        public String simplified()
        {
            // Make type human readable
            final var type = type();

            // If the type is not stack trace itself
            if (!type.equals(StackTrace.class.getName()))
            {
                // return the frame as a simplified string
                final var context = "(" + file + ":" + line + ")";
                return "  " + Align.right(context, 40, ' ') + " " + method;
            }

            // We don't want to include this frame
            return null;
        }

        private String type()
        {
            return type.replace('$', '.');
        }
    }

    private final String message;

    private final Frame[] frames;

    private StackTrace cause;

    private String exceptionType;

    private String fullExceptionType;

    public StackTrace()
    {
        this(new Throwable());
    }

    public StackTrace(final String message, final StackTraceElement[] elements)
    {
        this.message = message;
        frames = new Frame[elements.length];
        var index = 0;
        for (final var element : elements)
        {
            frames[index++] = new Frame(element);
        }
    }

    public StackTrace(final Throwable throwable)
    {
        this(throwable.getMessage(), throwable.getStackTrace());
        if (throwable.getCause() != null)
        {
            cause = new StackTrace(throwable.getCause());
        }
        fullExceptionType = throwable.getClass().getName();
        exceptionType = Classes.simpleName(throwable.getClass());
    }

    public StackTrace cause()
    {
        return cause;
    }

    @Override
    public int size()
    {
        return frames.length;
    }

    public String toHtmlString()
    {
        final var builder = new StringBuilder();
        builder.append("<p><b><font color='#808080'>").append(message).append("</font></b></p>");
        for (final var frame : frames)
        {
            builder.append("&nbsp;&nbsp;at ").append(frame).append("<br/>");
        }
        if (cause != null)
        {
            builder.append(cause.toHtmlString());
        }
        return builder.toString().replaceAll("\\$", ".");
    }

    @Override
    public String toString()
    {
        final var simple = "true".equalsIgnoreCase(System.getProperty("KIVAKIT_SIMPLIFIED_STACK_TRACES"));
        return trace(!simple);
    }

    public String top()
    {
        return frames[0].full();
    }

    private String message()
    {
        return message == null ? "" : ": " + message;
    }

    private String trace(final boolean full)
    {
        final var builder = new IndentingStringBuilder(IndentingStringBuilder.Style.TEXT, IndentingStringBuilder.Indentation.of(full ? 4 : 2));
        if (full)
        {
            builder.appendLine("Exception in thread \"" + Thread.currentThread().getName() + "\""
                    + fullExceptionType + message());
        }
        else
        {
            builder.appendLine(Align.right("(" + exceptionType + ")", 40, ' '));
        }
        var index = 0;
        final var limit = 60;
        final var include = limit / 2;
        var omitted = false;
        for (final var frame : frames)
        {
            final var omit = index > include && index < frames.length - include;
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
