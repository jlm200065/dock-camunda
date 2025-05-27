package com.forestlake.workflow.scripttask;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("queryAnnualLeave")
public class QueryAnnualLeaveService implements JavaDelegate {
  @Autowired
  private RepositoryService repositoryService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("进入查询剩余年假任务");
        Integer leftAnnualDays =(Integer) execution.getVariable("leftAnnualDays");
        //        添加业务逻辑接口更新年假天数


        System.out.println("剩余年假天数："+leftAnnualDays);
    }
}

