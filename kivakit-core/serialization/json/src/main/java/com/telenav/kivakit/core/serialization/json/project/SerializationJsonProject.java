package

        com.telenav.kivakit.core.serialization.json.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class SerializationJsonProject extends Project
{
    private static final Lazy<SerializationJsonProject> singleton = Lazy.of(SerializationJsonProject::new);

    public static SerializationJsonProject get()
    {
        return singleton.get();
    }

    protected SerializationJsonProject()
    {
    }
}
