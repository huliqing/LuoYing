/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.network;

import name.huliqing.core.game.service.ActorService;

/**
 * 所有Network逻辑必须遵循如下规则：
 * 1.如果是client模式,不处理任何逻辑,除UserCommandNetwork外。
 * 2.如果是server或single模式，则所有命令被直接执行，然后广播到所有客户端。
 * @author huliqing
 */
public interface ActorNetwork extends ActorService {
    
}
