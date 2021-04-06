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

package com.telenav.kivakit.core.network.ftp.secure;

import com.telenav.kivakit.core.network.ftp.project.lexakai.diagrams.DiagramSecureFtp;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.kivakit.core.kernel.language.io.IO;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.network.core.NetworkLocation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@UmlClassDiagram(diagram = DiagramSecureFtp.class)
public class SecureFtpInput extends InputStream
{
    protected static SecureFtpInput forConnectorAndLocation(final SecureFtpConnector connector,
                                                            final NetworkLocation networkLocation)
    {
        final InputStream in = new BufferedInputStream(connector.get(networkLocation),
                (int) getBufferSize().asBytes());
        return new SecureFtpInput(connector, in);
    }

    /**
     * JSch does not do any buffering, so we need to do it ourselves. When the round-trip time gets around 300ms and
     * you're doing one round-trip for every read, it really matters that your buffer size is not just 8KB, which is the
     * default for BufferedInputStream. This default should be good for most cases, just override this function if it's
     * not.
     *
     * @return The size of the buffer.
     */
    public static Bytes getBufferSize()
    {
        return Bytes.kilobytes(512);
    }

    @UmlAggregation(label = "connects with")
    private final SecureFtpConnector connector;

    private final InputStream in;

    private SecureFtpInput(final SecureFtpConnector connector, final InputStream in)
    {
        this.connector = connector;
        this.in = in;
    }

    @Override
    public void close()
    {
        // Close the wrapped input stream, and disconnect.
        IO.close(in);
        connector.safeDisconnect();
    }

    @Override
    public int read() throws IOException
    {
        return in.read();
    }
}
