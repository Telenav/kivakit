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

package com.telenav.kivakit.serialization.core;

import com.telenav.kivakit.core.code.UncheckedVoidCode;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.progress.reporters.ProgressiveInputStream;
import com.telenav.kivakit.core.progress.reporters.ProgressiveOutputStream;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.core.version.Versioned;
import com.telenav.kivakit.core.version.VersionedObject;
import com.telenav.kivakit.interfaces.io.Flushable;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.serialization.ObjectSerializer;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.resource.writing.WritableResource;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;

import static com.telenav.kivakit.core.ensure.Ensure.ensureFalse;
import static com.telenav.kivakit.serialization.core.SerializationSession.SessionType.RESOURCE;

/**
 * A high-level abstraction for serialization. This interface allows the serialization of a sequence of {@link
 * SerializableObject}s using an {@link ObjectSerializer} during a bracketed session associated with an {@link
 * InputStream} or {@link OutputStream}. This design provides ease-of-use while ensuring that the stream is always
 * assigned a version and the input or output stream is managed correctly.
 *
 * <p><b>Creating a Session</b></p>
 *
 * <p>
 * The method {@link SerializationSessionFactory#newSession(Listener)} can be called to obtain a {@link
 * SerializationSession} instance. Providers of serialization will provide a {@link SerializationSessionFactory} that
 * produces <i>thread-safe</i> {@link SerializationSession}s. Alternatively a session can be created by constructing an
 * implementation instance.
 * </p>
 *
 * <p><b>Opening a Session</b></p>
 *
 * <p>
 * A serialization session is initiated by calling:
 *
 * <ul>
 *     <li>{@link #open(InputStream)}</li>
 *     <li>{@link #open(InputStream, SessionType)}</li>
 *     <li>{@link #open(OutputStream, Version)}</li>
 *     <li>{@link #open(OutputStream, SessionType, Version)}</li>
 *     <li>{@link #open(Socket, SessionType, Version, ProgressReporter)}</li>
 *     <li>{@link #open(InputStream, OutputStream, SessionType, Version)}</li>
 * </ul>
 * <p>
 * When a {@link SessionType#CLIENT} or {@link SessionType#SERVER} session is opened, handshaking and
 * exchange for version information will take place.
 * </p>
 *
 * <p><b>Reading and Writing</b></p>
 *
 * <p>
 * A session will remember any input or output stream that was given to it when the session was opened, so that
 * when read and write methods are called:
 *
 * <ul>
 *     <li>{@link #read()} - Read a {@link SerializableObject} from input</li>
 *     <li>{@link #read(Class)} - Read an object of the given type from input</li>
 *     <li>{@link #write(Object)} - Write an object to output</li>
 *     <li>{@link #write(SerializableObject)} - Write a {@link SerializableObject} to output</li>
 * </ul>
 *
 * <p>
 * objects will be read and written to the streams passed to open().
 * </p>
 *
 * <p><b>Example</b></p>
 *
 * <pre>
 * var session = new KryoSerializationSession();
 * var version = session.open(SessionType.RESOURCE, input, output);
 * session.write(new SerializedObject&lt;&gt;("hello"));
 * session.close();
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see SerializationSessionFactory
 * @see SerializableObject
 * @see Version
 */
