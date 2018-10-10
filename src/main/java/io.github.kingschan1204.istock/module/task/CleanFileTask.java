package io.github.kingschan1204.istock.module.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;

/**
 * 数据文件清理
 * @author chenguoxiang
 * @create 2018-10-10 14:51
 **/
@Component
public class CleanFileTask implements Job {
    private Logger log = LoggerFactory.getLogger(CleanFileTask.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        File f = new File("./data/");
        if(!f.exists()){return;}
        Arrays.stream(f.listFiles()).forEach(file ->{
            log.info("清理文件:{}-{}",file.getName(),file.delete());
        });


    }
}
