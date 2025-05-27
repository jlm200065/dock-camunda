package com.forestlake.workflow.listeners;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("addLeaders")
public class AddLeadersListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) throws Exception {
        long leaveDay =  (long)execution.getVariable("leaveDays");
        System.out.println("进入增加领导集合类，员工请假天数："+leaveDay);
        List<String> leaders = new ArrayList<>();
        if (leaveDay>3 && leaveDay<=5){
            leaders.add("wangbing");
            leaders.add("zhangsan");
        }else if (leaveDay>5){
            leaders.add("wangbing");
            leaders.add("zhangsan");
            leaders.add("wangwu");
        }
        execution.setVariable("leaders",leaders);
    }
}