package io.github.kingschan1204.istock.module.spider.timerjob;

/**
 *
 * @author chenguoxiang
 * @create 2019-07-26 13:43
 **/
public abstract class AbstractTimeJob implements ITimerJob{
    protected String name;
    protected STATUS status=STATUS.STOP;
}
