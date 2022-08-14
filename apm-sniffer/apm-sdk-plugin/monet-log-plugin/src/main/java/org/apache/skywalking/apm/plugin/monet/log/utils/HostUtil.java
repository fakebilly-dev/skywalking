package org.apache.skywalking.apm.plugin.monet.log.utils;

/**
 * HostUtil
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class HostUtil {

    public static String getHost(String host) {
        if (null == host || "".equals(host) || "no-hostname".equals(host)) {
            return "";
        }
        return host;
    }

}
