package fdi.maps.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import fdi.maps.shared.MarkersParametre;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GMapsService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;

	ArrayList<MarkersParametre> getExtradata(String extradata, String datageturl, String protocol);
}
