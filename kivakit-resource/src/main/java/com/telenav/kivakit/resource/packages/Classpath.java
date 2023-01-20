package com.telenav.kivakit.resource.packages;

import com.google.common.reflect.ClassPath;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.language.packaging.PackageReference;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.object.Lazy;
import io.github.classgraph.ClassGraph;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
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
 *     <li>{@link #allResourceFolders(Listener)} - All resource folders on the classpath</li>
 * </ul>
 *
 * <p><b>Resources</b></p>
 *
 * <ul>
 *     <li>{@link #allResources(Listener)} - All resources on the classpath</li>
 *     <li>{@link #resourcesIn(Listener, PackageReference)} - All resources in the given package</li>
 *     <li>{@link #nestedResources(Listener, PackageReference)} - All resources under the given package</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see ClasspathResourceFolder
 * @see ClasspathResource
 */
@SuppressWarnings("unused")
@TypeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class Classpath
{
    /** The single instance of this class */
    private static final Lazy<Classpath> classpath = lazy(Classpath::new);

    /**
     * Get the singleton instance of this class
     */
    public synchronized static Classpath classpath()
    {
        return classpath.get();
    }

    /** All resource folders on the classpath */
    private final ObjectSet<ClasspathResourceFolder> resourceFolders = new ObjectSet<>();

    /** All resources on the classpath */
    private final ObjectList<ClasspathResource> resources = new ObjectList<>();

    private boolean scanned;

    /**
     * Returns all resource folders on the classpath
     */
    public synchronized ObjectList<ClasspathResourceFolder> allResourceFolders(Listener listener)
    {
        scan(listener);
        return resourceFolders.asList();
    }

    /**
     * Returns the set of all non-class resources on the class path, as supplied by Guava's {@link ClassPath} facility.
     */
    public synchronized ObjectList<ClasspathResource> allResources(Listener listener)
    {
        scan(listener);
        return resources;
    }

    /**
     * Returns all resource folders on the classpath
     */
    public synchronized ObjectList<ClasspathResourceFolder> nestedResourceFolders(Listener listener,
                                                                                  PackageReference under)
    {
        scan(listener);
        return resourceFolders.asList().matching(folder -> folder.packageReference().startsWith(under));
    }

    /**
     * Returns the resources under the given package reference
     *
     * @param under The package to look under
     * @return The list of resources
     */
    public synchronized ObjectList<ClasspathResource> nestedResources(Listener listener, PackageReference under)
    {
        return allResources(listener).matching(resource -> resource.packageReference().startsWith(under));
    }

    /**
     * Returns a list of all resources in the given package
     *
     * @param in The package
     * @return The list of resources
     */
    public synchronized ObjectList<ClasspathResourceFolder> resourceFoldersIn(Listener listener, PackageReference in)
    {
        return allResourceFolders(listener).matching(resource -> resource.packageReference().startsWith(in)
            && resource.packageReference().size() == in.size() + 1);
    }

    /**
     * Returns a list of all resources in the given package
     *
     * @param in The package
     * @return The list of resources
     */
    public synchronized ObjectList<ClasspathResource> resourcesIn(Listener listener, PackageReference in)
    {
        return allResources(listener).matching(resource -> resource.packageReference().equals(in));
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
