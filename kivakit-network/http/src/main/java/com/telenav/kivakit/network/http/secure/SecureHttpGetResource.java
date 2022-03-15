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

import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.http.HttpGetResource;
import com.telenav.kivakit.network.http.lexakai.DiagramHttps;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.apache.http.impl.client.DefaultHttpClient;

import static com.telenav.kivakit.network.core.NetworkAccessConstraints.DEFAULT;

/**
 * A resource accessed by HTTPS GET at the given network location using the given access constraints.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("deprecation")
@UmlClassDiagram(diagram = DiagramHttps.class)
@LexakaiJavadoc(complete = true)
public class SecureHttpGetResource extends HttpGetResource
{
    private boolean ignoreInvalidCertificates;

    public SecureHttpGetResource(SecureHttpNetworkLocation location, NetworkAccessConstraints constraints)
    {
        super(location, constraints);
    }

    public SecureHttpGetResource(final SecureHttpNetworkLocation location)
    {
        super(location, DEFAULT);
    }

    public void ignoreInvalidCertificates(final boolean ignore)
    {
        ignoreInvalidCertificates = ignore;
    }

    @Override
    protected DefaultHttpClient newClient()
    {
        return ignoreInvalidCertificates ? new InvalidCertificateTrustingHttpClient() : new DefaultHttpClient();
    }
}
