/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.test;

import com.jme3.math.Vector3f;

/**
 *
 * @author huliqing
 */
public class Test {
    public static void main(String[] args) {
        Vector3f obj1 = new Vector3f(1,1,1);
        Vector3f obj2 = new Vector3f(2,2,2);
        
        Vector3f obj3 = obj1;
        
        obj1 = obj2;
        
        System.out.println("obj3=" + obj3);
    }
}
