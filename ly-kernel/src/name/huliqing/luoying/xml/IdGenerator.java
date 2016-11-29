/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.xml;

/**
 * ID生成器.
 * @author huliqing
 */
public interface IdGenerator {
    
    /**
     * 生成一个唯一的id, 每次调用该方法时生成的ID应该都是不同的。
     * @return 
     */
    long generateUniqueId();
    
}
