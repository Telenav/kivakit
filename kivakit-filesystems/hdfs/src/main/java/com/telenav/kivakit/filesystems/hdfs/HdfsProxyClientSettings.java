package com.telenav.kivakit.filesystems.hdfs;

import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.core.network.core.EmailAddress;
import com.telenav.kivakit.core.network.core.Host;
import com.telenav.kivakit.core.network.http.HttpNetworkLocation;
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
public class HdfsProxyClientSettings
{
    /** Contact email for proxy's RMI and data services */
    static final EmailAddress CONTACT_EMAIL = EmailAddress.parse("jonathanl@telenav.com");

    /**
     * NOTE: When the HDFS proxy is updated, the following needs to occur:
     * <ul>
     *     <li>1. Build the KIVAKIT without tests (kivakit-build.sh all clean no-tests)</li>
     *     <li>2. Copy the resulting proxy jar to S3 (usually along with all the other relevant tool jars):
     *     <ul>
     *         <li><b>from:</b> hdfs-proxy/target/kivakit-hdfs-proxy-[VERSION].jar</li>
     *         <li><b>to:</b> s3://telenav-tdk.mypna.com/[VERSION]/bin</li>
     *     </ul>
     *     <li>3. Rebuild the KIVAKIT with tests enabled (kivakit-build.sh all clean)</li>
     * </ul>
     */
    static final HttpNetworkLocation HDFS_PROXY_JAR = new HttpNetworkLocation(
            new Host("telenav-tdk.mypna.com")
                    .http()
                    .path
                            ("/"
                                    + KivaKit.get().version()
                                    + "/bin/kivakit-hdfs-proxy-"
                                    + KivaKit.get().version()
                                    + ".jar"
                            ));
}
