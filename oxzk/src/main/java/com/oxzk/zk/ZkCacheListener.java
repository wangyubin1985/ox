package com.oxzk.zk;

import com.oxapi.service.ListenerService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * 描述:
 *
 * @author think
 * @create 2018-12-12 16:32
 */
public class ZkCacheListener implements TreeCacheListener{

    private final String watchPath;
    private final ListenerService listenerService;

    public ZkCacheListener(String watchPath , ListenerService listenerService) {
        this.watchPath = watchPath;
        this.listenerService = listenerService;
    }


    @Override
    public void childEvent(CuratorFramework client , TreeCacheEvent event) throws Exception {
        ChildData data = event.getData();
        if(data.getPath() == null)  return;
        if(watchPath.equals(data.getPath())){
            switch (event.getType()){
                case NODE_ADDED:
                    /**
                     * listenerService todo
                     */
                    break;
                case NODE_UPDATED:
                    /**
                     * listenerService todo
                     */
                    break;
                case NODE_REMOVED:
                    /**
                     * listenerService todo
                     */
                    break;
            }
        }

    }
}
