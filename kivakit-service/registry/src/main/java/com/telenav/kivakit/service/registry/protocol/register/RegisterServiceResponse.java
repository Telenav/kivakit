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
import com.telenav.kivakit.core.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.core.kernel.language.strings.formatting.KivaKitFormatProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.kivakit.service.registry.project.lexakai.annotations.DiagramRest;
import com.telenav.kivakit.service.registry.protocol.BaseResponse;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import io.swagger.v3.oas.annotations.media.Schema;

import static com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter.Format.MULTILINE;

/**
 * The registered service, including a port binding provided by the service registry.
 *
 * @author jonathanl (shibo)
 */
@Schema
@UmlClassDiagram(diagram = DiagramRest.class)
public class RegisterServiceResponse extends BaseResponse<Service>
{
    @JsonProperty
    private Service service;

    @KivaKitIncludeProperty
    @KivaKitFormatProperty(format = StringFormat.LOG_IDENTIFIER)
    public Service service()
    {
        return service;
    }

    public RegisterServiceResponse service(final Service service)
    {
        this.service = service;
        return this;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString(MULTILINE);
    }

    @Override
    protected void value(final Service value)
    {
        service = value;
    }

    @Override
    protected Service value()
    {
        return service;
    }
}
