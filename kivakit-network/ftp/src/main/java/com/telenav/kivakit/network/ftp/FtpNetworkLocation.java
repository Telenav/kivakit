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

package com.telenav.kivakit.network.ftp;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.core.NetworkPath;
import com.telenav.kivakit.network.ftp.internal.lexakai.DiagramFtp;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.network.core.Protocol.FTP;

/**
 * An FTP network location that can be accessed in {@link Mode#PASSIVE} or {@link Mode#ACTIVE}.
 *
 * @author jonathanl (shibo)
 * @see <a href="https://en.wikipedia.org/wiki/File_Transfer_Protocol">FTP documentation</a>
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramFtp.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class FtpNetworkLocation extends NetworkLocation
{
    /**
     * The FTP access mode, either passive or active. See <a
     * href="https://en.wikipedia.org/wiki/File_Transfer_Protocol">FTP documentation</a> for details.
     *
     * @author jonathanl (shibo)
     * @see <a href="https://en.wikipedia.org/wiki/File_Transfer_Protocol">FTP documentation</a>
     */
    @UmlClassDiagram(diagram = DiagramFtp.class)
    @TypeQuality(stability = STABLE_EXTENSIBLE,
                 testing = UNTESTED,
                 documentation = DOCUMENTED)
    public enum Mode
    {
        ACTIVE,
        PASSIVE
    }

    @UmlAggregation(label = "transfers in mode")
    private Mode mode;

    public FtpNetworkLocation(NetworkPath path)
    {
        super(path);
        ensure(FTP.equals(path.port().protocol()));
    }

    public Mode mode()
    {
        return mode;
    }

    public void mode(Mode mode)
    {
        this.mode = mode;
    }

    @UmlRelation(label = "creates")
    public FtpResource resource(NetworkAccessConstraints constraints)
    {
        return new FtpResource(this, constraints);
    }
}
