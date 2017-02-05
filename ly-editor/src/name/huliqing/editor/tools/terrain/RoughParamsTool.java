/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.ParamsTool;

/**
 * @author huliqing
 */
public class RoughParamsTool extends ParamsTool {
    private final NumberValueTool lacunarity;
    private final NumberValueTool octaves;
    private final NumberValueTool scale;
    
    public RoughParamsTool(String name, String tips, String icon) {
        super(name, tips, icon);
        lacunarity = new NumberValueTool("lacunarity", null, null);
        octaves = new NumberValueTool("octaves", null, null);
        scale = new NumberValueTool("scale", null, null);
        addChild(lacunarity);
        addChild(octaves);
        addChild(scale);

        lacunarity.setValue(2);
        octaves.setValue(6.0f);
        scale.setValue(0.1f);
        
//            params.lacunarity = 2;
//        params.octaves = 6;
//        params.scale = 0.1f;
    }

    public NumberValueTool getLacunarity() {
        return lacunarity;
    }

    public NumberValueTool getOctaves() {
        return octaves;
    }

    public NumberValueTool getScale() {
        return scale;
    }

    
}
