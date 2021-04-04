package

        com.telenav.kivakit.core.serialization.kryo.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class SerializationKryoProject extends Project
{
    private static final Lazy<SerializationKryoProject> singleton = Lazy.of(SerializationKryoProject::new);

    public static SerializationKryoProject get()
    {
        return singleton.get();
    }

    protected SerializationKryoProject()
    {
    }
}
