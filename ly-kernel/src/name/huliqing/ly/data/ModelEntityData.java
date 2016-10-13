/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.data;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author huliqing
 */
@Serializable
public class ModelEntityData extends EntityData {
    
    /**
     * 获取模块的位置
     * @return 
     */
    public Vector3f getLocation() {
        return getAsVector3f("location", Vector3f.ZERO);
    }
    
    /**
     * 设置模型的位置
     * @param location 
     */
    public void setLocation(Vector3f location) {
        setAttribute("location", location);
    }
    
    public Quaternion getRotation() {
        return getAsQuaternion("rotation", Quaternion.IDENTITY);
    }
    
    public void setRotation(Quaternion rotation) {
        setAttribute("rotation", rotation);
    }
    
    public Vector3f getScale() {
        return getAsVector3f("scale", Vector3f.UNIT_XYZ);
    }
    
    public void setScale(Vector3f scale) {
        setAttribute("scale", scale);
    }
    
    // xxx do shadowMode
    shadowMode
    

                <xs:attribute name="shadowMode" use="optional">
                    <xs:annotation><xs:documentation>投影模式，默认值Inherit</xs:documentation></xs:annotation>
                    <xs:simpleType>
                        <xs:restriction base="xs:string">
                            <xs:enumeration value="Off" />
                            <xs:enumeration value="Cast" />
                            <xs:enumeration value="Receive" />
                            <xs:enumeration value="CastAndReceive" />
                            <xs:enumeration value="Inherit" />
                        </xs:restriction>
                    </xs:simpleType>
                </xs:attribute>
    
}
