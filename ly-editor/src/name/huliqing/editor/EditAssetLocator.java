/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLocator;
import com.jme3.asset.AssetManager;
import com.jme3.asset.StreamAssetInfo;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.manager.Manager;
import name.huliqing.luoying.utils.FileUtils;

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
