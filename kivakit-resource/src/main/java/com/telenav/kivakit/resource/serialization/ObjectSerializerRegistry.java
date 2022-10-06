package com.telenav.kivakit.resource.serialization;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.resource.Extension;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.collections.list.ObjectList.objectList;

/**
 * A map from {@link Extension} to {@link ObjectSerializer}. By default, serializers are available for
 * <i>.properties</i> and <i>.json</i> files.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             documentation = DOCUMENTATION_COMPLETE,
             testing = TESTING_NONE)
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
        return objectList(serializers.values());
    }
}
