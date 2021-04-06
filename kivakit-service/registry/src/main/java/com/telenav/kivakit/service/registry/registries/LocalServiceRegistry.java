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

package com.telenav.kivakit.service.registry.registries;

import com.telenav.kivakit.core.kernel.messaging.messages.Result;
import com.telenav.kivakit.core.network.core.Host;
import com.telenav.kivakit.core.network.core.Port;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.kivakit.service.registry.project.lexakai.annotations.DiagramRegistry;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * <b>Not public API</b>
 * <p>
 * A service registry for the local host.
 * </p>
 */
@UmlClassDiagram(diagram = DiagramRegistry.class)
@UmlNotPublicApi
public class LocalServiceRegistry extends BaseServiceRegistry
{
    /** The first port to allocate at, normally near the start of the ephemeral range */
    private int firstPort;

    public LocalServiceRegistry(final int firstPort)
    {
        this.firstPort = firstPort;
    }

    protected LocalServiceRegistry()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Result<Service> register(final Service service)
    {
        return lock().write(() ->
        {
            trace("Registering service: $", service);

            // If the service is already registered
            if (isRegistered(service))
            {
                // then we just renew its registration. This code path isn't typical because renewals normally occur
                // directly through renew(), but it can happen in the case where an application goes down, comes back
                // up again and tries to re-register the same logical service. The registry will still remember the
                // original registration if it hasn't been that long, but the application will think it is registering
                // the service for the first time.
                return renew(service);
            }
            else
            {
                // otherwise, the service should be unbound,
                if (service.isUnbound())
                {
                    // so we bind it to a port,
                    service.port(nextPort());

                    // and add it to the registry.
                    addOrUpdate(service);
                    trace("Added new service binding: $", service);
                }
                else
                {
                    // This case shouldn't normally occur because a service that is not registered should be unbound
                    assert service.isBound();
                    trace("Service already registered: $", service);
                }
            }

            return Result.succeeded(service);
        });
    }

    @Override
    public @NotNull Result<Service> renew(final Service service)
    {
        return lock().write(() ->
        {
            trace("Renewing service: $", service);
            addOrUpdate(service);
            return Result.succeeded(service);
        });
    }

    /**
     * @return The next available port on the local host
     */
    private Port nextPort()
    {
        // NOTE: read/write lock is already held by caller

        // Then, if we are out of ports, we have to re-use ports that were already allocated at
        // some point. So we loop through ephemeral port range
        for (var portNumber = firstPort; portNumber < 65_536; portNumber++)
        {
            // and if the next available port is not in use or still reserved for an app that disappeared,
            if (isPortAvailable(portNumber))
            {
                // then return the port if it is available for use

                // NOTE: Yes, there is a race condition here between KivaKit applications and OTHER applications
                // that might be attempting to use ports in the same range. If the port bound to the service
                // returned by the register() method cannot be used, another application may have bound it
                // in the meantime and the caller should simply try again until successful. Another approach
                // would be to carefully select a range of ephemeral port numbers that no other application
                // on the host will try to use.

                final var port = Host.local().port(portNumber);
                if (port.isAvailable())
                {
                    return port;
                }
            }
        }
        return fail("Unable to find an available port");
    }
}
