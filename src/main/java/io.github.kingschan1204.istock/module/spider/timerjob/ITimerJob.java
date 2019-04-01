package io.github.kingschan1204.istock.module.spider.timerjob;

/**
 * 1天只需要执行一次的定时任务
 * timer job interface
 * @author chenguoxiang
 * @create 2019-03-28 0:25
 **/
public interface ITimerJob {

    /**
     * 执行
     * @throws Exception
     */
    void execute()throws Exception;

}
