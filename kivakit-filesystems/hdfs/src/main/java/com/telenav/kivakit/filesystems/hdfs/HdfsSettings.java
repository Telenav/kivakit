package com.telenav.kivakit.filesystems.hdfs;

import com.telenav.kivakit.core.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.core.network.core.EmailAddress;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.ResourceFolder;
import com.telenav.kivakit.filesystems.hdfs.project.lexakai.diagrams.DiagramHdfs;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

/**
 * <b>Not public API</b>
 * <p>
 *
 * @author jonathanl (shibo)
 */
@UmlNotPublicApi
@UmlClassDiagram(diagram = DiagramHdfs.class)
public class HdfsSettings
{
    /** Proxy executable JAR file */
    private Resource proxyJar;

    /** Contact email for proxy's RMI and data services */
    private EmailAddress contactEmail;

    /** User information */
    private String username;

    /** Container of HDFS site configuration resources */
    private ResourceFolder configurationFolder;

    public ResourceFolder configurationFolder()
    {
        return configurationFolder;
    }

    @KivaKitPropertyConverter(ResourceFolder.Converter.class)
    public void configurationFolder(final ResourceFolder configuration)
    {
        configurationFolder = configuration;
    }

    @KivaKitPropertyConverter(EmailAddress.Converter.class)
    public void contactEmail(final EmailAddress contactEmail)
    {
        this.contactEmail = contactEmail;
    }

    public EmailAddress contactEmail()
    {
        return contactEmail;
    }

    public Resource proxyJar()
    {
        return proxyJar;
    }

    @KivaKitPropertyConverter(Resource.Converter.class)
    public void proxyJar(final Resource proxyJar)
    {
        this.proxyJar = proxyJar;
    }

    public String username()
    {
        return username;
    }

    @KivaKitPropertyConverter
    public void username(final String username)
    {
        this.username = username;
    }
}
