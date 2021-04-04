open module kivakit.web.swagger
{
    requires transitive kivakit.core.resource;
    requires transitive kivakit.web.jersey;

    requires transitive io.swagger.v3.core;
    requires transitive io.swagger.v3.jaxrs2;
    requires transitive io.swagger.v3.oas.annotations;
    requires transitive io.swagger.v3.oas.models;
    requires transitive io.swagger.v3.oas.integration;

    requires transitive io.github.classgraph;
    requires transitive java.ws.rs;

    exports com.telenav.kivakit.web.swagger;
}
