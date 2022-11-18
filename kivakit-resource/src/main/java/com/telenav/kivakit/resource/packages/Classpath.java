package com.telenav.kivakit.resource.packages;

import com.google.common.reflect.ClassPath;
import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.language.packaging.PackageReference;
import com.telenav.kivakit.core.object.Lazy;
import io.github.classgraph.ClassGraph;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.object.Lazy.lazy;
import static com.telenav.kivakit.core.vm.JavaVirtualMachine.javaVirtualMachine;
import static com.telenav.kivakit.resource.packages.ClasspathResourceFolder.classpathResourceFolder;

/**
 * A model of the classpath. The model is presently exposes only the non-class resources on the classpath, as found by
 * <a href = "https://github.com/classgraph/classgraph">classgraph</a>.
 *
 * <p><b>Resource Folders</b></p>
 *
 * <ul>
 *     <li>{@link #resourceFolders()} - All resource folders on the classpath</li>
 * </ul>
 *
 * <p><b>Resources</b></p>
 *
 * <ul>
 *     <li>{@link #resources()} - All resources on the classpath</li>
 *     <li>{@link #resources(PackageReference)} - All resources in the given package</li>
 *     <li>{@link #nestedResources(PackageReference)} - All resources under the given package</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see ClasspathResourceFolder
 * @see ClasspathResource
 */
@SuppressWarnings("unused")
@CodeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Classpath
{
    /** All resource folders on the classpath */
    private final Map<URI, ClasspathResourceFolder> folders = new HashMap<>();

    /** All resources on the classpath */
    private final ObjectList<ClasspathResource> resources = new ObjectList<>();

    /** The single instance of this class */
    private static final Lazy<Classpath> classpath = lazy(Classpath::new);

    /**
     * Get the singleton instance of this class
     */
    public synchronized static Classpath classpath()
    {
        return classpath.get();
    }

    private Classpath()
    {
        try (var scan = new ClassGraph().scan(javaVirtualMachine().processors().asInt()))
        {
            scan.getAllResources().forEach(resource ->
            {
                var folderUri = resource.getClasspathElementURI();
                folders.put(folderUri, classpathResourceFolder(throwingListener(), folderUri));
            });
        }
    }

    /**
     * Returns all resource folders on the classpath
     */
    public ObjectList<ClasspathResourceFolder> resourceFolders()
    {
        return list(folders.values());
    }

    /**
     * Returns a list of all resources in the given package
     *
     * @param in The package
     * @return The list of resources
     */
    public ObjectList<ClasspathResource> resources(PackageReference in)
    {
        return resources().matching(resource -> resource.packageReference().equals(in));
    }

    /**
     * Returns the resources under the given package reference
     *
     * @param under The package to look under
     * @return The list of resources
     */
    public ObjectList<ClasspathResource> nestedResources(PackageReference under)
    {
        return resources().matching(resource -> resource.packageReference().startsWith(under));
    }

    /**
     * Returns the set of all non-class resources on the class path, as supplied by Guava's {@link ClassPath} facility.
     */
    public ObjectList<ClasspathResource> resources()
    {
        return resources;
    }
}
