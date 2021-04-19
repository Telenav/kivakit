open module kivakit.service.client
{
    requires transitive kivakit.core.application;
    requires transitive kivakit.service.registry;
    requires transitive kivakit.core.network.http;

    requires transitive jersey.client;
    requires transitive jersey.media.json.jackson;
    requires transitive com.fasterxml.jackson.core;
    requires transitive java.ws.rs;

    exports com.telenav.kivakit.service.registry.client;
    exports com.telenav.kivakit.service.registry.client.project;
    exports com.telenav.kivakit.service.registry.client.project.lexakai.diagrams;
}
