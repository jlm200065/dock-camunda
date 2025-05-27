package com.forestlake.workflow.gateway;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("checkVideoFormat")
public class CheckVideoFormatService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("进入检测视频格式任务");
        Object videoName = execution.getVariable("targetVideoName");
        System.out.println("视频名称："+videoName);
    }
}