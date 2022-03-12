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

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.name.Name;
import com.telenav.kivakit.network.core.project.lexakai.DiagramAuthentication;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A username for use in authentication.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramAuthentication.class)
@LexakaiJavadoc(complete = true)
public class UserName extends Name
{
    public static UserName parse(Listener listener, String name)
    {
        return new UserName(name);
    }

    /**
     * Converts {@link UserName} objects to and from strings.
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<UserName>
    {
        public Converter(Listener listener)
        {
            super(listener);
        }

        @Override
        protected UserName onToValue(String value)
        {
            return parse(this, value);
        }
    }

    protected UserName(String name)
    {
        super(name);
    }
}