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

package com.telenav.kivakit.network.socket.server;

import com.telenav.kivakit.kernel.language.threading.KivaKitThread;
import com.telenav.kivakit.kernel.language.threading.Threads;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.network.socket.project.lexakai.diagrams.DiagramSocketServer;
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
