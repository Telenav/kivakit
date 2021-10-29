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

package com.telenav.kivakit.security.authentication;

import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.language.values.name.Name;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.security.project.lexakai.diagrams.DiagramSecurity;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A user name for use in authentication.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSecurity.class)
@LexakaiJavadoc(complete = true)
public class UserName extends Name
{
    public static UserName parse(String name)
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
            return parse(value);
        }
    }

    protected UserName(String name)
    {
        super(name);
    }
}
