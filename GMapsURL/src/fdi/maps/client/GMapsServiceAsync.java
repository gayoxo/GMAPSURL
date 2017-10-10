package fdi.maps.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GMapsServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void getExtradata(String extradata, String datageturl, String protocol, AsyncCallback<String> asyncCallback);
}
