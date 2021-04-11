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

package com.telenav.kivakit.service.registry.protocol;

import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * The names of REST methods in the service registry rest protocol
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class ServiceRegistryProtocol
{
    public static final String NETWORK_REGISTRY_UPDATE = "network-registry-update";

    public static final String REGISTER_SERVICE = "register-service";

    public static final String RENEW_SERVICE = "renew-service";

    public static final String DISCOVER_APPLICATIONS = "discover-applications";

    public static final String DISCOVER_PORT_SERVICE = "discover-port-service";

    public static final String DISCOVER_SERVICES = "discover-services";

    public static final String SHOW_VERSION = "version";
}
