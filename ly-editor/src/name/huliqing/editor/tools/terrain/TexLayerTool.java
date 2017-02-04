/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.toolbar.TerrainToolbar;
import name.huliqing.editor.edit.SimpleEditListener;
import name.huliqing.editor.tools.EditTool;
import name.huliqing.editor.utils.TerrainUtils;
import name.huliqing.luoying.constants.AssetConstants;

/**
 * 地形图层工具,,注意：地形在载入的时候需要重新设置材质，使用地形中的所有分块指定同一个材质实例，否则指定刷到特定的材质上。
 * 参考以下代码：
 * <code>
 * <pre>
 * if (target.getSpatial() instanceof Terrain) {
 *      Terrain terrain = (Terrain) target.getSpatial();
 *      target.getSpatial().setMaterial(terrain.getMaterial());
 * }
 * </pre>
 * </code>
 * @author huliqing
 */
public class TexLayerTool extends EditTool<SimpleJmeEdit, TerrainToolbar> implements SimpleEditListener {
//    private static final Logger LOG = Logger.getLogger(TexLayerTool.class.getName());
    
    public interface LayerChangedListener {
        /**
         * 当贴图图层发生变化时该方法被调用。
         * @param tlt 
         */
        void onLayerChanged(TexLayerTool tlt);
    }
    
    private final List<LayerChangedListener> listeners = new ArrayList();
    private ControlTile lastSelected;
    
    // 当前选择的贴图图层索引
    private int selectLayerIndex;
    
