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
//import name.huliqing.fighter.enums.Plane;
//import name.huliqing.fighter.utils.MathUtils;
//
///**
// * 圆形面的shape,在xz平面上，粒子位置在这个圆形面的随机位置产生。
// * 通过设置yPosition可移动这个面的位置，让这个面向上或向下移。默认yPosition为0
// * ，即刚好在水平地平线上
// * @deprecated use #EmittrCircle instead
// * @author huliqing
// */
//public class EmitterCirclePlaneShape implements EmitterShape {
//    
//    // 圆的半径
//    private float radius = 1;
//    
//    // 产生粒子的Y值位置，默认0
//    private float yPosition;
//    
//    public EmitterCirclePlaneShape() {}
//    
//    public EmitterCirclePlaneShape(PositionData data) {
//        this.radius = data.getProto().getAsFloat("radius", radius);
//        this.yPosition = data.getProto().getAsFloat("yPosition", yPosition);
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
//    @Override
//    public void getRandomPoint(Vector3f store) {
//        TempVars tv = TempVars.get();
//        Vector3f ori = tv.vect1.set(FastMath.nextRandomFloat() * radius, yPosition, 0);
//        MathUtils.createRotation(FastMath.nextRandomFloat() * FastMath.TWO_PI, Vector3f.UNIT_Y, tv.quat1);
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
//            EmitterCirclePlaneShape clone = (EmitterCirclePlaneShape) super.clone();
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
//    }
//
//    @Override
//    public void read(JmeImporter im) throws IOException {
//        InputCapsule ic = im.getCapsule(this);
//        radius = ic.readFloat("radius", radius);
//        yPosition = ic.readFloat("yPosition", yPosition);
//    }
//    
//}
