import com.telenav.kivakit.core.kernel.logging.Log;
import com.telenav.kivakit.core.kernel.logging.logs.text.ConsoleLog;

open module kivakit.core.kernel
{
    // Java
    requires transitive jdk.attach;
    requires transitive java.instrument;
    requires transitive java.management;

    // Build
    requires transitive cactus.build.metadata;

    // Test
    requires transitive junit;

    // Annotations
    requires transitive lexakai.annotations;
    requires transitive org.jetbrains.annotations;
    requires transitive com.fasterxml.jackson.annotation;
    requires transitive io.swagger.v3.oas.annotations;

    uses Log;

    provides Log with ConsoleLog;

    exports com.telenav.kivakit.core.kernel.data.comparison;
    exports com.telenav.kivakit.core.kernel.data.conversion.string.collection;
    exports com.telenav.kivakit.core.kernel.data.conversion.string.enumeration;
    exports com.telenav.kivakit.core.kernel.data.conversion.string.language;
    exports com.telenav.kivakit.core.kernel.data.conversion.string.primitive;
    exports com.telenav.kivakit.core.kernel.data.conversion.string;
    exports com.telenav.kivakit.core.kernel.data.conversion;
    exports com.telenav.kivakit.core.kernel.data.extraction;
    exports com.telenav.kivakit.core.kernel.data.validation.ensure;
    exports com.telenav.kivakit.core.kernel.data.validation.listeners;
    exports com.telenav.kivakit.core.kernel.data.validation.reporters;
    exports com.telenav.kivakit.core.kernel.data.validation.validators;
    exports com.telenav.kivakit.core.kernel.data.validation;
    exports com.telenav.kivakit.core.kernel.interfaces.code;
    exports com.telenav.kivakit.core.kernel.interfaces.collection;
    exports com.telenav.kivakit.core.kernel.interfaces.comparison;
    exports com.telenav.kivakit.core.kernel.interfaces.factory;
    exports com.telenav.kivakit.core.kernel.interfaces.function;
    exports com.telenav.kivakit.core.kernel.interfaces.io;
    exports com.telenav.kivakit.core.kernel.interfaces.lifecycle;
    exports com.telenav.kivakit.core.kernel.interfaces.loading;
    exports com.telenav.kivakit.core.kernel.interfaces.messaging;
    exports com.telenav.kivakit.core.kernel.interfaces.model;
    exports com.telenav.kivakit.core.kernel.interfaces.naming;
    exports com.telenav.kivakit.core.kernel.interfaces.numeric;
    exports com.telenav.kivakit.core.kernel.interfaces.string;
    exports com.telenav.kivakit.core.kernel.interfaces.value;
    exports com.telenav.kivakit.core.kernel.language.bits;
    exports com.telenav.kivakit.core.kernel.language.collections.list;
    exports com.telenav.kivakit.core.kernel.language.collections.map;
    exports com.telenav.kivakit.core.kernel.language.collections.map.string;
    exports com.telenav.kivakit.core.kernel.language.collections.map.count;
    exports com.telenav.kivakit.core.kernel.language.collections.set;
    exports com.telenav.kivakit.core.kernel.language.collections;
    exports com.telenav.kivakit.core.kernel.language.io;
    exports com.telenav.kivakit.core.kernel.language.iteration;
    exports com.telenav.kivakit.core.kernel.language.locales;
    exports com.telenav.kivakit.core.kernel.language.matchers;
    exports com.telenav.kivakit.core.kernel.language.math;
    exports com.telenav.kivakit.core.kernel.language.modules;
    exports com.telenav.kivakit.core.kernel.language.objects.reference.virtual.types;
    exports com.telenav.kivakit.core.kernel.language.objects.reference.virtual;
    exports com.telenav.kivakit.core.kernel.language.objects.reference;
    exports com.telenav.kivakit.core.kernel.language.objects;
    exports com.telenav.kivakit.core.kernel.language.paths;
    exports com.telenav.kivakit.core.kernel.language.patterns.character;
    exports com.telenav.kivakit.core.kernel.language.patterns.closure;
    exports com.telenav.kivakit.core.kernel.language.patterns.group;
    exports com.telenav.kivakit.core.kernel.language.patterns.logical;
    exports com.telenav.kivakit.core.kernel.language.patterns;
    exports com.telenav.kivakit.core.kernel.language.primitives;
    exports com.telenav.kivakit.core.kernel.language.progress.reporters;
    exports com.telenav.kivakit.core.kernel.language.progress;
    exports com.telenav.kivakit.core.kernel.language.reflection.access;
    exports com.telenav.kivakit.core.kernel.language.reflection.populator;
    exports com.telenav.kivakit.core.kernel.language.reflection.property.filters.field;
    exports com.telenav.kivakit.core.kernel.language.reflection.property.filters;
    exports com.telenav.kivakit.core.kernel.language.reflection.property;
    exports com.telenav.kivakit.core.kernel.language.reflection;
    exports com.telenav.kivakit.core.kernel.language.strings.conversion;
    exports com.telenav.kivakit.core.kernel.language.strings.formatting;
    exports com.telenav.kivakit.core.kernel.language.strings;
    exports com.telenav.kivakit.core.kernel.language.threading.conditions;
    exports com.telenav.kivakit.core.kernel.language.threading.context;
    exports com.telenav.kivakit.core.kernel.language.threading.latches;
    exports com.telenav.kivakit.core.kernel.language.threading.locks;
    exports com.telenav.kivakit.core.kernel.language.threading.status;
    exports com.telenav.kivakit.core.kernel.language.threading;
    exports com.telenav.kivakit.core.kernel.language.time.conversion.converters;
    exports com.telenav.kivakit.core.kernel.language.time.conversion;
    exports com.telenav.kivakit.core.kernel.language.time;
    exports com.telenav.kivakit.core.kernel.language.trait;
    exports com.telenav.kivakit.core.kernel.language.types;
    exports com.telenav.kivakit.core.kernel.language.values.count;
    exports com.telenav.kivakit.core.kernel.language.values.identifier;
    exports com.telenav.kivakit.core.kernel.language.values.level;
    exports com.telenav.kivakit.core.kernel.language.values.mutable;
    exports com.telenav.kivakit.core.kernel.language.values.name;
    exports com.telenav.kivakit.core.kernel.language.values.version;
    exports com.telenav.kivakit.core.kernel.language.vm;
    exports com.telenav.kivakit.core.kernel.logging.filters;
    exports com.telenav.kivakit.core.kernel.logging.loggers;
    exports com.telenav.kivakit.core.kernel.logging.logs.text.formatters;
    exports com.telenav.kivakit.core.kernel.logging.logs.text;
    exports com.telenav.kivakit.core.kernel.logging.logs;
    exports com.telenav.kivakit.core.kernel.logging;
    exports com.telenav.kivakit.core.kernel.messaging.broadcasters;
    exports com.telenav.kivakit.core.kernel.messaging.filters.operators;
    exports com.telenav.kivakit.core.kernel.messaging.filters;
    exports com.telenav.kivakit.core.kernel.messaging.listeners;
    exports com.telenav.kivakit.core.kernel.messaging.messages.lifecycle;
    exports com.telenav.kivakit.core.kernel.messaging.messages.status;
    exports com.telenav.kivakit.core.kernel.messaging.messages;
    exports com.telenav.kivakit.core.kernel.messaging.repeaters;
    exports com.telenav.kivakit.core.kernel.messaging;
    exports com.telenav.kivakit.core.kernel.project.lexakai.diagrams;
    exports com.telenav.kivakit.core.kernel.project;
    exports com.telenav.kivakit.core.kernel;
}
