////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.service.registry.project;

import com.telenav.kivakit.core.collections.project.CoreCollectionsProject;
import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;
import com.telenav.kivakit.core.serialization.core.SerializationSessionFactory;
import com.telenav.kivakit.core.serialization.kryo.CoreKernelKryoTypes;
import com.telenav.kivakit.core.serialization.kryo.KryoTypes;

import java.util.Set;

/**
 * @author jonathanl (shibo)
 */
public class ServiceRegistryProject extends Project
{
    private static final Lazy<ServiceRegistryProject> singleton = Lazy.of(ServiceRegistryProject::new);

    private static final KryoTypes KRYO_TYPES = new ServiceRegistryKryoTypes()
            .mergedWith(new CoreKernelKryoTypes());

    public static ServiceRegistryProject get()
    {
        return singleton.get();
    }

    private ServiceRegistryProject()
    {
    }

    @Override
    public Set<Project> dependencies()
    {
        return Set.of(CoreCollectionsProject.get());
    }

    public SerializationSessionFactory sessionFactory()
    {
        return KRYO_TYPES.sessionFactory();
    }
}
