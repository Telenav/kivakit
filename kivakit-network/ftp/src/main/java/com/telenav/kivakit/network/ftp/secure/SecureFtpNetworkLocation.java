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

package com.telenav.kivakit.network.ftp.secure;

import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.core.NetworkPath;
import com.telenav.kivakit.network.core.Protocol;
import com.telenav.kivakit.network.ftp.project.lexakai.diagrams.DiagramSecureFtp;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;

/**
 * A secure ftp (SFTP) network location.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSecureFtp.class)
@LexakaiJavadoc(complete = true)
public class SecureFtpNetworkLocation extends NetworkLocation
{
    public SecureFtpNetworkLocation(final NetworkPath path)
    {
        super(path);
        ensure(Protocol.SFTP.equals(path.port().protocol()));
    }

    @UmlRelation(label = "creates")
    public SecureFtpResource resource(final NetworkAccessConstraints constraints)
    {
        return new SecureFtpResource(this, constraints);
    }
}
