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

package com.telenav.kivakit.network.ftp.secure;

import com.telenav.kivakit.language.time.Duration;
import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.network.ftp.project.lexakai.DiagramSecureFtp;
import com.telenav.kivakit.security.authentication.Password;
import com.telenav.kivakit.security.authentication.UserName;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Setting for performing SFTP operations, including host, username and password and a timeout duration.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSecureFtp.class)
@LexakaiJavadoc(complete = true)
public class SecureFtpSettings
{
    private Host host;

    private UserName userName;

    private Password password;

    private Duration timeout;

    public SecureFtpSettings(Host host, UserName userName, Password password, Duration timeout)
    {
        this.host = host;
        this.userName = userName;
        this.password = password;
        this.timeout = timeout;
    }

    public Host host()
    {
        return host;
    }

    public void host(Host host)
    {
        this.host = host;
    }

    public Password password()
    {
        return password;
    }

    public void password(Password password)
    {
        this.password = password;
    }

    public Duration timeout()
    {
        return timeout;
    }

    public void timeout(Duration timeout)
    {
        this.timeout = timeout;
    }

    public UserName userName()
    {
        return userName;
    }

    public void username(UserName username)
    {
        userName = username;
    }
}
