////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
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
 * The project class for kivakit-service-registry. Includes a {@link SerializationSessionFactory} that can serialize
 * local and network service registries, retrieved with {@link #sessionFactory()}.
 *
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
