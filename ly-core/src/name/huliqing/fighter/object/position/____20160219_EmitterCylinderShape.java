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
// * 粒子shape， 产生一个竖立内中空的圆柱形shape， 该圆柱坚立在xz平面上，
// * 粒子在innerRadius和outerRadius之间的圆柱环中随机产生,通过height来设置
// * 这个圆柱环的高度。可以想象为一条坚立在地面上的水管，粒子就在水管的内壁和
// * 外壁之间的位置上随机产生。
// * @deprecated use #EmittrCircle instead
// * @author huliqing
// */
//public class EmitterCylinderShape  implements EmitterShape {
//    private float innerRadius = 0;
//    private float outerRadius = 1;
//    private float height = 2;
//
//    public EmitterCylinderShape() {}
//    
//    public EmitterCylinderShape(PositionData data) {
//        innerRadius = data.getProto().getAsFloat("innerRadius", innerRadius);
//        outerRadius = data.getProto().getAsFloat("outerRadius", outerRadius);
//        height = data.getProto().getAsFloat("height", height);
//    }
//
//    @Override
//    public void getRandomPoint(Vector3f store) {
//        TempVars tv = TempVars.get();
//        Vector3f temp = tv.vect1;
//        temp.x = FastMath.nextRandomFloat() * (outerRadius - innerRadius) + innerRadius;
//        temp.y = (FastMath.nextRandomFloat() * 2 - 1) * height;
//        temp.z = 0;
//        MathUtils.createRotation(FastMath.nextRandomFloat() * FastMath.TWO_PI
//                , Vector3f.UNIT_Y, tv.quat1);
//        tv.quat1.mult(temp, store);
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
//            EmitterCylinderShape clone = (EmitterCylinderShape) super.clone();
//            return clone;
//        } catch (CloneNotSupportedException ex) {
//            throw new AssertionError();
//        }
//    }
//
//    @Override
//    public void write(JmeExporter ex) throws IOException {
//        OutputCapsule oc = ex.getCapsule(this);
//        oc.write(innerRadius, "innerRadius", 0);
//        oc.write(outerRadius, "outerRadius", 1);
//        oc.write(height, "height", 2);
//    }
//
//    @Override
//    public void read(JmeImporter im) throws IOException {
//        InputCapsule ic = im.getCapsule(this);
//        innerRadius = ic.readFloat("innerRadius", 0);
//        outerRadius = ic.readFloat("outerRadius", 1);
//        height = ic.readFloat("height", 2);
//    }
//}
