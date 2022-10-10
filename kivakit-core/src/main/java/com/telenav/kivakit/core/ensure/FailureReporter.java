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

package com.telenav.kivakit.core.ensure;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramFailureReporter;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.messaging.MessageFormat.WITH_EXCEPTION;

/**
 * Reporter of failure messages. Reporter implementations include:
 *
 * <ul>
 *     <li>{@link #assertingFailureReporter()}</li>
 *     <li>{@link #emptyFailureReporter()}</li>
 *     <li>{@link #loggingFailureReporter()}</li>
 *     <li>{@link #throwingFailureReporter()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramFailureReporter.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface FailureReporter extends Listener
{
    /**
     * Returns a failure reporter that uses Java assertions
     */
    static FailureReporter assertingFailureReporter()
    {
        return message ->
        {
            assert false : "Failure occurred: " + message.description() + "\n\n" + new Throwable();
        };
    }

    /**
     * Returns a failure reporter that does nothing
     */
    static FailureReporter emptyFailureReporter()
    {
        return ignored ->
        {
        };
    }

    /**
     * Returns a failure reporter that logs failures
     */
    static FailureReporter loggingFailureReporter()
    {
        return message -> Logger.logger().log(message);
    }

    /**
     * Returns a failure reporter that throws an exception
     */
    static FailureReporter throwingFailureReporter()
    {
        return message ->
        {
            throw new IllegalStateException(message.formatted(WITH_EXCEPTION), message.cause());
        };
    }

    /**
     * Reports any failure messages
     *
     * @param message The message
     */
    @Override
    default void onMessage(Message message)
    {
        if (message.isFailure())
        {
            report(message);
        }
    }

    /**
     * Reports a validation issue
     */
    void report(Message message);
}
