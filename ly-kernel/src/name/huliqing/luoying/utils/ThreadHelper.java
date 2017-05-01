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
package name.huliqing.luoying.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 线程,线程池管理，所有新建的线程都由该类管理，不要在代码中其它地方手动
 * 创建线程，否则很难管理。
 * /////////////////////
 * 关于Future在JME中的典型使用一定要注意，否则可能在android下出现一些问题,
 * 类似如线程挂起、卡住、始终没有返回的情况，正确代码参考以下示例：
 * @author huliqing
 */
public class ThreadHelper {
    
    // 线程池的大小一般为CPU核心数的2倍比较合适， 这里暂定为4.
    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(8);
    
    /**
     * 创建一个需要在新线程中运行的任务。
     * <code>
     * <pre>
     * if (future != null) {
     *      if (future.isDone()) {
     *          try {
     *              Object result = future.get();
     *              // ..doLogic
     *              future = null;
     *          } catch (Exception ex) {
     *              // 注意点1：当发生异常时，这里要偿试取消
     *              if (future != null) {
     *                  future.cancel(true);
     *              }
     *              future = null;
     *          }
     *          注意点2：这里要判断是否已经取消
     *      } else if (future.isCancelled()){
     *          future = null;
     *      }
     * }
     * </pre>
     * </code>
     * @param <T>
     * @param callable
     * @return 
     */
    public static <T> Future<T> submit(Callable<T> callable) {
        return executor.submit(callable);
    }
    
    /**
     * 在系统退出时清理线程池占用的资源。
     */
    public static void cleanup() {
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
    }
    
    /**
     * 设置线程数, 默认8线程。线程数不是越多越好， 一般可以设置为: CPU核心数 * 2;  
     * @param corePoolSize 
     */
    public void setCorePoolSize(int corePoolSize) {
        executor.setCorePoolSize(corePoolSize);
    }
}
