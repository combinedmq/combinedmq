package com.github.combinedmq.util;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author xiaoyu
 */
@SuppressWarnings("unused")
public class ConfigUtils {
    /**
     * 从项目根目录加载文件
     *
     * @param fileName
     * @return
     */
    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        InputStream inputStream = asInputStream(fileName);
        if (null == inputStream) return null;
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    public static Object loadYaml(String fileName) {
        Yaml yaml = new Yaml();
        InputStream inputStream = asInputStream(fileName);
        if (null == inputStream) return null;
        Object load = yaml.load(inputStream);
        return load;
    }

    public static <T> T loadYaml(String fileName, Class<T> cls) {
        Yaml yaml = new Yaml();
        InputStream inputStream = asInputStream(fileName);
        if (null == inputStream) return null;
        T t = yaml.loadAs(inputStream, cls);
        return t;
    }

    private static InputStream asInputStream(String fileName) {
        return ClassLoader.getSystemResourceAsStream(fileName);
    }

}
