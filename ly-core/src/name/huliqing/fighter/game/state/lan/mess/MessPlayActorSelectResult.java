/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.mess;

import com.jme3.network.serializing.Serializable;

/**
 * 服务端响应客户端的角色选择结果.
 * @author huliqing
 */
@Serializable
public class MessPlayActorSelectResult extends MessBase {

    // 从服务端返回的角色唯一ID，该ID即为客户端所选的玩家角色
    private long actorId;
    // 服务端的应答结果，true表示允许选择，false表示选择失败
    private boolean success;
    // 如果选择失败，则error中包含错误信息
    private String error;
    
    public MessPlayActorSelectResult() {}

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    /**
     * true:选择没有问题；false：选择失败
     * @return 
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 设置选择结果
     * @param result 
     */
    public void setSuccess(boolean result) {
        this.success = result;
    }

    /**
     * 如果选择失败，可以从这个方法中获得失败原因
     * @return 
     */
    public String getError() {
        return error;
    }

    /**
     * 设置选择失败的原因
     * @param error 
     */
    public void setError(String error) {
        this.error = error;
    }

    // remove20160616
//    @Override
//    public void applyOnClient() {
//        if (success) {
//            if (Config.debug) {
//                Logger.getLogger(MessPlayActorSelectResult.class.getName())
//                        .log(Level.INFO, "客户端选择角色成功,记住角色唯一ID -> playerActorUniqueId={0}", actorUniqueId);
//            }
//            // 记住这个ID，以便当前客户端知道应该控制哪一个角色
//            LanClientListener.playerActorUniqueId = actorUniqueId;
//        }
//    }
    
    
}
