/**
 * 
 */
package fdi.maps.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class MarkersParametre implements Serializable,IsSerializable{

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private Double lat;
	private Double lng;
	private String urlFrame;

	
	
	public MarkersParametre() {
		// TODO Auto-generated constructor stub
	}



	public MarkersParametre(Double lat, Double lng, String UrlFrame) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.urlFrame=UrlFrame;
	}



	public Double getLat() {
		return lat;
	}
	
	
	public Double getLng() {
		return lng;
	}
	
	public String getUrlFrame() {
		return urlFrame;
	}
}
