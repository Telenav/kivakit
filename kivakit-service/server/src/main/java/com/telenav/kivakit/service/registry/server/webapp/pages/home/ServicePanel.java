package com.telenav.kivakit.service.registry.server.webapp.pages.home;

import com.telenav.kivakit.core.kernel.language.collections.map.count.CountMap;
import com.telenav.kivakit.service.registry.Service;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import static java.lang.Thread.State.BLOCKED;
import static java.lang.Thread.State.RUNNABLE;
import static java.lang.Thread.State.TIMED_WAITING;
import static java.lang.Thread.State.WAITING;

public class ServicePanel extends Panel
{
    private final IModel<Service> model;

    public ServicePanel(final String id, final WebMarkupContainer container, final IModel<Service> model)
    {
        super(id);
        this.model = model;

        final var service = service();
        final var metadata = service.metadata();

        final var application = new Label("application", service::application);
        final var type = new Label("type", service::type);

        final var header = new WebMarkupContainer("header");
        header.add(new Image("status-light", () -> new PackageResourceReference(getClass(), service.isStale()
                ? "icons/red-light-24.png"
                : "icons/green-light-24.png")));
        header.add(application);
        header.add(type);
        add(header);

        final var health = service.health();

        add(new Label("scope", () -> service.scope().name()));
        add(new Label("host", () -> service.port().host().toString()));
        add(new Label("port", () -> service.port().number()));
        add(new Label("description", metadata::description));
        add(new Label("renewed", () -> service.renewedAt().elapsedSince()));
        add(new Label("up-time", health::upTime));
        add(new Label("cpu-time", () -> health.elapsedCpuTime().toString()));
        add(new Label("cpu-use", () -> (int) health.cpuUse()));
        add(new Label("memory-used", health::usedMemory));
        add(new Label("memory-maximum", health::maximumMemory));
        add(new Label("memory-use", () -> (int) health.memoryUse()));
        add(new Label("kivakit-version", () -> metadata.tdkVersion().toString()));
        add(new Label("version", () -> metadata.version().toString()));
        add(new Label("contact-email", metadata::contactEmail));
        add(new Label("critical-alerts", () -> health.count("CriticalAlert")));
        add(new Label("alerts", () -> health.count("Alert")));
        add(new Label("problems", () -> health.count("Problem")));
        add(new Label("warnings", () -> health.count("Warning")));
        add(new Label("quibbles", () -> health.count("Quibble")));
        add(new Label("threads", () -> health.threadSnapshot().threads().size()));

        final var states = new CountMap<Thread.State>();
        health.threadSnapshot().threads().forEach(thread -> states.increment(thread.state()));
        add(new Label("threads-running", () -> states.count(RUNNABLE)));
        add(new Label("threads-waiting", () -> states.count(BLOCKED)
                .plus(states.count(WAITING))
                .plus(states.count(TIMED_WAITING))));
    }

    private Service service()
    {
        return model.getObject();
    }
}
