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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.ftp.internal.lexakai.DiagramSecureFtp;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.value.count.Bytes.kilobytes;

/**
 * <b>Not public API</b>
 * <p>
 * FTP input stream
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSecureFtp.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
class SecureFtpInput extends InputStream
{
    /**
     * JSch does not do any buffering, so we need to do it ourselves. When the round-trip time gets around 300 ms, and
     * you're doing one round-trip for every read, it really matters that your buffer size is not just 8 KB, which is
     * the default for BufferedInputStream. This default should be good for most cases, just override this function if
     * it's not.
     *
     * @return The size of the buffer.
     */
    public static Bytes getBufferSize()
    {
        return kilobytes(512);
    }

    protected static SecureFtpInput secureFtpInput(SecureFtpConnector connector,
                                                   NetworkLocation networkLocation)
    {
        InputStream in = new BufferedInputStream(connector.get(networkLocation),
                (int) getBufferSize().asBytes());
        return new SecureFtpInput(connector, in);
    }

    @UmlAggregation(label = "connects with")
    private final SecureFtpConnector connector;

    private final InputStream in;

    private SecureFtpInput(SecureFtpConnector connector, InputStream in)
    {
        this.connector = connector;
        this.in = in;
    }

    @Override
    public void close()
    {
        // Close the wrapped input stream, and disconnect.
        IO.close((Listener) this, in);
        connector.disconnect();
    }

    @Override
    public int read() throws IOException
    {
        return in.read();
    }
}
