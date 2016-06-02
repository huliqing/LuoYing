///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.position;
//
//import com.jme3.effect.shapes.EmitterShape;
//import com.jme3.export.InputCapsule;
//import com.jme3.export.JmeExporter;
//import com.jme3.export.JmeImporter;
//import com.jme3.export.OutputCapsule;
//import com.jme3.math.FastMath;
//import com.jme3.math.Vector3f;
//import com.jme3.util.TempVars;
//import java.io.IOException;
//import name.huliqing.fighter.data.PositionData;
//import name.huliqing.fighter.utils.MathUtils;
//
///**
// * 圆环形的shape,在xz平面上，并且粒子位置也是按圆周顺序产生，且位置平均分布
// * @deprecated use #EmittrCircle instead
// * @author huliqing
// */
//public class EmitterCircleShape implements EmitterShape {
//
//    // 圆周半径
//    private float radius = 1;
//    
//    // 产生粒子的Y值位置，默认0
//    private float yPosition;
//    
//    // 产生的位置数
//    private int size = 36;
//    
//    // ---- 内部参数
//    private int usedIndex = -1;
//    // 粒子位置之间的平均弧度距离
//    private float angle = FastMath.TWO_PI / size;
//    
//    public EmitterCircleShape() {}
//    
//    public EmitterCircleShape(PositionData data) {
//        this.radius = data.getProto().getAsFloat("radius", radius);
//        this.yPosition = data.getProto().getAsFloat("yPosition", yPosition);
//        this.size = data.getProto().getAsInteger("size", size);
//        this.setSize(size);
//    }
//
//    public float getRadius() {
//        return radius;
//    }
//
//    public void setRadius(float radius) {
//        this.radius = radius;
//    }
//
//    public float getyPosition() {
//        return yPosition;
//    }
//
//    public void setyPosition(float yPosition) {
//        this.yPosition = yPosition;
//    }
//
//    public int getSize() {
//        return size;
//    }
//
//    public void setSize(int size) {
//        this.size = size;
//        angle = FastMath.TWO_PI / size;
//    }
//    
//    @Override
//    public void getRandomPoint(Vector3f store) {
//        // 按圆周环形顺序产生粒子位置
//        usedIndex++;
//        if (usedIndex >= size) {
//            usedIndex = 0;
//            store.set(radius, yPosition, 0);
//            return;
//        }
//        TempVars tv = TempVars.get();
//        Vector3f ori = tv.vect1;
//        ori.x = radius;
//        ori.y = yPosition;
//        ori.z = 0;
//        MathUtils.createRotation(angle * usedIndex, Vector3f.UNIT_Y, tv.quat1);
//        tv.quat1.mult(ori, store);
//        tv.release();
//    }
//
//    @Override
//    public void getRandomPointAndNormal(Vector3f store, Vector3f normal) {
//        getRandomPoint(store);
//    }
//
//    @Override
//    public EmitterShape deepClone() {
//        try {
//            EmitterCircleShape clone = (EmitterCircleShape) super.clone();
//            return clone;
//        } catch (CloneNotSupportedException ex) {
//            throw new AssertionError();
//        }
//    }
//
//    @Override
//    public void write(JmeExporter ex) throws IOException {
//        OutputCapsule oc = ex.getCapsule(this);
//        oc.write(radius, "radius", radius);
//        oc.write(yPosition, "yPosition", yPosition);
//        oc.write(size, "size", size);
//        oc.write(usedIndex, "usedIndex", usedIndex);
//        oc.write(angle, "angle", angle);
//    }
//
//    @Override
//    public void read(JmeImporter im) throws IOException {
//        InputCapsule ic = im.getCapsule(this);
//        radius = ic.readFloat("radius", radius);
//        yPosition = ic.readFloat("yPosition", yPosition);
//        size = ic.readInt("size", size);
//        usedIndex = ic.readInt("usedIndex", usedIndex);
//        angle = ic.readFloat("angle", angle);
//    }
//    
//}
