open module kivakit.core.network.email
{
    requires transitive kivakit.core.configuration;
    requires transitive kivakit.core.network.core;

    requires java.mail;
    requires jakarta.activation;

    exports com.telenav.kivakit.core.network.email;
    exports com.telenav.kivakit.core.network.email.senders;
    exports com.telenav.kivakit.core.network.email.project;
    exports com.telenav.kivakit.core.network.email.project.lexakai.diagrams;
    exports com.telenav.kivakit.core.network.email.converters;
}
