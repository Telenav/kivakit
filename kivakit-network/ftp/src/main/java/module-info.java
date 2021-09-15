open module kivakit.network.ftp
{
    // KivaKit
    requires transitive kivakit.network.core;

    // Networking
    requires transitive commons.net;
    requires transitive jsch;
    requires transitive j2ssh.core;

    // Module exports
    exports com.telenav.kivakit.network.ftp;
    exports com.telenav.kivakit.network.ftp.secure;
    exports com.telenav.kivakit.network.ftp.project.lexakai.diagrams;
}
