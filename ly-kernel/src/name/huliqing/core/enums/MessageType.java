/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.enums;

import com.jme3.math.ColorRGBA;

/**
 * 用于定义HUD提示信息的类型
 * @author huliqing
 */
public enum MessageType {
    
    /** 普通提示信息 */
    info(ColorRGBA.LightGray),
    
    /** 重要提示 */
    notice(ColorRGBA.Yellow),
    
    /** 谈话对话 */
    talk(ColorRGBA.Cyan),
    
    /** 获得物品 */
    item(ColorRGBA.Green),
    
    /** 获得物品（任务） */
    itemTask(new ColorRGBA(0.6f, 1f, 0.6f, 1f)),
    
    /** 等级提升 */
    levelUp(new ColorRGBA(0.9f, 0.5f, 0.9f, 1f));
    
    
    private ColorRGBA color = ColorRGBA.White.clone();
    
    private MessageType(ColorRGBA color) {
        this.color.set(color);
    }
    
    public ColorRGBA getColor() {
        return color;
    }
}
