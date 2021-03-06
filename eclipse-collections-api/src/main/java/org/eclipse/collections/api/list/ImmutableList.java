/*
 * Copyright (c) 2015 Goldman Sachs.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.api.list;

import java.util.List;

import net.jcip.annotations.Immutable;
import org.eclipse.collections.api.block.HashingStrategy;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.function.primitive.BooleanFunction;
import org.eclipse.collections.api.block.function.primitive.ByteFunction;
import org.eclipse.collections.api.block.function.primitive.CharFunction;
import org.eclipse.collections.api.block.function.primitive.DoubleFunction;
import org.eclipse.collections.api.block.function.primitive.FloatFunction;
import org.eclipse.collections.api.block.function.primitive.IntFunction;
import org.eclipse.collections.api.block.function.primitive.LongFunction;
import org.eclipse.collections.api.block.function.primitive.ShortFunction;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.collection.ImmutableCollection;
import org.eclipse.collections.api.list.primitive.ImmutableBooleanList;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import org.eclipse.collections.api.list.primitive.ImmutableCharList;
import org.eclipse.collections.api.list.primitive.ImmutableDoubleList;
import org.eclipse.collections.api.list.primitive.ImmutableFloatList;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.eclipse.collections.api.list.primitive.ImmutableLongList;
import org.eclipse.collections.api.list.primitive.ImmutableShortList;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.eclipse.collections.api.partition.list.PartitionImmutableList;
import org.eclipse.collections.api.tuple.Pair;

/**
 * ImmutableList is the non-modifiable equivalent interface to {@link MutableList}. {@link MutableList#toImmutable()}
 * will give you an appropriately trimmed implementation of ImmutableList.  All ImmutableList implementations must
 * implement the java.util.List interface so they can satisfy the equals() contract and be compared against other list
 * structures like FastList or ArrayList.
 */
@Immutable
public interface ImmutableList<T>
        extends ImmutableCollection<T>, ListIterable<T>
{
    ImmutableList<T> newWith(T element);

    ImmutableList<T> newWithout(T element);

    ImmutableList<T> newWithAll(Iterable<? extends T> elements);

    ImmutableList<T> newWithoutAll(Iterable<? extends T> elements);

    ImmutableList<T> tap(Procedure<? super T> procedure);

    ImmutableList<T> select(Predicate<? super T> predicate);

    <P> ImmutableList<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    ImmutableList<T> reject(Predicate<? super T> predicate);

    <P> ImmutableList<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionImmutableList<T> partition(Predicate<? super T> predicate);

    <P> PartitionImmutableList<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <S> ImmutableList<S> selectInstancesOf(Class<S> clazz);

    <V> ImmutableList<V> collect(Function<? super T, ? extends V> function);

    ImmutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction);

    ImmutableByteList collectByte(ByteFunction<? super T> byteFunction);

    ImmutableCharList collectChar(CharFunction<? super T> charFunction);

    ImmutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction);

    ImmutableFloatList collectFloat(FloatFunction<? super T> floatFunction);

    ImmutableIntList collectInt(IntFunction<? super T> intFunction);

    ImmutableLongList collectLong(LongFunction<? super T> longFunction);

    ImmutableShortList collectShort(ShortFunction<? super T> shortFunction);

    <P, V> ImmutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> ImmutableList<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ImmutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    <V> ImmutableListMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ImmutableListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    ImmutableList<T> distinct();

    ImmutableList<T> distinct(HashingStrategy<? super T> hashingStrategy);

    <S> ImmutableList<Pair<T, S>> zip(Iterable<S> that);

    ImmutableList<Pair<T, Integer>> zipWithIndex();

    ImmutableList<T> take(int count);

    ImmutableList<T> takeWhile(Predicate<? super T> predicate);

    ImmutableList<T> drop(int count);

    ImmutableList<T> dropWhile(Predicate<? super T> predicate);

    PartitionImmutableList<T> partitionWhile(Predicate<? super T> predicate);

    List<T> castToList();

    /**
     * @see List#subList(int, int)
     * @since 6.0
     */
    ImmutableList<T> subList(int fromIndex, int toIndex);

    ImmutableList<T> toReversed();
}
