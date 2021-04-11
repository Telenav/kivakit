////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.service.registry.server.webapp.pages.home;

import com.telenav.kivakit.core.kernel.language.collections.map.count.CountMap;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
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

/**
 * An Apache Wicket panel that shows information about a {@link Service}. Used to display lists of services on the
 * {@link HomePage}.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
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
        add(new Label("kivakit-version", () -> metadata.kivakitVersion().toString()));
        add(new Label("version", () -> metadata.version().toString()));
        add(new Label("contact-email", metadata::contactEmail));
        add(new Label("critical-alerts", () -> health.count("CriticalAlert")));
        add(new Label("alerts", () -> health.count("Alert")));
        add(new Label("problems", () -> health.count("Problem")));
        add(new Label("warnings", () -> health.count("Warning")));
        add(new Label("quibbles", () -> health.count("Quibble")));
        add(new Label("threads", () -> health.threadSnapshot().snapshot().size()));

        final var states = new CountMap<Thread.State>();
        health.threadSnapshot().snapshot().forEach(thread -> states.increment(thread.state()));
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
