/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.sound;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.data.SoundData;
import name.huliqing.core.data.define.MatObject;
import name.huliqing.core.enums.Mat;
import name.huliqing.core.xml.DataFactory;

/**
 *
 * @author huliqing
 */
public class SoundCollision {
    private List<Collision> soundCollisions; 
    
    private void initSoundCollision() {
        soundCollisions = new ArrayList<Collision>();
        
        // v2 wood hit stone
        soundCollisions.add(new Collision(Mat.wood, Mat.stone, (SoundData) DataFactory.createData("soundCollisionWS")));
        // v2 wood hit body
        soundCollisions.add(new Collision(Mat.wood, Mat.body,  (SoundData) DataFactory.createData("soundCollisionWB")));
        // v2 wood hit wood
        soundCollisions.add(new Collision(Mat.wood, Mat.wood, (SoundData) DataFactory.createData("soundCollisionWW")));
        // v2 wood hit metal
        soundCollisions.add(new Collision(Mat.wood, Mat.metal, (SoundData) DataFactory.createData("soundCollisionWM")));
        
        // old
        soundCollisions.add(new Collision(Mat.metal, Mat.metal, (SoundData) DataFactory.createData("soundCollisionMM")));
        soundCollisions.add(new Collision(Mat.metal, Mat.stone, (SoundData) DataFactory.createData("soundCollisionMS")));
        soundCollisions.add(new Collision(Mat.metal, Mat.body,  (SoundData) DataFactory.createData("soundCollisionMB")));
        soundCollisions.add(new Collision(Mat.stone, Mat.body,  (SoundData) DataFactory.createData("soundCollisionMB")));
        soundCollisions.add(new Collision(Mat.body, Mat.body,   (SoundData) DataFactory.createData("soundCollisionMB")));
        
    }
    
    /**
     * 处理物体碰撞声音。
     * @param obj1
     * @param obj2 
     * @param position 
     */
    public void playCollision(ObjectData obj1, ObjectData obj2, Vector3f position) {
        if (!(obj1 instanceof MatObject) || !(obj2 instanceof MatObject)) {
            return;
        }
        playCollision(((MatObject)obj1).getMat(), ((MatObject)obj2).getMat(), position);
    }
    
    public void playCollision(Mat mat1, Mat mat2, Vector3f position) {
        if (soundCollisions == null) {
            initSoundCollision();
        }
        for (Collision sc : soundCollisions) {
            if (sc.checkCound(mat1, mat2)) {
                SoundManagerOutdate.getInstance().playSound(sc.getSound(), position);
                return;
            }
        }
    }
    
    private class Collision {
        private final Mat mat1;
        private final Mat mat2;
        private final SoundData sound;
        public Collision(Mat mat1, Mat mat2, SoundData sound) {
            this.mat1 = mat1;
            this.mat2 = mat2;
            this.sound = sound;
        }
        
        public boolean checkCound(Mat mat1, Mat mat2) {
            return (this.mat1 == mat1 && this.mat2 == mat2) 
                    || (this.mat1 == mat2 && this.mat2 == mat1);
        }
        
        public SoundData getSound() {
            return this.sound;
        }

        @Override
        public String toString() {
            return "SoundCollision{" + "mat1=" + mat1 + ", mat2=" + mat2 + ", sound=" + sound + '}';
        }
    }
}
