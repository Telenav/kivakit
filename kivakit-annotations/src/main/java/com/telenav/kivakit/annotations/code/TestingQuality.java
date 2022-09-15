package com.telenav.kivakit.annotations.code;

/**
 * The quality of tests for a given class, as evaluted by a developer. This is different from a simple code coverage
 * metric because there are many methods and even whole classes that do not need full testing.
 */
public enum TestingQuality
{
    SUFFICIENT,
    INSUFFICIENT,
    UNNECESSARY,
    UNEVALUATED
}
