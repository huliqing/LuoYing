/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import name.huliqing.editor.tools.BooleanValueTool;
import name.huliqing.editor.tools.ParamsTool;

/**
 * @author huliqing
 */
public class SlopeParamsTool extends ParamsTool {
    
//                SlopeExtraToolParams params = new SlopeExtraToolParams();
//            params.precision = false; // Snap on terrain editor
//            params.lock = false; // Contain on terrain editor
    
    private final BooleanValueTool precision;
    private final BooleanValueTool lock;
    
    public SlopeParamsTool(String name, String tips, String icon) {
        super(name, tips, icon);
        precision = new BooleanValueTool("precision", null, null);
        lock = new BooleanValueTool("lock", null, null);
        addChild(precision);
        addChild(lock);

        precision.setValue(false);
        lock.setValue(false);
    }

    public BooleanValueTool getPrecision() {
        return precision;
    }

    public BooleanValueTool getLock() {
        return lock;
    }

}
