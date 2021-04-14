package com.example.gather.config;

public class ZookeeperConf {
    /**
     * 服务节点
     */
    public static final String ZK_DATA_PATH = "/gatherServer";

    /**
     * 主节点路径
     */
    public static final String ZK_REGISTRY_PATH = "/tmp_root_path";
    /**
     * 会话延时
     */
    public static final int ZK_SESSION_TIMEOUT = 20000 ;

    /**
     * 注册中心地址
     */
    public static final String discoveryAddress = "192.168.138.128:2181"; // 注册中心的地址

}
