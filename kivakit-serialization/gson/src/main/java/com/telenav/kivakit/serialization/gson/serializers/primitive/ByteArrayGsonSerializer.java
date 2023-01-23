package com.telenav.kivakit.serialization.gson.serializers.primitive;

import com.telenav.kivakit.serialization.gson.serializers.BaseGsonSerializer;

import java.util.Base64;

/**
 * Serializes byte arrays in <a href="https://en.wikipedia.org/wiki/Base64">Base64</a>
 *
 * @author Jonathan Locke
 */
public class ByteArrayGsonSerializer extends BaseGsonSerializer<byte[], String>
{
    public ByteArrayGsonSerializer()
    {
        super(byte[].class, String.class);
    }

    @Override
    protected byte[] onDeserialize(String serialized)
    {
        return Base64.getDecoder().decode(serialized);
    }

    @Override
    protected String onSerialize(byte[] value)
    {
        return Base64.getEncoder().encodeToString(value);
    }
}
