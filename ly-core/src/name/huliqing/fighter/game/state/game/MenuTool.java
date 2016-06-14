/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.game;

import name.huliqing.fighter.ui.UI;

/**
 * 界面上的工具栏，可用于添加按钮
 * @author huliqing
 */
public interface MenuTool {
    
    /**
     * 添加一个菜单 
     * @param menu
     */
    void addMenu(UI menu);
    
    /**
     * 添加菜单到指定索引位置
     * @param menu
     * @param index 
     */
    void addMenu(UI menu, int index);
 
    /**
     * 移除指定的菜单
     * @param menu
     * @return 
     */
    boolean removeMenu(UI menu);
}
