# kivakit-core-kernel &nbsp;&nbsp;![](../../documentation/images/nucleus-40.png)

This module provides extensions to the JDK and other packages for everyday Java development.

![](documentation/images/horizontal-line.png)

### Index

[**Summary**](#summary)  
[**Dependencies**](#dependencies)  
[**Class Diagrams**](#class-diagrams)  
[**Package Diagrams**](#package-diagrams)  
[**Javadoc**](#javadoc)

![](documentation/images/horizontal-line.png)

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

KivaKit kernel is consists of several integrated frameworks in a number of different packages.  
The major feature areas are listed here, each having its own documentation:

[**Data - Comparison**](documentation/data-comparison.md)  
[**Data - Conversion**](documentation/data-conversion.md)  
[**Data - Extraction**](documentation/data-extraction.md)  
[**Data - Validation**](documentation/data-validation.md)  
[**Interfaces**](documentation/interfaces.md)  
[**Language - Bits**](documentation/language-bits.md)  
[**Language - Collections**](documentation/language-collections.md)  
[**Language - I/O**](documentation/language-io.md)  
[**Language - Iteration**](documentation/language-iteration.md)  
[**Language - Locale**](documentation/language-locales.md)  
[**Language - Matching**](documentation/language-matching.md)  
[**Language - Math**](documentation/language-math.md)  
[**Language - Modules**](documentation/language-modules.md)  
[**Language - Objects**](documentation/language-objects.md)  
[**Language - Paths**](documentation/language-paths.md)  
[**Language - Patterns**](documentation/language-patterns.md)  
[**Language - Primitives**](documentation/language-primitives.md)  
[**Language - Progress**](documentation/language-progress.md)  
[**Language - Reflection**](documentation/language-reflection.md)  
[**Language - Strings**](documentation/language-strings.md)  
[**Language - Threading**](documentation/language-threading.md)    
[**Language - Time**](documentation/language-time.md)  
[**Language - Types**](documentation/language-types.md)  
[**Language - Values**](documentation/language-values.md)  
[**Language - VM**](documentation/language-vm.md)  
[**Logging**](documentation/logging.md)  
[**Messaging**](documentation/messaging.md)  
[**Messaging - Debugging**](documentation/messaging-debugging.md)  
[**Projects**](documentation/project.md)

![](documentation/images/horizontal-line.png)

[//]: # (end-user-text)

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp;  ![](documentation/images/dependencies-40.png)

[*Dependency Diagram*](documentation/diagrams/dependencies.svg)

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-core-kernel</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp;![](documentation/images/diagram-48.png)

[*Data - Collection and Other Converters*](documentation/diagrams/diagram-data-conversion-other.svg)  
[*Data - Comparison*](documentation/diagrams/diagram-data-comparison.svg)  
[*Data - Conversion*](documentation/diagrams/diagram-data-conversion.svg)  
[*Data - Extraction*](documentation/diagrams/diagram-data-extraction.svg)  
[*Data - Primitive Converters*](documentation/diagrams/diagram-data-conversion-primitive.svg)  
[*Data - State Validation*](documentation/diagrams/diagram-data-validation-ensure.svg)  
[*Data - Validation*](documentation/diagrams/diagram-data-validation.svg)  
[*Data - Validation Reporting*](documentation/diagrams/diagram-data-validation-reporter.svg)  
[*Fine-Grained Inheritance of Super-Interfaces by BaseList*](documentation/diagrams/diagram-example-base-list.svg)  
[*Interfaces - Code*](documentation/diagrams/diagram-interface-code.svg)  
[*Interfaces - Collection*](documentation/diagrams/diagram-interface-collection.svg)  
[*Interfaces - Comparison*](documentation/diagrams/diagram-interface-comparison.svg)  
[*Interfaces - Factory*](documentation/diagrams/diagram-interface-factory.svg)  
[*Interfaces - Functional*](documentation/diagrams/diagram-interface-function.svg)  
[*Interfaces - I/O*](documentation/diagrams/diagram-interface-io.svg)  
[*Interfaces - Life Cycle*](documentation/diagrams/diagram-interface-life-cycle.svg)  
[*Interfaces - Messaging*](documentation/diagrams/diagram-interface-messaging.svg)  
[*Interfaces - Model*](documentation/diagrams/diagram-interface-model.svg)  
[*Interfaces - Naming*](documentation/diagrams/diagram-interface-naming.svg)  
[*Interfaces - Numeric*](documentation/diagrams/diagram-interface-numeric.svg)  
[*Interfaces - Persistence*](documentation/diagrams/diagram-interface-persistence.svg)  
[*Interfaces - String*](documentation/diagrams/diagram-interface-string.svg)  
[*Interfaces - Value*](documentation/diagrams/diagram-interface-value.svg)  
[*Language - Bit Manipulation*](documentation/diagrams/diagram-language-bits.svg)  
[*Language - Collections - Lists*](documentation/diagrams/diagram-language-collections-list.svg)  
[*Language - Concurrency*](documentation/diagrams/diagram-language-thread.svg)  
[*Language - I/O*](documentation/diagrams/diagram-language-io.svg)  
[*Language - Iteration*](documentation/diagrams/diagram-language-iteration.svg)  
[*Language - Java Modules*](documentation/diagrams/diagram-language-modules.svg)  
[*Language - Java Virtual Machine*](documentation/diagrams/diagram-language-java-virtual-machine.svg)  
[*Language - Matching*](documentation/diagrams/diagram-language-matchers.svg)  
[*Language - Math*](documentation/diagrams/diagram-language-math.svg)  
[*Language - Paths*](documentation/diagrams/diagram-language-path.svg)  
[*Language - Pattern Matching*](documentation/diagrams/diagram-language-pattern.svg)  
[*Language - Primitives*](documentation/diagrams/diagram-language-primitive.svg)  
[*Language - Progress Reporting*](documentation/diagrams/diagram-language-progress.svg)  
[*Language - Reflection*](documentation/diagrams/diagram-language-reflection.svg)  
[*Language - String Utilities*](documentation/diagrams/diagram-language-string.svg)  
[*Language - Thread Synchronization*](documentation/diagrams/diagram-language-thread-synchronization.svg)  
[*Language - Time*](documentation/diagrams/diagram-language-time.svg)  
[*Language - Values*](documentation/diagrams/diagram-language-value.svg)  
[*Language - Collections - Maps*](documentation/diagrams/diagram-language-collections-map.svg)  
[*Logging*](documentation/diagrams/diagram-logging.svg)  
[*Logging - Logs*](documentation/diagrams/diagram-logging-logs.svg)  
[*Messaging*](documentation/diagrams/diagram-messaging.svg)  
[*Messaging - Broadcasting*](documentation/diagrams/diagram-message-broadcaster.svg)  
[*Messaging - Core Message Listeners*](documentation/diagrams/diagram-message-listener-type.svg)  
[*Messaging - Listening*](documentation/diagrams/diagram-message-listener.svg)  
[*Messaging - Message Types*](documentation/diagrams/diagram-message-type.svg)  
[*Messaging - Repeating*](documentation/diagrams/diagram-message-repeater.svg)  
[*Projects*](documentation/diagrams/diagram-project.svg)  
[*diagram-language-locale*](documentation/diagrams/diagram-language-locale.svg)  
[*diagram-language-object*](documentation/diagrams/diagram-language-object.svg)  
[*diagram-language-object-reference*](documentation/diagrams/diagram-language-object-reference.svg)

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp;![](documentation/images/box-40.png)

[*com.telenav.kivakit.core.kernel*](documentation/diagrams/com.telenav.kivakit.core.kernel.svg)  
[*com.telenav.kivakit.core.kernel.data.comparison*](documentation/diagrams/com.telenav.kivakit.core.kernel.data.comparison.svg)  
[*com.telenav.kivakit.core.kernel.data.conversion*](documentation/diagrams/com.telenav.kivakit.core.kernel.data.conversion.svg)  
[*
com.telenav.kivakit.core.kernel.data.conversion.string*](documentation/diagrams/com.telenav.kivakit.core.kernel.data.conversion.string.svg)  
[*
com.telenav.kivakit.core.kernel.data.conversion.string.collection*](documentation/diagrams/com.telenav.kivakit.core.kernel.data.conversion.string.collection.svg)  
[*
com.telenav.kivakit.core.kernel.data.conversion.string.enumeration*](documentation/diagrams/com.telenav.kivakit.core.kernel.data.conversion.string.enumeration.svg)  
[*
com.telenav.kivakit.core.kernel.data.conversion.string.language*](documentation/diagrams/com.telenav.kivakit.core.kernel.data.conversion.string.language.svg)  
[*
com.telenav.kivakit.core.kernel.data.conversion.string.primitive*](documentation/diagrams/com.telenav.kivakit.core.kernel.data.conversion.string.primitive.svg)  
[*com.telenav.kivakit.core.kernel.data.extraction*](documentation/diagrams/com.telenav.kivakit.core.kernel.data.extraction.svg)  
[*com.telenav.kivakit.core.kernel.data.validation*](documentation/diagrams/com.telenav.kivakit.core.kernel.data.validation.svg)  
[*
com.telenav.kivakit.core.kernel.data.validation.ensure*](documentation/diagrams/com.telenav.kivakit.core.kernel.data.validation.ensure.svg)  
[*
com.telenav.kivakit.core.kernel.data.validation.listeners*](documentation/diagrams/com.telenav.kivakit.core.kernel.data.validation.listeners.svg)  
[*
com.telenav.kivakit.core.kernel.data.validation.reporters*](documentation/diagrams/com.telenav.kivakit.core.kernel.data.validation.reporters.svg)  
[*
com.telenav.kivakit.core.kernel.data.validation.validators*](documentation/diagrams/com.telenav.kivakit.core.kernel.data.validation.validators.svg)  
[*com.telenav.kivakit.core.kernel.interfaces.code*](documentation/diagrams/com.telenav.kivakit.core.kernel.interfaces.code.svg)  
[*
com.telenav.kivakit.core.kernel.interfaces.collection*](documentation/diagrams/com.telenav.kivakit.core.kernel.interfaces.collection.svg)  
[*
com.telenav.kivakit.core.kernel.interfaces.comparison*](documentation/diagrams/com.telenav.kivakit.core.kernel.interfaces.comparison.svg)  
[*com.telenav.kivakit.core.kernel.interfaces.factory*](documentation/diagrams/com.telenav.kivakit.core.kernel.interfaces.factory.svg)  
[*com.telenav.kivakit.core.kernel.interfaces.function*](documentation/diagrams/com.telenav.kivakit.core.kernel.interfaces.function.svg)  
[*com.telenav.kivakit.core.kernel.interfaces.io*](documentation/diagrams/com.telenav.kivakit.core.kernel.interfaces.io.svg)  
[*com.telenav.kivakit.core.kernel.interfaces.lifecycle*](documentation/diagrams/com.telenav.kivakit.core.kernel.interfaces.lifecycle.svg)  
[*com.telenav.kivakit.core.kernel.interfaces.loading*](documentation/diagrams/com.telenav.kivakit.core.kernel.interfaces.loading.svg)  
[*com.telenav.kivakit.core.kernel.interfaces.messaging*](documentation/diagrams/com.telenav.kivakit.core.kernel.interfaces.messaging.svg)  
[*com.telenav.kivakit.core.kernel.interfaces.model*](documentation/diagrams/com.telenav.kivakit.core.kernel.interfaces.model.svg)  
[*com.telenav.kivakit.core.kernel.interfaces.naming*](documentation/diagrams/com.telenav.kivakit.core.kernel.interfaces.naming.svg)  
[*com.telenav.kivakit.core.kernel.interfaces.numeric*](documentation/diagrams/com.telenav.kivakit.core.kernel.interfaces.numeric.svg)  
[*com.telenav.kivakit.core.kernel.interfaces.string*](documentation/diagrams/com.telenav.kivakit.core.kernel.interfaces.string.svg)  
[*com.telenav.kivakit.core.kernel.interfaces.value*](documentation/diagrams/com.telenav.kivakit.core.kernel.interfaces.value.svg)  
[*com.telenav.kivakit.core.kernel.language.bits*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.bits.svg)  
[*com.telenav.kivakit.core.kernel.language.collections*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.collections.svg)  
[*
com.telenav.kivakit.core.kernel.language.collections.list*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.collections.list.svg)  
[*
com.telenav.kivakit.core.kernel.language.collections.map*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.collections.map.svg)  
[*
com.telenav.kivakit.core.kernel.language.collections.map.count*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.collections.map.count.svg)  
[*
com.telenav.kivakit.core.kernel.language.collections.map.string*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.collections.map.string.svg)  
[*
com.telenav.kivakit.core.kernel.language.collections.set*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.collections.set.svg)  
[*com.telenav.kivakit.core.kernel.language.io*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.io.svg)  
[*com.telenav.kivakit.core.kernel.language.iteration*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.iteration.svg)  
[*com.telenav.kivakit.core.kernel.language.locales*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.locales.svg)  
[*
com.telenav.kivakit.core.kernel.language.matching.matchers*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.matching.matchers.svg)  
[*com.telenav.kivakit.core.kernel.language.math*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.math.svg)  
[*com.telenav.kivakit.core.kernel.language.modules*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.modules.svg)  
[*com.telenav.kivakit.core.kernel.language.objects*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.objects.svg)  
[*
com.telenav.kivakit.core.kernel.language.objects.reference*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.objects.reference.svg)  
[*
com.telenav.kivakit.core.kernel.language.objects.reference.virtual*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.objects.reference.virtual.svg)  
[*
com.telenav.kivakit.core.kernel.language.objects.reference.virtual.types*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.objects.reference.virtual.types.svg)  
[*com.telenav.kivakit.core.kernel.language.paths*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.paths.svg)  
[*com.telenav.kivakit.core.kernel.language.patterns*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.patterns.svg)  
[*
com.telenav.kivakit.core.kernel.language.patterns.character*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.patterns.character.svg)  
[*
com.telenav.kivakit.core.kernel.language.patterns.closure*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.patterns.closure.svg)  
[*
com.telenav.kivakit.core.kernel.language.patterns.group*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.patterns.group.svg)  
[*
com.telenav.kivakit.core.kernel.language.patterns.logical*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.patterns.logical.svg)  
[*com.telenav.kivakit.core.kernel.language.primitives*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.primitives.svg)  
[*com.telenav.kivakit.core.kernel.language.progress*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.progress.svg)  
[*
com.telenav.kivakit.core.kernel.language.progress.reporters*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.progress.reporters.svg)  
[*com.telenav.kivakit.core.kernel.language.reflection*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.reflection.svg)  
[*
com.telenav.kivakit.core.kernel.language.reflection.access*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.reflection.access.svg)  
[*
com.telenav.kivakit.core.kernel.language.reflection.access.field*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.reflection.access.field.svg)  
[*
com.telenav.kivakit.core.kernel.language.reflection.access.method*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.reflection.access.method.svg)  
[*
com.telenav.kivakit.core.kernel.language.reflection.populator*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.reflection.populator.svg)  
[*
com.telenav.kivakit.core.kernel.language.reflection.property*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.reflection.property.svg)  
[*
com.telenav.kivakit.core.kernel.language.reflection.property.filters*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.reflection.property.filters.svg)  
[*
com.telenav.kivakit.core.kernel.language.reflection.property.filters.field*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.reflection.property.filters.field.svg)  
[*
com.telenav.kivakit.core.kernel.language.reflection.property.filters.method*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.reflection.property.filters.method.svg)  
[*com.telenav.kivakit.core.kernel.language.strings*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.strings.svg)  
[*
com.telenav.kivakit.core.kernel.language.strings.conversion*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.strings.conversion.svg)  
[*
com.telenav.kivakit.core.kernel.language.strings.formatting*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.strings.formatting.svg)  
[*com.telenav.kivakit.core.kernel.language.threading*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.threading.svg)  
[*
com.telenav.kivakit.core.kernel.language.threading.conditions*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.threading.conditions.svg)  
[*
com.telenav.kivakit.core.kernel.language.threading.context*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.threading.context.svg)  
[*
com.telenav.kivakit.core.kernel.language.threading.latches*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.threading.latches.svg)  
[*
com.telenav.kivakit.core.kernel.language.threading.locks*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.threading.locks.svg)  
[*
com.telenav.kivakit.core.kernel.language.threading.locks.legacy*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.threading.locks.legacy.svg)  
[*
com.telenav.kivakit.core.kernel.language.threading.status*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.threading.status.svg)  
[*com.telenav.kivakit.core.kernel.language.time*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.time.svg)  
[*
com.telenav.kivakit.core.kernel.language.time.conversion*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.time.conversion.svg)  
[*
com.telenav.kivakit.core.kernel.language.time.conversion.converters*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.time.conversion.converters.svg)  
[*com.telenav.kivakit.core.kernel.language.types*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.types.svg)  
[*
com.telenav.kivakit.core.kernel.language.values.count*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.values.count.svg)  
[*
com.telenav.kivakit.core.kernel.language.values.identifier*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.values.identifier.svg)  
[*
com.telenav.kivakit.core.kernel.language.values.level*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.values.level.svg)  
[*
com.telenav.kivakit.core.kernel.language.values.mutable*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.values.mutable.svg)  
[*com.telenav.kivakit.core.kernel.language.values.name*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.values.name.svg)  
[*
com.telenav.kivakit.core.kernel.language.values.version*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.values.version.svg)  
[*com.telenav.kivakit.core.kernel.language.vm*](documentation/diagrams/com.telenav.kivakit.core.kernel.language.vm.svg)  
[*com.telenav.kivakit.core.kernel.logging*](documentation/diagrams/com.telenav.kivakit.core.kernel.logging.svg)  
[*com.telenav.kivakit.core.kernel.logging.filters*](documentation/diagrams/com.telenav.kivakit.core.kernel.logging.filters.svg)  
[*com.telenav.kivakit.core.kernel.logging.loggers*](documentation/diagrams/com.telenav.kivakit.core.kernel.logging.loggers.svg)  
[*com.telenav.kivakit.core.kernel.logging.logs*](documentation/diagrams/com.telenav.kivakit.core.kernel.logging.logs.svg)  
[*com.telenav.kivakit.core.kernel.logging.logs.text*](documentation/diagrams/com.telenav.kivakit.core.kernel.logging.logs.text.svg)  
[*
com.telenav.kivakit.core.kernel.logging.logs.text.formatters*](documentation/diagrams/com.telenav.kivakit.core.kernel.logging.logs.text.formatters.svg)  
[*com.telenav.kivakit.core.kernel.messaging*](documentation/diagrams/com.telenav.kivakit.core.kernel.messaging.svg)  
[*
com.telenav.kivakit.core.kernel.messaging.broadcasters*](documentation/diagrams/com.telenav.kivakit.core.kernel.messaging.broadcasters.svg)  
[*com.telenav.kivakit.core.kernel.messaging.filters*](documentation/diagrams/com.telenav.kivakit.core.kernel.messaging.filters.svg)  
[*
com.telenav.kivakit.core.kernel.messaging.filters.operators*](documentation/diagrams/com.telenav.kivakit.core.kernel.messaging.filters.operators.svg)  
[*com.telenav.kivakit.core.kernel.messaging.listeners*](documentation/diagrams/com.telenav.kivakit.core.kernel.messaging.listeners.svg)  
[*com.telenav.kivakit.core.kernel.messaging.messages*](documentation/diagrams/com.telenav.kivakit.core.kernel.messaging.messages.svg)  
[*
com.telenav.kivakit.core.kernel.messaging.messages.lifecycle*](documentation/diagrams/com.telenav.kivakit.core.kernel.messaging.messages.lifecycle.svg)  
[*
com.telenav.kivakit.core.kernel.messaging.messages.status*](documentation/diagrams/com.telenav.kivakit.core.kernel.messaging.messages.status.svg)  
[*com.telenav.kivakit.core.kernel.messaging.repeaters*](documentation/diagrams/com.telenav.kivakit.core.kernel.messaging.repeaters.svg)  
[*com.telenav.kivakit.core.kernel.project*](documentation/diagrams/com.telenav.kivakit.core.kernel.project.svg)

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp;![](documentation/images/books-40.png)

| Class | Documentation Sections |
|---|---|
| [*
Activity*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/status/Activity.html) |  |  
| [*
Addable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/collection/Addable.html) |  |  
| [*
Alert*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/status/Alert.html) |  |  
| [*
Align*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/Align.html) |  |  
| [*
All*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/matching/matchers/All.html) |  |  
| [*
AllFields*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/property/filters/field/AllFields.html) |  |  
| [*
AllMessages*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/filters/AllMessages.html) |  |  
| [*
AllMethods*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/property/filters/method/AllMethods.html) |  |  
| [*
And*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/filters/operators/And.html) |  |  
| [*
Announcement*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/status/Announcement.html) |  |  
| [*
Appendable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/collection/Appendable.html) |  |  
| [*
Arrays*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/primitives/Arrays.html) |  |  
| [*
AsIndentedString*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/conversion/AsIndentedString.html) |  |  
| [*
AsString*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/conversion/AsString.html) |  |  
| [*
AsStringIndenter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/conversion/AsStringIndenter.html) |  |  
| [*
AsciiArt*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/AsciiArt.html) |  |  
| [*
AssertingValidationReporter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/validation/reporters/AssertingValidationReporter.html) |  |  
| [*
AudienceMember*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/broadcasters/AudienceMember.html) |  |  
| [*
BaseCollectionConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/collection/BaseCollectionConverter.html) |  |  
| [*
BaseConcurrentMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/map/BaseConcurrentMap.html) |  |  
| [*
BaseConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/BaseConverter.html) |  |  
| [*
BaseExtractor*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/extraction/BaseExtractor.html) |  |  
| [*
BaseFormattedConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/conversion/BaseFormattedConverter.html) |  |  
| [*
BaseFormattedLocalTimeConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/conversion/BaseFormattedLocalTimeConverter.html) |  |  
| [*
BaseIndexedMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/map/BaseIndexedMap.html) |  |  
| [*
BaseIterable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/iteration/BaseIterable.html) |  |  
| [*
BaseIterator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/iteration/BaseIterator.html) |  |  
| [*
BaseList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/list/BaseList.html) | Conversions |  
| | String Conversions |  
| | Checks |  
| | Bounds |  
| | Functional Methods |  
| [*
BaseListConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/collection/BaseListConverter.html) |  |  
| [*
BaseLog*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/logs/BaseLog.html) |  |  
| [*
BaseLogger*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/loggers/BaseLogger.html) |  |  
| [*
BaseMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/map/BaseMap.html) |  |  
| [*
BasePropertyFilter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/property/filters/BasePropertyFilter.html) |  |  
| [*
BasePropertyFilter.Include*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/property/filters/BasePropertyFilter.Include.html) |  |  
| [*
BasePropertyFilter.NamingConvention*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/property/filters/BasePropertyFilter.NamingConvention.html) |  |  
| [*
BaseRepeater*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/repeaters/BaseRepeater.html) |  |  
| [*
BaseSet*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/set/BaseSet.html) |  |  
| [*
BaseSetConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/collection/BaseSetConverter.html) |  |  
| [*
BaseStringConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/BaseStringConverter.html) | Thread Safety |  
| | Empty Strings |  
| [*
BaseStringMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/map/string/BaseStringMap.html) |  |  
| [*
BaseTextLog*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/logs/text/BaseTextLog.html) |  |  
| [*
BaseValidationReporter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/validation/BaseValidationReporter.html) |  |  
| [*
BaseValidator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/validation/validators/BaseValidator.html) |  |  
| [*
BaseValidator.Statistics*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/validation/validators/BaseValidator.Statistics.html) |  |  
| [*
BeansProperties*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/property/filters/BeansProperties.html) |  |  
| [*
BitCount*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/count/BitCount.html) |  |  
| [*
BitDiagram*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/bits/BitDiagram.html) | Example |  
| [*
BitDiagram.BitField*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/bits/BitDiagram.BitField.html) |  |  
| [*
Bits*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/bits/Bits.html) |  |  
| [*
BooleanConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/primitive/BooleanConverter.html) |  |  
| [*
BooleanFunction*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/function/BooleanFunction.html) |  |  
| [*
BooleanLock*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/locks/legacy/BooleanLock.html) |  |  
| [*
BooleanValued*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/value/BooleanValued.html) |  |  
| [*
Booleans*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/primitives/Booleans.html) |  |  
| [*
Broadcaster*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/Broadcaster.html) | Convenience Methods |  
| [*
Build*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/project/Build.html) | Build Date, Number and Name |  
| [*
ByteSized*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/io/ByteSized.html) |  |  
| [*
ByteSizedOutput*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/io/ByteSizedOutput.html) |  |  
| [*
Bytes*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/count/Bytes.html) |  |  
| [*
Bytes.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/count/Bytes.Converter.html) |  |  
| [*
CallStack*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/context/CallStack.html) |  |  
| [*
CallStack.Matching*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/context/CallStack.Matching.html) |  |  
| [*
CallStack.Proximity*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/context/CallStack.Proximity.html) |  |  
| [*
Callback*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/code/Callback.html) |  |  
| [*
CaseFormat*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/CaseFormat.html) |  |  
| [*
Character*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/patterns/character/Character.html) |  |  
| [*
CharacterClass*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/patterns/character/CharacterClass.html) |  |  
| [*
ClassConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/language/ClassConverter.html) |  |  
| [*
ClassMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/map/ClassMap.html) |  |  
| [*
Classes*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/types/Classes.html) |  |  
| [*
Closeable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/io/Closeable.html) |  |  
| [*
Closure*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/patterns/closure/Closure.html) |  |  
| [*
Code*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/code/Code.html) |  |  
| [*
CodeContext*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/context/CodeContext.html) |  |  
| [*
Collections*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/Collections.html) |  |  
| [*
ColumnarFormatter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/logs/text/formatters/ColumnarFormatter.html) |  |  
| [*
ColumnarFormatter.Column*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/logs/text/formatters/ColumnarFormatter.Column.html) |  |  
| [*
ColumnarFormatter.Layout*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/logs/text/formatters/ColumnarFormatter.Layout.html) |  |  
| [*
ColumnarFormatter.LineOutput*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/logs/text/formatters/ColumnarFormatter.LineOutput.html) |  |  
| [*
Comparison*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/Comparison.html) |  |  
| [*
CompletionLatch*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/latches/CompletionLatch.html) |  |  
| [*
CompressibleCollection*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/CompressibleCollection.html) |  |  
| [*
CompressibleCollection.CompressionEvent*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/CompressibleCollection.CompressionEvent.html) |  |  
| [*
CompressibleCollection.Method*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/CompressibleCollection.Method.html) |  |  
| [*
ConcurrentCountMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/map/count/ConcurrentCountMap.html) |  |  
| [*
ConcurrentMutableCount*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/count/ConcurrentMutableCount.html) |  |  
| [*
ConcurrentProgress*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/progress/reporters/ConcurrentProgress.html) |  |  
| [*
ConditionLock*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/locks/legacy/ConditionLock.html) |  |  
| [*
Confidence*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/level/Confidence.html) |  |  
| [*
Confidence.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/level/Confidence.Converter.html) |  |  
| [*
Configured*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/lifecycle/Configured.html) |  |  
| [*
ConsoleLog*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/logs/text/ConsoleLog.html) |  |  
| [*
ConsoleLog.Format*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/logs/text/ConsoleLog.Format.html) |  |  
| [*
ConsoleLogger*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/loggers/ConsoleLogger.html) |  |  
| [*
ConsoleWriter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/listeners/ConsoleWriter.html) |  |  
| [*
Contains*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/collection/Contains.html) |  |  
| [*
Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/Converter.html) |  |  
| [*
CoreKernelLimits*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/project/CoreKernelLimits.html) |  |  
| [*
CoreKernelProject*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/project/CoreKernelProject.html) |  |  
| [*
Count*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/count/Count.html) |  |  
| [*
Count.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/count/Count.Converter.html) |  |  
| [*
CountMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/map/count/CountMap.html) |  |  
| [*
Countable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/numeric/Countable.html) |  |  
| [*
CountryIsoCode*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/locales/CountryIsoCode.html) |  |  
| [*
CriticalAlert*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/status/CriticalAlert.html) |  |  
| [*
DayOfWeek*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/DayOfWeek.html) |  |  
| [*
Debug*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/Debug.html) |  |  
| [*
Differences*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/comparison/Differences.html) |  |  
| [*
DoubleConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/primitive/DoubleConverter.html) |  |  
| [*
Doubles*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/primitives/Doubles.html) |  |  
| [*
Duration*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/Duration.html) |  |  
| [*
Duration.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/Duration.Converter.html) |  |  
| [*
Duration.MillisecondsConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/Duration.MillisecondsConverter.html) |  |  
| [*
Duration.Range*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/Duration.Range.html) |  |  
| [*
Duration.SecondsConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/Duration.SecondsConverter.html) |  |  
| [*
Ensure*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/validation/ensure/Ensure.html) | Validation Reporters |  
| | Failure Types |  
| | Unsupported Methods |  
| | Exception-Throwing Methods |  
| | Default Reporting |  
| | Fail Methods |  
| | Ensure Methods |  
| [*
EnumConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/enumeration/EnumConverter.html) |  |  
| [*
EnumGroup*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/patterns/group/EnumGroup.html) |  |  
| [*
EnumListConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/enumeration/EnumListConverter.html) |  |  
| [*
EnumSetConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/enumeration/EnumSetConverter.html) |  |  
| [*
Equality*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/comparison/Equality.html) |  |  
| [*
Escape*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/Escape.html) |  |  
| [*
Estimate*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/count/Estimate.html) |  |  
| [*
ExpiringReference*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/objects/reference/ExpiringReference.html) |  |  
| [*
Expression*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/patterns/Expression.html) |  |  
| [*
Extractor*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/extraction/Extractor.html) | Object Extraction |  
| [*
Factory*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/factory/Factory.html) |  |  
| [*
Failure*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/status/Failure.html) |  |  
| [*
Field*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/Field.html) |  |  
| [*
FieldGetter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/access/field/FieldGetter.html) |  |  
| [*
FieldSetter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/access/field/FieldSetter.html) |  |  
| [*
Filter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/comparison/Filter.html) |  |  
| [*
Filtered*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/comparison/Filtered.html) |  |  
| [*
FloatConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/primitive/FloatConverter.html) |  |  
| [*
Flushable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/io/Flushable.html) |  |  
| [*
FormattedDoubleConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/primitive/FormattedDoubleConverter.html) |  |  
| [*
FormattedIntegerConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/primitive/FormattedIntegerConverter.html) |  |  
| [*
FormattedLongConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/primitive/FormattedLongConverter.html) |  |  
| [*
Frequency*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/Frequency.html) |  |  
| [*
Frequency.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/Frequency.Converter.html) |  |  
| [*
Frequency.Cycle*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/Frequency.Cycle.html) |  |  
| [*
Getter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/access/Getter.html) |  |  
| [*
Group*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/patterns/group/Group.html) |  |  
| [*
HardReferencedValue*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/objects/reference/virtual/types/HardReferencedValue.html) |  |  
| [*
Hash*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/objects/Hash.html) |  |  
| [*
HexadecimalLongConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/primitive/HexadecimalLongConverter.html) |  |  
| [*
HexadecimalLongConverter.Style*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/primitive/HexadecimalLongConverter.Style.html) |  |  
| [*
HumanizedLocalDateTimeConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/conversion/converters/HumanizedLocalDateTimeConverter.html) |  |  
| [*
IO*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/io/IO.html) |  |  
| [*
IO.CopyStyle*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/io/IO.CopyStyle.html) |  |  
| [*
Identifiable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/model/Identifiable.html) |  |  
| [*
Identifier*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/identifier/Identifier.html) |  |  
| [*
Identifier.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/identifier/Identifier.Converter.html) |  |  
| [*
IdentifierFactory*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/identifier/IdentifierFactory.html) |  |  
| [*
IdentityConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/language/IdentityConverter.html) |  |  
| [*
Importance*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/Importance.html) |  |  
| [*
Incomplete*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/status/Incomplete.html) |  |  
| [*
Indent*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/Indent.html) |  |  
| [*
IndentingStringBuilder*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/formatting/IndentingStringBuilder.html) |  |  
| [*
IndentingStringBuilder.Indentation*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/formatting/IndentingStringBuilder.Indentation.html) |  |  
| [*
IndentingStringBuilder.Style*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/formatting/IndentingStringBuilder.Style.html) |  |  
| [*
Indexable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/collection/Indexable.html) |  |  
| [*
Indexed*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/collection/Indexed.html) |  |  
| [*
IndexedNameMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/map/string/IndexedNameMap.html) |  |  
| [*
Information*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/status/Information.html) |  |  
| [*
Initializable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/lifecycle/Initializable.html) |  |  
| [*
InitializationLatch*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/latches/InitializationLatch.html) |  |  
| [*
Initializer*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/model/Initializer.html) |  |  
| [*
IntMapFactory*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/factory/IntMapFactory.html) |  |  
| [*
IntegerConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/primitive/IntegerConverter.html) |  |  
| [*
IntegerIdentifier*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/identifier/IntegerIdentifier.html) |  |  
| [*
IntegerIdentifier.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/identifier/IntegerIdentifier.Converter.html) |  |  
| [*
Ints*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/primitives/Ints.html) |  |  
| [*
IsoTimeFormatter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/logs/text/formatters/IsoTimeFormatter.html) |  |  
| [*
Iterables*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/iteration/Iterables.html) |  |  
| [*
Iterators*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/iteration/Iterators.html) |  |  
| [*
JavaVirtualMachine*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/vm/JavaVirtualMachine.html) |  |  
| [*
JavaVirtualMachine.KivaExcludeFromSizeOf*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/vm/JavaVirtualMachine.KivaExcludeFromSizeOf.html) |  |  
| [*
JavaVirtualMachine.KivaExcludeFromSizeOfDebugTracing*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/vm/JavaVirtualMachine.KivaExcludeFromSizeOfDebugTracing.html) |  |  
| [*
JavaVirtualMachine.KivaNonCyclicObjectGraph*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/vm/JavaVirtualMachine.KivaNonCyclicObjectGraph.html) |  |  
| [*
JavaVirtualMachineHealth*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/vm/JavaVirtualMachineHealth.html) |  |  
| [*
Join*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/Join.html) |  |  
| [*
Keyed*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/collection/Keyed.html) |  |  
| [*
LanguageIsoCode*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/locales/LanguageIsoCode.html) |  |  
| [*
LanguageIsoCode.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/locales/LanguageIsoCode.Converter.html) |  |  
| [*
Lazy*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/objects/Lazy.html) |  |  
| [*
Level*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/level/Level.html) |  |  
| [*
Level.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/level/Level.Converter.html) |  |  
| [*
LimitedInput*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/io/LimitedInput.html) |  |  
| [*
LinkedObjectList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/list/LinkedObjectList.html) |  |  
| [*
Listener*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/Listener.html) | Listening to Broadcasters |  
| | Repeater Chains |  
| | Convenience Methods and Logging |  
| [*
Lists*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/list/Lists.html) |  |  
| [*
LiteralCharacter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/patterns/character/LiteralCharacter.html) |  |  
| [*
Loadable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/loading/Loadable.html) |  |  
| [*
LocalDateConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/conversion/converters/LocalDateConverter.html) |  |  
| [*
LocalDateTimeConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/conversion/converters/LocalDateTimeConverter.html) |  |  
| [*
LocalDateTimeWithMillisecondsConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/conversion/converters/LocalDateTimeWithMillisecondsConverter.html) |  |  
| [*
LocalDateTimeWithSecondsConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/conversion/converters/LocalDateTimeWithSecondsConverter.html) |  |  
| [*
LocalTime*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/LocalTime.html) |  |  
| [*
LocalTimeConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/conversion/converters/LocalTimeConverter.html) |  |  
| [*
Locale*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/locales/Locale.html) |  |  
| [*
Locator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/value/Locator.html) |  |  
| [*
Lock*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/locks/Lock.html) |  |  
| [*
Log*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/Log.html) |  |  
| [*
LogEntriesOfType*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/filters/LogEntriesOfType.html) |  |  
| [*
LogEntriesSubclassing*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/filters/LogEntriesSubclassing.html) |  |  
| [*
LogEntriesWithSeverityGreaterThanOrEqualTo*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/filters/LogEntriesWithSeverityGreaterThanOrEqualTo.html) |  |  
| [*
LogEntriesWithSeverityLessThanOrEqualTo*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/filters/LogEntriesWithSeverityLessThanOrEqualTo.html) |  |  
| [*
LogEntry*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/LogEntry.html) |  |  
| [*
LogEntryFormatter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/logs/text/LogEntryFormatter.html) |  |  
| [*
LogServiceLoader*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/loggers/LogServiceLoader.html) |  |  
| [*
LogServiceLogger*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/loggers/LogServiceLogger.html) |  |  
| [*
LogValidationReporter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/validation/reporters/LogValidationReporter.html) |  |  
| [*
Logger*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/Logger.html) |  |  
| [*
LoggerCodeContext*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/LoggerCodeContext.html) |  |  
| [*
LoggerFactory*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/LoggerFactory.html) |  |  
| [*
LoggersInPackage*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/filters/LoggersInPackage.html) |  |  
| [*
LongConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/primitive/LongConverter.html) |  |  
| [*
LongKeyed*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/collection/LongKeyed.html) |  |  
| [*
LongMapFactory*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/factory/LongMapFactory.html) |  |  
| [*
LongRange*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/count/LongRange.html) |  |  
| [*
Longs*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/primitives/Longs.html) |  |  
| [*
LookAheadReader*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/io/LookAheadReader.html) |  |  
| [*
Loopable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/code/Loopable.html) |  |  
| [*
MapFactory*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/factory/MapFactory.html) |  |  
| [*
MappedLazy*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/objects/MappedLazy.html) |  |  
| [*
Maps*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/map/Maps.html) |  |  
| [*
Matcher*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/comparison/Matcher.html) |  |  
| [*
MatcherSet*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/comparison/MatcherSet.html) |  |  
| [*
Matching*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/iteration/Matching.html) |  |  
| [*
Maximizable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/numeric/Maximizable.html) |  |  
| [*
Maximum*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/count/Maximum.html) |  |  
| [*
Maximum.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/count/Maximum.Converter.html) |  |  
| [*
Meridiem*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/Meridiem.html) |  |  
| [*
Message*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/Message.html) |  |  
| [*
Message.OperationStatus*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/Message.OperationStatus.html) |  |  
| [*
Message.Status*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/Message.Status.html) |  |  
| [*
MessageCounter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/listeners/MessageCounter.html) |  |  
| [*
MessageFilter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/MessageFilter.html) |  |  
| [*
MessageFormatter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/MessageFormatter.html) | Example |  
| | Interpolations |  
| [*
MessageFormatter.Format*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/MessageFormatter.Format.html) |  |  
| [*
MessageList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/listeners/MessageList.html) |  |  
| [*
MessageType*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/filters/MessageType.html) |  |  
| [*
Method*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/Method.html) |  |  
| [*
MethodGetter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/access/method/MethodGetter.html) |  |  
| [*
MethodSetter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/access/method/MethodSetter.html) |  |  
| [*
Minimizable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/numeric/Minimizable.html) |  |  
| [*
Minimum*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/count/Minimum.html) |  |  
| [*
Minimum.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/count/Minimum.Converter.html) |  |  
| [*
ModelListener*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/model/ModelListener.html) |  |  
| [*
ModificationTimestamped*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/ModificationTimestamped.html) |  |  
| [*
ModuleResource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/modules/ModuleResource.html) | NOTE |  
| [*
Modules*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/modules/Modules.html) |  |  
| [*
Monitor*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/locks/Monitor.html) |  |  
| [*
Multicaster*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/broadcasters/Multicaster.html) |  |  
| [*
MutableCount*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/count/MutableCount.html) |  |  
| [*
MutableIndex*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/mutable/MutableIndex.html) |  |  
| [*
MutableInteger*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/mutable/MutableInteger.html) |  |  
| [*
MutableLong*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/mutable/MutableLong.html) |  |  
| [*
MutableValue*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/mutable/MutableValue.html) |  |  
| [*
Name*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/name/Name.html) |  |  
| [*
NameMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/map/string/NameMap.html) |  |  
| [*
Nameable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/naming/Nameable.html) |  |  
| [*
Named*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/naming/Named.html) |  |  
| [*
NamedField*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/property/filters/field/NamedField.html) |  |  
| [*
NamedMethod*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/property/filters/method/NamedMethod.html) |  |  
| [*
NamedObject*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/naming/NamedObject.html) |  |  
| [*
NamedValue*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/naming/NamedValue.html) |  |  
| [*
Narration*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/status/Narration.html) |  |  
| [*
NewInstance*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/value/NewInstance.html) |  |  
| [*
Next*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/iteration/Next.html) |  |  
| [*
None*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/matching/matchers/None.html) |  |  
| [*
Normalize*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/Normalize.html) |  |  
| [*
Not*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/filters/operators/Not.html) |  |  
| [*
NotifyAllBooleanLock*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/locks/legacy/NotifyAllBooleanLock.html) |  |  
| [*
NotifyBooleanLock*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/locks/legacy/NotifyBooleanLock.html) |  |  
| [*
NullListener*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/listeners/NullListener.html) |  |  
| [*
NullLogger*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/loggers/NullLogger.html) |  |  
| [*
NullValidationReporter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/validation/reporters/NullValidationReporter.html) |  |  
| [*
ObjectFormatter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/formatting/ObjectFormatter.html) |  |  
| [*
ObjectFormatter.Format*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/formatting/ObjectFormatter.Format.html) |  |  
| [*
ObjectIdentifier*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/identifier/ObjectIdentifier.html) |  |  
| [*
ObjectList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/list/ObjectList.html) | Partitioning |  
| [*
ObjectMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/map/ObjectMap.html) |  |  
| [*
ObjectPopulator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/populator/ObjectPopulator.html) |  |  
| [*
ObjectSet*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/set/ObjectSet.html) |  |  
| [*
Objects*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/objects/Objects.html) |  |  
| [*
Objects.ClassIdentityMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/objects/Objects.ClassIdentityMap.html) |  |  
| [*
OneOrMore*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/patterns/closure/OneOrMore.html) |  |  
| [*
Openable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/io/Openable.html) |  |  
| [*
OperatingSystem*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/vm/OperatingSystem.html) |  |  
| [*
Operation*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/lifecycle/Operation.html) |  |  
| [*
OperationFailed*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/lifecycle/OperationFailed.html) |  |  
| [*
OperationHalted*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/lifecycle/OperationHalted.html) |  |  
| [*
OperationLifecycleMessage*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/OperationLifecycleMessage.html) |  |  
| [*
OperationMessage*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/OperationMessage.html) |  |  
| [*
OperationStarted*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/lifecycle/OperationStarted.html) |  |  
| [*
OperationStatusMessage*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/OperationStatusMessage.html) |  |  
| [*
OperationSucceeded*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/lifecycle/OperationSucceeded.html) |  |  
| [*
Optional*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/patterns/closure/Optional.html) |  |  
| [*
Or*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/patterns/logical/Or.html) |  |  
| [*
PackagePath*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/paths/PackagePath.html) | Parsing |  
| | Factories |  
| | Examples |  
| [*
PackagePathed*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/paths/PackagePathed.html) |  |  
| [*
Pair*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/objects/Pair.html) |  |  
| [*
Parenthesized*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/patterns/Parenthesized.html) |  |  
| [*
Path*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/paths/Path.html) | Relative and Absolute Paths |  
| | Checks |  
| | Element Retrieval |  
| | Functional Methods |  
| [*
PathStrings*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/PathStrings.html) |  |  
| [*
Pattern*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/patterns/Pattern.html) |  |  
| [*
PatternConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/language/PatternConverter.html) |  |  
| [*
Pausable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/lifecycle/Pausable.html) |  |  
| [*
Percent*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/level/Percent.html) |  |  
| [*
Percent.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/level/Percent.Converter.html) |  |  
| [*
Plural*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/Plural.html) |  |  
| [*
Prependable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/collection/Prependable.html) |  |  
| [*
Primes*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/math/Primes.html) |  |  
| [*
Priority*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/level/Priority.html) |  |  
| [*
Problem*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/status/Problem.html) |  |  
| [*
Processes*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/vm/Processes.html) |  |  
| [*
Progress*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/progress/reporters/Progress.html) |  |  
| [*
ProgressListener*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/progress/ProgressListener.html) |  |  
| [*
ProgressReporter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/progress/ProgressReporter.html) |  |  
| [*
ProgressiveInput*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/io/ProgressiveInput.html) |  |  
| [*
ProgressiveOutput*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/io/ProgressiveOutput.html) |  |  
| [*
Project*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/project/Project.html) | Dependencies and Initialization |  
| | Important Note: Project Initialization |  
| | Properties |  
| [*
Project.Visitor*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/project/Project.Visitor.html) |  |  
| [*
Property*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/property/Property.html) |  |  
| [*
PropertyFilter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/property/PropertyFilter.html) |  |  
| [*
PropertyValueSource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/property/PropertyValueSource.html) |  |  
| [*
Quantizable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/numeric/Quantizable.html) |  |  
| [*
Quantizable.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/numeric/Quantizable.Converter.html) |  |  
| [*
Quibble*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/status/Quibble.html) |  |  
| [*
Range*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/count/Range.html) |  |  
| [*
Ranged*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/numeric/Ranged.html) |  |  
| [*
Rate*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/Rate.html) |  |  
| [*
RateCalculator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/RateCalculator.html) |  |  
| [*
ReadWriteLock*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/locks/ReadWriteLock.html) |  |  
| [*
Readable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/io/Readable.html) |  |  
| [*
Receiver*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/messaging/Receiver.html) |  |  
| [*
ReentrancyTracker*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/status/ReentrancyTracker.html) |  |  
| [*
Release*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/project/Release.html) |  |  
| [*
Repeater*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/Repeater.html) |  |  
| [*
RepeatingThread*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/RepeatingThread.html) |  |  
| [*
Resettable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/lifecycle/Resettable.html) |  |  
| [*
Result*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/Result.html) |  |  
| [*
Retry*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/Retry.html) |  |  
| [*
Separators*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/formatting/Separators.html) |  |  
| [*
Sequence*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/collection/Sequence.html) | Joining Sequences |  
| | Checks |  
| | Iteration |  
| | Collections |  
| | Finding Elements and Subsequences |  
| [*
SequenceNumber*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/identifier/SequenceNumber.html) |  |  
| [*
Sets*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/set/Sets.html) |  |  
| [*
Setter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/access/Setter.html) |  |  
| [*
Severity*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/Severity.html) |  |  
| [*
SeverityGreaterThan*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/filters/SeverityGreaterThan.html) |  |  
| [*
SeverityGreaterThanOrEqualTo*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/filters/SeverityGreaterThanOrEqualTo.html) |  |  
| [*
SeverityLessThan*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/filters/SeverityLessThan.html) |  |  
| [*
SimpleFormatter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/logs/text/formatters/SimpleFormatter.html) |  |  
| [*
SimplifiedPattern*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/patterns/SimplifiedPattern.html) |  |  
| [*
Sized*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/numeric/Sized.html) |  |  
| [*
SoftReferencedValue*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/objects/reference/virtual/types/SoftReferencedValue.html) |  |  
| [*
Source*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/value/Source.html) |  |  
| [*
Split*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/Split.html) |  |  
| [*
StackTrace*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/context/StackTrace.html) |  |  
| [*
StackTrace.Frame*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/context/StackTrace.Frame.html) |  |  
| [*
Startable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/lifecycle/Startable.html) |  |  
| [*
StateMachine*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/conditions/StateMachine.html) |  |  
| [*
Stoppable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/lifecycle/Stoppable.html) |  |  
| [*
Streams*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/iteration/Streams.html) |  |  
| [*
Streams.Processing*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/iteration/Streams.Processing.html) |  |  
| [*
StringConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/StringConverter.html) |  |  
| [*
StringFormat*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/conversion/StringFormat.html) |  |  
| [*
StringIdentifier*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/identifier/StringIdentifier.html) |  |  
| [*
StringInput*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/io/StringInput.html) |  |  
| [*
StringList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/list/StringList.html) | Conversions |  
| | Length |  
| | Factory Methods |  
| | String Operations |  
| [*
StringMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/map/string/StringMap.html) |  |  
| [*
StringPath*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/paths/StringPath.html) | String Values |  
| | Parsing |  
| | Factories |  
| [*
StringReader*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/io/StringReader.html) |  |  
| [*
StringSource*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/string/StringSource.html) |  |  
| [*
StringTo*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/StringTo.html) |  |  
| [*
StringToStringMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/map/string/StringToStringMap.html) |  |  
| [*
Strings*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/Strings.html) |  |  
| [*
Strip*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/Strip.html) |  |  
| [*
SubClassesOf*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/filters/SubClassesOf.html) |  |  
| [*
Success*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/status/Success.html) |  |  
| [*
Kiva*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/Kiva.html) |  |  
| [*
KivaExcludeProperty*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/property/filters/KivaExcludeProperty.html) |  |  
| [*
KivaFormatProperty*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/formatting/KivaFormatProperty.html) |  |  
| [*
KivaIncludeProperty*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/property/filters/KivaIncludeProperty.html) |  |  
| [*
KivaProperties*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/property/filters/KivaProperties.html) |  |  
| [*
KivaPropertyConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/populator/KivaPropertyConverter.html) |  |  
| [*
KivaPropertyOptional*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/populator/KivaPropertyOptional.html) |  |  
| [*
KivaShutdownHook*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/vm/KivaShutdownHook.html) |  |  
| [*
KivaShutdownHook.Order*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/vm/KivaShutdownHook.Order.html) |  |  
| [*
KivaThread*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/KivaThread.html) |  |  
| [*
Then*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/patterns/logical/Then.html) |  |  
| [*
ThreadSnapshot*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/status/ThreadSnapshot.html) |  |  
| [*
ThreadStatus*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/status/ThreadStatus.html) |  |  
| [*
Threads*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/Threads.html) |  |  
| [*
ThrowableConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/language/ThrowableConverter.html) |  |  
| [*
ThrowingListener*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/listeners/ThrowingListener.html) |  |  
| [*
ThrowingValidationReporter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/validation/reporters/ThrowingValidationReporter.html) |  |  
| [*
Time*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/Time.html) |  |  
| [*
TimeAndMessageFormatter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/logging/logs/text/formatters/TimeAndMessageFormatter.html) |  |  
| [*
TimeFormat*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/conversion/TimeFormat.html) |  |  
| [*
TimeZones*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/time/TimeZones.html) |  |  
| [*
Trace*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/status/Trace.html) |  |  
| [*
Transceiver*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/Transceiver.html) | Debugging |  
| | Convenience Methods |  
| [*
Transmittable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/messaging/Transmittable.html) |  |  
| [*
Transmitter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/messaging/Transmitter.html) |  |  
| [*
Triaged*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/Triaged.html) |  |  
| [*
Type*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/reflection/Type.html) |  |  
| [*
Unloadable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/loading/Unloadable.html) |  |  
| [*
Unsupported*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/status/Unsupported.html) |  |  
| [*
Validatable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/validation/Validatable.html) |  |  
| [*
Validation*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/validation/Validation.html) |  |  
| [*
ValidationFailure*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/validation/reporters/ValidationFailure.html) |  |  
| [*
ValidationIssues*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/validation/listeners/ValidationIssues.html) |  |  
| [*
ValidationProblem*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/validation/ensure/ValidationProblem.html) |  |  
| [*
ValidationReporter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/validation/ValidationReporter.html) |  |  
| [*
Validator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/validation/Validator.html) |  |  
| [*
ValueWatcher*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/conditions/ValueWatcher.html) |  |  
| [*
Valued*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/value/Valued.html) |  |  
| [*
VariableMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/collections/map/string/VariableMap.html) |  |  
| [*
Version*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/version/Version.html) | Functional |  
| | Parsing |  
| | Comparison |  
| | Information |  
| [*
VersionConverter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/data/conversion/string/language/VersionConverter.html) |  |  
| [*
Versioned*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/version/Versioned.html) |  |  
| [*
VersionedObject*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/version/VersionedObject.html) |  |  
| [*
VirtualReference*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/objects/reference/virtual/VirtualReference.html) |  |  
| [*
VirtualReferenceTracker*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/objects/reference/virtual/VirtualReferenceTracker.html) |  |  
| [*
VirtualReferenceType*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/objects/reference/virtual/VirtualReferenceType.html) |  |  
| [*
WakeState*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/threading/status/WakeState.html) |  |  
| [*
Warning*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/messaging/messages/status/Warning.html) |  |  
| [*
Watchable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/model/Watchable.html) |  |  
| [*
WeakReferencedValue*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/objects/reference/virtual/types/WeakReferencedValue.html) |  |  
| [*
Weight*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/values/level/Weight.html) |  |  
| [*
WithName*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/matching/matchers/WithName.html) |  |  
| [*
WithNameMatching*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/matching/matchers/WithNameMatching.html) |  |  
| [*
Wrap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/strings/Wrap.html) |  |  
| [*
Writable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/interfaces/io/Writable.html) |  |  
| [*
ZeroOrMore*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.kernel/com/telenav/kivakit/core/kernel/language/patterns/closure/ZeroOrMore.html) |  |  

[//]: # (start-user-text)


[//]: # (end-user-text)

<br/>

![](documentation/images/horizontal-line.png)

<sub>Copyright 2011-2021 [Telenav](http://telenav.com), Inc. Licensed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by Lexakai on 2021.04.01</sub>    
<sub>UML diagrams courtesy of PlantUML (http://plantuml.com)</sub>

