package com.telenav.kivakit.resource;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.interfaces.comparison.Matcher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.telenav.kivakit.core.collections.set.ObjectSet.objectSet;

@SuppressWarnings("unused")
public abstract class BaseResourceList<T extends Resource> extends ObjectList<T>
{
    /**
     * Adds the given resource to this list at the given index
     */
    @Override
    public void add(int index, T resource)
    {
        if (accepts(resource.fileName()))
        {
            add(index, resource);
        }
    }

    /**
     * Adds the given resource to this list
     */
    @Override
    public boolean add(T resource)
    {
        if (accepts(resource.fileName()))
        {
            return super.add(resource);
        }
        return false;
    }

    /**
     * Returns this list as a set
     */
    @Override
    public ObjectSet<T> asSet()
    {
        return objectSet(this);
    }

    /**
     * Returns the number of resources in this list
     */
    @Override
    public Count count()
    {
        return Count.count(size());
    }

    /**
     * Returns a message digest of this resource list's resource metadata, including the absolute path to the resource,
     * its creation and last modification times, and its size in bytes.
     *
     * @return The message digest
     */
    public byte[] digest()
    {
        MessageDigest digester;
        try
        {
            digester = MessageDigest.getInstance("MD5");
            var builder = new StringBuilder();
            for (var resource : this)
            {
                builder.append("[")
                        .append(resource.path().asAbsolute())
                        .append(":")
                        .append(resource.createdAt())
                        .append(":")
                        .append(resource.lastModified())
                        .append(":")
                        .append(resource.sizeInBytes())
                        .append("]");
            }
            return digester.digest(builder.toString().getBytes());
        }
        catch (NoSuchAlgorithmException ignored)
        {
            // Not possible
        }
        return null;
    }

    /**
     * Returns the largest resource in this list
     */
    public Resource largest()
    {
        Resource largest = null;
        for (var resource : this)
        {
            if (largest == null || resource.isLargerThan(largest))
            {
                largest = resource;
            }
        }
        return largest;
    }

    /**
     * Returns the resources in this list that match the given extension
     */
    public BaseResourceList<T> matching(Extension extension)
    {
        return matching(resource ->
        {
            var fileExtension = resource.compoundExtension();
            return fileExtension != null && fileExtension.endsWith(extension);
        });
    }

    /**
     * Returns the resources in this list that match the given matcher
     */
    @Override
    public BaseResourceList<T> matching(Matcher<T> matcher)
    {
        var matches = newResourceList();
        for (var resource : this)
        {
            if (matcher.matches(resource))
            {
                matches.add(resource);
            }
        }
        return matches;
    }

    /**
     * Returns this resource list relative to the given folder
     */
    public BaseResourceList<T> relativeTo(ResourceFolder<?> folder)
    {
        var resources = newResourceList();
        for (var resource : this)
        {
            var path = resource.path().relativeTo(folder.path());
            resources.addIfNotNull(newResource(path));
        }
        return resources;
    }

    @Override
    public T set(int index, T resource)
    {
        if (accepts(resource.fileName()))
        {
            return set(index, resource);
        }
        return null;
    }

    /**
     * Returns the smallest resource in this list
     */
    public Resource smallest()
    {
        Resource smallest = null;
        for (var resource : this)
        {
            if (smallest == null || resource.isSmallerThan(smallest))
            {
                smallest = resource;
            }
        }
        return smallest;
    }

    /**
     * Returns a list of resources sorted from largest to smallest
     */
    public BaseResourceList<T> sortedLargestToSmallest()
    {
        var sorted = newResourceList();
        sorted.sort((a, b) ->
        {
            if (a.isLargerThan(b))
            {
                return -1;
            }
            if (b.isLargerThan(a))
            {
                return 1;
            }
            return 0;
        });
        return sorted;
    }

    /**
     * Returns a list of resources sorted from oldest to newest
     */
    @SuppressWarnings("UnusedReturnValue")
    public BaseResourceList<T> sortedOldestToNewest()
    {
        var sorted = newResourceList();
        sorted.sort((a, b) ->
        {
            if (a.isOlderThan(b))
            {
                return -1;
            }
            if (b.isOlderThan(a))
            {
                return 1;
            }
            return 0;
        });
        return sorted;
    }

    /**
     * Returns the total size of all resources in this list
     */
    public Bytes totalSize()
    {
        var bytes = Bytes._0;
        for (var resource : this)
        {
            bytes = bytes.plus(resource.sizeInBytes());
        }
        return bytes;
    }

    protected boolean accepts(FileName name)
    {
        return true;
    }

    /**
     * Creates a new resource with the given path
     *
     * @param path The path
     * @return The resource
     */
    protected abstract T newResource(ResourcePath path);

    /**
     * Creates a new resource list of the subclass type
     */
    protected abstract BaseResourceList<T> newResourceList();
}
