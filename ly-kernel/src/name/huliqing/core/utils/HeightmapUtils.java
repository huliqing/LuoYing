/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.utils;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import name.huliqing.core.LY;

/**
 * 从模型中生成高度图
 * @author huliqing
 */
public class HeightmapUtils {
    
    public static class J3oToHeightMap {
        
        // 默认的背景颜色
        private float defaultValue = 128;
        // 高度最低值
        private float minValue = 90;
        // 高度最高值
        private float maxValue = 225;
        // 是否反转y轴
        private boolean flip = true;
        
        public J3oToHeightMap() {}

        public float getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(float defaultValue) {
            this.defaultValue = defaultValue;
        }

        public float getMinValue() {
            return minValue;
        }

        public void setMinValue(float minValue) {
            this.minValue = minValue;
        }

        public float getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(float maxValue) {
            this.maxValue = maxValue;
        }

        public boolean isFlip() {
            return flip;
        }

        public void setFlip(boolean flip) {
            this.flip = flip;
        }
        
        /**
         * 转化J3O为高度图
         * @param spatial
         * @param distFile
         * @throws IOException 
         */
        public void convert(Spatial spatial, String distFile) throws IOException {
            BoundingBox bound = (BoundingBox) spatial.getWorldBound();
            int xLen = (int) Math.ceil(bound.getXExtent() * 2);
            int zLen = (int) Math.ceil(bound.getZExtent() * 2);
            float xStart =  (bound.getCenter().x - bound.getXExtent());
            float zStart =  (bound.getCenter().z + bound.getZExtent());

            BufferedImage bi = new BufferedImage(xLen, zLen, BufferedImage.TYPE_INT_RGB);
            WritableRaster wr = bi.getRaster();
            float originY = bound.getCenter().y + bound.getYExtent();
            float spatialHeight = bound.getYExtent() * 2;

            Ray ray = new Ray();
            ray.setDirection(new Vector3f(0,-1,0).normalizeLocal());
            CollisionResults result = new CollisionResults();
            float x = xStart;
            float[] colors = new float[3];
            for (int i = 0; i < xLen; i++, x++) {
                float z = zStart;    
                for (int j = 0; j < zLen; j++, z--) {
                    result.clear();
                    ray.origin.set(x, originY, z);
                    spatial.collideWith(ray, result);
                    if (result.size() > 0) {
                        float distance = result.getClosestCollision().getDistance();
                        if (distance < spatialHeight) {
                            float gray =  (1.0f - distance / spatialHeight) * (maxValue - minValue) + minValue;
                            gray = MathUtils.clamp(gray, 0, 255);
                            colors[0] = gray;
                            colors[1] = gray;
                            colors[2] = gray;
                        }
                    } else {
                        colors[0] = defaultValue;
                        colors[1] = defaultValue;
                        colors[2] = defaultValue;
                    }
                    wr.setPixel(i, flip ? zLen - j - 1: j, colors);
                }
            }
            File file = new File(distFile);
            ImageIO.write(bi, "jpg", file);
        }
        
    }
    
    /**
     * 将j3o文件转换为heightmap图片
     * @param spatial
     * @param distFile 
     * @throws java.io.IOException 
     */
    public static void j3oToHeightmap(Spatial spatial, String distFile) throws IOException {
        J3oToHeightMap jt = new J3oToHeightMap();
        jt.setMinValue(0);
        jt.setMaxValue(255);
        jt.setDefaultValue(0);
        jt.convert(spatial, distFile);
    }
    
    public static void main(String[] args) {
        AssetManager am = LY.getAssetManager();
//        Spatial spatial = am.loadModel("Models/env/terrain/treasure.j3o");
        Spatial spatial = am.loadModel("Models/env/terrain/scene.j3o");
//        Spatial spatial = am.loadModel("Models/env/rock/rock.j3o");
//        Spatial spatial = am.loadModel("Models/monster/raptor/raptor.mesh.j3o");
        spatial.setLocalScale(5);
        
        
        try {
            j3oToHeightmap(spatial, "C:\\Users\\huliqing\\Desktop\\tttt\\test.jpg");
        } catch (IOException ex) {
            Logger.getLogger(HeightmapUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
            
}
