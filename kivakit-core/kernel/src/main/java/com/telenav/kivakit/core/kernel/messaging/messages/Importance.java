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

package com.telenav.kivakit.core.kernel.messaging.messages;

import com.telenav.kivakit.core.kernel.language.values.level.Level;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.messages.status.*;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessaging;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;
import java.util.Map;

/**
 * The (relative) importance of {@link Message}s as a level from zero to one.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessaging.class)
@LexakaiJavadoc(complete = true)
public class Importance extends Level
{
    private static final Map<Class<? extends Message>, Importance> levels = new HashMap<>();

    static
    {
        // Built-in messages in order of increasing importance. Additional messages can register
        // their importance as mid-way between two registered messages with register()

        register
                (
                        Trace.class,
                        Activity.class,
                        Information.class,
                        Success.class,
                        Narration.class,
                        Announcement.class,
                        Quibble.class,
                        Warning.class,
                        Incomplete.class,
                        Problem.class,
                        Failure.class,
                        Alert.class,
                        CriticalAlert.class
                );
    }

    public static Importance level(final double level)
    {
        return new Importance(level);
    }

    public static Importance of(final Class<? extends Message> type)
    {
        return levels.get(type);
    }

    /**
     * An importance level for a user-defined message class that is midway between the two given message classes.
     *
     * @param low The lower importance message class
     * @param high The higher importance message class
     * @return The importance between the two message classes
     */
    public static Importance register(final Class<? extends Message> low, final Class<? extends Message> high)
    {
        final var lowValue = levels.get(low).asZeroToOne();
        final var highValue = levels.get(high).asZeroToOne();
        final var difference = highValue - lowValue;
        return level(lowValue + difference / 2.0);
    }

    private Importance(final double level)
    {
        super(level);
    }

    @SafeVarargs
    private static void register(final Class<? extends Message>... messages)
    {
        double level = 0.0;
        final double increment = 1.0 / (messages.length - 1);
        for (final var message : messages)
        {
            levels.put(message, level(level));
            level += increment;
            level = Math.min(1.0, level);
        }
    }
}
