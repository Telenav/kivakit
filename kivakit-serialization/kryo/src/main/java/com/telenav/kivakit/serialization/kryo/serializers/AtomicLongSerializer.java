package com.telenav.kivakit.serialization.kryo.serializers;

import com.telenav.kivakit.serialization.kryo.BaseSerializer;
import com.telenav.kivakit.serialization.kryo.KryoSerializationSession;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("unused")
public class AtomicLongSerializer extends BaseSerializer<AtomicLong>
{
    public AtomicLongSerializer()
    {
        super(AtomicLong.class);
    }

    @Override
    protected AtomicLong onRead(KryoSerializationSession session)
    {
        return new AtomicLong(session.read(Integer.class));
    }

    @Override
    protected void onWrite(KryoSerializationSession session, @NotNull AtomicLong value)
    {
        session.write(value.get());
    }
}
