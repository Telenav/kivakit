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
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.level.Level;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Represents the severity of the current state of an operation or a step in an operation.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramMessaging.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public final class Severity extends Level implements Named
{
    private static final Map<String, Severity> severities = new HashMap<>();

    public static final Severity NONE = new Severity("None", 0);

    public static final Severity LOW = new Severity("Low", 0.1);

    public static final Severity MEDIUM = new Severity("Medium", 0.5);

    public static final Severity HIGH = new Severity("High", 0.7);

    public static final Severity CRITICAL = new Severity("Critical", 0.9);

    public static Severity parseSeverity(Listener listener, String name)
    {
        return listener.problemIfNull(severities.get(name), "Invalid severity: $", name);
    }

    private String name;

    public Severity(String name, double severity)
    {
        super(severity);

        assert name != null;
        this.name = name;

        severities.put(name, this);
    }

    private Severity()
    {
        super(0.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name()
    {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return name;
    }
}
