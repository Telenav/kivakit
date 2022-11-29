package com.telenav.kivakit.resource.packages;

import com.google.common.reflect.ClassPath;
import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.language.packaging.PackageReference;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.object.Lazy;
import io.github.classgraph.ClassGraph;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.logging.LoggerFactory.newLogger;
import static com.telenav.kivakit.core.object.Lazy.lazy;
import static com.telenav.kivakit.core.vm.JavaVirtualMachine.javaVirtualMachine;
import static com.telenav.kivakit.resource.packages.ClasspathResource.classpathResource;
import static com.telenav.kivakit.resource.packages.ClasspathResourceFolder.classpathResourceFolder;
import static java.util.regex.Pattern.compile;

/**
 * A model of the classpath. The model is presently exposes only the non-class resources on the classpath, as found by
 * <a href = "https://github.com/classgraph/classgraph">classgraph</a>.
 *
 * <p><b>Resource Folders</b></p>
 *
 * <ul>
 *     <li>{@link #resourceFolders(Listener)} - All resource folders on the classpath</li>
 * </ul>
 *
 * <p><b>Resources</b></p>
 *
 * <ul>
 *     <li>{@link #resources(Listener)} - All resources on the classpath</li>
 *     <li>{@link #resources(Listener, PackageReference)} - All resources in the given package</li>
 *     <li>{@link #nestedResources(Listener, PackageReference)} - All resources under the given package</li>
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
    /** The single instance of this class */
    private static final Lazy<Classpath> classpath = lazy(Classpath::new);

    private static final Logger LOGGER = newLogger();

    /**
     * Get the singleton instance of this class
     */
    public synchronized static Classpath classpath()
    {
        return classpath.get();
    }

    /** All resource folders on the classpath */
    private final ObjectList<ClasspathResourceFolder> resourceFolders = new ObjectList<>();

    /** All resources on the classpath */
    private final ObjectList<ClasspathResource> resources = new ObjectList<>();

    private boolean scanned;

    /**
     * Returns the resources under the given package reference
     *
     * @param under The package to look under
     * @return The list of resources
     */
    public ObjectList<ClasspathResource> nestedResources(Listener listener, PackageReference under)
    {
        return resources(listener).matching(resource -> resource.packageReference().startsWith(under));
    }

    /**
     * Returns all resource folders on the classpath
     */
    public ObjectList<ClasspathResourceFolder> resourceFolders(Listener listener)
    {
        scan(listener);
        return resourceFolders;
    }

    /**
     * Returns a list of all resources in the given package
     *
     * @param in The package
     * @return The list of resources
     */
    public ObjectList<ClasspathResource> resources(Listener listener, PackageReference in)
    {
        return resources(listener).matching(resource -> resource.packageReference().equals(in));
    }

    /**
     * Returns the set of all non-class resources on the class path, as supplied by Guava's {@link ClassPath} facility.
     */
    public ObjectList<ClasspathResource> resources(Listener listener)
    {
        scan(listener);
        return resources;
    }

    private synchronized void scan(Listener listener)
    {
        if (!scanned)
        {
            try (var scan = new ClassGraph().scan(javaVirtualMachine().processors().asInt()))
            {
                scan.getResourcesMatchingPattern(compile("^.*(?<!\\.class)$")).forEach(at ->
                {
                    // Get the resource folder,
                    var folder = classpathResourceFolder(listener, at);
                    if (folder != null)
                    {
                        // and the resource inside it,
                        var resource = classpathResource(listener, at);
                        if (resource != null)
                        {
                            // set the parent folder of the resource,
                            resource.parent(folder);

                            // then add the resource to the list of resources,
                            resources.add(resource);

                            // and to the folder,
                            folder.add(resource);

                            // and finally add the folder to the list of folders.
                            resourceFolders.add(folder);
                        }
                    }
                });
            }
        }
        scanned = true;
    }
}
