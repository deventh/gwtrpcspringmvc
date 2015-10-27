package org.gspring.mvc.sample.application.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.gspring.mvc.sample.application.shared.ApplicationException;

import java.util.List;

@RemoteServiceRelativePath("greet/greetingService.gwt")
public interface PostEmailService extends RemoteService {
	List<String> post(String email) throws ApplicationException;
}
