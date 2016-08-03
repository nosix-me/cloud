package com.nosix.cloud.common.extension;

import com.nosix.cloud.common.extension.support.SpiGroupComparator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class SpiLoader<T> {
    private static final String DEFAULT_CHARACTER = "utf-8";
    private static final String PREFIX = "META-INF/services/";

    private static ConcurrentMap<Class<?>, SpiLoader<?>> spiLoaders = new ConcurrentHashMap<Class<?>, SpiLoader<?>>();

    private ConcurrentMap<String, Class<T>> extensionClasses = null;
    private ConcurrentMap<String, T> extensionInstances = null;

    private Class<T> type;
    private ClassLoader classLoader;

    private SpiLoader(Class<T> type) {
        this(type, Thread.currentThread().getContextClassLoader());
    }

    private SpiLoader(Class<T> type, ClassLoader classLoader) {
        this.type = type;
        this.classLoader = classLoader;
    }

    @SuppressWarnings("unchecked")
    public static <T> SpiLoader<T> getInstance(Class<T> type) {
        SpiLoader<T> instance = (SpiLoader<T>) spiLoaders.get(type);
        if (instance != null) {
            return instance;
        }

        synchronized (spiLoaders) {
            instance = (SpiLoader<T>) spiLoaders.get(type);
            if (instance == null) {
                instance = new SpiLoader<T>(type);
                instance.init();
                spiLoaders.putIfAbsent(type, instance);
            }
        }

        instance = (SpiLoader<T>) spiLoaders.get(type);

        return instance;
    }

    public T getExtension(String name) {
        if (name == null) {
            return null;
        }

        Class<T> clz = extensionClasses.get(name);
        if (clz == null) {
            return null;
        }

        SpiScope spiScope = getSpiScope(clz);
        if (spiScope.equals(SpiScope.PROTOTYPE)) {
            return initPrototype(clz);
        } else if (spiScope.equals(SpiScope.SINGLETON)) {
            return initSingleton(name, clz);
        }

        return null;
    }

    public List<T> getExtensions(String groupName) {
        if (groupName == null) {
            return null;
        }
        List<T> list = new ArrayList<T>();
        for (Entry<String, Class<T>> entry : extensionClasses.entrySet()) {
            Class<T> clz = entry.getValue();
            if (groupName.equals(getSpiGroup(clz))) {
                list.add(initSingleton(getSpiGroupName(clz), clz));
            }
        }

        Collections.sort(list, new SpiGroupComparator());
        return list;
    }

    private void init() {
        checkSpiInterface(type);
        List<String> classNameList = loadFile(PREFIX + type.getName());
        extensionClasses = loadClass(type, classNameList);
        extensionInstances = new ConcurrentHashMap<String, T>();
    }

    private void checkSpiInterface(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Error extension type is null");
        }

        if (!type.isInterface()) {
            throw new IllegalArgumentException("Error extension type(" + type + ") is not interface");
        }

        if (!type.isAnnotationPresent(Spi.class)) {
            throw new IllegalArgumentException("Error extension type without @Spi annotation");
        }
    }

    private List<String> loadFile(String fullName) {
        List<String> classNames = new ArrayList<String>();
        try {
            Enumeration<URL> urls = classLoader.getResources(fullName);
            if (urls == null || !urls.hasMoreElements()) {
                return classNames;
            }

            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                parseUrl(url, classNames);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error extension file (" + fullName + ") load");
        }

        return classNames;
    }

    private void parseUrl(URL url, List<String> classNames) {
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = url.openStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, DEFAULT_CHARACTER));
            String line = null;
            while ((line = reader.readLine()) != null) {
                parseLine(url, line, classNames);
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(url.toString() + " encode error");
        } catch (IOException e) {
            throw new IllegalArgumentException(url.toString() + " read error");
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseLine(URL url, String line, List<String> names) throws IOException{
        int ci = line.indexOf('#');
        if (ci >= 0) {
            line = line.substring(0, ci);
        }
        line = line.trim();
        if (line.length() <= 0) {
            return;
        }

        if ((line.indexOf(' ') >= 0) || (line.indexOf('\t') >= 0)) {
            throw new IllegalArgumentException(url.toString() + " Illegal spi configuration-file syntax");
        }

        int cp = line.codePointAt(0);
        if (!Character.isJavaIdentifierStart(cp)) {
            throw new IllegalArgumentException(url.toString() + " Illegal spi provider-class name");
        }

        for (int i = Character.charCount(cp); i < line.length(); i += Character.charCount(cp)) {
            cp = line.codePointAt(i);
            if (!Character.isJavaIdentifierPart(cp) && (cp != '.')) {
                throw new IllegalArgumentException(url.toString() + " Illegal spi provider-class name");
            }
        }

        if (!names.contains(line)) {
            names.add(line);
        }
    }

    @SuppressWarnings("unchecked")
    private ConcurrentMap<String, Class<T>> loadClass(Class<T> type, List<String> classNames) {
        ConcurrentMap<String, Class<T>> map = new ConcurrentHashMap<String, Class<T>>();

        for (String className : classNames) {
            try {
                Class<T> clz = (Class<T>) Class.forName(className, true, classLoader);
                checkSpiInstance(type, clz);
                String spiName = getSpiName(clz);
                if (map.containsKey(spiName)) {
                    throw new IllegalArgumentException(clz.getName() + "Error spiName already exist " + spiName);
                } else {
                    map.put(spiName, clz);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(className + "Error load spi class");
            }
        }
        return map;
    }

    private void checkSpiInstance(Class<T> type, Class<T> clz) {
        if (!Modifier.isPublic(clz.getModifiers())) {
            throw new IllegalArgumentException(clz.getName() + " Error is not a public class");
        }

        if (!type.isAssignableFrom(clz)) {
            throw new IllegalArgumentException(clz.getName() + "Error is not instanceof " + type.getName());
        }

        Constructor<?>[] constructors = clz.getConstructors();

        if (constructors == null || constructors.length == 0) {
            throw new IllegalArgumentException(clz.getName() + "Error has no public no-args constructor");
        }

        for (Constructor<?> constructor : constructors) {
            if (Modifier.isPublic(constructor.getModifiers()) && constructor.getParameterTypes().length == 0) {
                return;
            }
        }

        throw new IllegalArgumentException(clz.getName() + "Error has no public no-args constructor");
    }

    private String getSpiName(Class<?> clz) {
        Spi spi = clz.getAnnotation(Spi.class);
        String name = (spi != null && !"".equals(spi.name())) ? spi.name() : clz.getSimpleName();
        return name;
    }

    private SpiScope getSpiScope(Class<?> clz) {
        Spi spi = clz.getAnnotation(Spi.class);
        return spi.scope();
    }

    private String getSpiGroupName(Class<?> clz) {
        SpiGroup group = clz.getAnnotation(SpiGroup.class);
        return group.name();
    }

    private String getSpiGroup(Class<?> clz) {
        SpiGroup group = clz.getAnnotation(SpiGroup.class);
        return group.group();
    }

    private T initPrototype(Class<T> clz) {
        try {
            return clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    private T initSingleton(String name, Class<T> clz) {
        T obj = extensionInstances.get(name);
        if (obj != null) {
            return obj;
        }

        synchronized (extensionInstances) {
            obj = extensionInstances.get(name);
            if (obj != null) {
                return obj;
            }
            obj = initPrototype(clz);
            extensionInstances.put(name, obj);
            return obj;
        }
    }
}
