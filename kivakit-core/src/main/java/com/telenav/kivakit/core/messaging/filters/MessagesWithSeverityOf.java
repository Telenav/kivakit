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

package com.telenav.kivakit.core.messaging.filters;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.MessageFilter;
import com.telenav.kivakit.core.messaging.messages.Severity;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A message filter that matches messages with the given severity, or worse.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class MessagesWithSeverityOf implements MessageFilter
{
    /** The minimum severity */
    private final Severity minimumSeverity;

    public MessagesWithSeverityOf(Severity minimumSeverity)
    {
        this.minimumSeverity = minimumSeverity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accepts(Message value)
    {
        return value.severity().isGreaterThanOrEqualTo(this.minimumSeverity);
    }

    @Override
    public String toString()
    {
        return minimumSeverity.name();
    }
}
