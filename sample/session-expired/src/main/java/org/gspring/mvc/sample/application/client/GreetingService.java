package org.gspring.mvc.sample.application.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.gspring.mvc.sample.application.shared.security.AppSecurityException;

@RemoteServiceRelativePath("greet/greetingService.gwt")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws AppSecurityException;
}
