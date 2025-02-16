package com.example.backend.utils.object;

import com.example.backend.utils.AssertUtils;
import lombok.NonNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Collection工具类，包含一些常用的方法，如判断集合是否为空、是否不为空等。
 */
public class CollectionUtils extends ObjectUtils {
    public static boolean isEmpty(Collection<?> col) {
        return isNull(col) || col.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || isEmpty(map.entrySet());
    }

    public static boolean notEmpty(Collection<?> col) {
        return nonNull(col) && !col.isEmpty();
    }

    public static <T> List<T> toList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    public static <T> Set<T> toSet(T[] array) {
        return new HashSet<>(Arrays.asList(array));
    }

    public static <K, V> Map<K, V> toMap(List<K> keys, List<V> values) {
        AssertUtils.isEquals(keys.size(), values.size(), "to map but key size no equal value size");
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), values.get(i));
        }
        return map;
    }

    public static <T, K, V> Map<K, V> toMap(Collection<T> collection,
                                            Function<? super T, ? extends K> keyMapper,
                                            Function<? super T, ? extends V> valueMapper) {
        return collection.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public static <T> T[] toArray(Collection<T> col, T[] array) {
        return col.toArray(array);
    }

    /**
     * 转换类型
     * @param col 需要转换类型的集合
     * @param changedType 转换后的类型
     * @return 转换后的类型列表
     * @param <R> 原类型
     * @param <C> 替换后的类型
     */
    public static <R, C> List<C> mapToList(@NonNull Collection<R> col, Function<? super R, ? extends C> changedType) {
        return col.stream().map(changedType).collect(Collectors.toList());
    }

    public static <R, C> Set<C> mapToSet(@NonNull Collection<R> col, Function<? super R, ? extends C> changedType) {
        return col.stream().map(changedType).collect(Collectors.toSet());
    }

    public static <T, K> int indexOf(@NonNull List<T> cols, Function<? super T, ? extends K> func, K value) {
        List<? extends K> list = cols.stream().map(func).collect(Collectors.toList());
        return list.indexOf(value);
    }

    public static <T> void foreachIfNonNull(Collection<T> col, Consumer<? super T> action) {
        foreachIfNonNull(col, action, false);
    }

    public static <T> void foreachIfNonNull(Collection<T> col, Consumer<? super T> action, boolean throwable) {
        if(nonNull(col)) {
            for(T c : col) {
                action.accept(c);
            }
        }
        else if(throwable) {
            throw new NullPointerException("Collection is null");
        }
    }

    public static <K, V> void foreachIfNonNull(Map<K, V> map, BiConsumer<? super K, ? super  V> action) {
        foreachIfNonNull(map, action, false);
    }

    public static <K, V> void foreachIfNonNull(Map<K, V> map, BiConsumer<? super K, ? super  V> action, boolean throwable) {
        if(nonNull(map)) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                K k = entry.getKey();
                V v = entry.getValue();;
                action.accept(k, v);
            }
        }
        else if(throwable) {
            throw new NullPointerException("map is null");
        }
    }

    /**
     * 将null转换为空
     */
    public static <K, V> Map<K, V> getNonNull(Map<K, V> map) {
        return isNull(map) ? new HashMap<>() : map;
    }

    public static <K, V> void putIfNonNull(Map<K, V> argsMap, K key, V value) {
        if(nonNull(value)) {
            argsMap.put(key, value);
        }
    }
}
