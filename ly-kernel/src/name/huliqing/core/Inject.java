/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core;

/**
 * 所有Service,Network都需要实现这个接口，需要实现Inject方法，
 * 用于初始化的时候实现对其它Service、Network的引用的注入。
 * @author huliqing
 */
public interface Inject {
    
    /**
     * 这个方法会在Service,Network初始化的时候调用，在Service、Network初始化完成后将不再调用。<br>
     * 主要用于在Service,Network初始化的时候注入对其它Service,Network类的引用。
     */
    void inject();

   
}
