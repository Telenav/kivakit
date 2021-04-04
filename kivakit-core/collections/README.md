# kivakit-core-collections &nbsp;&nbsp;![](../../documentation/images/set-40.png)

This module provides collections, iteration support, primitive collections, stacks, collection observation and bit I/O.

![](documentation/images/horizontal-line.png)

### Index

[**Batcher**](#batcher)  
[**Iterables and Iterators**](#iterables-and-iterators)  
[**Maps**](#maps)  
[**Sets**](#sets)  
[**Stack**](#stack)  
[**Collection Watching**](#collection-watching)  
[**Primitive Collections**](#primitive-collections)  
[**Dependencies**](#dependencies)  
[**Class Diagrams**](#class-diagrams)  
[**Package Diagrams**](#package-diagrams)  
[**Javadoc**](#javadoc)

![](documentation/images/horizontal-line.png)

[//]: # (start-user-text)


[//]: # (end-user-text)

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp;  ![](documentation/images/dependencies-40.png)

[*Dependency Diagram*](documentation/diagrams/dependencies.svg)

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-core-collections</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp;![](documentation/images/diagram-48.png)

[*Batch Processing*](documentation/diagrams/diagram-batch-processing.svg)  
[*Collection Watching*](documentation/diagrams/diagram-watcher.svg)  
[*Iteration and Iterables*](documentation/diagrams/diagram-iteration.svg)  
[*Maps*](documentation/diagrams/diagram-map.svg)  
[*Primitive Arrays*](documentation/diagrams/diagram-primitive-array.svg)  
[*Primitive Bit I/O*](documentation/diagrams/diagram-primitive-array-bit-io.svg)  
[*Primitive Collections*](documentation/diagrams/diagram-primitive-collection.svg)  
[*Primitive Lists*](documentation/diagrams/diagram-primitive-list.svg)  
[*Primitive Maps*](documentation/diagrams/diagram-primitive-map.svg)  
[*Primitive Multi-Maps*](documentation/diagrams/diagram-primitive-multi-map.svg)  
[*Primitive Sets*](documentation/diagrams/diagram-primitive-set.svg)  
[*Sets*](documentation/diagrams/diagram-set.svg)  
[*Split Primitive Arrays*](documentation/diagrams/diagram-primitive-split-array.svg)  
[*Stacks*](documentation/diagrams/diagram-stack.svg)  
[*Two-Dimensional Arrays*](documentation/diagrams/diagram-primitive-array-array.svg)

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp;![](documentation/images/box-40.png)

[*com.telenav.kivakit.core.collections.batcher*](documentation/diagrams/com.telenav.kivakit.core.collections.batcher.svg)  
[*
com.telenav.kivakit.core.collections.iteration.iterables*](documentation/diagrams/com.telenav.kivakit.core.collections.iteration.iterables.svg)  
[*
com.telenav.kivakit.core.collections.iteration.iterators*](documentation/diagrams/com.telenav.kivakit.core.collections.iteration.iterators.svg)  
[*com.telenav.kivakit.core.collections.map*](documentation/diagrams/com.telenav.kivakit.core.collections.map.svg)  
[*com.telenav.kivakit.core.collections.primitive*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.svg)  
[*com.telenav.kivakit.core.collections.primitive.array*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.svg)  
[*
com.telenav.kivakit.core.collections.primitive.array.arrays*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.arrays.svg)  
[*
com.telenav.kivakit.core.collections.primitive.array.bits*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.bits.svg)  
[*
com.telenav.kivakit.core.collections.primitive.array.bits.io*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.bits.io.svg)  
[*
com.telenav.kivakit.core.collections.primitive.array.bits.io.input*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.bits.io.input.svg)  
[*
com.telenav.kivakit.core.collections.primitive.array.bits.io.output*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.bits.io.output.svg)  
[*
com.telenav.kivakit.core.collections.primitive.array.packed*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.packed.svg)  
[*
com.telenav.kivakit.core.collections.primitive.array.scalars*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.scalars.svg)  
[*
com.telenav.kivakit.core.collections.primitive.array.strings*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.strings.svg)  
[*
com.telenav.kivakit.core.collections.primitive.iteration*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.iteration.svg)  
[*com.telenav.kivakit.core.collections.primitive.list*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.list.svg)  
[*
com.telenav.kivakit.core.collections.primitive.list.store*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.list.store.svg)  
[*com.telenav.kivakit.core.collections.primitive.map*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.svg)  
[*
com.telenav.kivakit.core.collections.primitive.map.multi*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.multi.svg)  
[*
com.telenav.kivakit.core.collections.primitive.map.multi.dynamic*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.multi.dynamic.svg)  
[*
com.telenav.kivakit.core.collections.primitive.map.multi.fixed*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.multi.fixed.svg)  
[*
com.telenav.kivakit.core.collections.primitive.map.objects*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.objects.svg)  
[*
com.telenav.kivakit.core.collections.primitive.map.scalars*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.scalars.svg)  
[*
com.telenav.kivakit.core.collections.primitive.map.split*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.split.svg)  
[*com.telenav.kivakit.core.collections.primitive.set*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.set.svg)  
[*com.telenav.kivakit.core.collections.project*](documentation/diagrams/com.telenav.kivakit.core.collections.project.svg)  
[*com.telenav.kivakit.core.collections.set*](documentation/diagrams/com.telenav.kivakit.core.collections.set.svg)  
[*com.telenav.kivakit.core.collections.set.logical*](documentation/diagrams/com.telenav.kivakit.core.collections.set.logical.svg)  
[*
com.telenav.kivakit.core.collections.set.logical.operations*](documentation/diagrams/com.telenav.kivakit.core.collections.set.logical.operations.svg)  
[*com.telenav.kivakit.core.collections.stack*](documentation/diagrams/com.telenav.kivakit.core.collections.stack.svg)  
[*com.telenav.kivakit.core.collections.watcher*](documentation/diagrams/com.telenav.kivakit.core.collections.watcher.svg)

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp;![](documentation/images/books-40.png)

| Class | Documentation Sections |
|---|---|
| [*
BaseBitReader*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/input/BaseBitReader.html) |  |  
| [*
BaseBitWriter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/output/BaseBitWriter.html) |  |  
| [*
BaseCollectionChangeWatcher*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/watcher/BaseCollectionChangeWatcher.html) |  |  
| [*
Batcher*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/batcher/Batcher.html) | Example |  
| | Adding Elements |  
| | Processing Elements |  
| [*
Batcher.Batch*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/batcher/Batcher.Batch.html) |  |  
| [*
Batcher.BatchAdder*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/batcher/Batcher.BatchAdder.html) |  |  
| [*
BigSplitPackedArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/packed/BigSplitPackedArray.html) |  |  
| [*
BitArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/BitArray.html) |  |  
| [*
BitInput*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/input/BitInput.html) |  |  
| [*
BitOutput*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/output/BitOutput.html) |  |  
| [*
BitReader*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/BitReader.html) |  |  
| [*
BitWriter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/BitWriter.html) |  |  
| [*
ByteArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/ByteArray.html) |  |  
| [*
ByteArrayArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/arrays/ByteArrayArray.html) |  |  
| [*
ByteCollection*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/ByteCollection.html) |  |  
| [*
ByteIterable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/ByteIterable.html) |  |  
| [*
ByteIterator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/ByteIterator.html) |  |  
| [*
ByteList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/ByteList.html) |  |  
| [*
CacheMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/map/CacheMap.html) |  |  
| [*
CaseFoldingStringMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/map/CaseFoldingStringMap.html) |  |  
| [*
CharArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/CharArray.html) |  |  
| [*
CharArray.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/CharArray.Converter.html) |  |  
| [*
CharCollection*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/CharCollection.html) |  |  
| [*
CharIterable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/CharIterable.html) |  |  
| [*
CharIterator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/CharIterator.html) |  |  
| [*
CharList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/CharList.html) |  |  
| [*
CollectionChangeListener*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/watcher/CollectionChangeListener.html) |  |  
| [*
CollectionChangeWatcher*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/watcher/CollectionChangeWatcher.html) |  |  
| [*
CompoundIterator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/CompoundIterator.html) |  |  
| [*
CompoundSet*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/set/CompoundSet.html) |  |  
| [*
ConcurrentHashSet*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/set/ConcurrentHashSet.html) |  |  
| [*
CoreCollectionsKryoTypes*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/project/CoreCollectionsKryoTypes.html) |  |  
| [*
CoreCollectionsProject*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/project/CoreCollectionsProject.html) |  |  
| [*
CoreCollectionsUnitTest*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/project/CoreCollectionsUnitTest.html) |  |  
| [*
DeduplicatingIterable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterables/DeduplicatingIterable.html) |  |  
| [*
DeduplicatingIterator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/DeduplicatingIterator.html) |  |  
| [*
DefaultHashingStrategy*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/DefaultHashingStrategy.html) |  |  
| [*
EmptyIterator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/EmptyIterator.html) |  |  
| [*
FilteredIterable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterables/FilteredIterable.html) |  |  
| [*
FilteredIterator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/FilteredIterator.html) |  |  
| [*
FixedSizeBitArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/FixedSizeBitArray.html) |  |  
| [*
HashingStrategy*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/HashingStrategy.html) |  |  
| [*
IdentitySet*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/set/IdentitySet.html) |  |  
| [*
IntArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/IntArray.html) |  |  
| [*
IntArray.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/IntArray.Converter.html) |  |  
| [*
IntArrayArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/arrays/IntArrayArray.html) |  |  
| [*
IntCollection*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/IntCollection.html) |  |  
| [*
IntIterable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/IntIterable.html) |  |  
| [*
IntIterator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/IntIterator.html) |  |  
| [*
IntLinkedListStore*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/store/IntLinkedListStore.html) |  |  
| [*
IntList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/IntList.html) |  |  
| [*
IntList.Adapter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/IntList.Adapter.html) |  |  
| [*
IntMultiMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/IntMultiMap.html) |  |  
| [*
IntToByteFixedMultiMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/IntToByteFixedMultiMap.html) |  |  
| [*
IntToByteMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToByteMap.html) |  |  
| [*
IntToByteMap.EntryVisitor*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToByteMap.EntryVisitor.html) |  |  
| [*
IntToIntFixedMultiMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/IntToIntFixedMultiMap.html) |  |  
| [*
IntToIntMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToIntMap.html) |  |  
| [*
IntToIntMap.EntryVisitor*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToIntMap.EntryVisitor.html) |  |  
| [*
IntToLongFixedMultiMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/IntToLongFixedMultiMap.html) |  |  
| [*
IntToLongMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToLongMap.html) |  |  
| [*
IntToLongMap.EntryVisitor*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToLongMap.EntryVisitor.html) |  |  
| [*
IntToPackedArrayFixedMultiMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/IntToPackedArrayFixedMultiMap.html) |  |  
| [*
Intersection*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/operations/Intersection.html) |  |  
| [*
LinkedMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/map/LinkedMap.html) |  |  
| [*
LogicalSet*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/LogicalSet.html) |  |  
| [*
LongArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/LongArray.html) |  |  
| [*
LongArray.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/LongArray.Converter.html) |  |  
| [*
LongArrayArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/arrays/LongArrayArray.html) |  |  
| [*
LongCollection*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/LongCollection.html) |  |  
| [*
LongIterable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/LongIterable.html) |  |  
| [*
LongIterator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/LongIterator.html) |  |  
| [*
LongLinkedListStore*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/store/LongLinkedListStore.html) |  |  
| [*
LongList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/LongList.html) |  |  
| [*
LongMultiMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/LongMultiMap.html) |  |  
| [*
LongSet*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/set/LongSet.html) |  |  
| [*
LongToByteFixedMultiMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/LongToByteFixedMultiMap.html) |  |  
| [*
LongToByteMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToByteMap.html) |  |  
| [*
LongToByteMap.EntryVisitor*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToByteMap.EntryVisitor.html) |  |  
| [*
LongToIntFixedMultiMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/LongToIntFixedMultiMap.html) |  |  
| [*
LongToIntMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToIntMap.html) |  |  
| [*
LongToIntMap.EntryVisitor*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToIntMap.EntryVisitor.html) |  |  
| [*
LongToIntMultiMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/dynamic/LongToIntMultiMap.html) |  |  
| [*
LongToLongFixedMultiMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/LongToLongFixedMultiMap.html) |  |  
| [*
LongToLongMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToLongMap.html) |  |  
| [*
LongToLongMap.EntryVisitor*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToLongMap.EntryVisitor.html) |  |  
| [*
LongToLongMultiMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/dynamic/LongToLongMultiMap.html) |  |  
| [*
LongToObjectMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/objects/LongToObjectMap.html) |  |  
| [*
MultiMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/map/MultiMap.html) |  |  
| [*
MultiSet*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/map/MultiSet.html) |  |  
| [*
PackedArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/packed/PackedArray.html) |  |  
| [*
PackedPrimitiveArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/packed/PackedPrimitiveArray.html) |  |  
| [*
PackedPrimitiveArray.OverflowHandling*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/packed/PackedPrimitiveArray.OverflowHandling.html) |  |  
| [*
PackedStringArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/strings/PackedStringArray.html) |  |  
| [*
PackedStringArray.Type*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/strings/PackedStringArray.Type.html) |  |  
| [*
PackedStringStore*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/store/PackedStringStore.html) |  |  
| [*
PeriodicCollectionChangeWatcher*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/watcher/PeriodicCollectionChangeWatcher.html) |  |  
| [*
PrimitiveArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/PrimitiveArray.html) |  |  
| [*
PrimitiveArrayArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/PrimitiveArrayArray.html) |  |  
| [*
PrimitiveCollection*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/PrimitiveCollection.html) |  |  
| [*
PrimitiveCollection.AllocationStackTrace*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/PrimitiveCollection.AllocationStackTrace.html) |  |  
| [*
PrimitiveCollection.CompressionRecord*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/PrimitiveCollection.CompressionRecord.html) |  |  
| [*
PrimitiveCollection.IndexedToString*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/PrimitiveCollection.IndexedToString.html) |  |  
| [*
PrimitiveIterator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/PrimitiveIterator.html) |  |  
| [*
PrimitiveList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/PrimitiveList.html) |  |  
| [*
PrimitiveListStore*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/store/PrimitiveListStore.html) |  |  
| [*
PrimitiveMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/PrimitiveMap.html) |  |  
| [*
PrimitiveMap.MapToString*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/PrimitiveMap.MapToString.html) |  |  
| [*
PrimitiveMultiMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/PrimitiveMultiMap.html) |  |  
| [*
PrimitiveMultiMap.MultiMapToString*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/PrimitiveMultiMap.MultiMapToString.html) |  |  
| [*
PrimitiveScalarMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/PrimitiveScalarMap.html) |  |  
| [*
PrimitiveScalarMultiMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/PrimitiveScalarMultiMap.html) |  |  
| [*
PrimitiveSet*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/set/PrimitiveSet.html) |  |  
| [*
PrimitiveSet.SetToString*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/set/PrimitiveSet.SetToString.html) |  |  
| [*
PrimitiveSplitArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/PrimitiveSplitArray.html) |  |  
| [*
ReferenceCountMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/map/ReferenceCountMap.html) |  |  
| [*
SetDifferencer*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/set/SetDifferencer.html) |  |  
| [*
ShortArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/ShortArray.html) |  |  
| [*
ShortArray.Converter*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/ShortArray.Converter.html) |  |  
| [*
ShortCollection*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/ShortCollection.html) |  |  
| [*
ShortIterable*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/ShortIterable.html) |  |  
| [*
ShortIterator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/ShortIterator.html) |  |  
| [*
ShortList*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/ShortList.html) |  |  
| [*
SingletonIterator*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/SingletonIterator.html) |  |  
| [*
SplitByteArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/SplitByteArray.html) |  |  
| [*
SplitCharArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/SplitCharArray.html) |  |  
| [*
SplitIntArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/SplitIntArray.html) |  |  
| [*
SplitIntToIntMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/split/SplitIntToIntMap.html) |  |  
| [*
SplitLongArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/SplitLongArray.html) |  |  
| [*
SplitLongSet*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/set/SplitLongSet.html) |  |  
| [*
SplitLongToByteMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/split/SplitLongToByteMap.html) |  |  
| [*
SplitLongToIntMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/split/SplitLongToIntMap.html) |  |  
| [*
SplitLongToLongMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/split/SplitLongToLongMap.html) |  |  
| [*
SplitPackedArray*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/packed/SplitPackedArray.html) |  |  
| [*
SplitPrimitiveMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/SplitPrimitiveMap.html) |  |  
| [*
Stack*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/stack/Stack.html) |  |  
| [*
StringToIntMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/StringToIntMap.html) |  |  
| [*
StringToObjectMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/StringToObjectMap.html) |  |  
| [*
Subset*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/operations/Subset.html) |  |  
| [*
TwoWayMap*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/map/TwoWayMap.html) |  |  
| [*
Union*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/operations/Union.html) |  |  
| [*
VariableReadSizeBitInput*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/input/VariableReadSizeBitInput.html) |  |  
| [*
Without*](http://telenav-kivakit.mypna.com/0.9.0-SNAPSHOT/apidocs/com.telenav.kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/operations/Without.html) |  |  

[//]: # (start-user-text)

### Batcher <a name = "batcher"></a>

The Batcher class combines an ArrayBlockingQueue with an ExecutorService to create a batch-processor which  
aggregates elements in batches and processes them with a set of worker threads. A batcher is created like this, passing in the maximum
number of batches in the queue and the size of each batch:

    Batcher<Record> batcher = new Batcher<>("RecordBatcher", Maximum._16, Count.16_384)
    {
        protected void onBatch(final Batch batch)
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

### Primitive Collections <a name = "primitive-collections"></a>

The *primitive* package contains an extensive set of classes for storing data in primitive data structures, including:

- Dynamic primitive arrays (1 and 2 dimensional)
- Primitive lists, maps, sets and multi-maps
- Bit arrays and bitwise I/O
- Bit-packed arrays
- *Split* versions of many data structures which virtualize several primitive data structures into a single  
  large one. This can be beneficial in keeping object allocation and garbage collection under control.

[//]: # (end-user-text)

<br/>

![](documentation/images/horizontal-line.png)

<sub>Copyright 2011-2021 [Telenav](http://telenav.com), Inc. Licensed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by Lexakai on 2021.04.01</sub>    
<sub>UML diagrams courtesy of PlantUML (http://plantuml.com)</sub>

