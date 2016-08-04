/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.network.serializing.Serializable;

/**
 *
 * @author huliqing
 */
@Serializable
public class ChannelData extends ObjectData {
    
    private String[] fromRootBones;
    private String[] toRootBones;
    private String[] bones;
    
    public ChannelData() {}

    public String[] getFromRootBones() {
        return fromRootBones;
    }

    public void setFromRootBones(String[] fromRootBones) {
        this.fromRootBones = fromRootBones;
    }

    public String[] getToRootBones() {
        return toRootBones;
    }

    public void setToRootBones(String[] toRootBones) {
        this.toRootBones = toRootBones;
    }

    public String[] getBones() {
        return bones;
    }

    public void setBones(String[] bones) {
        this.bones = bones;
    }
    
}
