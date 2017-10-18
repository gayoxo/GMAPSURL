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
	private boolean info;
	
	
	public MarkersParametre() {
		// TODO Auto-generated constructor stub
	}



	public MarkersParametre(Double lat, Double lng, String UrlFrame,boolean info) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.urlFrame=UrlFrame;
		this.info=info;
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
	
	public boolean isInfo() {
		return info;
	}
	
	public void setInfo(boolean info) {
		this.info = info;
	}
}
