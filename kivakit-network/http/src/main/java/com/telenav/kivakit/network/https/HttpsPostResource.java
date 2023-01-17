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

package com.telenav.kivakit.network.https;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.http.HttpPostResource;
import com.telenav.kivakit.network.http.internal.lexakai.DiagramHttps;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.network.core.NetworkAccessConstraints.defaultNetworkAccessConstraints;

/**
 * A resource accessed by HTTPS POST at the given network location using the given access constraints.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramHttps.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class HttpsPostResource extends HttpPostResource
{
    public HttpsPostResource(NetworkLocation networkLocation, NetworkAccessConstraints constraints)
    {
        super(networkLocation, constraints);
        if (!(networkLocation instanceof HttpsNetworkLocation))
        {
            throw new IllegalArgumentException(
                    "Secure post request must use a secure network location:  " + networkLocation);
        }
    }

    public HttpsPostResource(HttpsNetworkLocation location)
    {
        super(location, defaultNetworkAccessConstraints());
    }
}
