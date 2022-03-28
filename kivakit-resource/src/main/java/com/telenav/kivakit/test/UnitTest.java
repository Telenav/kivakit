package com.telenav.kivakit.test;

import com.telenav.kivakit.core.language.trait.LanguageTrait;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.project.ProjectTrait;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.test.CoreUnitTest;
import com.telenav.kivakit.core.vm.JavaTrait;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.kivakit.resource.packages.PackageTrait;

public class UnitTest extends CoreUnitTest implements
        JavaTrait,
        ProjectTrait,
        PackageTrait,
        RegistryTrait,
        LanguageTrait,
        Repeater,
        NamedObject
{
}
