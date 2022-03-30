package com.telenav.kivakit.resource;

import com.telenav.kivakit.interfaces.comparison.Matchable;
import com.telenav.kivakit.interfaces.comparison.Matcher;

import java.util.regex.Pattern;

/**
 * A resource "glob" is similar to a file glob in UNIX. It matches resources by their paths, according to a pattern
 * created by calling various logical methods.
 *
 * <p><b>Examples</b></p>
 *
 * <pre>
 * // Match all Java files
 * match(Extension.JAVA)
 * match(JAVA)
 * match("**.java")
 *
 * // Match all but Java files
 * match(JAVA).not()
 *
 * // Match all Java and markdown files
 * match(JAVA).or(MARKDOWN)
 *
 * // Match all Java files except tests
 * match(JAVA).not(resource -> resource.endsWith("Test"))
 *
 * // Match all class files in all target folders
 * match("**&#47;target/**").and(CLASS)
 * match(CLASS).under(targetFolder)
 * matchUnder(targetFolder).and(CLASS)
 * </pre>
 *
 * @author jonathanl (shibo)
 */
public class ResourceGlob<T extends Resource> implements Matcher<T>
{
    /**
     * Returns a glob that matches resources with {@link Matcher} for the given {@link Matchable}. For example, the
     * {@link Extension} class is {@link Matchable<Resource>}, which allows it to be passed to this method to select
     * resources:
     *
     * <pre>
     * match(Extension.JAVA)
     * match(file.extension())
     * </pre>
     *
     * @param matchable The source of a matcher
     * @return The glob
     */
    public static <T extends Resource> ResourceGlob<T> match(Matchable<T> matchable)
    {
        return match(resource -> matchable.matcher().matches(resource));
    }

    /**
     * Returns a glob that matches resources with the given regular expression pattern. For example:
     *
     * <pre>
     * match(Pattern.compile("[a-z]+[0-9]*"))
     * </pre>
     *
     * @param pattern The pattern to match
     * @return The glob
     */
    public static <T extends Resource> ResourceGlob<T> match(Pattern pattern)
    {
        return match(resource -> pattern.matcher(resource.path().asString()).matches());
    }

    /**
     * Returns a glob that matches resources with the given pattern. The pattern is a regular expression, with these
     * differences applied:
     *
     * <ul>
     *     <li>** - matches anything, including slashes</li>
     *     <li>* - matches anything, except a slash</li>
     *     <li>. - matches a dot</li>
     * </ul>
     *
     * <p>
     * Glob patterns are case-sensitive.
     * </p>
     *
     * @param globPattern The pattern to match
     * @return The glob
     */
    public static <T extends Resource> ResourceGlob<T> match(String globPattern)
    {
        var pattern = globPattern
                .replaceAll("\\*\\*", ".*")
                .replaceAll("\\*", "[^/]*")
                .replaceAll("\\.", "\\.");

        return match(Pattern.compile(pattern));
    }

    /**
     * Returns a glob for the given resource matcher
     *
     * @param matcher The matcher
     * @return The glob
     */
    public static <T extends Resource> ResourceGlob<T> match(Matcher<T> matcher)
    {
        return new ResourceGlob<>(matcher);
    }

    /**
     * Returns a resource glob that will match anything
     */
    public static <T extends Resource> ResourceGlob<T> matchAll()
    {
        return match(resource -> true);
    }

    /**
     * Returns a glob that matches resources under the given folder
     *
     * @param folder The folder that resources must be under (recursively)
     * @return The glob
     */
    public static <T extends Resource> ResourceGlob<T> matchAllIn(ResourceFolder<?> folder)
    {
        return match(resource -> folder.equals(resource.parent()));
    }

    /**
     * Returns a glob that matches resources under the given folder
     *
     * @param folder The folder that resources must be under (recursively)
     * @return The glob
     */
    public static <T extends Resource> ResourceGlob<T> matchAllUnder(ResourceFolder<?> folder)
    {
        return match(folder::contains);
    }

    private final Matcher<T> matcher;

    public ResourceGlob(Matcher<T> matcher)
    {
        this.matcher = matcher;
    }

    /**
     * Returns a glob that requires that resources match both this glob and the given glob
     */
    public ResourceGlob<T> and(Matcher<T> that)
    {
        return match(resource -> matches(resource) && that.matches(resource));
    }

    /**
     * Returns a glob that requires that resources match both this glob and the given glob
     */
    public ResourceGlob<T> and(Matchable<T> that)
    {
        return and(that.matcher());
    }

    /**
     * Returns a glob that matches everything this glob does not
     */
    public ResourceGlob<T> in(ResourceFolder<?> folder)
    {
        return and(matchAllIn(folder));
    }

    @Override
    public boolean matches(T resource)
    {
        return matcher.matches(resource);
    }

    /**
     * Returns a glob that matches everything this glob does not
     */
    public ResourceGlob<T> not()
    {
        return match(resource -> !matches(resource));
    }

    /**
     * Returns a glob that matches everything this glob does not
     */
    public ResourceGlob<T> not(Matchable<T> that)
    {
        return not(that.matcher());
    }

    /**
     * Returns a glob that matches everything this glob does not
     */
    public ResourceGlob<T> not(Matcher<T> that)
    {
        return match(resource -> matches(resource) && !that.matches(resource));
    }

    /**
     * Returns a glob that matches this glob or the give glob
     */
    public ResourceGlob<T> or(Matcher<T> that)
    {
        return match(resource -> matches(resource) || that.matches(resource));
    }

    /**
     * Returns a glob that matches this glob or the give glob
     */
    public ResourceGlob<T> or(Matchable<T> that)
    {
        return or(that.matcher());
    }

    /**
     * Returns a glob that matches everything this glob does not
     */
    public ResourceGlob<T> under(ResourceFolder<?> folder)
    {
        return match(resource -> matches(resource) && folder.contains(resource));
    }
}
