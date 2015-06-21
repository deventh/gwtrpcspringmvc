package org.gspring.mvc.sample.application.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;

public abstract class GenericAsyncCallback<T> implements AsyncCallback<T> {
    private static final int REQUIRES_RELOGIN_STATUS_CODE = 401;

    @Override
    public final void onFailure(Throwable caught) {
        if (caught instanceof StatusCodeException) {
            StatusCodeException statusCodeException = (StatusCodeException) caught;

            if(statusCodeException.getStatusCode() == REQUIRES_RELOGIN_STATUS_CODE) {
                Window.alert("Session is expired, please refresh browser");
            }

            return;
        }

        catchFailure(caught);
    }

    protected abstract void catchFailure(Throwable caught);
}
