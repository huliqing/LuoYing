/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.define;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.data.DefineData;

/**
 * 材质定义, 主要用于定义物体的各种材质及碰撞声效
 * @author huliqing
 */
public class MatDefine extends Define {
    private static final Logger LOG = Logger.getLogger(MatDefine.class.getName());

    // 质地定义列表
    private final List<String> matList = new ArrayList<String>();
    // 质地碰撞声效
    private final Map<Long, String> collisionSounds = new HashMap<Long, String>();
    // 质地碰撞特效
    private final Map<Long, String> collisionEffects = new HashMap<Long, String>();
    
    @Override
    public void setData(DefineData data) {
        super.setData(data);
        String[] mats = data.getAsArray("mats");
        if (mats != null && mats.length > 0) {
            for (String mat : mats) {
                registerMat(mat);
            }
        }
        // 碰撞声音的定义："mat1|mat2|soundId1,mat3|mat4|soundId2,..."
        String[] csArr = data.getAsArray("collisionSounds");
        if (csArr != null && csArr.length > 0) {
            for (String cs : csArr) {
                String[] arr = cs.split("\\|");
                if (arr.length < 3)
                    continue;
                registerCollisionSound(arr[0], arr[1], arr[2]);
            }
        }
        // 碰撞特效的定义："mat1|mat2|effect1,mat3|mat4|effect2,..."
        String[] ceArr = data.getAsArray("collisionEffects");
        if (ceArr != null && ceArr.length > 0) {
            for (String ce : ceArr) {
                String[] arr = ce.split("\\|");
                if (arr.length < 3)
                    continue;
                registerCollisionEffect(arr[0], arr[1], arr[2]);
            }
        }
    }
    
    /**
     * 获取当前质地(Mat)定义的数量。
     * @return 
     */
    public final int size() {
        return matList.size();
    }
    
    /**
     * 清理所有的Mat定义及碰撞声效的定义。
     */
    public void clear() {
        matList.clear();
        collisionSounds.clear();
    }
    
    /**
     * 将mat转化为整形值,每个不同的Mat都会获得一个唯一的整数值表示形式, 如果指定的mat未定义则该方法返回-1.
     * @param mat
     * @return 
     * @see #getMat(int) 
     */
    public int getMat(String mat) {
        if (mat == null) {
            return -1;
        }
        return matList.indexOf(mat);
    }
    
    /**
     * 将Mat的整形表示转化为字符串定义,每个Mat的字符串表示都是唯一的。
     * @param index
     * @return 
     * @see #getMat(java.lang.String) 
     */
    public String getMat(int index) {
        if (index < 0 || index >= matList.size()) {
            return null;
        }
        return matList.get(index);
    }
    
    /**
     * 获取两个Mat的碰撞声效(id), 如果找不到关于这两个Mat的碰撞声效的定义则该方法返回null.
     * @param mat1
     * @param mat2
     * @return 
     */
    public String getCollisionSound(int mat1, int mat2) {
        if (mat1 < 0 || mat2 < 0)
            return null;
        
        return collisionSounds.get(1L << mat1 | 1L << mat2);
    }
    
    /**
     * 获取两个Mat的碰撞效果(id),如果找不到则返回null.
     * @param mat1
     * @param mat2
     * @return 
     */
    public String getCollisionEffect(int mat1, int mat2) {
        if (mat1 < 0 || mat2 < 0)
            return null;
        
        return collisionEffects.get(1L << mat1 | 1L << mat2);
    }
    
    /**
     * 注册、登记一个质地(mat)类型
     * @param mat 
     */
    public synchronized void registerMat(String mat) {
        if (matList.contains(mat)) {
            LOG.log(Level.WARNING, "Mat already exists! mat={0}", mat);
            return;
        }
        if (size() >= 64) {
            LOG.log(Level.WARNING
                    , "Could not register mat, the size of matList could not more than 64! mat={0}, current size={1}"
                    , new Object[] {mat, size()});
            return;
        }
        matList.add(mat);
    }
    
    /**
     * 注册一个碰撞声效, 注意：如果给定的两种质地(mat)有任何一种未定义，则该方法将什么也不做,注册将不会成功。
     * @param mat1 质地1
     * @param mat2 质地2
     * @param soundId 声效id.
     */
    public synchronized void registerCollisionSound(String mat1, String mat2, String soundId) {
        int indexMat1 = getMat(mat1);
        if (indexMat1 == -1) {
            LOG.log(Level.WARNING, "Mat undefined, could not register collision sounds, mat={0}", mat1);
            return;
        }
        int indexMat2 = getMat(mat2);
        if (indexMat2 == -1) {
            LOG.log(Level.WARNING, "Mat undefined, could not register collision sounds, mat={0}", mat2);
            return;
        }
        collisionSounds.put(1L << indexMat1 | 1L << indexMat2, soundId);
    }
    
    /**
     * 注册一个碰撞特效, 注意：如果给定的两种质地(mat)有任何一种未定义，则该方法将什么也不做,注册将不会成功。
     * @param mat1 质地1
     * @param mat2 质地2
     * @param effectId 特效id.
     */
    public synchronized void registerCollisionEffect(String mat1, String mat2, String effectId) {
        int indexMat1 = getMat(mat1);
        if (indexMat1 == -1) {
            LOG.log(Level.WARNING, "Mat undefined, could not register collision effect, mat={0}", mat1);
            return;
        }
        int indexMat2 = getMat(mat2);
        if (indexMat2 == -1) {
            LOG.log(Level.WARNING, "Mat undefined, could not register collision effect, mat={0}", mat2);
            return;
        }
        collisionEffects.put(1L << indexMat1 | 1L << indexMat2, effectId);
    }
    
    
}
