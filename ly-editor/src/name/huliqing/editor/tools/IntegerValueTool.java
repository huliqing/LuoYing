/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

/**
 * 整数值类型的工具,只取Int值
 * @author huliqing
 */
public class IntegerValueTool extends NumberValueTool {

    public IntegerValueTool(String name, String tips, String icon) {
        super(name, tips, icon);
        setValue(0);
    }

    @Override
    public <T extends ValueTool> T setValue(Number newValue) {
        return super.setValue(newValue.intValue());
    }

}
