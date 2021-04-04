package

        com.telenav.kivakit.core.serialization.core;

import com.telenav.kivakit.core.kernel.interfaces.factory.Factory;
import com.telenav.kivakit.core.kernel.messaging.Listener;

/**
 * Creates new thread-local instances of {@link SerializationSession} using the {@link Factory} passed to the
 * constructor.
 *
 * @author jonathanl (shibo)
 */
public class SerializationSessionFactory
{
    /** Factory that produces serialization objects */
    private final Factory<SerializationSession> factory;

    /**
     * @param factory The factory for creating {@link SerializationSession} objects
     */
    public SerializationSessionFactory(final Factory<SerializationSession> factory)
    {
        this.factory = factory;
    }

    /**
     * @return A thread-local {@link SerializationSession} object with only the given listener
     */
    public SerializationSession session(final Listener listener)
    {
        return listener.listenTo(factory.newInstance());
    }
}
