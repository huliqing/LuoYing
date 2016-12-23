/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.forms;

/**
 * 物体的变换模式
 * @author huliqing
 */
public enum Mode {
    
    /** 全局模式，在目标所在的世界空间中变换 */
    GLOBAL,
    
    /** 局部模式，在目标的本地空间中变换 */
    LOCAL
}
