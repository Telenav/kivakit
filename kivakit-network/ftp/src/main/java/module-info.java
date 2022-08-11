open module kivakit.network.ftp
{
    // KivaKit
    requires transitive kivakit.network.core;

    // Networking
    requires commons.net;
    requires jsch;

    // Module exports
    exports com.telenav.kivakit.network.ftp;
    exports com.telenav.kivakit.network.ftp.secure;
}
