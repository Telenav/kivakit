package com.telenav.kivakit.filesystems.hdfs.proxy;

import com.telenav.kivakit.core.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.core.resource.ResourceFolder;
import com.telenav.kivakit.filesystems.hdfs.proxy.converters.UserGroupInformationConverter;
import com.telenav.kivakit.filesystems.hdfs.proxy.project.lexakai.diagrams.DiagramHdfsProxy;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * <b>Not public API</b>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramHdfsProxy.class)
public class HdfsProxyServerSettings
{
    /** User information */
    private UserGroupInformation user;

    /** Container of HDFS site configuration resources */
    private ResourceFolder configurationFolder;

    public ResourceFolder configurationFolder()
    {
        return configurationFolder;
    }

    @KivaKitPropertyConverter(ResourceFolder.Converter.class)
    public HdfsProxyServerSettings configurationFolder(final ResourceFolder configuration)
    {
        configurationFolder = configuration;
        return this;
    }

    public UserGroupInformation user()
    {
        return user;
    }

    @KivaKitPropertyConverter(UserGroupInformationConverter.class)
    public HdfsProxyServerSettings user(final UserGroupInformation user)
    {
        this.user = user;
        return this;
    }
}
