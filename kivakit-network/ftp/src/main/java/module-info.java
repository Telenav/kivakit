open module kivakit.network.ftp
{
    requires transitive kivakit.resource;
    requires transitive kivakit.network.core;

    requires transitive commons.net;
    requires transitive jsch;
    requires transitive j2ssh.core;

    exports com.telenav.kivakit.network.ftp;
    exports com.telenav.kivakit.network.ftp.secure;
    exports com.telenav.kivakit.network.ftp.project;
    exports com.telenav.kivakit.network.ftp.project.lexakai.diagrams;
}
