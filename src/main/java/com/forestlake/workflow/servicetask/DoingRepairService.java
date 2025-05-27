package com.forestlake.workflow.servicetask;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;


@Service("doRepair")
public class DoingRepairService implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("开始上门修理");
        String currentActivityName = execution.getCurrentActivityName();
        System.out.println("当前活动名：" + currentActivityName);
        execution.setVariable("repairManName","王小满");
    }
}