public interface SerializationSession extends
        Named,
        Closeable,
        Flushable<Duration>,
        Versioned,
        Repeater,
        TryTrait
{
    /**
     * The type of serialization session. This determines the order of
     */
    enum SessionType
    {
        /** This session is interacting with a client socket */
        CLIENT,

        /** This session is interacting with a server socket */
        SERVER,

        /** This session is serializing to a resource */
        RESOURCE
    }

    @Override
    default void close()
    {
        onClose();
    }

    /**
     *
     */
    default boolean isActive()
    {
        return isReading() || isWriting();
    }

    /**
     * @return True if data is being read
     */
    boolean isReading();

    /**
     * @return True if data is being written
     */
    boolean isWriting();

    @Override
    default Duration maximumFlushTime()
    {
        return Duration.MAXIMUM;
    }

    /**
     * Ends a serialization session, flushing any pending output.
     */
    void onClose();

    /**
     * @return Opens the given socket for reading and writing. Version handshaking is performed automatically for {@link
     * SessionType#SERVER}s and {@link SessionType#CLIENT}s with the version of the connected endpoint returned to the
     * caller.
     */
    default Version open(Socket socket,
                         SessionType sessionType,
                         Version version,
                         ProgressReporter reporter)
    {
        try
        {
            trace("Opening socket");
            return open
                    (
                            new ProgressiveInputStream(socket.getInputStream(), reporter), new ProgressiveOutputStream(socket.getOutputStream(), reporter), sessionType,
                            version
                    );
        }
        catch (Exception e)
        {
            fatal(e, "Socket connection failed");
            return null;
        }
    }

    /**
     * Opens this session for reading
     *
     * @return The version or an exception is thrown
     */
    default Version open(InputStream input, SessionType sessionType)
    {
        return open(input, null, sessionType, null);
    }

    /**
     * Opens this session for reading from a resource
     *
     * @param input The input stream,
     * @return The version of the stream, or a runtime exception
     */
    default Version open(InputStream input)
    {
        return open(input, null, RESOURCE, null);
    }

    /**
     * Opens this session for writing
     */
    default void open(OutputStream output, SessionType sessionType, Version version)
    {
        open(null, output, sessionType, version);
    }

    /**
     * Opens this session for writing to a resource
     */
    default void open(OutputStream output, Version version)
    {
        open(output, RESOURCE, version);
    }

    /**
     * Opens this session for reading and/or writing. Retains the given input and output streams for future use, and
     * performs version any socket handshaking per the {@link SessionType} parameter.
     *
     * @return The resource, client or server version, or an exception
     */
    Version open(InputStream input, OutputStream output, SessionType sessionType, Version version);

    /**
     * @return A serializable object
     */
    <T> SerializableObject<T> read();

    /**
     * Reads an object of the given type from the input, discarding any version. If the object read from input is not of
     * the given type, an {@link IllegalStateException} will be thrown. This method can be used to read primitives, for
     * example: <i>read(Integer.class)</i>.
     *
     * @param type The type to read
     * @return The object
     */
    @SuppressWarnings("unchecked")
    default <T> T read(Class<T> type)
    {
        var object = read().object();
        if (type.isAssignableFrom(object.getClass()))
        {
            return (T) object;
        }
        return illegalState("Expected object of type $, not $", type, object.getClass());
    }

    /**
     * Reads a list of elements written by the {@link #writeList(Collection)} method
     *
     * @param type The element type
     * @return The list
     */
    default <Element> ObjectList<Element> readList(Class<Element> type)
    {
        var size = read(Integer.class);
        var list = new ObjectList<Element>();
        for (var i = 0; i < size; i++)
        {
            list.add(read(type));
        }
        return list;
    }

    /**
     * Runs the given code while the given resource is open for reading
     *
     * @param resource The resource to read from
     * @param code The code to run
     */
    default void readResource(Resource resource, UncheckedVoidCode code)
    {
        try (var input = resource.openForReading())
        {
            open(input, RESOURCE);
            tryCatchThrow(code, "Error while reading from: $");
            close();
        }
        catch (IOException e)
        {
            problem(e, "Auto-close failure");
        }
    }

    /**
     * Writes the given object to output without version information
     */
    default <T> void write(T object)
    {
        ensureFalse(object instanceof VersionedObject, "Use SerializableObject instead of VersionedObject");

        write(new SerializableObject<>(object));
    }

    /**
     * Writes the given {@link SerializableObject} to output
     */
    <T> void write(SerializableObject<T> object);

    /**
     * Writes the given collection of elements as a list
     *
     * @param list The list to write
     */
    default <Element> void writeList(Collection<Element> list)
    {
        write(list.size());
        for (var element : list)
        {
            write(element);
        }
    }

    /**
     * Runs the given code while the given resource is open for writing
     *
     * @param resource The resource to write to
     * @param version The output version
     * @param code The code to run
     */
    default void writeResource(WritableResource resource, Version version, UncheckedVoidCode code)
    {
        try (var output = resource.openForWriting())
        {
            open(output, RESOURCE, version);
            tryCatchThrow(code, "Error while writing to: $");
            close();
        }
        catch (IOException e)
        {
            problem(e, "Auto-close failure");
        }
    }
}
