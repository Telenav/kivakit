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

import com.telenav.kivakit.core.project.lexakai.DiagramMessaging;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.level.Level;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the severity of the current state of an operation or a step in an operation.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessaging.class)
@LexakaiJavadoc(complete = true)
public final class Severity extends Level implements Named
{
    private static final Map<String, Severity> severities = new HashMap<>();

    public static final Severity TRACE = new Severity("Trace", 0);

    public static final Severity NONE = new Severity("None", 0.05);

    public static final Severity LOW = new Severity("Low", 0.1);

    public static final Severity MEDIUM = new Severity("Medium", 0.5);

    public static final Severity MEDIUM_HIGH = new Severity("Medium High", 0.6);

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

    @Override
    public boolean equals(Object object)
    {
        // Local fields are not considered
        return super.equals(object);
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public int hashCode()
    {
        // Local fields are not considered
        return super.hashCode();
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
