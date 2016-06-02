///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.view;
//
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Spatial;
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.data.ViewData;
//import name.huliqing.fighter.game.network.PlayNetwork;
//import name.huliqing.fighter.game.service.PlayService;
//import name.huliqing.fighter.loader.Loader;
//import name.huliqing.fighter.object.SyncObject;
//import name.huliqing.fighter.object.animation2.Animation2;
//import name.huliqing.fighter.ui.LinearLayout;
//import name.huliqing.fighter.ui.UI.Corner;
//import name.huliqing.fighter.utils.ConvertUtils;
//
///**
// * View的基类
// * @author huliqing
// */
//public abstract class AbstractView implements View, SyncObject {
//
//    private final PlayService playService = Factory.get(PlayService.class);
//    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
//    
//    protected ViewData data;
//    
//    // View的展示时间,如果为小于或等于0的值则永不停止。
//    protected float useTime;
//    // 是否自适应宽度高度
//    protected boolean resize;
//    // 固定位置,并不是始终固定
//    protected Vector3f fixedPosition;
//    // 角落位置,与fixPosition只能取一个
//    protected Corner cornerPosition;
//    protected boolean onTop;
//    // 是否开启与客户端的同步
//    protected boolean sync;
//    // 同步时间间隔，单位秒
//    protected float syncInterval = 10;
//    // View的动画id
//    private AnimWrap[] animations;
//    
//    // ---- inner
//    protected LinearLayout viewRoot;
//    protected boolean started;
//    protected float timeUsed;
//    protected float syncTimeUsed;
//    
//    public AbstractView(ViewData data) {
//        this.data = data;
//        float sw = playService.getScreenWidth();
//        float sh = playService.getScreenHeight();
//        
//        useTime = data.getAsFloat("useTime", 0);
//        resize = data.getAsBoolean("resize", false);
//        viewRoot = new LinearLayout(data.getAsFloat("widthWeight", 0) * sw, data.getAsFloat("heightWeight", 0) * sh);
//        
//        // [left,top,right,bottom]
//        float[] marginWeight = data.getAsFloatArray("marginWeight");
//        if (marginWeight != null) {
//            viewRoot.setMargin(marginWeight[0] * sw, marginWeight[1] * sh, marginWeight[2] * sw, marginWeight[3] * sh);
//        }
//        
//        // Fixed position weight
//        Vector3f tempFixedPosition = data.getAsVector3f("fixedPosition");
//        if (tempFixedPosition != null) {
//            fixedPosition = new Vector3f(
//                     tempFixedPosition.x * playService.getScreenWidth()
//                    ,tempFixedPosition.y * playService.getScreenHeight()
//                    ,0
//            );
//        }
//        
//        // Corner position
//        String tempCornerPosition = data.getAttribute("cornerPosition");
//        if (tempCornerPosition != null) {
//            this.cornerPosition = Corner.identify(tempCornerPosition);
//        }
//        this.onTop = data.getAsBoolean("onTop", true);
//        this.sync = data.getAsBoolean("sync", false);
//        this.syncInterval = data.getAsFloat("syncInterval", 10);
//        
//        // Format: "animation|timePoint,animation|timePoint,..."
//        String[] tempAnims = data.getAsArray("animations");
//        if (tempAnims != null) {
//            this.animations = new AnimWrap[tempAnims.length];
//            for (int i = 0; i < tempAnims.length; i++) {
//                String[] taArr = tempAnims[i].split("\\|");
//                AnimWrap aw = new AnimWrap();
//                aw.animationId = taArr[0];
//                if (taArr.length > 1) {
//                    aw.timeStart = ConvertUtils.toFloat(taArr[1], 0);
//                }
//                this.animations[i] = aw;
//            }
//        }
//    }
//    
//    @Override
//    public long getSyncId() {
//        return data.getUniqueId();
//    }
//
//    @Override
//    public ViewData getData() {
//        return data;
//    }
//
//    @Override
//    public ViewData getUpdateData() {
//        data.putAttribute("timeUsed", String.valueOf(timeUsed));
//        return data;
//    }
//    
//    @Override
//    public void start() {
//        if (started) {
//            return;
//        }
//        started = true;
//        doViewInit();
//        doViewAnimation(0); // 部分动画需要在一开始时启动(因动画需要doInit).
//    }
//    
//    @Override
//    public final void update(float tpf) {
//        if (!started) 
//            return;
//        
//        doViewAnimation(tpf);
//        
//        doViewLogic(tpf);
//        
//        if (sync) {
//            syncTimeUsed += tpf;
//            if (syncTimeUsed >= syncInterval) {
//                doSyncToClient();
//                syncTimeUsed = 0;
//            }
//        }
//        
//        timeUsed += tpf;
//        
//        if (useTime > 0) {
//            if (timeUsed > useTime) {
//                // 不要调用cleanup
//                doEnd();
//            }
//        }
//    }
//    
//    /**
//     * 初始化View
//     */
//    protected void doViewInit() {
//        // 把viewRoot添加到场景
//        playService.addObject(viewRoot, true);
//        
//        if (resize) {
//            viewRoot.resize();
//        }
//        if (onTop) {
//            viewRoot.setOnTop();
//        }
//        if (fixedPosition != null) {
//            viewRoot.setPosition(fixedPosition.x, fixedPosition.y);
//        } else if (cornerPosition != null) {
//            viewRoot.setToCorner(cornerPosition);
//        }
//    }
//    
//    /**
//     * 更新动画
//     * @param tpf 
//     */
//    protected final void doViewAnimation(float tpf) {
//        // 更新动画
//        if (animations != null) {
//            for (AnimWrap aw : animations) {
//                aw.update(tpf, timeUsed);
//            }
//        }
//    }
//    
//    /**
//     * 更新View逻辑
//     * @param tpf 
//     */
//    protected void doViewLogic(float tpf) {}
//
//    protected final void doSyncToClient() {
//        // 这种同步不需要使用TCP
//        playNetwork.syncObject(this, false);
//    }
//    
//    /**
//     * 结束View的运行,不要把removevObject(this)放在cleanup中。cleanup会由外部
//     * 去调用。
//     */
//    protected final void doEnd() {
//        // 把View移除出场景
//        playService.removeObject(this);
//    }
//    
//    @Override
//    public void cleanup() {
//        // 注：cleanup一般只清理内部数据，不要再去调用Service以避免死循环调用。
//        // 导致产生异常
//        started = false;
//        timeUsed = 0;
//        syncTimeUsed = 0;
//        // 清理animation
//        if (animations != null) {
//            for (AnimWrap aw : animations) {
//                aw.cleanup();
//            }
//        }
//        viewRoot.removeFromParent();
//    }
//
//    private class AnimWrap {
//        // 动画ID
//        String animationId;
//        // 执行该动画的时间点.单位秒,该时间从View开始运行后计算。
//        // 如 timeStart=1.5表示在View开始运行1.5秒后启动该动画
//        float timeStart;
//        
//        boolean started;
//        Animation2 animation;
//        
//        void update(float tpf, float timeUsed) {
//            if (started) {
//                animation.update(tpf);
//                return;
//            }
//            
//            if (timeUsed >= timeStart) {
//                if (animation == null) {
//                    animation = Loader.loadAnimation(animationId);
//                    animation.setTarget(AbstractView.this);
//                }
//                animation.start();
//                started = true;
//            }
//        }
//        
//        void cleanup() {
//            if (animation != null && !animation.isEnd()) {
//                animation.cleanup();
//            }
//            started = false;
//        }
//    }
//}
