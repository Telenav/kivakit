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

package com.telenav.kivakit.service.registry.serialization;

import com.telenav.kivakit.core.serialization.jersey.json.JerseyGsonSerializer;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * Factory for Jersey GSON serializers
 *
 * @author jonathanl (shibo)
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@LexakaiJavadoc(complete = true)
public class ServiceRegistryJerseySerializer<T> extends JerseyGsonSerializer<T>
{
    public ServiceRegistryJerseySerializer()
    {
        super(new ServiceRegistryGsonFactory());
    }
}
