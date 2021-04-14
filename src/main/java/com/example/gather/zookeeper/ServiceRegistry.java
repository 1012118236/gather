package com.example.gather.zookeeper;

import com.example.gather.config.ZookeeperConf;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ServiceRegistry {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);
    /**
     * zook 连接同步器
     */
    private CountDownLatch latch = new CountDownLatch(1);

    /**
     * zookeeper 注册中心的地址
     */
    private String registryAddress;


    public ServiceRegistry(String registryAddress) {

        this.registryAddress = registryAddress;
    }

    public void register(String data) {
        if (data != null) {
            ZooKeeper zk = connectServer();
            if (zk != null) {
                createNode(zk, data);
            }
        }
    }

    /**
     * 连接 zookeeper 服务器
     * @return
     */
    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, ZookeeperConf.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }

                }
            });
            latch.await();
        } catch (IOException | InterruptedException e) {
            logger.error("", e);
        }
        return zk;
    }

    /**
     * 创建节点
     * @param zk
     * @param data
     */
    private void createNode(ZooKeeper zk, String data) {
        try {
            byte[] bytes = data.getBytes();
            String path = "";
            if(zk.exists(ZookeeperConf.ZK_REGISTRY_PATH,false)==null)
                path = zk.create(ZookeeperConf.ZK_REGISTRY_PATH, "gatherService".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            /**
             * path	需要创建的数据节点的节点路径。比如，/zk-book/foo。
             * data[]	一个字节数组，是节点创建后的初始内容。
             * acl	节点的 ACL 策略。
             * createMode	节点类型，是一个枚举类型，通常有4种可选的节点类型：持久（PERSISTENT）、持久顺序（PERSISTENT_SEQUENTIAL）、临时（EPHEMERAL）、临时顺序（EPHEMERAL_SEQUENTIAL）。关于ZNode的节点特性，将在后面做详解介绍。
             * cb	注册一个异步回调函数。开发人员需要实现StringCallBack接口，主要是对下面这个方法的重写：void processResult(int rc, String path, Object ctx, String name);当服务端节点创建完毕后，ZooKeeper 客户端就会自动调用这个方法，这样就可以处理相关的业务逻辑了。
             * ctx	用于传递一个对象，可以在毁掉方法执行的时候使用，通常是一个上下文(Context)信息。
             */
            path = zk.create(ZookeeperConf.ZK_REGISTRY_PATH+ ZookeeperConf.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("path:   "+path);
            logger.debug("create zookeeper node ({} => {})", path, data);
        } catch (KeeperException | InterruptedException e) {
            logger.error("", e);
        }
    }
}
