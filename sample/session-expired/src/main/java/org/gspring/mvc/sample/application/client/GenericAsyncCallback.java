package org.gspring.mvc.sample.application.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.gspring.mvc.sample.application.shared.security.SessionExpiredExceptionApp;

public abstract class GenericAsyncCallback<T> implements AsyncCallback<T> {
    @Override
    public final void onFailure(Throwable caught) {
        if (caught instanceof SessionExpiredExceptionApp) {
            Window.alert("Session is expired, please refresh browser");
            return;
        }

        catchFailure(caught);
    }

    protected abstract void catchFailure(Throwable caught);
}
