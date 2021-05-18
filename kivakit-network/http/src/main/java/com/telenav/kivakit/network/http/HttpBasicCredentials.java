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

package com.telenav.kivakit.network.http;

import com.telenav.kivakit.network.http.project.lexakai.diagrams.DiagramHttp;
import com.telenav.kivakit.security.authentication.Password;
import com.telenav.kivakit.security.authentication.UserName;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Credentials for HTTP Basic authentication using clear text username and password. This method of authentication is
 * not secure. When possible use classes from the *secure* package.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramHttp.class)
@LexakaiJavadoc(complete = true)
public class HttpBasicCredentials
{
    private final Password password;

    private final UserName username;

    public HttpBasicCredentials(final UserName username, final Password password)
    {
        this.username = username;
        this.password = password;
    }

    public Password password()
    {
        return password;
    }

    @Override
    public String toString()
    {
        return username.toString();
    }

    public UserName userName()
    {
        return username;
    }
}
