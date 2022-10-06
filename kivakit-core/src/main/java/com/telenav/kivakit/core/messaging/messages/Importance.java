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

package com.telenav.kivakit.core.messaging.messages;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramMessaging;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.messages.status.Alert;
import com.telenav.kivakit.core.messaging.messages.status.Announcement;
import com.telenav.kivakit.core.messaging.messages.status.CriticalAlert;
import com.telenav.kivakit.core.messaging.messages.status.Glitch;
import com.telenav.kivakit.core.messaging.messages.status.Information;
import com.telenav.kivakit.core.messaging.messages.status.Narration;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Trace;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.messaging.messages.status.activity.Step;
import com.telenav.kivakit.core.messaging.messages.status.activity.StepFailure;
import com.telenav.kivakit.core.messaging.messages.status.activity.StepIncomplete;
import com.telenav.kivakit.core.messaging.messages.status.activity.StepSuccess;
import com.telenav.kivakit.core.value.level.Level;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * The (relative) importance of {@link Message}s as a level from zero to one.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessaging.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public class Importance extends Level
{
    /** Importance level for each message type */
    private static final Map<Class<? extends Message>, Importance> levels = new HashMap<>();

    static
    {
        // Built-in messages in order of increasing importance. Additional messages can register
        // their importance as midway between two registered messages with register()

        register(
                Trace.class,
                Step.class,
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

    /**
     * Returns an {@link Importance} for the given level
     */
    public static Importance importance(double level)
    {
        return new Importance(level);
    }

    /**
     * Returns an {@link Importance} for the given message type
     */
    public static Importance importanceOfMessage(Class<? extends Message> type)
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
    public static Importance register(Class<? extends Message> low, Class<? extends Message> high)
    {
        var lowValue = levels.get(low).asZeroToOne();
        var highValue = levels.get(high).asZeroToOne();
        var difference = highValue - lowValue;
        return importance(lowValue + difference / 2.0);
    }

    private Importance(double level)
    {
        super(level);
    }

    @SafeVarargs
    private static void register(Class<? extends Message>... messages)
    {
        double level = 0.0;
        double increment = 1.0 / (messages.length - 1);
        for (var message : messages)
        {
            levels.put(message, importance(level));
            level += increment;
            level = Math.min(1.0, level);
        }
    }
}
