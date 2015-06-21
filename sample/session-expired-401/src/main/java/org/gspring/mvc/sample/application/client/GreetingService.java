package org.gspring.mvc.sample.application.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greet/greetingService.gwt")
public interface GreetingService extends RemoteService {
	String greetServer(String name);
}
