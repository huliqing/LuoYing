/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.ui;

/**
 * 滚动条滚动过程的侦听器
 * @author huliqing
 */
public interface ScrollListener {
    
   /**
    * 滚动过程的侦听.
    * @param length 已经滚动的距离. 
    */
    void onScroll(float length);
}
