/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.manager;

import java.util.Random;

/**
 * 随机数管理器, 随机数是预先产生的。这个管理器会预先生成127个随机数。
 * 通过设置随机种子可以重新产生随机数。通过随机种子的共享可以在客户端和服务端之间产生相同的随机数。
 * @author huliqing
 */
public class RandomManager {
    
    private final static int MAX_RANDOM = 127;
    
    private static long seed;
    private static final float[] VALUES = new float[MAX_RANDOM];
    
    static {
        RandomManager.setRandomSeed(new Random().nextLong());
    }
    
    /**
     * 从所有已经产生的127个随机数中获取指定索引处的数值， index必须是在[0~126]这个范围内（包含0和126）
     * @param index
     * @return 
     */
    public static float getValue(int index) {
        if (index >= MAX_RANDOM) {
            throw new ArrayIndexOutOfBoundsException("Random index only supported 0 to 126!!! index=" + index);
        }
        return VALUES[index & 0xFF];
    }
    
    /**
     * 设置随机种子，注意：该方法会重新产生随机数。
     * @param seed 
     */
    public static void setRandomSeed(long seed) {
        RandomManager.seed = seed;
        Random random  = new Random(seed);
        for (int i = 0; i < MAX_RANDOM; i++) {
            VALUES[i] = random.nextFloat();
        }
    }
    
    /**
     * 获得当前的随机种子
     * @return 
     */
    public static long getRandomSeed() {
        return seed;
    }
    
    public static void main(String[] args) {
        for (int i = 0; i < VALUES.length; i++) {
            System.out.println(VALUES[i]);
        }
    }
}
