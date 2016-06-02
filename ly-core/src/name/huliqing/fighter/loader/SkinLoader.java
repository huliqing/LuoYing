/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import name.huliqing.fighter.data.SkinData;
import name.huliqing.fighter.object.skin.OutfitSkin;
import name.huliqing.fighter.object.skin.Skin;
import name.huliqing.fighter.object.skin.WeaponSkin;

/**
 * 皮肤，装备，武器
 * @author huliqing
 */
class SkinLoader {

    public static Skin load(SkinData data) {
        String tagName = data.getProto().getTagName();
        Skin skin = null;
        if (tagName.equals("skinOutfit")) {
            skin = new OutfitSkin(data);
        } else if (tagName.equals("skinWeapon")) {
            skin = new WeaponSkin(data);
        }
        if (skin != null) {
            return skin;
        }
        throw new UnsupportedOperationException("Unknow tagname=" + tagName);
    }
    
}
