open module kivakit.network.email
{
    requires transitive kivakit.configuration;
    requires transitive kivakit.network.core;

    requires java.mail;
    requires jakarta.activation;

    exports com.telenav.kivakit.network.email;
    exports com.telenav.kivakit.network.email.senders;
    exports com.telenav.kivakit.network.email.project;
    exports com.telenav.kivakit.network.email.project.lexakai.diagrams;
    exports com.telenav.kivakit.network.email.converters;
}
