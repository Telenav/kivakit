package com.telenav.kivakit.resource.resources;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceService;
import com.telenav.kivakit.resource.spi.ResourceResolver;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static java.lang.Long.parseLong;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Resolves {@link ResourceIdentifier}s of the form "section:[start-offset]:[end-offset]:[resource-identifier]". For
 * example "section:128:256:classpath:/resource.txt" would identify bytes 128 to 256, exclusive, of the resource.txt
 * file at the root of the classpath.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@TypeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class ResourceSectionResolver implements ResourceResolver
{
    public static final String SCHEME = "section:";

    /** section:[start-offset]:[end-offset]:[resource-identifier] */
    private final Pattern PATTERN = Pattern.compile(SCHEME + "(<?start-offset>\\d+):(<?end-offset>\\d+):(<?<resource-identifier>.*)", CASE_INSENSITIVE);

    @Override
    public boolean accepts(@NotNull ResourceIdentifier identifier)
    {
        return identifier.identifier().startsWith(SCHEME);
    }

    @Override
    public Resource resolve(@NotNull ResourceIdentifier identifier)
    {
        var matcher = PATTERN.matcher(identifier.identifier());
        if (matcher.matches())
        {
            var startOffset = parseLong(matcher.group("start-offset"));
            var endOffset = parseLong(matcher.group("end-offset"));
            var within = matcher.group("resource-identifier");
            var resource = new ResourceIdentifier(within).resolve(this);

            return new ResourceSection(resource, startOffset, endOffset);
        }
        problem("Could not resolve resource: $", identifier);
        return null;
    }
}
