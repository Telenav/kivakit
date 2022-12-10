package com.telenav.kivakit.core;

import com.telenav.kivakit.core.project.Project;

/**
 * Base class for KivaKit projects
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public abstract class BaseKivaKitProject extends Project
{
    @Override
    protected Class<?> metadataType()
    {
        return KivaKit.class;
    }
}
