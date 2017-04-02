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

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLocator;
import com.jme3.asset.AssetManager;
import com.jme3.asset.StreamAssetInfo;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.manager.Manager;

/**
 *
 * @author huliqing
 */
public class EditAssetLocator implements AssetLocator {
    private static final Logger LOG = Logger.getLogger(EditAssetLocator.class.getName());
    
    @Override
    public void setRootPath(String rootPath) {}

    @Override
    public AssetInfo locate(final AssetManager manager, final AssetKey key) {

        File assetsDir = new File(Manager.getConfigManager().getMainAssetDir()); 
       
        File file = new File(assetsDir, key.getName());

        if (!file.exists()) {
            LOG.log(Level.WARNING, "Asset not found, assetDir={0}, key={1}"
                    , new Object[] {assetsDir.getAbsolutePath(), key.getName()});
            return null;
        }

        try {
            return new StreamAssetInfo(manager, key, new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
