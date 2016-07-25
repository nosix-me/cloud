package com.nosix.cloud.common.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import java.util.ArrayList;
import java.util.List;

public class ReflectFactory {
	private static final ConcurrentMap<String, Class<?>> name2ClassCache = new ConcurrentHashMap<String, Class<?>>();
	private static final ConcurrentMap<Class<?>, String> class2NameCache = new ConcurrentHashMap<Class<?>, String>();
	private static final ConcurrentMap<String, Method> name2MethodCache = new ConcurrentHashMap<String, Method>();
	
	/**
	 * java.lang.Object[][].class => "java.lang.Object[][]"
	 */
	public static String class2Name(final Class<?> clz) {
		if (clz == null) {
			return null;
		}
		
		String className = class2NameCache.get(clz);
		if (className != null) {
			return className;
		}
		
		className = class2NameNoCache(clz);
		class2NameCache.putIfAbsent(clz, className);
		
		return className;
	}
	
	/**
	 * "java.lang.Object[][]" ==> java.lang.Object[][].class
	 */
	public static Class<?> name2Class(String className) {
		if (null == className || "".equals(className)) {
            return null;
        }

        Class<?> clz = name2ClassCache.get(className);
        if (clz != null) {
            return clz;
        }

        try {
			clz = name2ClassNoCache(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
        
        name2ClassCache.putIfAbsent(className, clz);
        return clz;
	}
	
	/**
	 * "void method(int)", "void method()", "int method(java.lang.String,boolean)"
	 * @return
	 */
	public static String method2Name(final Method m) {
		StringBuilder sb = new StringBuilder();
		sb.append(class2Name(m.getReturnType()));
		sb.append(' ');
		sb.append(m.getName()).append('(');
		Class<?>[] parameterTypes = m.getParameterTypes();
		if (parameterTypes != null) {
			for(int i = 0; i < parameterTypes.length; i++) {
				if( i > 0 ) {
					sb.append(',');
				}
				sb.append(class2Name(parameterTypes[i]));
			}
		}
		sb.append(')');
		
		return sb.toString();
	}
	
	/**
	 * java.lang.Object[][]
	 * int method(java.lang.String,boolean)
	 */
	public static Method name2Method(String cName, String mName) {
		String key = cName + "." + mName;
		Method method = name2MethodCache.get(key);
		if (method != null) {
			return method;
		}
		
		Class<?> clz = name2Class(cName);
		if (clz == null) {
			return null;
		}
		
		String methodName = getMethodName(mName);
		if (methodName == null) {
			return null;
		}
		
		List<String> paramTypeList = getParamTypeList(mName);
		Class<?>[] types = null;
		if (paramTypeList != null && paramTypeList.size() > 0) {
			types = new Class<?>[paramTypeList.size()];
			for (int i = 0;i < paramTypeList.size(); i++) {
				types[i] = name2Class(paramTypeList.get(i));
			}
		}
		
		try {
			if (types == null) {
				method = clz.getMethod(methodName);
			} else {
				method = clz.getMethod(methodName, types);	
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		if (method == null) {
			return null;
		}
		
		name2MethodCache.putIfAbsent(key, method);
		return method;
	}
	
	public static List<Class<?>> name2ParametersClass(String name) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		List<String> typeList = getParamTypeList(name);
		for (String type : typeList) {
			result.add(name2Class(type));
		}
		return result;
	}
	
	/**
	 * java.lang.Object[][].class => "java.lang.Object[][]"
	 */
	private static String class2NameNoCache(Class<?> clz) {
		if (clz == null) {
			return null;
		}
		
		if (!clz.isArray()) {
			return clz.getName();
		}
		
		StringBuilder sb = new StringBuilder();
		while(clz.isArray()) {
			sb.append("[]");
			clz = clz.getComponentType();
		}
		return clz.getName() + sb.toString();
	}
	
	/**
	 * "java.lang.Object[][]" => java.lang.Object[][].class
	 */
	private static Class<?> name2ClassNoCache(String name) throws ClassNotFoundException{
		int index = name.indexOf("[");
		//is not array
		if (index <= 0) {
			Class<?> clz = getPrimitiveClass(name);
			if (clz == null) {
				clz = Class.forName(name, true, Thread.currentThread().getContextClassLoader());	
			}
			return clz;
		}
		
		int dimensionSiz = (name.length() - index ) / 2;
		name = name.substring(0, index);
		Class<?> clz = getPrimitiveClass(name);
		if (clz == null) {
			clz = Class.forName(name, true, Thread.currentThread().getContextClassLoader());	
		}
		
		int[] dimensions = new int[dimensionSiz];
		return Array.newInstance(clz, dimensions).getClass();
	}
	
	private static Class<?> getPrimitiveClass(final String name) {
		String copyName = new String(name);
		int index = copyName.indexOf("[");
		if (index > 0) {
			copyName = copyName.substring(0, index);
		}
		if("void".equals(name)) return void.class;
		else if("boolean".equals(name)) return boolean.class;
		else if("byte".equals(name)) return byte.class;
		else if("char".equals(name)) return char.class;
		else if("double".equals(name)) return double.class;
		else if("float".equals(name)) return float.class;
		else if("int".equals(name)) return int.class;
		else if("long".equals(name)) return long.class;
		else if("short".equals(name)) return short.class;
		return null;
	}
	
	
	private static List<String> getParamTypeList(String name) {
		if (name == null) {
			return null;
		}
		List<String> result = new ArrayList<String>();
		
		StringBuilder sb = null;
		for (int i = 0;i < name.length(); i++) {
			char c = name.charAt(i);
			if (c == '(') {
				sb = new StringBuilder();
				continue;
			}
			
			if (c == ',') {
				if (sb != null && sb.length() > 0) {
					result.add(sb.toString());
					sb = new StringBuilder();
				}
			}
			
			if (c == ')') {
				if (sb != null && sb.length() > 0) {
					result.add(sb.toString());
				}
				break;
			}
			
			if (sb != null) {
				sb.append(c);
			}
		}
		return result;
	}
	
	private static String getMethodName(String name) {
		if (name == null) {
			return null;
		}
		
		StringBuilder sb = null;
		for (int i = 0;i < name.length(); i++) {
			char c = name.charAt(i);
			if (c == ' ') {
				sb = new StringBuilder();
				continue;
			}
			
			if (c == '(') {
				break;
			}
			
			if (sb != null) {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}