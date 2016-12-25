/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

/**
 *
 * @author huliqing
 */
public class Test {
    
    public static void main(String[] args) {
//        Vector3f dir1 = new Vector3f(1,0,0).normalizeLocal();
//        Vector3f dir2 = new Vector3f(1,-1,0).normalizeLocal();
//        System.out.println(dir1.dot(dir2));

        Vector3f vec1 = new Vector3f(FastMath.nextRandomFloat(), FastMath.nextRandomFloat(), FastMath.nextRandomFloat());
        vec1.normalizeLocal();
        vec1.normalizeLocal();
        vec1.normalizeLocal();
        vec1.normalizeLocal();
        vec1.normalizeLocal();
        
    }
}
