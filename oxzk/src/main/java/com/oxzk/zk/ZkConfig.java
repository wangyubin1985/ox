package com.oxzk.zk;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 描述:
 *
 * @author think
 * @create 2018-12-13 18:44
 */
@ConfigurationProperties
public class ZkConfig {
    public static final String serviceAdress = "";
    public static final int connectionTimeOutMs = 1000;
    public static final String digest = "ox";
    public static final int maxRetries = 3;
    public static final int maxSleepMs = 2000;
    public static final int sessionTimeOutMs = 3000;
    public static final String nameSpace = "wyb";
    public static final int bestSleepTimeMs = 1000;

}
