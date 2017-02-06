/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

/**
 *
 * @author huliqing
 */
public class TexLayer {
    private String diffuseMap;
    private String normalMap;
    private float scale;
    
    public TexLayer() {}
    public TexLayer(String diffuseMap, String normalMap, float scale) {
        this.diffuseMap = diffuseMap;
        this.normalMap = normalMap;
        this.scale = scale;
    }

    public String getDiffuseMap() {
        return diffuseMap;
    }

    public void setDiffuseMap(String diffuseMap) {
        this.diffuseMap = diffuseMap;
    }

    public String getNormalMap() {
        return normalMap;
    }

    public void setNormalMap(String normalMap) {
        this.normalMap = normalMap;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
    
}
