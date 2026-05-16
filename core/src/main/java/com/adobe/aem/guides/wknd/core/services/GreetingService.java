package com.adobe.aem.guides.wknd.core.services;

import lombok.Getter;
import org.osgi.service.component.annotations.Component;

@Component(service = GreetingService.class)
public class GreetingService {

    @Getter
    private String greetingMessage = "Hello from OSGI Service";
}
