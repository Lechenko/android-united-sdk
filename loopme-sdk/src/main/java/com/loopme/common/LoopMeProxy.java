package com.loopme.common;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Created by vynnykiakiv on 6/6/18.
 */

public class LoopMeProxy {
    private static final String NONE = "NONE";
    public static LoopMeProxy NO_PROXY = new LoopMeProxy(NONE, "", 0);
    private String name = "";
    private String host;
    private int port;

    public LoopMeProxy(String name, String host, int port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Proxy toJavaProxy() {
        if (name.equals(NONE)) {
            return Proxy.NO_PROXY;
        }
        return new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(host, port));
    }

    @Override
    public String toString() {
        return "LoopMeProxy{" +
                "name='" + name + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
