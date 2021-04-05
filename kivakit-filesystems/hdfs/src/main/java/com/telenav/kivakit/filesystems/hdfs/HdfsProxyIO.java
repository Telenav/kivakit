package com.telenav.kivakit.filesystems.hdfs;

import com.telenav.kivakit.filesystems.hdfs.project.lexakai.diagrams.DiagramHdfs;
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
 *
 * @author jonathanl (shibo)
 */
@UmlNotPublicApi
@UmlClassDiagram(diagram = DiagramHdfs.class)
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
