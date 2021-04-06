open module kivakit.service.server
{
    requires transitive kivakit.service.client;

    requires transitive kivakit.web.jetty;
    requires transitive kivakit.web.wicket;
    requires transitive kivakit.web.jersey;
    requires transitive kivakit.web.swagger;

    requires transitive wicket.extensions;
    requires transitive wicket.jquery.ui;
    requires transitive wicket.jquery.ui.core;
    requires transitive wicket.util;

    requires org.danekja.jdk.serializable.functional;
    requires java.prefs;

    requires io.swagger.v3.oas.annotations;
}
