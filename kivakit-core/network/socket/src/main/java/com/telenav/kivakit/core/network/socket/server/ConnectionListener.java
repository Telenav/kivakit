////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.socket.server;

import com.telenav.kivakit.core.kernel.language.threading.KivaKitThread;
import com.telenav.kivakit.core.kernel.language.threading.Threads;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.network.socket.project.lexakai.diagrams.DiagramSocketServer;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * Listens for client connections on a given port. Each new connection is passed to the socket {@link Consumer} passed
 * to {@link #listen(Consumer)} on an {@link ExecutorService}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSocketServer.class)
public class ConnectionListener extends BaseRepeater
{
    private final int port;

    private final int retries;

    private final ExecutorService executor = Threads.threadPool("Listener");

    public ConnectionListener(final int port)
    {
        this(port, Maximum.MAXIMUM);
    }

    public ConnectionListener(final int port, final Maximum retries)
    {
        this.port = port;
        this.retries = retries.asInt();
    }

    public void listen(final Consumer<Socket> listener)
    {
        final var outer = this;
        KivaKitThread.run(this, "ConnectionListener", () ->
        {
            int bindFailures = 0;
            while (bindFailures < retries)
            {
                trace("Creating server socket on port $", port());
                try (final var serverSocket = new ServerSocket(port()))
                {
                    while (true)
                    {
                        try
                        {
                            announce("Waiting for connections");
                            final var socket = serverSocket.accept();
                            if (socket != null)
                            {
                                trace("Accepted connection on $", socket);
                                socket.setSoTimeout(Integer.MAX_VALUE);
                                socket.setKeepAlive(true);
                                outer.executor.execute(() -> listener.accept(socket));
                            }
                            else
                            {
                                warning("Connection failed");
                            }
                        }
                        catch (final Exception e)
                        {
                            warning(e, "Exception thrown while waiting for client connections");
                        }
                    }
                }
                catch (final Exception e)
                {
                    if (e instanceof BindException)
                    {
                        bindFailures++;
                        Duration.seconds(15).sleep();
                    }
                    warning(e, "Connection failed");
                }
                Duration.seconds(1).sleep();
            }
        });
    }

    private int port()
    {
        return port;
    }
}
