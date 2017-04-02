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
package name.huliqing.editor.ui.utils;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/**
 * BORROWED FROM: http://andrewtill.blogspot.com/2012/12/dragging-to-resize-javafx-region.html
 * Modified to support all side resizing (no edges)
 *
 * <p>
 * <p>
 * {@link DragResizer} can be used to add mouse listeners to a {@link Region}
 * and make it resizable by the user by clicking and dragging the border in the
 * same way as a window.
 * <p>
 * Only height resizing is currently implemented. Usage: <pre>DragResizer.makeResizable(myAnchorPane);</pre>
 *
 * @author atill, hitchcock9307
 */
public class DragResizer {

    /**
     * The margin around the control that a user can click in to start resizing
     * the region.
     */
    private static final int RESIZE_MARGIN = 2;

    private final Region region;

    private double y, x;

    private boolean initMinHeight;

    private short dragging = 0;

    private final short NOTDRAGGING = 0;
    private final short NORTH = 1;
    private final short SOUTH = 2;
    private final short EAST = 3;
    private final short WEST = 4;

    private DragResizer(Region aRegion) {
        region = aRegion;
    }

    public static void makeResizable(Region region) {
        final DragResizer resizer = new DragResizer(region);

        region.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mousePressed(event);
            }
        });
        region.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseDragged(event);
            }
        });
        region.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseOver(event);
            }
        });
        region.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseReleased(event);
            }
        });
    }

    protected void mouseReleased(MouseEvent event) {
        initMinHeight = false; //Reset each time
        dragging = NOTDRAGGING;
        region.setCursor(Cursor.DEFAULT);
    }

    protected void mouseOver(MouseEvent event) {
        if (isInDraggableZoneS(event) || dragging == SOUTH) {
            region.setCursor(Cursor.S_RESIZE);
        } else if (isInDraggableZoneE(event) || dragging == EAST) {
            region.setCursor(Cursor.E_RESIZE);
        } else if (isInDraggableZoneN(event) || dragging == NORTH) {
            region.setCursor(Cursor.N_RESIZE);
        } else if (isInDraggableZoneW(event) || dragging == WEST) {
            region.setCursor(Cursor.W_RESIZE);
        } else {
            region.setCursor(Cursor.DEFAULT);
        }
    }

    private boolean isInDraggableZoneN(MouseEvent event) {
        return event.getY() < RESIZE_MARGIN;
    }

    private boolean isInDraggableZoneW(MouseEvent event) {
        return event.getX() < RESIZE_MARGIN;
    }

    private boolean isInDraggableZoneS(MouseEvent event) { return event.getY() > (region.getHeight() - RESIZE_MARGIN); }

    private boolean isInDraggableZoneE(MouseEvent event) { return event.getX() > (region.getWidth() - RESIZE_MARGIN); }


    private void mouseDragged(MouseEvent event) {
        if (dragging == SOUTH) {
            region.setMinHeight(event.getY());
        } else if (dragging == EAST) {
            region.setMinWidth(event.getX());
        } else if (dragging == NORTH) {
            double prevMin = region.getMinHeight();
            region.setMinHeight(region.getMinHeight() - event.getY());
            if (region.getMinHeight() < region.getPrefHeight()) {
                region.setMinHeight(region.getPrefHeight());
                region.setTranslateY(region.getTranslateY() - (region.getPrefHeight() - prevMin));
                return;
            }
            if (region.getMinHeight() > region.getPrefHeight() || event.getY() < 0)
                region.setTranslateY(region.getTranslateY() + event.getY());
        } else if (dragging == WEST) {
            double prevMin = region.getMinWidth();
            region.setMinWidth(region.getMinWidth() - event.getX());
            if (region.getMinWidth() < region.getPrefWidth()) {
                region.setMinWidth(region.getPrefWidth());
                region.setTranslateX(region.getTranslateX() - (region.getPrefWidth() - prevMin));
                return;
            }
            if (region.getMinWidth() > region.getPrefWidth() || event.getX() < 0)
                region.setTranslateX(region.getTranslateX() + event.getX());
        }


    }

    private void mousePressed(MouseEvent event) {
        // ignore clicks outside of the draggable margin
        if (isInDraggableZoneE(event)) {
            dragging = EAST;
        } else if (isInDraggableZoneS(event)) {
            dragging = SOUTH;
        } else if (isInDraggableZoneN(event)) {
            dragging = NORTH;
        } else if (isInDraggableZoneW(event)) {
            dragging = WEST;
        } else
            return;


        // make sure that the minimum height is set to the current height once,
        // setting a min height that is smaller than the current height will
        // have no effect
        if (!initMinHeight) {
            region.setMinHeight(region.getHeight());
            region.setMinWidth(region.getWidth());
            initMinHeight = true;
        }

    }
}
