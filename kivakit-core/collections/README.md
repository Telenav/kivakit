# kivakit-core collections &nbsp;&nbsp;![](https://www.kivakit.org/images/set-40.png)

This module provides collections, iteration support, primitive collections, stacks, collection observation and bit I/O.

![](https://www.kivakit.org/images/horizontal-line.png)

### Index

[**Batcher**](#batcher)  
[**Iterables and Iterators**](#iterables-and-iterators)  
[**Maps**](#maps)  
[**Sets**](#sets)  
[**Stack**](#stack)  
[**Collection Watching**](#collection-watching)  
[**Primitive Collections**](#primitive-collections)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

![](https://www.kivakit.org/images/horizontal-line.png)

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/dependencies-40.png)

[*Dependency Diagram*](documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId></artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>

![](https://www.kivakit.org/images/short-horizontal-line.png)

[//]: # (start-user-text)



[//]: # (end-user-text)

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; ![](https://www.kivakit.org/images/diagram-48.png)

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

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/box-40.png)

[*com.telenav.kivakit.core.collections.batcher*](documentation/diagrams/com.telenav.kivakit.core.collections.batcher.svg)  
[*com.telenav.kivakit.core.collections.iteration.iterables*](documentation/diagrams/com.telenav.kivakit.core.collections.iteration.iterables.svg)  
[*com.telenav.kivakit.core.collections.iteration.iterators*](documentation/diagrams/com.telenav.kivakit.core.collections.iteration.iterators.svg)  
[*com.telenav.kivakit.core.collections.map*](documentation/diagrams/com.telenav.kivakit.core.collections.map.svg)  
[*com.telenav.kivakit.core.collections.primitive*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.svg)  
[*com.telenav.kivakit.core.collections.primitive.array*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.svg)  
[*com.telenav.kivakit.core.collections.primitive.array.arrays*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.arrays.svg)  
[*com.telenav.kivakit.core.collections.primitive.array.bits*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.bits.svg)  
[*com.telenav.kivakit.core.collections.primitive.array.bits.io*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.bits.io.svg)  
[*com.telenav.kivakit.core.collections.primitive.array.bits.io.input*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.bits.io.input.svg)  
[*com.telenav.kivakit.core.collections.primitive.array.bits.io.output*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.bits.io.output.svg)  
[*com.telenav.kivakit.core.collections.primitive.array.packed*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.packed.svg)  
[*com.telenav.kivakit.core.collections.primitive.array.scalars*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.scalars.svg)  
[*com.telenav.kivakit.core.collections.primitive.array.strings*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.array.strings.svg)  
[*com.telenav.kivakit.core.collections.primitive.iteration*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.iteration.svg)  
[*com.telenav.kivakit.core.collections.primitive.list*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.list.svg)  
[*com.telenav.kivakit.core.collections.primitive.list.store*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.list.store.svg)  
[*com.telenav.kivakit.core.collections.primitive.map*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.svg)  
[*com.telenav.kivakit.core.collections.primitive.map.multi*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.multi.svg)  
[*com.telenav.kivakit.core.collections.primitive.map.multi.dynamic*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.multi.dynamic.svg)  
[*com.telenav.kivakit.core.collections.primitive.map.multi.fixed*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.multi.fixed.svg)  
[*com.telenav.kivakit.core.collections.primitive.map.objects*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.objects.svg)  
[*com.telenav.kivakit.core.collections.primitive.map.scalars*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.scalars.svg)  
[*com.telenav.kivakit.core.collections.primitive.map.split*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.map.split.svg)  
[*com.telenav.kivakit.core.collections.primitive.set*](documentation/diagrams/com.telenav.kivakit.core.collections.primitive.set.svg)  
[*com.telenav.kivakit.core.collections.project*](documentation/diagrams/com.telenav.kivakit.core.collections.project.svg)  
[*com.telenav.kivakit.core.collections.set*](documentation/diagrams/com.telenav.kivakit.core.collections.set.svg)  
[*com.telenav.kivakit.core.collections.set.logical*](documentation/diagrams/com.telenav.kivakit.core.collections.set.logical.svg)  
[*com.telenav.kivakit.core.collections.set.logical.operations*](documentation/diagrams/com.telenav.kivakit.core.collections.set.logical.operations.svg)  
[*com.telenav.kivakit.core.collections.stack*](documentation/diagrams/com.telenav.kivakit.core.collections.stack.svg)  
[*com.telenav.kivakit.core.collections.watcher*](documentation/diagrams/com.telenav.kivakit.core.collections.watcher.svg)  

![](https://www.kivakit.org/images/short-horizontal-line.png)

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; ![](https://www.kivakit.org/images/books-40.png)

Javadoc coverage for this project is 70.4%.  
  
&nbsp; &nbsp;  ![](https://www.kivakit.org/images/meter-70-12.png)

The following significant classes are undocumented:  

- BaseBitReader  
- BaseBitWriter  
- BitArray  
- LongToIntMultiMap  
- LongToLongMultiMap  
- LongToObjectMap  
- PrimitiveMap

| Class | Documentation Sections |
|---|---|
| [*BaseBitReader*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/input/BaseBitReader.html) |  |  
| [*BaseBitWriter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/output/BaseBitWriter.html) |  |  
| [*BaseCollectionChangeWatcher*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/watcher/BaseCollectionChangeWatcher.html) |  |  
| [*Batcher*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/batcher/Batcher.html) | Example |  
| | Adding Elements |  
| | Processing Elements |  
| [*Batcher.Batch*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/batcher/Batcher.Batch.html) |  |  
| [*Batcher.BatchAdder*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/batcher/Batcher.BatchAdder.html) |  |  
| [*BigSplitPackedArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/packed/BigSplitPackedArray.html) |  |  
| [*BitArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/BitArray.html) |  |  
| [*BitInput*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/input/BitInput.html) |  |  
| [*BitOutput*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/output/BitOutput.html) |  |  
| [*BitReader*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/BitReader.html) |  |  
| [*BitWriter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/BitWriter.html) |  |  
| [*ByteArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/ByteArray.html) |  |  
| [*ByteArrayArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/arrays/ByteArrayArray.html) |  |  
| [*ByteCollection*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/ByteCollection.html) |  |  
| [*ByteIterable*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/ByteIterable.html) |  |  
| [*ByteIterator*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/ByteIterator.html) |  |  
| [*ByteList*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/ByteList.html) |  |  
| [*CacheMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/map/CacheMap.html) |  |  
| [*CaseFoldingStringMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/map/CaseFoldingStringMap.html) |  |  
| [*CharArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/CharArray.html) |  |  
| [*CharArray.Converter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/CharArray.Converter.html) |  |  
| [*CharCollection*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/CharCollection.html) |  |  
| [*CharIterable*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/CharIterable.html) |  |  
| [*CharIterator*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/CharIterator.html) |  |  
| [*CharList*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/CharList.html) |  |  
| [*CollectionChangeListener*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/watcher/CollectionChangeListener.html) |  |  
| [*CollectionChangeWatcher*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/watcher/CollectionChangeWatcher.html) |  |  
| [*CompoundIterator*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/CompoundIterator.html) |  |  
| [*CompoundSet*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/set/CompoundSet.html) |  |  
| [*ConcurrentHashSet*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/set/ConcurrentHashSet.html) |  |  
| [*CoreCollectionsKryoTypes*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/project/CoreCollectionsKryoTypes.html) |  |  
| [*CoreCollectionsProject*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/project/CoreCollectionsProject.html) |  |  
| [*CoreCollectionsUnitTest*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/project/CoreCollectionsUnitTest.html) |  |  
| [*DeduplicatingIterable*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterables/DeduplicatingIterable.html) |  |  
| [*DeduplicatingIterator*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/DeduplicatingIterator.html) |  |  
| [*DefaultHashingStrategy*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/DefaultHashingStrategy.html) |  |  
| [*EmptyIterator*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/EmptyIterator.html) |  |  
| [*FilteredIterable*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterables/FilteredIterable.html) |  |  
| [*FilteredIterator*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/FilteredIterator.html) |  |  
| [*FixedSizeBitArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/FixedSizeBitArray.html) |  |  
| [*HashingStrategy*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/HashingStrategy.html) |  |  
| [*IdentitySet*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/set/IdentitySet.html) |  |  
| [*IntArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/IntArray.html) |  |  
| [*IntArray.Converter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/IntArray.Converter.html) |  |  
| [*IntArrayArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/arrays/IntArrayArray.html) |  |  
| [*IntCollection*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/IntCollection.html) |  |  
| [*IntIterable*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/IntIterable.html) |  |  
| [*IntIterator*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/IntIterator.html) |  |  
| [*IntLinkedListStore*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/store/IntLinkedListStore.html) |  |  
| [*IntList*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/IntList.html) |  |  
| [*IntMultiMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/IntMultiMap.html) |  |  
| [*IntToByteFixedMultiMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/IntToByteFixedMultiMap.html) |  |  
| [*IntToByteMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToByteMap.html) |  |  
| [*IntToByteMap.EntryVisitor*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToByteMap.EntryVisitor.html) |  |  
| [*IntToIntFixedMultiMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/IntToIntFixedMultiMap.html) |  |  
| [*IntToIntMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToIntMap.html) |  |  
| [*IntToIntMap.EntryVisitor*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToIntMap.EntryVisitor.html) |  |  
| [*IntToLongFixedMultiMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/IntToLongFixedMultiMap.html) |  |  
| [*IntToLongMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToLongMap.html) |  |  
| [*IntToLongMap.EntryVisitor*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/IntToLongMap.EntryVisitor.html) |  |  
| [*IntToPackedArrayFixedMultiMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/IntToPackedArrayFixedMultiMap.html) |  |  
| [*Intersection*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/operations/Intersection.html) |  |  
| [*LinkedMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/map/LinkedMap.html) |  |  
| [*LogicalSet*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/LogicalSet.html) |  |  
| [*LongArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/LongArray.html) |  |  
| [*LongArray.Converter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/LongArray.Converter.html) |  |  
| [*LongArrayArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/arrays/LongArrayArray.html) |  |  
| [*LongCollection*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/LongCollection.html) |  |  
| [*LongIterable*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/LongIterable.html) |  |  
| [*LongIterator*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/LongIterator.html) |  |  
| [*LongLinkedListStore*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/store/LongLinkedListStore.html) |  |  
| [*LongList*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/LongList.html) |  |  
| [*LongMultiMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/LongMultiMap.html) |  |  
| [*LongSet*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/set/LongSet.html) |  |  
| [*LongToByteFixedMultiMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/LongToByteFixedMultiMap.html) |  |  
| [*LongToByteMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToByteMap.html) |  |  
| [*LongToByteMap.EntryVisitor*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToByteMap.EntryVisitor.html) |  |  
| [*LongToIntFixedMultiMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/LongToIntFixedMultiMap.html) |  |  
| [*LongToIntMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToIntMap.html) |  |  
| [*LongToIntMap.EntryVisitor*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToIntMap.EntryVisitor.html) |  |  
| [*LongToIntMultiMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/dynamic/LongToIntMultiMap.html) |  |  
| [*LongToLongFixedMultiMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/fixed/LongToLongFixedMultiMap.html) |  |  
| [*LongToLongMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToLongMap.html) |  |  
| [*LongToLongMap.EntryVisitor*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/LongToLongMap.EntryVisitor.html) |  |  
| [*LongToLongMultiMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/dynamic/LongToLongMultiMap.html) |  |  
| [*LongToObjectMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/objects/LongToObjectMap.html) |  |  
| [*MultiMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/map/MultiMap.html) |  |  
| [*MultiSet*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/map/MultiSet.html) |  |  
| [*PackedArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/packed/PackedArray.html) |  |  
| [*PackedPrimitiveArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/packed/PackedPrimitiveArray.html) |  |  
| [*PackedPrimitiveArray.OverflowHandling*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/packed/PackedPrimitiveArray.OverflowHandling.html) |  |  
| [*PackedStringArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/strings/PackedStringArray.html) |  |  
| [*PackedStringArray.Type*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/strings/PackedStringArray.Type.html) |  |  
| [*PackedStringStore*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/store/PackedStringStore.html) |  |  
| [*PeriodicCollectionChangeWatcher*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/watcher/PeriodicCollectionChangeWatcher.html) |  |  
| [*PrimitiveArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/PrimitiveArray.html) |  |  
| [*PrimitiveArrayArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/PrimitiveArrayArray.html) |  |  
| [*PrimitiveCollection*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/PrimitiveCollection.html) |  |  
| [*PrimitiveCollection.AllocationStackTrace*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/PrimitiveCollection.AllocationStackTrace.html) |  |  
| [*PrimitiveCollection.CompressionRecord*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/PrimitiveCollection.CompressionRecord.html) |  |  
| [*PrimitiveCollection.IndexedToString*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/PrimitiveCollection.IndexedToString.html) |  |  
| [*PrimitiveIterator*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/PrimitiveIterator.html) |  |  
| [*PrimitiveList*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/PrimitiveList.html) |  |  
| [*PrimitiveListStore*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/store/PrimitiveListStore.html) |  |  
| [*PrimitiveMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/PrimitiveMap.html) |  |  
| [*PrimitiveMap.MapToString*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/PrimitiveMap.MapToString.html) |  |  
| [*PrimitiveMultiMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/PrimitiveMultiMap.html) |  |  
| [*PrimitiveMultiMap.MultiMapToString*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/PrimitiveMultiMap.MultiMapToString.html) |  |  
| [*PrimitiveScalarMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/PrimitiveScalarMap.html) |  |  
| [*PrimitiveScalarMultiMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/multi/PrimitiveScalarMultiMap.html) |  |  
| [*PrimitiveSet*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/set/PrimitiveSet.html) |  |  
| [*PrimitiveSet.SetToString*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/set/PrimitiveSet.SetToString.html) |  |  
| [*PrimitiveSplitArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/PrimitiveSplitArray.html) |  |  
| [*ReferenceCountMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/map/ReferenceCountMap.html) |  |  
| [*SetDifferencer*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/set/SetDifferencer.html) |  |  
| [*ShortArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/ShortArray.html) |  |  
| [*ShortArray.Converter*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/ShortArray.Converter.html) |  |  
| [*ShortCollection*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/ShortCollection.html) |  |  
| [*ShortIterable*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/ShortIterable.html) |  |  
| [*ShortIterator*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/iteration/ShortIterator.html) |  |  
| [*ShortList*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/list/ShortList.html) |  |  
| [*SingletonIterator*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/iteration/iterators/SingletonIterator.html) |  |  
| [*SplitByteArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/SplitByteArray.html) |  |  
| [*SplitCharArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/SplitCharArray.html) |  |  
| [*SplitIntArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/SplitIntArray.html) |  |  
| [*SplitIntToIntMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/split/SplitIntToIntMap.html) |  |  
| [*SplitLongArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/scalars/SplitLongArray.html) |  |  
| [*SplitLongSet*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/set/SplitLongSet.html) |  |  
| [*SplitLongToByteMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/split/SplitLongToByteMap.html) |  |  
| [*SplitLongToIntMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/split/SplitLongToIntMap.html) |  |  
| [*SplitLongToLongMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/split/SplitLongToLongMap.html) |  |  
| [*SplitPackedArray*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/packed/SplitPackedArray.html) |  |  
| [*SplitPrimitiveMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/SplitPrimitiveMap.html) |  |  
| [*Stack*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/stack/Stack.html) |  |  
| [*StringToIntMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/StringToIntMap.html) |  |  
| [*StringToObjectMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/map/scalars/StringToObjectMap.html) |  |  
| [*Subset*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/operations/Subset.html) |  |  
| [*TwoWayMap*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/map/TwoWayMap.html) |  |  
| [*Union*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/operations/Union.html) |  |  
| [*VariableReadSizeBitInput*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/primitive/array/bits/io/input/VariableReadSizeBitInput.html) |  |  
| [*Without*](https://telenav.github.io/kivakit-data/javadoc/kivakit.core.collections/com/telenav/kivakit/core/collections/set/logical/operations/Without.html) |  |  

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

![](https://www.kivakit.org/images/horizontal-line.png)

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai) on 2021.04.15. UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

