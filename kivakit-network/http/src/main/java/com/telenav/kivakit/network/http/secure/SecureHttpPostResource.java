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
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.http.HttpPostResource;
import com.telenav.kivakit.network.http.internal.lexakai.DiagramHttps;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.network.core.NetworkAccessConstraints.DEFAULT;

/**
 * A resource accessed by HTTPS POST at the given network location using the given access constraints.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramHttps.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class SecureHttpPostResource extends HttpPostResource
{
    public SecureHttpPostResource(NetworkLocation networkLocation, NetworkAccessConstraints constraints)
    {
        super(networkLocation, constraints);
        if (!(networkLocation instanceof SecureHttpNetworkLocation))
        {
            throw new IllegalArgumentException(
                    "Secure post request must use a secure network location:  " + networkLocation);
        }
    }

    public SecureHttpPostResource(SecureHttpNetworkLocation location)
    {
        super(location, DEFAULT);
    }
}
