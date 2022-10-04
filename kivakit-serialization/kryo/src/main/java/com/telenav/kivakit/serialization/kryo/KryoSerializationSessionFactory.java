package com.telenav.kivakit.serialization.kryo;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.serialization.core.SerializationSessionFactory;
import com.telenav.kivakit.serialization.kryo.types.KryoTypes;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A factory that creates {@link KryoSerializationSession}s. Kryo sessions are thread-local, and therefore thread-safe.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class KryoSerializationSessionFactory implements SerializationSessionFactory
{
    /** Kryo type registration information */
    private KryoTypes types;

    /** Thread-local sessions */
    private final ThreadLocal<KryoSerializationSession> threadSession =
            ThreadLocal.withInitial(() -> new KryoSerializationSession(types));

    public KryoSerializationSessionFactory(KryoTypes types)
    {
        this.types = types;
    }

    @Override
    public KryoSerializationSession newSession(@NotNull Listener listener)
    {
        var session = threadSession.get();
        session.clearListeners();
        listener.listenTo(session);
        return session;
    }
}
