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

import com.telenav.kivakit.network.core.NetworkPath;
import com.telenav.kivakit.network.http.HttpGetResource;
import com.telenav.kivakit.network.http.HttpNetworkLocation;
import com.telenav.kivakit.network.http.project.lexakai.diagrams.DiagramHttps;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.network.core.Protocol.HTTPS;

/**
 * A secure network location using the HTTPS protocol.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramHttps.class)
@UmlRelation(label = "creates", referent = SecureHttpGetResource.class)
@UmlRelation(label = "creates", referent = SecureHttpPostResource.class)
@LexakaiJavadoc(complete = true)
public class SecureHttpNetworkLocation extends HttpNetworkLocation
{
    public SecureHttpNetworkLocation(final NetworkPath path)
    {
        super(path);
        ensure(HTTPS.equals(path.port().protocol()));
    }

    @Override
    public HttpGetResource get()
    {
        return new SecureHttpGetResource(this);
    }
}
