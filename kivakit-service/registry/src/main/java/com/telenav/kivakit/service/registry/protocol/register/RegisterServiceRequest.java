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

package com.telenav.kivakit.service.registry.protocol.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.kivakit.service.registry.protocol.BaseRequest;
import com.telenav.kivakit.service.registry.protocol.ServiceRegistryProtocol;
import io.swagger.v3.oas.annotations.media.Schema;

import static com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter.Format.MULTILINE;

/**
 * A request from an application (usually a server) to register a service and begin leasing a port for it.
 *
 * @author jonathanl (shibo)
 */
@Schema
public class RegisterServiceRequest extends BaseRequest
{
    /** The potentially unbound service to register or renew */
    @JsonProperty
    @Schema(description = "The service that should be registered and allocated a port",
            required = true)
    private Service service;

    public RegisterServiceRequest(final Service service)
    {
        this.service = service;
    }

    protected RegisterServiceRequest()
    {
    }

    @Override
    public String path()
    {
        return ServiceRegistryProtocol.REGISTER_SERVICE;
    }

    @KivaKitIncludeProperty
    public Service service()
    {
        return service;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString(MULTILINE);
    }
}
