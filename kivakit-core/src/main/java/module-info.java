import com.telenav.kivakit.core.logging.Log;

@SuppressWarnings("JavaRequiresAutoModule")
open module kivakit.core
{
    uses Log;

    // Cactus
    requires transitive cactus.metadata;

    // KivaKit
    requires transitive kivakit.annotations;
    requires transitive kivakit.interfaces;
    requires transitive kivakit.mixins;

    // Java
    requires java.instrument;
    requires jdk.attach;
    requires java.management;

    // Module exports
    exports com.telenav.kivakit.core.code;
    exports com.telenav.kivakit.core.collections.iteration;
    exports com.telenav.kivakit.core.collections.list;
    exports com.telenav.kivakit.core.collections.map;
    exports com.telenav.kivakit.core.collections.set;
    exports com.telenav.kivakit.core.collections;
    exports com.telenav.kivakit.core.ensure;
    exports com.telenav.kivakit.core.function;
    exports com.telenav.kivakit.core.function.arities;
    exports com.telenav.kivakit.core.io;
    exports com.telenav.kivakit.core.language.module;
    exports com.telenav.kivakit.core.language.primitive;
    exports com.telenav.kivakit.core.language.reflection.property;
    exports com.telenav.kivakit.core.language.reflection;
    exports com.telenav.kivakit.core.language.trait;
    exports com.telenav.kivakit.core.language;
    exports com.telenav.kivakit.core.locale;
    exports com.telenav.kivakit.core.logging.logs;
    exports com.telenav.kivakit.core.logging;
    exports com.telenav.kivakit.core.messaging.broadcasters;
    exports com.telenav.kivakit.core.messaging.context;
    exports com.telenav.kivakit.core.messaging.filters;
    exports com.telenav.kivakit.core.messaging.listeners;
    exports com.telenav.kivakit.core.messaging.messages.lifecycle;
    exports com.telenav.kivakit.core.messaging.messages.status.activity;
    exports com.telenav.kivakit.core.messaging.messages.status;
    exports com.telenav.kivakit.core.messaging.messages;
    exports com.telenav.kivakit.core.messaging.repeaters;
    exports com.telenav.kivakit.core.messaging;
    exports com.telenav.kivakit.core.object;
    exports com.telenav.kivakit.core.os;
    exports com.telenav.kivakit.core.path;
    exports com.telenav.kivakit.core.progress.reporters;
    exports com.telenav.kivakit.core.progress;
    exports com.telenav.kivakit.core.project;
    exports com.telenav.kivakit.core.registry;
    exports com.telenav.kivakit.core.string;
    exports com.telenav.kivakit.core.thread.latches;
    exports com.telenav.kivakit.core.thread.locks;
    exports com.telenav.kivakit.core.thread;
    exports com.telenav.kivakit.core.testing;
    exports com.telenav.kivakit.core.time;
    exports com.telenav.kivakit.core.value.count;
    exports com.telenav.kivakit.core.value.identifier;
    exports com.telenav.kivakit.core.value.level;
    exports com.telenav.kivakit.core.value.name;
    exports com.telenav.kivakit.core.version;
    exports com.telenav.kivakit.core.vm;
    exports com.telenav.kivakit.core;
    exports com.telenav.kivakit.core.logging.logs.text;
    exports com.telenav.kivakit.core.logging.loggers;
    exports com.telenav.kivakit.core.language.object;
    exports com.telenav.kivakit.core.language.reflection.filters.field;
    exports com.telenav.kivakit.core.messaging.alarms;
    exports com.telenav.kivakit.core.value.mutable;
    exports com.telenav.kivakit.core.bits;
    exports com.telenav.kivakit.core.logging.logs.text.formatters;
    exports com.telenav.kivakit.core.math;
}
