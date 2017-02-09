/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import name.huliqing.luoying.utils.modifier.TreeFixUtils;

/**
 *
 * @author huliqing
 */
public class Test {
    
    public static void main(String[] args) {
        TreeFixUtils.setAlphaFallOff(0.1f);
        TreeFixUtils.fix("Models/plants/Bush-1064.j3o", "Bush-1064-geom-0");
        TreeFixUtils.fix("Models/plants/Bush-1086.j3o", "Bush-1086-geom-0");
        TreeFixUtils.fix("Models/plants/Bush-1158.j3o", "Bush-1158-geom-0");
        TreeFixUtils.fix("Models/plants/Flower-242.j3o", "Flower-242-geom-0", "Flower-242-geom-1");
        TreeFixUtils.fix("Models/plants/Flower-261.j3o", "Flower-261-geom-0", "Flower-261-geom-1");
        TreeFixUtils.fix("Models/plants/Flower-428.j3o", "Flower-428-geom-0", "Flower-428-geom-1");
        TreeFixUtils.fix("Models/plants/Flower-441.j3o", "Flower-441-geom-0", "Flower-441-geom-1");
        TreeFixUtils.fix("Models/plants/Flower-603.j3o", "Flower-603-geom-0", "Flower-603-geom-2"); // 2
        TreeFixUtils.fix("Models/plants/Tree-1003.j3o", "Tree-1003-geom-0");
        TreeFixUtils.fix("Models/plants/Tree-1010.j3o", "Tree-1010-geom-0");
        TreeFixUtils.fix("Models/plants/Tree-771.j3o", "Tree-771-geom-0");
        TreeFixUtils.fix("Models/plants/Tree-799.j3o", "Tree-799-geom-0");
        TreeFixUtils.fix("Models/plants/bush-11219.j3o", "bush-11219-geom-0");
        TreeFixUtils.fix("Models/plants/dry-651.j3o", "dry-651-geom-0");
        TreeFixUtils.fix("Models/plants/dry-807.j3o", "dry-807-geom-0");
        TreeFixUtils.fix("Models/plants/fir-456.j3o", "fir-456-geom-0");
        TreeFixUtils.fix("Models/plants/palm-11111.j3o", "palm-11111-geom-1");
        TreeFixUtils.fix("Models/plants/palm-11124.j3o", "palm-11124-geom-1", "palm-11124-geom-2");
        TreeFixUtils.fix("Models/plants/palm-11144.j3o", "palm-11144-geom-1", "palm-11144-geom-2");
        TreeFixUtils.fix("Models/plants/palm-11225.j3o", "palm-11225-geom-1");
    }
}
