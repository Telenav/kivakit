package com.telenav.kivakit.serialization.kryo.serializers;

import com.telenav.kivakit.serialization.kryo.BaseSerializer;
import com.telenav.kivakit.serialization.kryo.KryoSerializationSession;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unused")
public class AtomicIntegerSerializer extends BaseSerializer<AtomicInteger>
{
    public AtomicIntegerSerializer()
    {
        super(AtomicInteger.class);
    }

    @Override
    protected AtomicInteger onRead(KryoSerializationSession session)
    {
        return new AtomicInteger(session.read(Integer.class));
    }

    @Override
    protected void onWrite(KryoSerializationSession session, @NotNull AtomicInteger value)
    {
        session.write(value.get());
    }
}
