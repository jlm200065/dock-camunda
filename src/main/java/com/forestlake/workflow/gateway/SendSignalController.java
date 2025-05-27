package com.forestlake.workflow.gateway;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendSignalController {
    @Autowired
    private RuntimeService runtimeService;

    @GetMapping("sendSignal")
    public boolean sendSignal(){
        runtimeService.createSignalEvent("Signal_direct_leader").send();
        return true;
    }

}