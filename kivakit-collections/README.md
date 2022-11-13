[//]: # (start-user-text)

<a href="https://www.kivakit.org">
<img src="https://telenav.github.io/telenav-assets/images/icons/web-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/web-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://twitter.com/openkivakit">
<img src="https://telenav.github.io/telenav-assets/images/logos/twitter/twitter-32.png" srcset="https://telenav.github.io/telenav-assets/images/logos/twitter/twitter-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://kivakit.zulipchat.com">
<img src="https://telenav.github.io/telenav-assets/images/logos/zulip/zulip-32.png" srcset="https://telenav.github.io/telenav-assets/images/logos/zulip/zulip-32-2x.png 2x"/>
</a>

[//]: # (end-user-text)

# kivakit-collections &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/set-48.png" srcset="https://telenav.github.io/telenav-assets/images/icons/set-48-2x.png 2x"/>

This module provides collections, iteration support, stacks, collection observation and bit I/O.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Index

[**Batcher**](#batcher)  
[**Iterables and Iterators**](#iterables-and-iterators)  
[**Maps**](#maps)  
[**Sets**](#sets)  
[**Stack**](#stack)  
[**Collection Watching**](#collection-watching)  

[**Dependencies**](#dependencies) | [**Code Quality**](#code-quality) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/dependencies-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.8.4/lexakai/kivakit/kivakit-collections/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-collections</artifactId>
        <version>1.8.4</version>
    </dependency>

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Batcher <a name = "batcher"></a>

The Batcher class combines an ArrayBlockingQueue with an ExecutorService to create a batch-processor which
aggregates elements in batches and processes them with a set of worker threads. A batcher is created like this, passing in the maximum
number of batches in the queue and the size of each batch:

    Batcher<Record> batcher = new Batcher<>("RecordBatcher", Maximum._16, Count.16_384)
    {
        protected void onBatch( Batch batch)
        {
            for (var record : batch)
            {
                [...]
            }
        }
    };

The batcher lifecycle then looks like this:

    batcher.start(Count._8);

        [...]

    batcher.adder().add(record);

        [...]

    batcher.close();

### Iterables and Iterators <a name = "iterables-and-iterators"></a>

Several implementations of the Java *Iterable* and *Iterator* interfaces are available:

- *DeduplicatingIterable* / *DeduplicatingIterator* - Wraps an *Iterable* or *Iterator* an iterates through
  all elements, keeping a set of elements that have been seen. Elements that have already been seen
  are skipped.
- *FilteredIterable* / *FilteredIterator* - Wraps an *Iterable* or *Iterator*, returning only elements that
  match the given Matcher.
- *CompoundIterator* - Combines a sequence of iterators into a single iterator
- *EmptyIterator* - An *Iterator* with no elements
- *SingletonIterator* - An *Iterator* with a single element

### Maps <a name = "maps"></a>

The available implementations of the *Map* interface in *kivakit-core-collections* extend the abstract base class,
*BaseMap*, from *kivakit-core-kernel*:

- *CacheMap* - A most-recently-used (MRU) map which caches elements up to a maximum capacity
- *CaseFoldingStringMap* - A *StringMap* implementation which is case-independent
- *LinkedMap* - A map that uses LinkedHashMap as its implementation
- *ReferenceCountMap* - A map that counts the number of references to its key type
- *TwoWayMap* - A map that can also map from value back to key
- *MultiMap* - A map from a single key to a list of values
- *MultiSet* - A map from a single key to a set of values

### Sets <a name = "sets"></a>

The *set* package provides several useful implementations of *BaseSet* as well as a set differencer that
determines what changes are required to turn one set into another.

- *CompoundSet* - A set of sets that logically combines the sets without creating a new set
- *ConcurrentHashSet* - A hash set implementation that is thread-safe
- *IdentitySet* - A set based on reference and not the *hashCode()* / *equals()* contract
- *LogicalSet* - An operation applied to two sets that logically combines the sets without actually
  combining the sets. Implementations include *Intersection*, *Subset*, *Union* and *Without*
- *SetDifferencer* - Determines what changes are required to turn one set into another

### Stack <a name = "stack"></a>

The *Stack* class is a subclass of *ObjectList* which adds push() and pop() methods.

### Collection Watching <a name = "collection-watching"></a>

The *watcher* package contains a *BaseCollectionChangeWatcher* for observing changes to collections of
objects (for example, a set of *File* objects). This base is extended by *PeriodicCollectionChangeWatcher*
which observes a given collection at a set *Frequency*. When the collection of objects changes, one or
more methods in *CollectionChangeListener* is called with information about what changed.

[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Code Quality <a name="code-quality"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/ruler-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/ruler-32-2x.png 2x"/>

Code quality for this project is 68.9%.  
  
&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-70-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-70-96-2x.png 2x"/>

| Measurement   | Value                    |
|---------------|--------------------------|
| Stability     | 100.0%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-100-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-100-96-2x.png 2x"/>     |
| Testing       | 6.7%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-10-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-10-96-2x.png 2x"/>       |
| Documentation | 100.0%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-100-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-100-96-2x.png 2x"/> |

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/diagram-40.png" srcset="https://telenav.github.io/telenav-assets/images/icons/diagram-40-2x.png 2x"/>

[*Collection Watching*](https://www.kivakit.org/1.8.4/lexakai/kivakit/kivakit-collections/documentation/diagrams/diagram-watcher.svg)  
[*Maps*](https://www.kivakit.org/1.8.4/lexakai/kivakit/kivakit-collections/documentation/diagrams/diagram-map.svg)  
[*Sets*](https://www.kivakit.org/1.8.4/lexakai/kivakit/kivakit-collections/documentation/diagrams/diagram-set.svg)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/box-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/box-24-2x.png 2x"/>

[*com.telenav.kivakit.collections.internal.lexakai*](https://www.kivakit.org/1.8.4/lexakai/kivakit/kivakit-collections/documentation/diagrams/com.telenav.kivakit.collections.internal.lexakai.svg)  
[*com.telenav.kivakit.collections.map*](https://www.kivakit.org/1.8.4/lexakai/kivakit/kivakit-collections/documentation/diagrams/com.telenav.kivakit.collections.map.svg)  
[*com.telenav.kivakit.collections.set*](https://www.kivakit.org/1.8.4/lexakai/kivakit/kivakit-collections/documentation/diagrams/com.telenav.kivakit.collections.set.svg)  
[*com.telenav.kivakit.collections.set.operations*](https://www.kivakit.org/1.8.4/lexakai/kivakit/kivakit-collections/documentation/diagrams/com.telenav.kivakit.collections.set.operations.svg)  
[*com.telenav.kivakit.collections.watcher*](https://www.kivakit.org/1.8.4/lexakai/kivakit/kivakit-collections/documentation/diagrams/com.telenav.kivakit.collections.watcher.svg)

### Javadoc <a name="code-quality"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/books-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/books-24-2x.png 2x"/>

| Class | Documentation Sections  |
|-------|-------------------------|
| [*BaseCollectionChangeWatcher*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/watcher/BaseCollectionChangeWatcher.html) | Changes |  
| | Listeners |  
| [*BaseIndexedMap*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/map/BaseIndexedMap.html) | Access |  
| | Operations |  
| [*CollectionChangeListener*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/watcher/CollectionChangeListener.html) |  |  
| [*CollectionChangeWatcher*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/watcher/CollectionChangeWatcher.html) |  |  
| [*CompoundSet*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/set/CompoundSet.html) |  |  
| [*DiagramMap*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/internal/lexakai/DiagramMap.html) |  |  
| [*DiagramSet*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/internal/lexakai/DiagramSet.html) |  |  
| [*DiagramWatcher*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/internal/lexakai/DiagramWatcher.html) |  |  
| [*IndexedNameMap*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/map/IndexedNameMap.html) | Example |  
| | Access |  
| | Operations |  
| [*Intersection*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/set/operations/Intersection.html) |  |  
| [*LogicalSet*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/set/LogicalSet.html) |  |  
| [*PeriodicCollectionChangeWatcher*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/watcher/PeriodicCollectionChangeWatcher.html) | Lifecycle |  
| | Collection Access |  
| [*ReferenceCountMap*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/map/ReferenceCountMap.html) | Reference Counts |  
| | Referencing |  
| [*SetDifferencer*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/set/SetDifferencer.html) | Comparison |  
| [*Subset*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/set/operations/Subset.html) |  |  
| [*TwoWayMap*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/map/TwoWayMap.html) | Access |  
| | Operations |  
| [*Union*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/set/operations/Union.html) |  |  
| [*Without*](https://www.kivakit.org/1.8.4/javadoc/kivakit/kivakit-collections/com/telenav/kivakit/collections/set/operations/Without.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>
