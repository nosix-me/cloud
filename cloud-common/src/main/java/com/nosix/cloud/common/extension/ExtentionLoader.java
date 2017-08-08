package com.nosix.cloud.common.extension;

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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class ExtentionLoader<T> {

    private  static ConcurrentHashMap<Class<?>, ExtentionLoader<?>> extentionloders = new ConcurrentHashMap<Class<?>, ExtentionLoader<?>>();
    private static final String PREFIX = "META-INF/services/";
    private static final String DEFAULT_CHARACTER = "utf-8";

    private ConcurrentMap<String, Class<T>> extensionClasses = null;
    private ConcurrentMap<String, T> extensionInstances = null;

    private volatile boolean init = false;

    private Class<T> type;
    private ClassLoader classLoader;


    private ExtentionLoader(Class<T> type) {
        this(type, Thread.currentThread().getContextClassLoader());
    }

    private ExtentionLoader(Class<T> type, ClassLoader classLoader) {
        this.type = type;
        this.classLoader = classLoader;
    }

    @SuppressWarnings("unchecked")
    public static <T> ExtentionLoader<T> getExtensionLoader(Class<T> type) {
        checkInterface(type);
        ExtentionLoader<T> loader = (ExtentionLoader<T>) extentionloders.get(type);
        if(loader == null) {
            loader = initExtentionLoader(type);
        }
        return loader;
    }

    @SuppressWarnings("unchecked")
    private static synchronized <T> ExtentionLoader<T> initExtentionLoader(Class<T> type) {
        ExtentionLoader<T> instance = (ExtentionLoader<T>) extentionloders.get(type);
        if(instance == null) {
            instance = new ExtentionLoader<T>(type);
            extentionloders.putIfAbsent(type, instance);
            instance = (ExtentionLoader<T>) extentionloders.get(type);
        }
        return instance;
    }

    private void checkInit() {
        if(!init) {
            loadSpiClasses();
        }
    }

    private synchronized void loadSpiClasses() {
        if(init) {
            return;
        }
        extensionClasses = loadSpiClasses(PREFIX);
        extensionInstances = new ConcurrentHashMap<String, T>();
    }

    private ConcurrentMap<String, Class<T>> loadSpiClasses(String prefix) {
        String fullName = prefix + type.getName();
        List<String> classNames = new ArrayList<String>();

        try {
            Enumeration<URL> urls;
            if (classLoader == null) {
                urls = ClassLoader.getSystemResources(fullName);
            } else {
                urls = classLoader.getResources(fullName);
            }

            if (urls == null || !urls.hasMoreElements()) {
                return new ConcurrentHashMap<String, Class<T>>();
            }

            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();

                parseUrl( url, classNames);
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "ExtentionLoader loadExtensionClasses error, prefix: " + prefix + " type: " + type.getClass(), e);
        }
        return loadClass(type, classNames);
    }

    public T getExtension(String name) {
        checkInit();

        if(name == null) {
            return null;
        }
        Class<T> clz = extensionClasses.get(name);

        if(clz == null) {
            return null;
        }
        Scope scope = getScope(clz);
        if(scope == Scope.SINGLETON) {
            return initSingleton(name, clz);
        } else {
            return initPrototype(clz);
        }
    }

    public List<T> getExtensions(String key) {
        checkInit();

        if (extensionClasses.size() == 0) {
            return Collections.emptyList();
        }

        // 如果只有一个实现，直接返回
        List<T> exts = new ArrayList<T>(extensionClasses.size());

        // 多个实现，按优先级排序返回
        for (Map.Entry<String, Class<T>> entry : extensionClasses.entrySet()) {
            Activation activation = entry.getValue().getAnnotation(Activation.class);
            if (key == null || key.equals("")) {
                exts.add(getExtension(entry.getKey()));
            } else if (activation != null && activation.key() != null) {
                for (String k : activation.key()) {
                    if (key.equals(k)) {
                        exts.add(getExtension(entry.getKey()));
                        break;
                    }
                }
            }
        }
        Collections.sort(exts, new ActivationComparator<T>());
        return exts;
    }


    private Scope getScope(Class<T> clz) {
        Spi spi = clz.getAnnotation(Spi.class);
        return spi.scope();
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

    private void init() {
        checkInterface(type);
        List<String> classNameList = loadFile(PREFIX + type.getName());
        extensionClasses = loadClass(type, classNameList);
        extensionInstances = new ConcurrentHashMap<String, T>();

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

    private String getSpiName(Class<?> clz) {
        Spi spi = clz.getAnnotation(Spi.class);
        String name = (spi != null && !"".equals(spi.name())) ? spi.name() : clz.getSimpleName();
        return name;
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

    private static <T> void checkInterface(Class<T> type) {

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

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

}
