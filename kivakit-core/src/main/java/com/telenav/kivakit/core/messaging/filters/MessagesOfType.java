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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.interfaces.comparison.Filter;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A filter that matches messages equal to or subclassing the given type.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class MessagesOfType<MessageType extends Message> implements Filter<MessageType>
{
    /** The type of message to include */
    private final Class<MessageType> type;

    public MessagesOfType(Class<MessageType> type)
    {
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accepts(MessageType message)
    {
        if (message == null)
        {
            return false;
        }
        return message.getClass().isAssignableFrom(type);
    }

    @Override
    public String toString()
    {
        return type.getSimpleName();
    }
}
