package com.nosix.cloud.common;

import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class URL implements Serializable {
    private static final long serialVersionUID = 81218104672794786L;

    private String protocol;

    private String host;

    private int port;

    private String path;

    private Map<String, String> parameters;

    public URL(String protocol, String host, int port, String interfaceName) {
        this(protocol, host, port, interfaceName, new HashMap<String, String>());
    }

    public URL(String protocol, String host, int port, String path, Map<String, String> parameters) {
        if (protocol == null || host == null) {
            throw new IllegalArgumentException("protocol and host must not be null");
        }
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
        this.parameters = parameters;
    }

    public static URL valueOf(String url) {
        if (url == null) {
            throw new IllegalArgumentException("url is null");
        }
        //去掉首尾空格
        int start = filterLeadingWhitespace(url);
        int limit = filterTailingWhitespace(url);
        if (start >= limit) {
            throw new IllegalArgumentException("url is illegal");
        }
        //url
        String queryString = parseQueryString(url, start, limit);
        if (queryString != null) {
            limit -= (queryString.length() + 1);
        }

        //protocol
        String protocol = parseProtocol(url, start, limit);
        if (protocol == null) {
            throw new IllegalArgumentException("url protocol is illegal");
        }
        start += protocol.length();

        //host port
        String[] hostAndPort = parseHostAndPort(url, start, limit);

        String host = hostAndPort[0];
        int port = ((hostAndPort[1]) == null ? 0 : Integer.parseInt(hostAndPort[1]));

        if (host == null) {
            throw new IllegalArgumentException("url host and port is illegal");
        }
        //:// + host + : + port
        start += Constants.SEPARATOR_PROTOCOL.length() +
                hostAndPort[0].length() +
                Constants.SEPARATOR_PORT.length() +
                ((hostAndPort[1]) == null ? 0 : hostAndPort[1].length());

        //path
        String interfaceName = null;
        if (start <= limit -1) {
            interfaceName = url.substring((start + 1), limit);
        }

        if (interfaceName == null) {
            throw new IllegalArgumentException("url interfaceName is illegal");
        }

        //param
        Map<String, String> parameters = parseParameters(queryString);
        return new URL(protocol, host, port, interfaceName, parameters);
    }

    public String getUri() {
        return protocol + Constants.SEPARATOR_PROTOCOL + host + ":" + port +
                Constants.SEPARATOR_PATH + path;
    }
    /**
     * host:port
     */
    public String getHostAndPort() {
        if (this.getPort() <= 0) {
            return this.getHost();
        }

        return this.getHost() + Constants.SEPARATOR_PORT + this.getPort();
    }
    /**
     * protocol://host:port
     */
    public String getProtocolAndHostAndPort() {
        return this.getProtocol() + Constants.SEPARATOR_PROTOCOL + this.getHostAndPort();
    }

    /**
     * protocol://host:port/group/interface/version
     */
    public String getProtocolUri() {
        return getProtocolAndHostAndPort() + Constants.SEPARATOR_PATH + getServiceUri();
    }

    /**
     * group/interface/version
     */
    public String getServiceUri() {
        return this.getGroup() + Constants.SEPARATOR_PATH +
                this.getPath() + Constants.SEPARATOR_PATH +
                this.getVersion();
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public String getParameter(String name, String defaultValue) {
        String value = getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public int getIntParameter(String name, int defaultValue) {
        String value = getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public String getId() {
        return getParameter(URLParam.id.getName(), URLParam.id.getValue());
    }

    public String getVersion() {
        return getParameter(URLParam.version.getName(), URLParam.version.getValue());
    }

    public String getGroup() {
        return getParameter(URLParam.group.getName(), URLParam.group.getValue());
    }

    private static int filterLeadingWhitespace(String url) {
        int start = 0;
        while ((start < url.length()) && (url.charAt(start) <= ' ')) {
            start++;
        }
        return start;
    }

    private static int filterTailingWhitespace(String url) {
        int limit = url.length();
        while ((limit > 0) && (url.charAt(limit - 1) <= ' ')) {
            limit--;
        }
        return limit;
    }

    private static String parseQueryString(String url, int start, int limit) {
        String queryString = null;
        int queryStart = url.indexOf('?');
        if ((queryStart != -1) && (queryStart < limit)) {
            queryString = url.substring((queryStart + 1), limit);
        }
        return queryString;
    }

    private static String parseProtocol(String url, int start, int limit) {
        String protocol = null;
        char c;
        for (int i = start; ((c = url.charAt(i)) != '/' && i < limit); i++) {
            if (c == ':') {
                protocol = url.substring(start, i);
            }
        }
        return protocol;
    }

    private static String[] parseHostAndPort(String url, int start, int limit) {
        String[] hostAndPortArray = new String[2];
        if ((start <= (limit - 3)) &&
                (url.charAt(start) == ':') &&
                (url.charAt(start + 1) == '/') &&
                (url.charAt(start + 2) == '/')) {
            int i = url.indexOf('/', (start + 3));
            if (i < 0) {
                i = limit;
            }
            String hostAndPort = url.substring((start + 3), i);
            i = hostAndPort.indexOf(':');
            if (i >= 0) {
                hostAndPortArray[0] = hostAndPort.substring(0, i);
                // port can be null
                if (hostAndPort.length() > (i + 1)) {
                    hostAndPortArray[1] = hostAndPort.substring(i + 1);
                }
            }
        }

        return hostAndPortArray;
    }

    private static Map<String, String> parseParameters(String queryString) {
        Map<String, String> map = new HashMap<String, String>();
        if (queryString == null || queryString.equals("")) {
            return map;
        }
        byte[] data = queryString.getBytes();
        if (data == null || data.length <= 0) {
            return map;
        }

        int	ix = 0;
        int	ox = 0;
        String key = null;
        String value = null;
        while (ix < data.length) {
            byte c = data[ix++];
            switch ((char) c) {
                case '&':
                    value = new String(data, 0, ox);
                    if (key != null) {
                        map.put(key, value);
                        key = null;
                    }
                    ox = 0;
                    break;
                case '=':
                    if (key == null) {
                        key = new String(data, 0, ox);
                        ox = 0;
                    } else {
                        data[ox++] = c;
                    }
                    break;
                case '+':
                    data[ox++] = (byte)' ';
                    break;
                case '%':
                    data[ox++] = (byte)((convertHexDigit(data[ix++]) << 4) + convertHexDigit(data[ix++]));
                    break;
                default:
                    data[ox++] = c;
            }
        }
        //The last value does not end in '&'.  So save it now.
        if (key != null) {
            value = new String(data, 0, ox);
            map.put(key, value);
        }

        return map;
    }

    private static byte convertHexDigit( byte b ) {
        if ((b >= '0') && (b <= '9')) return (byte)(b - '0');
        if ((b >= 'a') && (b <= 'f')) return (byte)(b - 'a' + 10);
        if ((b >= 'A') && (b <= 'F')) return (byte)(b - 'A' + 10);
        throw new IllegalArgumentException("convertHexDigit.notHex");
    }

    @Override
    public int hashCode() {
        int factor = 31;
        int rs = 1;
        rs = factor * rs + ObjectUtils.hashCode(protocol);
        rs = factor * rs + ObjectUtils.hashCode(host);
        rs = factor * rs + ObjectUtils.hashCode(port);
        rs = factor * rs + ObjectUtils.hashCode(path);
        rs = factor * rs + ObjectUtils.hashCode(parameters);
        return rs;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof URL)) {
            return false;
        }
        URL ou = (URL) obj;
        if (!ObjectUtils.equals(this.protocol, ou.protocol)) {
            return false;
        }
        if (!ObjectUtils.equals(this.host, ou.host)) {
            return false;
        }
        if (!ObjectUtils.equals(this.port, ou.port)) {
            return false;
        }
        if (!ObjectUtils.equals(this.path, ou.path)) {
            return false;
        }
        return ObjectUtils.equals(this.parameters, ou.parameters);
    }

    @Override
    public String toString() {
        return getProtocolAndHostAndPort() + Constants.SEPARATOR_PATH + getServiceUri();
    }

    public String toFullString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getProtocolAndHostAndPort())
                .append(Constants.SEPARATOR_PATH)
                .append(getPath());

        boolean isFirst = true;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (isFirst) {
                builder.append("?").append(name).append("=").append(value);
                isFirst = false;
            } else {
                builder.append("&").append(name).append("=").append(value);
            }
        }

        return builder.toString();
    }

    public static String encode(String url) {
        try {
            return java.net.URLEncoder.encode(url, Constants.CHARACTER_UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decode(String url) {
        try {
            return java.net.URLDecoder.decode(url, Constants.CHARACTER_UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
//public final class URL implements Serializable {
//    private static final long serialVersionUID = 81218104672794786L;
//
//    private final String  protocol;
//
//    private final String  host;
//
//    private final int     port;
//
//    private final String  path;
//
//    private final Map<String,String> parameters;
//
//    public URL(String protocol, String host, int port, String path, Map<String, String> parameters) {
//        this.protocol = protocol;
//        this.host = host;
//        this.port = port;
//        this.path = path;
//        this.parameters = parameters;
//    }
//
//    public URL(String protocol, String host, int port, Map<String,String> parameters) {
//        this(protocol, host, port, null, parameters);
//    }
//
//    public URL(String path, int port, String host, String protocol) {
//        this(protocol, host, port, path, null);
//    }
//
//    public static URL valueOf(String url) {
//        if(url == null || url.trim().length() == 0) {
//            throw new  IllegalStateException("url == null");
//        }
//        String urlBak = new String(url);
//        //去除URL中的空白字符
//        url = url.replaceAll("\\s*","");
//
//        Map<String,String> parameters = null;
//        int i = url.indexOf("?");
//        if(i >= 0) {
//            String parametersStr = url.substring(i+1);
//            if(parametersStr != null && parametersStr.length() > 0) {
//                String []parts = parametersStr.split("\\&");
//                parameters = new HashMap<String,String>();
//                for(String part : parts) {
//                    part = part.trim();
//                    if(part.length() > 0) {
//                        int j = part.indexOf("=");
//                        if(j >= 0) {
//                            parameters.put(part.substring(0,j), part.substring(j+1));
//                        } else {
//                            parameters.put(part,part);
//                        }
//                    }
//                }
//            }
//            url = url.substring(0, i);
//        }
//
//        String protocol = null;
//        i = url.indexOf("://");
//        if(i >= 0) {
//            if(i==0) {
//                throw new IllegalStateException("url missing protocol: \"" + urlBak + "\"");
//            }
//            protocol = url.substring(0, i);
//            url = url.substring(i+3);
//        }
//        String host = null;
//        int port = 0;
//        i = url.indexOf(":");
//        if(i >= 0) {
//            host = url.substring(0,i);
//            url = url.substring(i + 1);
//        }
//        i = url.indexOf("/");
//        if(i >=0) {
//            String portStr = url.substring(0,i);
//            if(portStr == null || portStr.trim().length() == 0) {
//                throw new IllegalStateException("url missing port:\"" + urlBak + "\"");
//            } else {
//                port = Integer.parseInt(portStr);
//                url = url.substring(i+1);
//            }
//        }
//        String path = null;
//        i = url.indexOf("?");
//        if(i >= 0 && i < url.length() - 1) {
//            path = url.substring(0, i);
//        } else {
//            path = url;
//        }
//        return new URL(protocol, host, port, path, parameters);
//    }
//
//    public String getUri() {
//        return protocol + Constants.SEPARATOR_PROTOCOL + host + ":" + port +
//                Constants.SEPARATOR_PATH + path;
//    }
//
//
//
//    public String getProtocol() {
//        return protocol;
//    }
//
//    public String getHost() {
//        return host;
//    }
//    public int getPort() {
//        return port;
//    }
//
//    public String getPath() {
//        return path;
//    }
//
//    public Map<String, String> getParameters() {
//        return parameters;
//    }
//
//    public String getParameter(String name)
//    {
//        return parameters.get(name);
//    }
//    public String getParameter(String name, String defaultValue) {
//        String value = getParameter(name);
//        if (value == null) {
//            return defaultValue;
//        }
//        return value;
//    }
//    public void addParameter(String name, String value) {
//        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(value)) {
//            return;
//        }
//        parameters.put(name, value);
//    }
//
//    public void removeParameter(String name) {
//        if (name != null) {
//            parameters.remove(name);
//        }
//    }
//
//    public void addParameters(Map<String, String> params) {
//        parameters.putAll(params);
//    }
//
//    public void addParameterIfAbsent(String name, String value) {
//        if (hasParameter(name)) {
//            return;
//        }
//        parameters.put(name, value);
//    }
//
//    public boolean hasParameter(String key) {
//        return StringUtils.isNotBlank(getParameter(key));
//    }
//
//    public String getVersion() {
//        return getParameter(URLParam.version.getName(), URLParam.version.getValue());
//    }
//
//    /**
//     * host:port
//     */
//    public String getHostAndPort() {
//        if (this.getPort() <= 0) {
//            return this.getHost();
//        }
//
//        return this.getHost() + Constants.SEPARATOR_PORT + this.getPort();
//    }
//    /**
//     * protocol://host:port
//     */
//    public String getProtocolAndHostAndPort() {
//        return this.getProtocol() + Constants.SEPARATOR_PROTOCOL + this.getHostAndPort();
//    }
//
//    /**
//     * protocol://host:port/group/interface/version
//     */
//    public String getProtocolUri() {
//        return getProtocolAndHostAndPort() + Constants.SEPARATOR_PATH + getServiceUri();
//    }
//
//    /**
//     * group/interface/version
//     */
//    public String getServiceUri() {
//        return this.getGroup() + Constants.SEPARATOR_PATH +
//                this.getPath() + Constants.SEPARATOR_PATH +
//                this.getVersion();
//    }
//    @Override
//    public String toString() {
//        return getProtocolAndHostAndPort() + Constants.SEPARATOR_PATH + getServiceUri();
//    }
//
//    public String toFullString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append(getProtocolAndHostAndPort())
//                .append(Constants.SEPARATOR_PATH)
//                .append(getPath());
//
//        boolean isFirst = true;
//        for (Map.Entry<String, String> entry : parameters.entrySet()) {
//            String name = entry.getKey();
//            String value = entry.getValue();
//            if (isFirst) {
//                builder.append("?").append(name).append("=").append(value);
//                isFirst = false;
//            } else {
//                builder.append("&").append(name).append("=").append(value);
//            }
//        }
//
//        return builder.toString();
//    }
//
//    public String getGroup() {
//        return getParameter(URLParam.group.getName(), URLParam.group.getValue());
//    }
//    @Override
//    public int hashCode() {
//        int factor = 31;
//        int rs = 1;
//        rs = factor * rs + ObjectUtils.hashCode(protocol);
//        rs = factor * rs + ObjectUtils.hashCode(host);
//        rs = factor * rs + ObjectUtils.hashCode(port);
//        rs = factor * rs + ObjectUtils.hashCode(path);
//        rs = factor * rs + ObjectUtils.hashCode(parameters);
//        return rs;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null || !(obj instanceof URL)) {
//            return false;
//        }
//        URL ou = (URL) obj;
//        if (!ObjectUtils.equals(this.protocol, ou.protocol)) {
//            return false;
//        }
//        if (!ObjectUtils.equals(this.host, ou.host)) {
//            return false;
//        }
//        if (!ObjectUtils.equals(this.port, ou.port)) {
//            return false;
//        }
//        if (!ObjectUtils.equals(this.path, ou.path)) {
//            return false;
//        }
//        return ObjectUtils.equals(this.parameters, ou.parameters);
//    }
//
//    public static String encode(String url) {
//        try {
//            return java.net.URLEncoder.encode(url, Constants.CHARACTER_UTF8);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static String decode(String url) {
//        try {
//            return java.net.URLDecoder.decode(url, Constants.CHARACTER_UTF8);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
