open module kivakit.network.ftp
{
    // KivaKit
    requires transitive kivakit.network.core;

    // Networking
    requires commons.net;
    requires jsch;
    requires j2ssh.core;

    // Module exports
    exports com.telenav.kivakit.network.ftp;
    exports com.telenav.kivakit.network.ftp.secure;
    exports com.telenav.kivakit.network.ftp.project.lexakai.diagrams;
}
