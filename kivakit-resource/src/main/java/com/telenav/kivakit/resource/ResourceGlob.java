package com.telenav.kivakit.resource;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.interfaces.comparison.Matchable;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A resource "glob" is similar to a file glob in UNIX. It matches resources, according to one or more patterns, which
 * can be combined with logical operators.
 *
 * <p><b>Patterns</b></p>
 *
 * <p>
 * Glob patterns are case-sensitive Java regular expressions where just three symbols are re-defined:
 * </p>
 *
 * <pre>
 *     <b>**</b> - matches anything, including slashes
 *      <b>*</b> - matches anything, except a slash
 *      <b>.</b> - matches a literal dot</pre>
 *
 * <p>
 * For example, "**&#47;target/**.class" would match each of these resources:
 * </p>
 *
 * <ul>
 *     <li>my-project/target/MyApp.class</li>
 *     <li>my-project/my-sub-project/target/MyApp.class</li>
 *     <li>my-project/my-sub-project/target/com/me/MyApp.class</li>
 * </ul>
 *
 * <p><b>Creating Globs</b></p>
 *
 * <p>
 * {@link ResourceGlob}s can be created with the <i>match*()</i> static factory methods:
 * </p>
 *
 * <ul>
 *     <li>{@link #glob(Matcher)} - Creates a glob that matches resources with the given {@link Matcher}</li>
 *     <li>{@link #glob(Pattern)} - Creates a glob that matches resources with the given Java regular expression {@link Pattern}</li>
 *     <li>{@link #glob(String)} - Creates a glob that matches resources with the given <i>glob pattern</i> (as described above)</li>
 *     <li>{@link #globAll()} - Creates a glob that matches <i>all</i> resources</li>
 *     <li>{@link #globAllIn(ResourceFolder)} - Creates a glob that matches all resources directly in the given folder</li>
 *     <li>{@link #globAllUnder(ResourceFolder)} - Creates a glob that matches all resources in the given folder, and in all sub-folders, recursively</li>
 * </ul>
 *
 * <p><b>Logical Operations</b></p>
 *
 * <p>
 * Boolean logical operators are provided for combining globs:
 * </p>
 *
 * <ul>
 *     <li>{@link #in(ResourceFolder)} - Matches what this matches, but only if it's in the given {@link ResourceFolder}</li>
 *     <li>{@link #minus(Matchable)} - Matches what this glob matches, but without what the given {@link Matcher} matches</li>
 *     <li>{@link #not()} - Matches everything this does not match</li>
 *     <li>{@link #plus(Matchable)} - Matches what this matches, plus what the given {@link Matcher} matches</li>
 *     <li>{@link #select(Matchable)} - Matches what this <i>and</i>> the given {@link Matcher} both match</li>
 *     <li>{@link #under(ResourceFolder)} - Matches what this matches, but only if it's under the given {@link ResourceFolder}</li>
 * </ul>
 *
 * <p><b>Examples</b></p>
 *
 * <pre>
 * glob(Extension.JAVA)                         // Match .java resources
 * glob(JAVA)                                   // Match .java resources
 * glob("**.java")                              // Match .java resources
 * glob(JAVA).not()                             // Match all but .java resources
 * glob(JAVA).plus(MARKDOWN)                    // Match .java and .md resources
 * glob(JAVA).minus(it -> it.endsWith("Test"))  // Match .java resources except tests
 * glob("**&#47;target/**").select(CLASS)           // Match class files under target
 * glob(CLASS).under(target)                    // Match class files under target
 * globAllUnder(target).select(CLASS)           // Match class files under target
 * glob(JAVA).plus(target)                      // Match .java and target files</pre>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class ResourceGlob implements Matcher<ResourcePathed>
{
    /**
     * Returns a glob that matches resources with the {@link Matcher} provided by the given {@link Matchable}. For
     * example, the {@link Extension} class is {@link Matchable<ResourcePathed>}, which allows it to be passed to this
     * method to select resources:
     *
     * <pre>
     * match(Extension.JAVA)
     * match(file.extension())</pre>
     *
     * @param matchable The source of a matcher
     * @return The glob
     */
    public static ResourceGlob glob(@NotNull Matchable<ResourcePathed> matchable)
    {
        return glob(matchable::matches);
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
    public static ResourceGlob glob(@NotNull Pattern pattern)
    {
        return glob(it -> pattern.matcher(it.path().asString()).matches());
    }

    /**
     * Returns a glob that matches resources with the given pattern. The pattern is a regular expression, with these
     * differences applied:
     *
     * <pre>
     *  <b>**</b> - matches anything, including slashes
     *   <b>*</b> - matches anything, except a slash
     *   <b>.</b> - matches a literal dot</pre>
     *
     * <p>
     * Glob patterns are case-sensitive.
     * </p>
     *
     * @param glob The pattern to match
     * @return The glob
     */
    public static ResourceGlob glob(@NotNull String glob)
    {

        return glob(Pattern.compile(glob
                .replaceAll("\\*\\*", ".*")
                .replaceAll("\\*", "[^/]*")
                .replaceAll("\\.", "\\.")));
    }

    /**
     * Returns a glob for the given matcher
     *
     * @param matcher The matcher
     * @return The glob
     */
    public static ResourceGlob glob(@NotNull Matcher<ResourcePathed> matcher)
    {
        return new ResourceGlob(matcher);
    }

    /**
     * Returns a resource glob that matches all resources
     */
    public static ResourceGlob globAll()
    {
        return glob(it -> true);
    }

    /**
     * Returns a glob that matches all resources under the given folder
     *
     * @param folder The folder that resources must be under (recursively)
     * @return The glob
     */
    public static ResourceGlob globAllIn(@NotNull ResourceFolder<?> folder)
    {
        return glob(folder.matchAllPathsIn());
    }

    /**
     * Returns a glob that matches resources under the given folder
     *
     * @param folder The folder that resources must be under (recursively)
     * @return The glob
     */
    public static ResourceGlob globAllUnder(@NotNull ResourceFolder<?> folder)
    {
        return glob(folder.matchAllPathsUnder());
    }

    /** The matcher for this glob */
    private final Matcher<ResourcePathed> matcher;

    /**
     * <b>Not public API</b>
     */
    protected ResourceGlob(@NotNull Matcher<ResourcePathed> matcher)
    {
        this.matcher = matcher;
    }

    /**
     * Returns this glob, but with matches restricted to the given folder
     */
    public ResourceGlob in(@NotNull ResourceFolder<?> folder)
    {
        return select(folder.matchAllPathsIn());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(@NotNull ResourcePathed resource)
    {
        return matcher.matches(resource);
    }

    /**
     * Returns a glob that matches this glob minus whatever matches the given {@link Matchable}
     */
    public ResourceGlob minus(@NotNull Matchable<ResourcePathed> that)
    {
        return glob(it -> this.matches(it) && !that.matches(it));
    }

    /**
     * Returns a glob that matches everything this glob doesn't match
     */
    public ResourceGlob not()
    {
        return glob(it -> !matches(it));
    }

    /**
     * Returns a glob that matches this glob or the given {@link Matchable}
     */
    public ResourceGlob plus(@NotNull Matchable<ResourcePathed> that)
    {
        return glob(it -> this.matches(it) || that.matches(it));
    }

    /**
     * Returns a glob that matches only what this glob <i>and</i> the given {@link Matchable} both match
     */
    public ResourceGlob select(@NotNull Matchable<ResourcePathed> that)
    {
        return glob(it -> this.matches(it) && that.matches(it));
    }

    /**
     * Returns a glob that matches this glob, and all resources in or under the given {@link ResourceFolder}.
     */
    public ResourceGlob under(@NotNull ResourceFolder<?> folder)
    {
        return select(folder.matchAllPathsUnder());
    }
}
