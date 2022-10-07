package com.telenav.kivakit.annotations.code;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_FURTHER_EVALUATION_NEEDED;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * The level of stability for a class, as evaluated by a developer. This is different from a measure of past source code
 * change because it is future-looking. It is based on the <i>anticipated</i> level of <i>incompatible</i> change in the
 * future.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = CODE_FURTHER_EVALUATION_NEEDED,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE,
             reviews = 1,
             reviewers = "shibo")
public enum CodeStability
{
    /** The code is not expected to change */
    CODE_STABLE,

    /** The code is not expected to change, except that new methods may be added */
    CODE_STABLE_EXTENSIBLE,

    /** The code may be changed */
    CODE_UNSTABLE,

    /** Requires more evaluation */
    CODE_FURTHER_EVALUATION_NEEDED,

    /** The code has not been evaluated for stability */
    CODE_UNEVALUATED;

    public boolean isStable()
    {
        return this == CODE_STABLE || this == CODE_STABLE_EXTENSIBLE;
    }

    public boolean isUnstable()
    {
        return this == CODE_UNSTABLE;
    }
}
