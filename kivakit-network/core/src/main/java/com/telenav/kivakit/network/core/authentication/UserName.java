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

package com.telenav.kivakit.network.core.authentication;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.name.Name;
import com.telenav.kivakit.network.core.internal.lexakai.DiagramAuthentication;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_DEFAULT_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A username for use in authentication.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramAuthentication.class)
@ApiQuality(stability = API_STABLE_DEFAULT_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class UserName extends Name
{
    /**
     * Returns the given text parsed into a {@link UserName}
     *
     * @param listener The listener to notify of any problems
     * @param text The text to parse
     */
    public static UserName parseUserName(Listener listener, String text)
    {
        return new UserName(text);
    }

    /**
     * Converts {@link UserName} objects to and from strings.
     *
     * @author jonathanl (shibo)
     */
    @ApiQuality(stability = API_STABLE_EXTENSIBLE,
                testing = TESTING_NONE,
                documentation = DOCUMENTATION_COMPLETE)
    public static class Converter extends BaseStringConverter<UserName>
    {
        public Converter(Listener listener)
        {
            super(listener);
        }

        @Override
        protected UserName onToValue(String value)
        {
            return UserName.parseUserName(this, value);
        }
    }

    protected UserName(String name)
    {
        super(name);
    }
}
