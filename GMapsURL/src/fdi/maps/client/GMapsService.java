package fdi.maps.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GMapsService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;

	String getExtradata(String extradata, String datageturl, String protocol);
}
