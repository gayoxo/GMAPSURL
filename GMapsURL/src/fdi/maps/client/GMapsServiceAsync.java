package fdi.maps.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fdi.maps.shared.MarkersParametre;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GMapsServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void getExtradata(String extradata, String datageturl, String protocol, String visualdatageturl, String documentid, AsyncCallback<ArrayList<MarkersParametre>> asyncCallback);
}
