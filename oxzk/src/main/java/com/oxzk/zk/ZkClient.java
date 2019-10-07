package com.oxzk.zk;

import com.oxapi.service.ListenerService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 描述:
 *
 * @author think
 * @create 2019-4-11 20:25
 */
public class ZkClient {

    private final Logger logger = LoggerFactory.getLogger(ZkClient.class);
    private CuratorFramework client;
    private TreeCache treeCache;
    private final String NAMESPACE = "wyb";
    public static DistributedAtomicLong distributedAtomicLong;
    public static DistributedBarrier barrier;

    private static volatile ZkClient zk;
    public static ZkClient getZkClient(){
        if(zk == null){
            synchronized (ZkClient.class){
                if(zk == null){
                    zk = new ZkClient();
                }
            }
        }
        return zk;
    }

    private static class SingletonHolder{
        private final static String lockPath = "/curator/lock";
        private static InterProcessMutex mutex = new InterProcessMutex(zk.client, zk.NAMESPACE + lockPath );
        private static InterProcessReadWriteLock readWriteLock = new InterProcessReadWriteLock(zk.client, zk.NAMESPACE + lockPath);
    }

    private ZkClient(){
        if (client != null) return;
        try {
            client = CuratorFrameworkFactory.builder().connectString(ZkConfig.serviceAdress)
                    .retryPolicy(new ExponentialBackoffRetry(ZkConfig.bestSleepTimeMs , ZkConfig.maxRetries , ZkConfig.maxSleepMs))
                    .connectionTimeoutMs(ZkConfig.connectionTimeOutMs)
                    .sessionTimeoutMs(ZkConfig.sessionTimeOutMs)
                    .namespace(NAMESPACE)
                    /*
                     * scheme对应于采用哪种方案来进行权限管理，zookeeper实现了一个pluggable的ACL方案，可以通过扩展scheme，来扩展ACL的机制。
                     * zookeeper缺省支持下面几种scheme:
                     *
                     * world: 默认方式，相当于全世界都能访问; 它下面只有一个id, 叫anyone, world:anyone代表任何人，zookeeper中对所有人有权限的结点就是属于world:anyone的
                     * auth: 代表已经认证通过的用户(cli中可以通过addauth digest user:pwd 来添加当前上下文中的授权用户); 它不需要id, 只要是通过authentication的user都有权限（zookeeper支持通过kerberos来进行authencation, 也支持username/password形式的authentication)
                     * digest: 即用户名:密码这种方式认证，这也是业务系统中最常用的;它对应的id为username:BASE64(SHA1(password))，它需要先通过username:password形式的authentication
                     * ip: 使用Ip地址认证;它对应的id为客户机的IP地址，设置的时候可以设置一个ip段，比如ip:192.168.1.0/16, 表示匹配前16个bit的IP段
                     * super: 在这种scheme情况下，对应的id拥有超级权限，可以做任何事情(cdrwa)
                     */
                    .authorization("digest" , "digest".getBytes("utf-8"))
                    .aclProvider(new ACLProvider() {
                        @Override
                        public List<ACL> getDefaultAcl() {
                            return ZooDefs.Ids.CREATOR_ALL_ACL;
                        }

                        @Override
                        public List<ACL> getAclForPath(String s) {
                            return ZooDefs.Ids.CREATOR_ALL_ACL;
                        }
                    }).build();
            client.start();
            treeCache = new TreeCache(client , NAMESPACE);
            treeCache.start();

            distributedAtomicLong = new DistributedAtomicLong(client, "/curator/distributednum", new RetryNTimes(10, 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static InterProcessMutex getMutex(){
        return SingletonHolder.mutex;
    }

    private static InterProcessReadWriteLock getReadWriteLock(){
        return SingletonHolder.readWriteLock;
    }

    private void init() throws Exception {

    }

    /**
     * 获取子节点
     *
     * @param key
     * @return
     */
    public List<String> getChildrenKeys(final String key) {
        try {
            if (!isExits(key)) return Collections.emptyList();
            List<String> list = client.getChildren().forPath(key);
            list.sort(Comparator.reverseOrder());
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage() , e);
            return Collections.emptyList();
        }
    }

    /**
     * 判断节点是否存在
     *
     * @param key
     * @return
     */
    public boolean isExits(final String key) {
        try {
            return null == client.checkExists().forPath(key) ? false : true;
        } catch (Exception e) {
            logger.error(e.getMessage() , e);
            return false;
        }
    }

    /**
     * 更新数据
     * @param key
     * @param value
     */
    public void updateData(final String key , final String value) {
        if (isExits(key)) {
            try {
                client.setData().forPath(key , value.getBytes("utf-8"));
            } catch (Exception e) {
                logger.error(e.getMessage() , e);
            }
        }
    }

    /**
     * 删除节点
     * @param key
     */
    public void reomoceNode(final String key) {
        if (isExits(key)) {
            try {
                client.delete().deletingChildrenIfNeeded().forPath(key);
            } catch (Exception e) {
                logger.error(e.getMessage() , e);
            }
        }
    }

    /**
     * 注册节点
     * @param key
     * @param value
     * @param createMode
     */
    public void registerNode(final String key , final String value , final CreateMode createMode) {
        try {
            if (isExits(key)) {
                client.delete().deletingChildrenIfNeeded().forPath(key);
            }
            client.create().creatingParentsIfNeeded().withMode(createMode)
                    .forPath(key , value.getBytes("utf-8"));
        } catch (Exception e) {
            logger.error(e.getMessage() , e);
        }
    }

    /**
     * 注册持久化节点
     * @param key
     * @param value
     */
    public void registerPersist(final String key , final String value) {
        registerNode(key , value , CreateMode.PERSISTENT);
    }

    /**
     * 注册持久化有序节点
     * @param key
     * @param value
     */
    public void registerPersistSeq(final String key , final String value) {
        registerNode(key , value , CreateMode.PERSISTENT_SEQUENTIAL);
    }

    /**
     * 注册临时节点
     * @param key
     * @param value
     */
    public void registerEphemeral(final String key , final String value) {
        registerNode(key , value , CreateMode.EPHEMERAL);
    }

    /**
     * 注册临时有序节点
     * @param key
     * @param value
     */
    public void registerEphemeralSeq(final String key , final String value) {
        registerNode(key , value , CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    /**
     * 监听节点变化通知service
     * @param wathPath
     * @param listenerService
     */
    public void registerListener(final String wathPath , ListenerService listenerService) {
        treeCache.getListenable().addListener(new ZkCacheListener(wathPath , listenerService));
    }

}


