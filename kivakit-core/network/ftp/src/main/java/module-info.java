open module kivakit.core.network.ftp
{
    requires transitive kivakit.core.resource;
    requires transitive kivakit.core.network.core;

    requires transitive commons.net;
    requires transitive jsch;
    requires transitive j2ssh.core;

    exports com.telenav.kivakit.core.network.ftp;
    exports com.telenav.kivakit.core.network.ftp.secure;
}
