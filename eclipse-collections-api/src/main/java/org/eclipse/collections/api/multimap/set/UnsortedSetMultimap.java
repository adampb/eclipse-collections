/*
 * Copyright (c) 2015 Goldman Sachs.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.api.multimap.set;

import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.multimap.bag.UnsortedBagMultimap;
import org.eclipse.collections.api.set.UnsortedSetIterable;
import org.eclipse.collections.api.tuple.Pair;

public interface UnsortedSetMultimap<K, V> extends SetMultimap<K, V>
{
    UnsortedSetMultimap<K, V> newEmpty();

    UnsortedSetIterable<V> get(K key);

    MutableSetMultimap<K, V> toMutable();

    ImmutableSetMultimap<K, V> toImmutable();

    UnsortedSetMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate);

    UnsortedSetMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate);

    UnsortedSetMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    UnsortedSetMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    <K2, V2> UnsortedBagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <V2> UnsortedBagMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function);
}
