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

package com.telenav.kivakit.kernel.messaging.messages;

import com.telenav.kivakit.kernel.language.values.level.Level;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.status.Activity;
import com.telenav.kivakit.kernel.messaging.messages.status.Alert;
import com.telenav.kivakit.kernel.messaging.messages.status.Announcement;
import com.telenav.kivakit.kernel.messaging.messages.status.CriticalAlert;
import com.telenav.kivakit.kernel.messaging.messages.status.Glitch;
import com.telenav.kivakit.kernel.messaging.messages.status.Information;
import com.telenav.kivakit.kernel.messaging.messages.status.Narration;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.messaging.messages.status.StepFailure;
import com.telenav.kivakit.kernel.messaging.messages.status.StepIncomplete;
import com.telenav.kivakit.kernel.messaging.messages.status.StepSuccess;
import com.telenav.kivakit.kernel.messaging.messages.status.Trace;
import com.telenav.kivakit.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessaging;
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

        register(
                Trace.class,
                Activity.class,
                Information.class,
                StepSuccess.class,
                Narration.class,
                Announcement.class,
                Glitch.class,
                Warning.class,
                StepIncomplete.class,
                Problem.class,
                StepFailure.class,
                Alert.class,
                CriticalAlert.class
        );
    }

    public static Importance importance(final double level)
    {
        return new Importance(level);
    }

    public static Importance importance(final Class<? extends Message> type)
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
        return importance(lowValue + difference / 2.0);
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
            levels.put(message, importance(level));
            level += increment;
            level = Math.min(1.0, level);
        }
    }
}
