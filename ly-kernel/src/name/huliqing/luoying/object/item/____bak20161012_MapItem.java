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
//package name.huliqing.ly.object.item;
//
//import com.jme3.font.BitmapFont;
//import com.jme3.material.MatParamOverride;
//import com.jme3.material.RenderState;
//import com.jme3.math.ColorRGBA;
//import com.jme3.math.Quaternion;
//import com.jme3.math.Vector3f;
//import com.jme3.shader.VarType;
//import com.jme3.util.TempVars;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import name.huliqing.ly.Factory;
//import name.huliqing.ly.constants.InterfaceConstants;
//import name.huliqing.ly.constants.ResConstants;
//import name.huliqing.ly.data.ItemData;
//import name.huliqing.ly.manager.ResourceManager;
////import name.huliqing.luoying.network.UserCommandNetwork;
//import name.huliqing.ly.layer.service.ActorService;
//import name.huliqing.ly.layer.service.PlayService;
//import name.huliqing.ly.object.entity.Entity;
//import name.huliqing.ly.ui.Button;
//import name.huliqing.ly.ui.FrameLayout;
//import name.huliqing.ly.ui.Icon;
//import name.huliqing.ly.ui.LinearLayout;
//import name.huliqing.ly.ui.Text;
//import name.huliqing.ly.ui.UI;
//import name.huliqing.ly.ui.UIFactory;
//import name.huliqing.ly.ui.UIUtils;
//import name.huliqing.ly.utils.ConvertUtils;
//
///**
// * 地图物品，点击后可打开地图
// * @author huliqing
// */
//public class MapItem extends AbstractItem {
//    private final PlayService playService = Factory.get(PlayService.class);
//    private final ActorService actorService = Factory.get(ActorService.class);
////    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);
//    
//    private String image;
//    // 地图倍率大小
//    private float mapSize = 1.0f;
//    // 地图透明度
//    private float mapAlpha = 1.0f;
//    // 默认地点图标
//    private String locationIcon = InterfaceConstants.MAP_FLAG_LOCATION;
//    // 地图上位置图标的倍率
//    private float locationSize = 1.0f;
//    // 位置点数据
//    private List<Location> locations;
//    // 位置点图标的基本路径，如果指定了这个基本路径，则locations中的图标不需要再配置上完整的
//    // 图标路径。示例："Interface/map/loc/"
//    private String baseIconPath;
//    // 是否反转位置点上的垂直位置。
//    private boolean flipVertical;
//    
//    //----inner
//    private MapView mapView;
//    
//    @Override
//    public void setData(ItemData data) {
//        super.setData(data); 
//        this.image = data.getAsString("image");
//        this.mapSize = data.getAsFloat("mapSize", mapSize);
//        this.mapAlpha = data.getAsFloat("mapAlpha", mapAlpha);
//        this.locationSize = data.getAsFloat("locationSize", locationSize);
//        this.locationIcon = data.getAsString("locationIcon", locationIcon);
//        this.baseIconPath = data.getAsString("baseIconPath");
//        this.flipVertical = data.getAsBoolean("flipVertical", flipVertical);
//        String[] tempLocations = data.getAsArray("locations");
//        if (tempLocations != null) {
//            locations = new ArrayList<Location>(tempLocations.length);
//            for (String s : tempLocations) {
//                String[] tmp = s.split("\\|");
//                Location loc = new Location();
//                loc.id = tmp[0];
//                loc.x = ConvertUtils.toFloat(tmp[1], 0);
//                loc.y = ConvertUtils.toFloat(tmp[2], 0);
//                if (flipVertical) {
//                    loc.y = 1.0f - loc.y;
//                }
//                loc.enabled = "1".equals(tmp[3].trim());
//                loc.gameId = (tmp.length > 4 && !"".equals(tmp[4].trim())) ?  tmp[4].trim() : null;
//                loc.icon = tmp.length > 5 ? (baseIconPath != null ? baseIconPath + tmp[5] : tmp[5]) : locationIcon;
//                locations.add(loc);
//            }
//        }
//    }
//
//    @Override
//    public void use(Entity actor) {
//        super.use(actor);
//        
//        // 创建一个map view用于显示地图
//        if (mapView == null) {
//            mapView = new MapView(image, locations);
//            mapView.setToCorner(UI.Corner.CC);
//        }
//        
//        // 显示当前玩家位置及方向
//        Flag playerFlag = createFlag(playService.getPlayer(), playService.getGameId(), true, true);
//        mapView.mapContainer.addFlag(playerFlag);
//        playService.addObjectGui(mapView);
//    }
//    
//    
//    private Flag createFlag(Entity actor, String gameId, boolean showDirection, boolean focus) {
//        Flag flag = new Flag(actor.getData().getUniqueId(), InterfaceConstants.MAP_FLAG_PLAYER);
//        
//        // 计算位置
//        float mw = mapView.mapContainer.getWidth();
//        float mh = mapView.mapContainer.getHeight();
//        float x = 0;
//        float y = 0;
//        // 如果提供了locationId，则直接定位到locationId在地图上的位置.
//        // 否则将角色的当前世界世界位置转化到地图上的位置
//        if (gameId != null) {
//            if (mapView.mapContainer.locations != null) {
//                for (Location loc : mapView.mapContainer.locations) {
//                    if (gameId.equals(loc.gameId)) {
//                        x = mw * loc.x;
//                        y = mh * loc.y;
//                        break;
//                    }
//                }
//            }
//        } else {
//            // 20160626,暂不支持这个功能（本地地图）
//            //throw new UnsupportedOperationException("需要根据地图的实际大小来计算比例,然后计算实际位置");
//            // 需要根据地图的实际大小来计算比例,可用WorldBound来计算
////            Vector3f worldPos = actor.getLocation();
////            x = mw * 0.5f + worldPos.x;
////            y = mh * 0.5f + worldPos.z;
//        }
//        flag.setLocalTranslation(x, y, 0);
//        
//        // 计算方向，将角色的视角方向（ViewDirection)转化为地图上的方向，注意：用于标示方向的
//        // 图标(icon)的初始方向必须是向下的, 即在GUI上的初始指向是: (0,-1,0)。
//        if (showDirection) {
//           
//            // 把角色的世界视角方向转化到GUI上的方向.
//            // 1.将角色视角在世界上的x,z方向转化为GUI上的x,y
//            TempVars tv = TempVars.get();
//            Vector3f dir = tv.vect1;
//            dir.set(actorService.getViewDirection(actor)).setY(0).normalizeLocal();
//            dir.y = -dir.z;
//            dir.z = 0;
//            
//            // 2.根据与GUI上的flag icon的初始方向(0,-1,0)进行比较，计算出夹角，再根据夹角计算出旋转角度
//            Vector3f flagDir = tv.vect2.set(0, -1, 0);
//            float angle = dir.angleBetween(flagDir);
//            Quaternion rot = flag.getLocalRotation();
//            if (dir.x > 0) {
//                rot.fromAngleAxis(angle, Vector3f.UNIT_Z);
//            } else {
//                rot.fromAngleAxis(-angle, Vector3f.UNIT_Z);
//            }
//            flag.setLocalRotation(rot);
//            
//            tv.release();
//        }
//        
//        if (focus) {
//            float sw = playService.getScreenWidth();
//            float sh = playService.getScreenHeight();
//            float offsetX = sw * 0.5f - x ;
//            float offsetY = sh * 0.5f - y;
//            Vector3f loc = mapView.mapContainer.getLocalTranslation();
//            loc.addLocal(offsetX, offsetY, 0);
//            // 把当前点移动到中央，然后再fixPosition，避免地图出边界
//            mapView.mapContainer.setLocalTranslation(loc);
//            mapView.mapContainer.fixPosition();
//        }
//        
//        return flag;
//    }
//    
//    private class Location {
//        // 当前地图上的位置唯一标识
//        public String id;
//        // 在地图上的坐标比例，取值[0.0~1.0]
//        public float x;
//        public float y;
//        // 是否打开，只有打开才允许传送
//        public boolean enabled;
//        // 游戏ID，当location被点击后可以传送到这个游戏中
//        public String gameId;
//        // 可以为地图上的点定义一个图标
//        public String icon;
//    }
//    
//    /**
//     * MapView封装整个地图，并且始终和整个屏幕的大小完全匹配.
//     * 其中mapContainer封装基本地图及各个地图坐标点信息
//     */
//    private class MapView extends FrameLayout {
//        
//        // 存放基本地图及各个地图坐标点,
//        private MapContainer mapContainer;
//        // 关闭按钮
//        private Icon close;
//        
//        public MapView(String image, List<Location> locations) {
//            super();
//            this.width = playService.getScreenWidth();
//            this.height = playService.getScreenHeight();
//            
//            mapContainer = new MapContainer(image, locations);
//            mapContainer.setDragEnabled(true);
//            
//            // close button
//            close = new Icon();
//            close.setImage(InterfaceConstants.UI_CLOSE);
//            close.setWidth(32);
//            close.setHeight(32);
//            close.addClickListener(new UI.Listener() {
//                @Override
//                public void onClick(UI view, boolean isPressed) {
//                    if (isPressed) return;
//                    MapView.this.removeFromParent();
//                }
//            });
//            
//            addView(mapContainer);
//            addView(close);
//        }
//
//        @Override
//        protected void updateViewLayout() {
//            super.updateViewLayout(); 
//            close.setToCorner(UI.Corner.RT);
//        }
//        
//    }
//    
//    // MapContainer包含基本地图及坐标点
//    private class MapContainer extends FrameLayout {
//        
//        private List<Location> locations;
//        private Map<Long, Flag> flags;
//        private Command command;
//
//        public MapContainer(String baseMapImage, List<Location> locations) {
//            this.locations = locations;
//            // 基本地图,地图的宽度、高度和图片一致
//            Icon map = new Icon();
//            map.setImage(image);
//            map.setWidth(map.getWidth() * mapSize);
//            map.setHeight(map.getHeight() * mapSize);
//            map.getMaterial().setColor("Color", new ColorRGBA(1,1,1, mapAlpha));
//            map.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
//            
//            setWidth(map.getWidth());
//            setHeight(map.getHeight());
//            addView(map);
//            
//            // 添加各种地图坐标点
//            if (locations != null) {
//                for (int i = 0; i < locations.size(); i++) {
//                    final Location loc = locations.get(i);
//                    float posX = width * loc.x;
//                    float posY = height * loc.y;
//
//                    // 添加一个地点图标，点击后可显示对于当前的各种命令，如："传送"之类
//                    Icon icon = new Icon();
//                    icon.setImage(loc.icon);
//                    icon.setWidth(icon.getWidth() * locationSize);
//                    icon.setHeight(icon.getHeight() * locationSize);
//                    icon.setPosition(posX - icon.getWidth() * 0.5f, posY - icon.getHeight() * 0.5f);
//                    icon.addClickListener(new UI.Listener() {
//                        @Override
//                        public void onClick(UI view, boolean isPressed) {
//                            if (isPressed) return;
//                            if (!loc.enabled) return;
//                            showCommand(loc);
//                        }
//                    });
//                    icon.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
//                    if (!loc.enabled) {
//                        MatParamOverride mpo = new MatParamOverride(VarType.Vector4, "Color", new ColorRGBA(1,1,1,0.5f));
//                        icon.addMatParamOverride(mpo);
//                    }
//                    addView(icon);
//                }
//            }
//        }
//        
//        public void showCommand(Location loc) {
//            if (loc.gameId == null || "".equals(loc.gameId))
//                        return;
//            
//            if (command == null) {
//                command = new Command();
//                addView(command);
//            }
//            
//            command.updateLocation(loc);
//            command.setLocalTranslation(width * loc.x, height * loc.y, 0);
//            command.setVisible(true);
//            // 防止面板溢出屏幕外
//            UIUtils.fixOverflowScreen(command, playService.getScreenWidth(), playService.getScreenHeight());
//        }
//        
//        /**
//         * 添加一个标记到场景中,比如主角的位置标记
//         * @param flag 
//         */
//        public void addFlag(Flag flag) {
//            if (flags == null) {
//                flags = new HashMap<Long, Flag>();
//            }
//            // 移除旧的
//            if (flags.containsKey(flag.id)) {
//                removeView(flags.get(flag.id));
//            }
//            flags.put(flag.id, flag);
//            addView(flag);
//        }
//        
//        public Flag getFlag(long id) {
//            if (flags == null)
//                return null;
//            return flags.get(id);
//        }
//        
//        @Override
//        protected void onDragMove(float xMoveAmount, float yMoveAmount) {
//            super.onDragMove(xMoveAmount, yMoveAmount);
//            fixPosition();
//        }
//        
//        // 修正位置，不要让地图拖动的时候出边界
//        private void fixPosition() {
//            float sw = playService.getScreenWidth();
//            float sh = playService.getScreenHeight();
//            
//            Vector3f loc = getLocalTranslation();
//            float l = loc.x;
//            float r = loc.x + width;
//            float b = loc.y;
//            float t = loc.y + height;
//            
//            if (width < sw) {
//                loc.x = (sw - width) * 0.5f;
//            } else {
//                if (l > 0) {
//                    loc.x = 0;
//                }
//                if (r < sw) {
//                    loc.x = sw - width;
//                }
//            }
//            
//            if (height < sh) {
//                loc.y = (sh - height) * 0.5f;
//            } else {
//                if (b > 0) {
//                    loc.y = 0;
//                }
//                if (t <  sh) {
//                    loc.y = sh - height;
//                }                
//            }
//            setPosition(loc.x, loc.y);
//        }
//        
//    }
//    
//    /**
//     * 用于标记地图上的特殊位置点,如玩家位置,保藏、物品、。。。等等特殊的物品都可以作为一个flag添加进去。
//     */
//    private class Flag extends FrameLayout {
//        private long id;
//        public Flag(long id, String image) {
//            this.id = id;
//            Icon icon = new Icon();
//            icon.setImage(image);
//            icon.setWidth(32);
//            icon.setHeight(32);
//            icon.setLocalTranslation(-0.5f * icon.getWidth(), -0.5f * icon.getHeight(), 0);
//            addView(icon);
//        }
//    }
//    
//    /**
//     * 定义一个命令界面，当点击地图上的某个点之后会在地图上弹出这个界面，用来显示对于这个位置点的所有命令操作，包含：
//     * "传送"之类.
//     */
//    private class Command extends LinearLayout {
//        private Location loc;
//        private Text title;
//        private Button transfer;
//        private Button cancel;
//        public Command() {
//            super(playService.getScreenWidth() * 0.2f, 0);
//            title = new Text("");
//            title.setVerticalAlignment(BitmapFont.VAlign.Center);
//            title.setWidth(width);
//            title.setHeight(UIFactory.getUIConfig().getTitleHeight());
//            title.setBackground(UIFactory.getUIConfig().getBackground(), true);
//            title.setBackgroundColor(UIFactory.getUIConfig().getTitleBgColor(), true);
//            title.setVerticalAlignment(BitmapFont.VAlign.Center);
//            transfer = new Button(ResourceManager.get(ResConstants.COMMON_MAP_TRANSFER));
//            transfer.setWidth(width);
//            transfer.addClickListener(new UI.Listener() {
//                @Override
//                public void onClick(UI view, boolean isPressed) {
//                    if (isPressed) 
//                        return;
//                    if (loc.gameId == null || "".equals(loc.gameId))
//                        return;
//                    userCommandNetwork.changeGameState(loc.gameId);
//                    Command.this.setVisible(false);
//                    mapView.removeFromParent();
//                }
//            });
//            
//            cancel = new Button(ResourceManager.get(ResConstants.COMMON_MAP_CANCEL));
//            cancel.setWidth(width);
//            cancel.addClickListener(new UI.Listener() {
//                @Override
//                public void onClick(UI view, boolean isPressed) {
//                    if (isPressed) return;
//                    Command.this.setVisible(false);
//                }
//            });
//            addView(title);
//            addView(transfer);
//            addView(cancel);
//            this.resize();
//        }
//        
//        public void updateLocation(Location loc) {
//            this.loc = loc;
//            title.setText("  " + ResourceManager.getObjectExt(data.getId(), "loc." + loc.id));
//            if (!loc.enabled) {
//                transfer.setDisabled(true);
//                transfer.setFontColor(ColorRGBA.Gray);
//            } else {
//                transfer.setDisabled(false);
//                transfer.setFontColor(ColorRGBA.White);
//            }
//        }
//    }
//    
//}
