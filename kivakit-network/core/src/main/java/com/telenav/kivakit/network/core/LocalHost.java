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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.logging.LoggerCodeContext;
import com.telenav.kivakit.core.object.Lazy;
import com.telenav.kivakit.core.os.OperatingSystem;
import com.telenav.kivakit.network.core.internal.lexakai.DiagramPort;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * The local {@link Host}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPort.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public class LocalHost extends Host
{
    /** The local host */
    private static final Lazy<LocalHost> localhost = Lazy.lazy(LocalHost::new);

    static
    {
        LoggerCodeContext.hostResolver(LocalHost::hostname);
    }

    /**
     * Returns the name of the local host
     */
    public static String hostname()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e)
        {
            return fail(e, "Cannot determine local hostname");
        }
    }

    /**
     * Returns the local host
     */
    public static LocalHost localhost()
    {
        return localhost.get();
    }

    protected LocalHost()
    {
        super("localhost");
    }

    /**
     * Returns the first non-loopback, non-virtual, IPV4 InetAddress in the list of network interfaces. This is
     * necessary on the Mac because Oracle broke InetAddress.getLocalhost() on the Mac (and does not intend to fix it)
     */
    @Override
    public InetAddress onResolveAddress()
    {
        if (OperatingSystem.operatingSystem().isWindows())
        {
            try
            {
                return InetAddress.getLocalHost();
            }
            catch (UnknownHostException e)
            {
                fail(e, "Couldn't find localhost interface");
            }
        }
        try
        {
            var interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements())
            {
                var next = interfaces.nextElement();
                if (!next.isLoopback() && !next.isVirtual() && next.isUp())
                {
                    var addresses = next.getInetAddresses();
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
            return fail(e, "Couldn't find localhost interface");
        }
        return fail("Couldn't find localhost interface");
    }
}
