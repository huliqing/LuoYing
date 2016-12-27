
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author huliqing
 */
public class Test_1 {
    
    public static void main(String[] args) {
        List<Integer> aa = new ArrayList<Integer>();
        aa.add(1);
        aa.add(2);
        aa.add(3);
        System.out.println(aa.stream().allMatch(t -> t == 1));
    }
}
