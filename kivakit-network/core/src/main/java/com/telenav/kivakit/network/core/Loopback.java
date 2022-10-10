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

package com.telenav.kivakit.network.core;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.object.Lazy;
import com.telenav.kivakit.network.core.internal.lexakai.DiagramPort;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * The loopback adapter 'host'.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPort.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public class Loopback extends Host
{
    /** The loopback interface for this host */
    private static final Lazy<Loopback> loopback = Lazy.lazy(Loopback::new);

    /**
     * Returns the loopback address (normally 127.0.01) as a host
     */
    public static Loopback loopback()
    {
        return loopback.get();
    }

    protected Loopback()
    {
        super("loopback");
    }

    /**
     * Returns the loopback address (normally 127.0.0.1)
     */
    @Override
    public InetAddress onResolveAddress()
    {
        try
        {
            var interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements())
            {
                var at = interfaces.nextElement();
                if (at.isLoopback())
                {
                    var addresses = at.getInetAddresses();
                    while (addresses.hasMoreElements())
                    {
                        var address = addresses.nextElement();
                        if (address instanceof Inet4Address)
                        {
                            return address;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            return fail(e, "Couldn't find loopback interface");
        }
        return fail("Couldn't find loopback interface");
    }
}
