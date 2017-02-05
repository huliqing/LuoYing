/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import name.huliqing.editor.tools.BooleanValueTool;
import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.ParamsTool;

/**
 * @author huliqing
 */
public class LevelParamsTool extends ParamsTool {
    
    private final NumberValueTool height;
    private final BooleanValueTool absolute;
    private final BooleanValueTool precision;
    
    public LevelParamsTool(String name, String tips, String icon) {
        super(name, tips, icon);
        height = new NumberValueTool("height", null, null);
        absolute = new BooleanValueTool("absolute", null, null);
        precision = new BooleanValueTool("precision", null, null);
        height.setValue(0);
        absolute.setValue(false);
        precision.setValue(false);
        addChild(height);
        addChild(absolute);
        addChild(precision);
    }

    public NumberValueTool getHeight() {
        return height;
    }

    public BooleanValueTool getAbsolute() {
        return absolute;
    }

    public BooleanValueTool getPrecision() {
        return precision;
    }

}
