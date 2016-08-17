/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

/**
 * 等级模型接口，实现这个接口来让模块支持"等级"功能，如设置模块等级，升级等。
 * @author huliqing
 */
public interface LevelModule extends Module {
    
    /**
     * 设置模块的等级
     * @param level 
     */
    void setLevel(int level);
    
    /**
     * 获得模块的等级
     * @return 
     */
    int getLevel();
    
    /**
     * 升级模块, levelUpCount表示要升级多少个级别，比如当前等级为5, levelUpCount=3,那么执行这个方法之后，模块的等级
     * 应该为8.
     * @param levelUpCount 升级的等级数。
     */
    void levelUp(int levelUpCount);
}
