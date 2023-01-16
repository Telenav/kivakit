package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.resource.Extension;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;

/**
 * A map from {@link Extension} to {@link ObjectSerializer}. By default, serializers are available for
 * <i>.properties</i> and <i>.json</i> files.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             documentation = DOCUMENTED,
             testing = UNTESTED)
public class ObjectSerializerRegistry
{
    private final Map<Extension, ObjectSerializer> serializers = new LinkedHashMap<>();

    /**
     * Add an {@link ObjectSerializer}
     *
     * @param extension The extension that the serializer knows how to handle
     * @param serializer The object serializer
     */
    public ObjectSerializerRegistry add(@NotNull Extension extension,
                                        @NotNull ObjectSerializer serializer)
    {
        serializers.put(extension, serializer);
        return this;
    }

    /**
     * Returns the object serializer for the given file extension
     *
     * @param extension The file extension
     * @return The object serializer
     */
    public ObjectSerializer serializer(@NotNull Extension extension)
    {
        return serializers.get(extension);
    }

    /**
     * Returns a list of all known object serializers
     */
    public ObjectList<ObjectSerializer> serializers()
    {
        return list(serializers.values());
    }
}