    public TexLayerTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }
    
    @Override
    public void initialize(SimpleJmeEdit edit, TerrainToolbar toolbar) {
        super.initialize(edit, toolbar);
        edit.addListener(this);
    }
    
    @Override
    public void cleanup() {
        edit.removeListener(this);
        super.cleanup(); 
    }
    
    @Override
    public void onModeChanged(Mode mode) {
        // ignore
    }
    
    @Override
    public void onSelected(ControlTile selected) {
//        if (selected instanceof EntityControlTile) {
//            if (((EntityControlTile) selected).getTarget().getSpatial() instanceof Terrain) {
//                float scale = DEFAULT_TEXTURE_SCALE;
//                setDiffuseTexture(1, "Textures/grass.jpg");
//                setDiffuseTextureScale(1, scale);
//                Terrain terrain = getTerrain();
//                ((Node) terrain).setMaterial(terrain.getMaterial());
//            }
//        }
        
        if (selected != lastSelected) {
            lastSelected = selected;
            listeners.forEach(t -> {t.onLayerChanged(this);});
        }
    }
    
    public int getLayerCounts() {
        Terrain terrain = getTerrain();
        if (terrain == null)
            return 0;
        for (int i = 0; i < TerrainUtils.MAX_TEXTURES; i++) {
            if (TerrainUtils.getDiffuseTexture(terrain, i) == null) {
                return i;
            }
        }
        return 0;
    }
    
    /**
     * 获取图层，如果当前没有选择中地形物体，则该方法可能返回null.
     * @return 
     */
    public List<Layer> getLayers() {
        Terrain terrain = getTerrain();
        if (terrain == null)
            return null;
        List<Layer> layers = new ArrayList(TerrainUtils.MAX_TEXTURES);
        Texture normalTex;
        for (int i = 0; i < TerrainUtils.MAX_TEXTURES; i++) {
            Texture tex = TerrainUtils.getDiffuseTexture(terrain, i);
            if (tex == null) {
                break;
            }
            Layer layer = new Layer();
            layer.diffuseMap = tex.getName();
            layer.scale = TerrainUtils.getDiffuseTextureScale(terrain, i);
            normalTex = TerrainUtils.getNormalTexture(terrain, i);
            if (normalTex != null) {
                layer.normalMap = normalTex.getName();
            }
            layers.add(layer);
        }
        return layers;
    }
    
    /**
     * 添加一个贴图图层
     * @param layer 
     */
    public void addTextureLayer(int layer) {
        if (layer >= TerrainUtils.MAX_TEXTURES) 
            return;
        Terrain terrain = getTerrain();
        if (terrain == null)
            return;
        TerrainUtils.setDiffuseTextureScale(terrain, layer, TerrainUtils.DEFAULT_TEXTURE_SCALE);
        Texture tex = editor.getAssetManager().loadTexture(AssetConstants.TEXTURES_TERRAIN_DIRT);
        TerrainUtils.setDiffuseTexture(terrain, layer, tex);
        listeners.forEach(t -> {t.onLayerChanged(this);});
    }
    
    /**
     * 移除一个贴图图层
     * @param layer 
     */
    public void removeTextureLayer(int layer) {
        if (layer >= TerrainUtils.MAX_TEXTURES) 
            return;
        Terrain terrain = getTerrain();
        if (terrain == null)
            return;
        TerrainUtils.removeDiffuseTexture(terrain, layer);
        TerrainUtils.removeNormalTexture(terrain, layer);
        TerrainUtils.doClearAlphaMap(terrain, layer);
        listeners.forEach(t -> {t.onLayerChanged(this);});
    }
    
    public void setTextureScale(int layer, float scale) {
        if (layer >= TerrainUtils.MAX_TEXTURES) 
            return;
        Terrain terrain = getTerrain();
        if (terrain == null)
            return;
        TerrainUtils.setDiffuseTextureScale(terrain, layer, scale);
        listeners.forEach(t -> {t.onLayerChanged(this);});
    }
    
    /**
     * 设置指定图层的贴图，如果texturePath为null则移除这个贴图
     * @param layer
     * @param texturePath 
     */
    public void setDiffuseTexture(int layer, String texturePath) {
        if (layer >= TerrainUtils.MAX_TEXTURES) 
            return;
        Terrain terrain = getTerrain();
        if (terrain == null)
            return;
        if (texturePath == null) {
            TerrainUtils.removeDiffuseTexture(terrain, layer);
        } else {
            Texture tex = editor.getAssetManager().loadTexture(texturePath);
            TerrainUtils.setDiffuseTexture(terrain, layer, tex);
        }
        listeners.forEach(t -> {t.onLayerChanged(this);});
    }
    
    /**
     * 设置指定图层的法线贴图，如果texturePath为null则移除这个贴图
     * @param layer
     * @param texturePath 
     */
    public void setNormalTexture(int layer, String texturePath) {
        if (layer >= TerrainUtils.MAX_TEXTURES) 
            return;
        Terrain terrain = getTerrain();
        if (terrain == null)
            return;
        if (texturePath == null) {
            TerrainUtils.removeNormalTexture(terrain, layer);
        } else {
            Texture tex = editor.getAssetManager().loadTexture(texturePath);
            TerrainUtils.setNormalTexture(terrain, layer, tex);
        }
        listeners.forEach(t -> {t.onLayerChanged(this);});
    }
    
    public int getSelectLayerIndex() {
        return selectLayerIndex;
    }
    
    public void setSelectLayerIndex(int layer) {
        this.selectLayerIndex = layer;
    }
    
    public void addListener(LayerChangedListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public boolean removeListener(LayerChangedListener listener) {
        return listeners.remove(listener);
    }
    
    @Override
    protected void onToolEvent(Event e) {} // ignore
    
    public class Layer {
        public String diffuseMap;
        public String normalMap;
        public float scale;
    }
    
    private Terrain getTerrain() {
        ControlTile ct = edit.getSelected();
        if (!(ct instanceof EntityControlTile)) {
            return null;
        }
        Spatial terrainSpatial = ((EntityControlTile) ct).getTarget().getSpatial();
        if (!(terrainSpatial instanceof Terrain)) {
            return null;
        }
        Terrain terrain = (Terrain) terrainSpatial;
        return terrain;
    }
}
