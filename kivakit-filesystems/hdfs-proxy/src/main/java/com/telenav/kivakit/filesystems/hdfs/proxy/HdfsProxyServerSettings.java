package com.telenav.kivakit.filesystems.hdfs.proxy;

import com.telenav.kivakit.filesystems.hdfs.proxy.project.lexakai.diagrams.DiagramHdfsProxy;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramHdfsProxy.class)
public class HdfsProxyServerSettings
{
    /** HDFS user for access */
    static final UserGroupInformation HDFS_USER = UserGroupInformation.createRemoteUser("automation");
}
