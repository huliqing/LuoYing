/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import name.huliqing.fighter.Common;

/**
 *
 * @author huliqing
 */
class MatLoader {
    
    static Material loadMaterial(String j3mFile) {
        AssetManager am = Common.getAssetManager();
        return am.loadMaterial(j3mFile);
    }
}
