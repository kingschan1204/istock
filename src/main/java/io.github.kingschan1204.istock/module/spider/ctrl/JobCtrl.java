package io.github.kingschan1204.istock.module.spider.ctrl;

import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimeJobFactory;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimerJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenguoxiang
 * @create 2019-09-20 14:20
 **/
@Slf4j
@RequestMapping("/job")
@Controller
public class JobCtrl {

    @ResponseBody
    @GetMapping("/joblist")
    public JSONArray jobList() {
        return ITimeJobFactory.getTasks();
    }

    @RequestMapping("/index")
    public String index(){
        return "/job/job";
    }

    @ResponseBody
    @GetMapping("/stop/{job}")
    public String stop(@PathVariable("job") String job)throws Exception {
         ITimeJobFactory.getJob(ITimeJobFactory.TIMEJOB.valueOf(job)).execute(ITimerJob.COMMAND.STOP);
         return "ok";
    }

    @ResponseBody
    @GetMapping("/start/{job}")
    public String start(@PathVariable("job") String job)throws Exception {
        ITimeJobFactory.getJob(ITimeJobFactory.TIMEJOB.valueOf(job)).execute(ITimerJob.COMMAND.START);
        return "ok";
    }
}
