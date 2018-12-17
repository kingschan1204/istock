package io.github.kingschan1204.istock.common.exception;

/**
 *  爬取异常
 * @author chenguoxiang
 * @create 2018-11-13 16:20
 **/
public class CrawlException extends RuntimeException {

    public CrawlException(){
        super();
    }

    public CrawlException(String message){
        super(message);
    }

    public CrawlException(String message,Throwable e){
        super(message,e);
    }
}
