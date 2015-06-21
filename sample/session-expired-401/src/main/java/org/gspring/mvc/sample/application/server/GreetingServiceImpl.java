package org.gspring.mvc.sample.application.server;

import org.gspring.mvc.sample.application.client.GreetingService;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service("greetingService")
public class GreetingServiceImpl implements GreetingService {
    @Override
    public String greetServer(String name) {
        return MessageFormat.format("Hello, {0}!", name);
    }
}
