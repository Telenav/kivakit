package

        com.telenav.kivakit.core.serialization.core.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class SerializationCoreProject extends Project
{
    private static final Lazy<SerializationCoreProject> singleton = Lazy.of(SerializationCoreProject::new);

    public static SerializationCoreProject get()
    {
        return singleton.get();
    }

    protected SerializationCoreProject()
    {
    }
}
