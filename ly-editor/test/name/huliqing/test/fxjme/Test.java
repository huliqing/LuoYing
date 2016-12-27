/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.test.fxjme;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author huliqing
 */
public class Test {
    public static void main(String[] args) {
//        Vector3f a = new Vector3f(1,0,0);
//        Vector3f b = new Vector3f(0,1,0);
//        List<Vector3f> test = new ArrayList<Vector3f>();
//        test.add(a);
//        test.add(b);

        List<Integer> test = new ArrayList<Integer>();
        test.add(1);
        test.add(2);
        test.add(3);
        test.stream().filter(t -> t == 2).forEach(t -> {System.out.println("t=" + t);});
    }
}
