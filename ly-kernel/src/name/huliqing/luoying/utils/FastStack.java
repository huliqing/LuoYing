/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.utils;

import java.util.Arrays;

/**
 * 快速高效的堆栈。
 * @author huliqing
 * @param <T>
 */
public final class FastStack<T> {
    
    private Object[] elements;
    private int index = -1;
    
    public FastStack(int initialCapacity) {
        elements = new Object[initialCapacity];
    }
    
    public void push(T o) {
        index++;
        if (index >= elements.length) {
            elements = Arrays.copyOf(elements, elements.length + 1);
        }
        elements[index] = o;
    }
    
    public T pop() {
        T object = (T) elements[index];
        elements[index] = null;
        index--;
        return object;
    }
    
    public T get(int index) {
        return (T) elements[index];
    }
    
    public int size() {
        return index + 1;
    }
}
