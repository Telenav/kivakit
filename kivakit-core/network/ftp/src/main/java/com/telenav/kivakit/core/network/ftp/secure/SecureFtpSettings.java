////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.ftp.secure;

import com.telenav.kivakit.core.network.ftp.project.lexakai.diagrams.DiagramSecureFtp;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.network.core.Host;
import com.telenav.kivakit.core.security.authentication.Password;
import com.telenav.kivakit.core.security.authentication.UserName;

@UmlClassDiagram(diagram = DiagramSecureFtp.class)
public class SecureFtpSettings
{
    private Host host;

    private UserName userName;

    private Password password;

    private Duration timeout;

    public SecureFtpSettings(final Host host, final UserName userName, final Password password, final Duration timeout)
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

    public void host(final Host host)
    {
        this.host = host;
    }

    public Password password()
    {
        return password;
    }

    public void password(final Password password)
    {
        this.password = password;
    }

    public Duration timeout()
    {
        return timeout;
    }

    public void timeout(final Duration timeout)
    {
        this.timeout = timeout;
    }

    public UserName userName()
    {
        return userName;
    }

    public void username(final UserName username)
    {
        userName = username;
    }
}
