/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.editor;

import com.jme3.asset.AssetEventListener;
import com.jme3.asset.AssetKey;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import name.huliqing.luoying.LuoYing;

/**
 * 缓存过滤器，这个类用于排除JME对于一些资源的缓存。对于一些特殊的资源，如果不想系统进行缓存时可以使用这个
 * 类来添加排除，示例：
 * <code>
 * <pre>
 * UncacheAssetEventListener.getInstance().addUncache("Models/someModel.j3o");
 * </pre>
 * </code>
 * @author huliqing
 */
public class UncacheAssetEventListener implements AssetEventListener {
    private static final Logger LOG = Logger.getLogger(UncacheAssetEventListener.class.getName());

    private final static UncacheAssetEventListener INSTANCE = new UncacheAssetEventListener();
    
    // 存在于这个列表中的文件路径都是不希望进行缓存的资源。
    private final Set<String> uncache = new HashSet();
    
    private UncacheAssetEventListener() {}
    
    public final static UncacheAssetEventListener getInstance() {
        return INSTANCE;
    }
    
    @Override
    public void assetLoaded(AssetKey key) {
//        LOG.log(Level.INFO, "AssetLoaded, key={0}", key.getName());
    }

    @Override
    public void assetRequested(AssetKey key) {
//        LOG.log(Level.INFO, "AssetRequested, key={0}", key.getName());

        if (uncache.contains(key.getName())) {
            // LOG.log(Level.INFO, "Delete cache----> {0}", key.getName());
            LuoYing.getAssetManager().deleteFromCache(key);
            return;
        }
        
        // 不要缓存lyo文件
        if (key.getName().endsWith(".lyo")) {
            // LOG.log(Level.INFO, "Delete cache----> {0}", key.getName());
            LuoYing.getAssetManager().deleteFromCache(key);
        }
    }

    @Override
    public void assetDependencyNotFound(AssetKey parentKey, AssetKey dependentAssetKey) {
    }
    
    /**
     * 添加一个不希望进行缓存的资源路径, 添加之后，这个资源将不会再进行缓存
     * @param assetFilePath 
     */
    public void addUncache(String assetFilePath) {
        uncache.add(assetFilePath);
    }
    
    /**
     * 将资源路径移除，移除后在系统载入这个资源的时候，这个资源有可能被重新缓存。
     * @param assetFilePath 
     * @return  
     */
    public boolean removeUncache(String assetFilePath) {
        return uncache.remove(assetFilePath);
    }
}
