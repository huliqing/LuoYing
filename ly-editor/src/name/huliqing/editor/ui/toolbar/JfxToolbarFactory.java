/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
                    , "Could not createJfxToolbar, unknow toolbar class={0}, now use JfxEditToolbar instead!"
                    , toolbar.getClass());
            jtb = new JfxEditToolbar();
        } else {
            try {
                jtb = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                LOG.log(Level.SEVERE, "Could not createJfxToolbar, now use JfxEditToolbar instead!", ex);
                jtb = new JfxEditToolbar();
            }
        }
        jtb.setToolbar(toolbar);
        return jtb;
    }
}
