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

package com.telenav.kivakit.core.network.ftp;

import com.telenav.kivakit.core.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.core.network.core.NetworkLocation;
import com.telenav.kivakit.core.network.core.NetworkPath;
import com.telenav.kivakit.core.network.core.Protocol;
import com.telenav.kivakit.core.network.ftp.project.lexakai.diagrams.DiagramFtp;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

@UmlClassDiagram(diagram = DiagramFtp.class)
public class FtpNetworkLocation extends NetworkLocation
{
    @UmlClassDiagram(diagram = DiagramFtp.class)
    public enum Mode
    {
        Active,
        Passive
    }

    @UmlAggregation(label = "transfers in mode")
    private Mode mode;

    public FtpNetworkLocation(final NetworkPath path)
    {
        super(path);
        ensure(Protocol.FTP.equals(path.port().protocol()));
    }

    public Mode mode()
    {
        return mode;
    }

    public void mode(final Mode mode)
    {
        this.mode = mode;
    }

    @UmlRelation(label = "creates")
    public FtpResource resource(final NetworkAccessConstraints constraints)
    {
        return new FtpResource(this, constraints);
    }
}
