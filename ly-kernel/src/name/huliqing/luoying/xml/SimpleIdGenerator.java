/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.xml;

/**
 * SimpleIdGenerator作为简单的ID生成器，使用当前系统时间的毫秒数作为初始ID，每次生成ID时在该基础上递增。
 * @author huliqing
 */
public class SimpleIdGenerator implements IdGenerator {

    private static long currentId = System.currentTimeMillis();
    
    @Override
    public synchronized long generateUniqueId() {
        return currentId++;
    }
    
}
