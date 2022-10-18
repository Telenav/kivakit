package com.telenav.kivakit.interfaces.serialization;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Implemented by an object that is serialized by kivakit-serialization.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
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
