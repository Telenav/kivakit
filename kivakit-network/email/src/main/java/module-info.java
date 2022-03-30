open module kivakit.network.email
{
    // KivaKit
    requires transitive kivakit.network.core;

    // Java mail
    requires java.mail;
    requires java.activation;

    // Module exports
    exports com.telenav.kivakit.network.email;
    exports com.telenav.kivakit.network.email.senders;
    exports com.telenav.kivakit.network.email.lexakai;
    exports com.telenav.kivakit.network.email.converters;
}
