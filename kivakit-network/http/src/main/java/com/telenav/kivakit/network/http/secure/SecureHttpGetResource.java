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

package com.telenav.kivakit.network.http.secure;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.http.HttpGetResource;
import com.telenav.kivakit.network.http.internal.lexakai.DiagramHttps;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.network.core.NetworkAccessConstraints.DEFAULT;

/**
 * A resource accessed by HTTPS GET at the given network location using the given access constraints.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramHttps.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class SecureHttpGetResource extends HttpGetResource
{
    public SecureHttpGetResource(SecureHttpNetworkLocation location, NetworkAccessConstraints constraints)
    {
        super(location, constraints);
    }

    public SecureHttpGetResource(SecureHttpNetworkLocation location)
    {
        super(location, DEFAULT);
    }
}
