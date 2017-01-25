/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bounding.BoundingBox;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Ray;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import jme3tools.converters.ImageToAwt;
import name.huliqing.editor.Editor;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.select.SpatialSelectObj;
import name.huliqing.luoying.manager.PickManager;

/**
 *
 * @author huliqing
 */
public class TerrainEdit extends SimpleJmeEdit {
    
    // texture settings
    private static final String DEFAULT_TERRAIN_TEXTURE = "com/jme3/gde/terraineditor/dirt.jpg";
    private static final float DEFAULT_TEXTURE_SCALE = 16.0625f;
    private static final int NUM_ALPHA_TEXTURES = 3;
    private final int MAX_DIFFUSE = 12;
    private final int MAX_TEXTURES = 16- NUM_ALPHA_TEXTURES; // 16 max (diffuse and normal), minus the ones we are reserving

    @Override
    public SelectObj doPick(Ray ray) {
        Spatial spatial = PickManager.pick(ray, editRoot);
        return new SpatialSelectObj(spatial);
    }

    @Override
    public void initialize(Editor editor) {
        super.initialize(editor); 
        setToolbar(new TerrainEditToolbar());
        
        try {
            Spatial terrain = generateTerrain();
            this.editRoot.attachChild(terrain);
            this.editRoot.addLight(new DirectionalLight());
            
//            Geometry geo = new Geometry("", new Box(0.5f, 0.5f, 0.5f));
//            geo.setMaterial(MaterialUtils.createLighting(ColorRGBA.Blue));
//            this.editRoot.attachChild(geo);
            
        } catch (Exception ex) {
            Logger.getLogger(TerrainEdit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Spatial generateTerrain() throws IOException {
        int totalSize = 256 + 1;
        int patchSize = 64 + 1;
        int alphaTextureSize = 256;

        float[] heightmapData = null;
        
//        AbstractHeightMap heightmap = null;
//        if (wizardDescriptor.getProperty("abstractHeightMap") != null)
//            heightmap = (AbstractHeightMap) wizardDescriptor.getProperty("abstractHeightMap");
//        
//        if (heightmap != null) {
//            heightmap.load(); // can take a while
//            Float smooth = (Float) wizardDescriptor.getProperty("heightMapSmooth");
//            if (smooth > 0)
//                heightmap.smooth(smooth, 2);
//            heightmapData = heightmap.getHeightMap();
//        }

        return doCreateTerrain(totalSize, patchSize, alphaTextureSize, heightmapData);
    }
    
    protected Spatial doCreateTerrain(int totalSize, int patchSize, int alphaTextureSize, float[] heightmapData) 
            throws IOException {
        
        Terrain terrain = new TerrainQuad("terrain", patchSize, totalSize, heightmapData); 
        Material mat = new Material(editor.getAssetManager(), AssetConstants.MATERIAL_TERRAIN_LIGHTING);

        AssetManager manager = editor.getAssetManager();
        String assetFolder = Manager.getConfigManager().getMainAssetDir();

        // write out 3 alpha blend images
        for (int i = 0; i < NUM_ALPHA_TEXTURES; i++) {
            BufferedImage alphaBlend = new BufferedImage(alphaTextureSize, alphaTextureSize, BufferedImage.TYPE_INT_ARGB);
            if (i == 0) {
                // the first alpha level should be opaque so we see the first texture over the whole terrain
                for (int h = 0; h < alphaTextureSize; h++)
                    for (int w = 0; w < alphaTextureSize; w++)
                        alphaBlend.setRGB(w, h, 0x00FF0000);//argb
            }
            File textureFolder = new File(assetFolder+"/Textures/");
            if (!textureFolder.exists())
                textureFolder.mkdir();
            
            File alphaFolder = new File(assetFolder+"/Textures/terrain-alpha/");
            if (!alphaFolder.exists())
                alphaFolder.mkdir();
            
            String alphaBlendFileName = "/Textures/terrain-alpha/test-alphablend" + i + ".png";
            File alphaImageFile = new File(assetFolder+alphaBlendFileName);
            ImageIO.write(alphaBlend, "png", alphaImageFile);
            Texture tex = manager.loadAsset(new TextureKey(alphaBlendFileName, false));
            switch (i) {
                case 0:
                    mat.setTexture("AlphaMap", tex);
                    break;
                case 1:
                    mat.setTexture("AlphaMap_1", tex);
                    break;
                case 2:
                    mat.setTexture("AlphaMap_2", tex);
                    break;
                default:
                    break;
            }
        }
        
        // copy the default texture to the assets folder if it doesn't exist there yet
        String dirtTextureName = "/Textures/dirt.jpg";
        File dirtTextureFile = new File(assetFolder+dirtTextureName);
        if (!dirtTextureFile.exists()) {
            BufferedImage bi = ImageToAwt.convert(manager.loadTexture(DEFAULT_TERRAIN_TEXTURE).getImage(), false, true, 0);
            ImageIO.write(bi, "jpg", dirtTextureFile);
        }
        // give the first layer default texture
        Texture dirtTexture = manager.loadTexture(dirtTextureName);
        dirtTexture.setWrap(WrapMode.Repeat);
        mat.setTexture("DiffuseMap", dirtTexture);
        mat.setFloat("DiffuseMap_0_scale", DEFAULT_TEXTURE_SCALE);
        mat.setBoolean("WardIso", true);
        mat.setFloat("Shininess", 0.01f);

        ((Node)terrain).setMaterial(mat);
        ((Node)terrain).setModelBound(new BoundingBox());
        ((Node)terrain).updateModelBound();
        ((Node)terrain).setLocalTranslation(0, 0, 0);
        ((Node)terrain).setLocalScale(1f, 1f, 1f);

        // add the lod control
        TerrainLodControl control = new TerrainLodControl(terrain, editor.getCamera());
        control.setLodCalculator(new DistanceLodCalculator(patchSize, 2.7f));
	((Node)terrain).addControl(control);

        //setNeedsSave(true);
        //addSpatialUndo(parent, (Node)terrain, jmeNodeParent);
        
        return (Spatial)terrain;
    }
}
