open module kivakit.network.email
{
    // KivaKit
    requires transitive kivakit.configuration;
    requires transitive kivakit.network.core;

    // Java mail
    requires java.mail;
    requires java.activation;

    // Module exports
    exports com.telenav.kivakit.network.email;
    exports com.telenav.kivakit.network.email.senders;
    exports com.telenav.kivakit.network.email.project;
    exports com.telenav.kivakit.network.email.project.lexakai.diagrams;
    exports com.telenav.kivakit.network.email.converters;
}
