package com.telenav.kivakit.interfaces.serialization;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Implemented by an object that is serialized by kivakit-serialization.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public interface SerializedObject
{
    /**
     * Called when an object has been deserialized
     */
    void onDeserialized();

    /**
     * Called when an object is about to be serialized
     */
    void onSerializing();
}
