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

package com.telenav.kivakit.filesystems.hdfs;

import com.telenav.kivakit.filesystems.hdfs.project.lexakai.diagrams.DiagramHdfs;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * <b>Not public API</b>
 * <p>
 * This class performs input and output for streams identified by *long* handles. The stream handle is written to a
 * socket on the {@link HdfsProxyClient} by {@link #in(long)} and {@link #out(long)}. This allows both the client and
 * server to associate socket input and output streams with the same handle.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlNotPublicApi
@UmlClassDiagram(diagram = DiagramHdfs.class)
@LexakaiJavadoc(complete = true)
public class HdfsProxyIO
{
    /**
     * @return The input stream to read from for the given handle
     */
    public static InputStream in(final long handle)
    {
        try
        {
            // We can ignore this resource leak warning because when the caller closes the input
            // stream, it will close this underlying socket
            @SuppressWarnings(
                    "resource") final Socket socket = new Socket("localhost", HdfsProxyClient.get().dataPort().number());
            final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            out.writeChar('i');
            out.writeLong(handle);
            out.flush();
            return socket.getInputStream();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return The output stream to write to for the given handle
     */
    public static OutputStream out(final long handle)
    {
        try
        {
            // We can ignore this resource leak warning because when the caller closes the input
            // stream, it will close this underlying socket
            @SuppressWarnings(
                    "resource") final Socket socket = new Socket("localhost", HdfsProxyClient.get().dataPort().number());
            final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            out.writeChar('o');
            out.writeLong(handle);
            out.flush();
            return socket.getOutputStream();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
