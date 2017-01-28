/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.toolbar;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.toolbar.BaseEditToolbar;
import name.huliqing.editor.toolbar.TerrainToolbar;
import name.huliqing.editor.toolbar.Toolbar;

/**
 *
 * @author huliqing
 */
public class JfxToolbarFactory {

    private static final Logger LOG = Logger.getLogger(JfxToolbarFactory.class.getName());
    
    private final static Map<Class<? extends Toolbar>, Class<? extends JfxToolbar>> MAPPING = new HashMap();
    
    static {
        MAPPING.put(BaseEditToolbar.class, JfxSimpleToolbar.class);
        MAPPING.put(TerrainToolbar.class, JfxEditToolbar.class);
    }
    
    public final static JfxToolbar createJfxToolbar(Toolbar toolbar) {
        Class<? extends JfxToolbar> clazz = MAPPING.get(toolbar.getClass());
        JfxToolbar jtb;
        if (clazz == null) {
            LOG.log(Level.WARNING
                    , "Could not createJfxToolbar, unknow toolbar class={0}, now use JfxSimpleToolbar instead!"
                    , toolbar.getClass());
            jtb = new JfxSimpleToolbar();
        } else {
            try {
                jtb = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                LOG.log(Level.SEVERE, "Could not createJfxToolbar, now use JfxSimpleToolbar instead!", ex);
                jtb = new JfxSimpleToolbar();
            }
        }
        jtb.setToolbar(toolbar);
        return jtb;
    }
}
