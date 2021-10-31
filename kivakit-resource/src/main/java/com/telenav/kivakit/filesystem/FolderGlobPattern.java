package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.messaging.Listener;

import java.util.regex.Pattern;

/**
 * A matcher for extended UNIX "glob" patterns. A glob pattern is a regular expression with four differences:
 *
 * <ol>
 *     <li>**&#x2F; - Matches any number of folders ending with a slash, such as a/b/ or a/b/c/</li>
 *     <li>* - Matches anything (same as the regular expression ".*")</li>
 *     <li>. - Matches a literal dot (not the same as the regular expression ".", which matches any character)</li>
 *     <li>? - Matches any character (same as the regular expression ".")</li>
 * </ol>
 *
 * @author jonathanl (shibo)
 */
public class FolderGlobPattern implements Matcher<Folder>
{
    /**
     * @return A matcher for the given glob pattern
     */
    public static FolderGlobPattern parse(Listener listener, String pattern)
    {
        pattern = pattern.replaceAll("\\*\\*/", "(.*?/)*")
                .replaceAll("\\*", ".*")
                .replaceAll("\\.", "\\.")
                .replaceAll("\\?", ".");

        return new FolderGlobPattern(Pattern.compile(pattern));
    }

    private final Pattern pattern;

    protected FolderGlobPattern(Pattern pattern)
    {
        this.pattern = pattern;
    }

    @Override
    public boolean matches(Folder file)
    {
        return pattern.matcher(file.path().asString()).matches();
    }
}