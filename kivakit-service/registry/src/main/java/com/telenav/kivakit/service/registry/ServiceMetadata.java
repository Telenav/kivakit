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

package com.telenav.kivakit.service.registry;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.values.version.Version;
import com.telenav.kivakit.core.network.core.EmailAddress;
import com.telenav.kivakit.service.registry.project.lexakai.annotations.DiagramRegistry;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Metadata describing a {@link Service}.
 *
 * <p><b>Service Metadata</b></p>
 *
 * <p>
 * A service has the following metadata:
 * </p>
 *
 * <ul>
 *     <li>{@link #description()} - Description of the service</li>
 *     <li>{@link #version()} - The service version</li>
 *     <li>{@link #kivakitVersion()} - The KivaKit version that the service is running on</li>
 *     <li>{@link #contactEmail()} - An email to contact the service developer</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@Schema(description = "Metadata describing a service")
@UmlClassDiagram(diagram = DiagramRegistry.class)
public class ServiceMetadata
{
    @JsonProperty
    @Schema(description = "A description of the service",
            required = true)
    private String description;

    @JsonProperty
    @Schema(description = "The service version",
            required = true)
    @UmlAggregation(label = "service version")
    private Version version;

    @JsonProperty
    @Schema(description = "The version of the KivaKit that the service is running, if any")
    @UmlAggregation(label = "kivakit version")
    private Version kivakitVersion;

    @JsonProperty
    @Schema(description = "An email address to which concerns about the service can be directed",
            required = true)
    @UmlAggregation(label = "contact email")
    private EmailAddress contactEmail;

    public EmailAddress contactEmail()
    {
        return contactEmail;
    }

    public ServiceMetadata contactEmail(final EmailAddress contactEmail)
    {
        this.contactEmail = contactEmail;
        return this;
    }

    public ServiceMetadata description(final String description)
    {
        this.description = description;
        return this;
    }

    @KivaKitIncludeProperty
    public String description()
    {
        return description;
    }

    public ServiceMetadata kivakitVersion(final Version version)
    {
        kivakitVersion = version;
        return this;
    }

    public Version kivakitVersion()
    {
        return kivakitVersion;
    }

    public Version version()
    {
        return version;
    }

    public ServiceMetadata version(final Version version)
    {
        this.version = version;
        return this;
    }
}
