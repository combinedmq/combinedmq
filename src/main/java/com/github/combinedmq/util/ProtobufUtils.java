package com.github.combinedmq.util;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import sun.misc.Unsafe;

/**
 * @author xiaoyu
 */
public final class ProtobufUtils {

    private static final Unsafe UNSAFE = UnsafeUtils.getUnsafe();

    public static <T> byte[] serialize(T obj) {
        LinkedBuffer buffer = LinkedBuffer.allocate(512);
        try {
            Schema<T> schema = RuntimeSchema.getSchema((Class<T>) obj.getClass());
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            T t = (T) UNSAFE.allocateInstance(clazz);
            Schema<T> schema = RuntimeSchema.getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(bytes, t, schema);
            return t;
        } catch (Exception e) {
            throw new IllegalStateException("Deserialization error," + e.getMessage());
        }
    }

}
