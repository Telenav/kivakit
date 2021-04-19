open module kivakit.service.registry
{
    requires transitive kivakit.core.application;
    requires transitive kivakit.core.network.core;
    requires transitive kivakit.core.serialization.jersey.json;

    requires transitive com.fasterxml.jackson.annotation;

    exports com.telenav.kivakit.service.registry;
    exports com.telenav.kivakit.service.registry.project;
    exports com.telenav.kivakit.service.registry.project.lexakai.diagrams;
    exports com.telenav.kivakit.service.registry.protocol;
    exports com.telenav.kivakit.service.registry.registries;
    exports com.telenav.kivakit.service.registry.store;
    exports com.telenav.kivakit.service.registry.protocol.discover;
    exports com.telenav.kivakit.service.registry.protocol.update;
    exports com.telenav.kivakit.service.registry.protocol.register;
    exports com.telenav.kivakit.service.registry.protocol.renew;
    exports com.telenav.kivakit.service.registry.serialization;
}
