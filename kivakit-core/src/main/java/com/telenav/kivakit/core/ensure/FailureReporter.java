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

import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.project.lexakai.DiagramFailureReporter;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.string.Formatter.Format.WITH_EXCEPTION;

/**
 * Reporter of failure messages.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFailureReporter.class)
public interface FailureReporter extends Listener
{
    static FailureReporter asserting()
    {
        return message ->
        {
            assert false : message.description();
        };
    }

    static FailureReporter logging()
    {
        return message -> Logger.logger().log(message);
    }

    static FailureReporter none()
    {
        return ignored ->
        {
        };
    }

    static FailureReporter throwing()
    {
        return message ->
        {
            throw new IllegalStateException(message.formatted(WITH_EXCEPTION), message.cause());
        };
    }

    @Override
    default void onMessage(Message message)
    {
        report(message);
    }

    /**
     * Reports a validation issue
     */
    void report(Message message);
}
