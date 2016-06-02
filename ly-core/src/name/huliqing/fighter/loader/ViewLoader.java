/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import name.huliqing.fighter.data.ViewData;
import name.huliqing.fighter.object.view.TextPanelView;
import name.huliqing.fighter.object.view.TextView;
import name.huliqing.fighter.object.view.TimerView;
import name.huliqing.fighter.object.view.View;

/**
 * @author huliqing
 */
class ViewLoader {
    
    public static View load(ViewData data) {
        String tagName = data.getTagName();
        if (tagName.equals("viewText")) {
            return new TextView(data);
        }
        
        if (tagName.equals("viewTextPanel")) {
            return new TextPanelView(data);
        }
        
        if (tagName.equals("viewTimer")) {
            return new TimerView(data);
        }
        
        throw new UnsupportedOperationException("tagName=" + tagName + ", data=" + data);
        
        // 在android下不能这样反射
//        String tagName = data.getTagName();
//        String className = tagMap.get(tagName);
//        try {
//            Class clazz = Class.forName(className);
//            Constructor<View> con = clazz.getConstructor(new Class[]{ViewData.class});
//            View view = con.newInstance(data);
//            return view;
//            
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        
    }
}
