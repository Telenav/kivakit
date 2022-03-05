package com.telenav.kivakit.core.project;

import com.telenav.kivakit.core.KivaKit;
import com.telenav.kivakit.core.version.Version;

public interface ProjectTrait
{
    default Version kivakitVersion()
    {
        return KivaKit.get().kivakitVersion();
    }

    default Build projectBuild()
    {
        return Build.build(getClass());
    }

    default Version projectVersion()
    {
        return KivaKit.get().projectVersion();
    }
}
