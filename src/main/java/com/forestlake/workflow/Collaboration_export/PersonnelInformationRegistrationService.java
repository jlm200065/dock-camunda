package com.forestlake.workflow.Collaboration_export;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("PersonnelInformationRegistration")
public class PersonnelInformationRegistrationService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("出海人员信息登记");

    }
}
