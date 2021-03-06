/**
 * 
 */
package fdi.maps.client;

import com.google.gwt.geolocation.client.Position.Coordinates;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class CoordinatesGeo implements Coordinates {

	private Double Lati;
	private Double Longi;
	private String UrlFrame;
	private boolean info;
	
	public CoordinatesGeo(Double latii, Double longii, String UrlFrame,boolean info) {
		Lati=latii;
		Longi=longii;
		this.UrlFrame=UrlFrame;
		this.info=info;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.geolocation.client.Position.Coordinates#getAccuracy()
	 */
	@Override
	public double getAccuracy() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.geolocation.client.Position.Coordinates#getAltitude()
	 */
	@Override
	public Double getAltitude() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.geolocation.client.Position.Coordinates#getAltitudeAccuracy()
	 */
	@Override
	public Double getAltitudeAccuracy() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.geolocation.client.Position.Coordinates#getHeading()
	 */
	@Override
	public Double getHeading() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.geolocation.client.Position.Coordinates#getLatitude()
	 */
	@Override
	public double getLatitude() {
		// TODO Auto-generated method stub
		return Lati;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.geolocation.client.Position.Coordinates#getLongitude()
	 */
	@Override
	public double getLongitude() {
		// TODO Auto-generated method stub
		return Longi;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.geolocation.client.Position.Coordinates#getSpeed()
	 */
	@Override
	public Double getSpeed() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getUrlFrame() {
		return UrlFrame;
	}
	
	public boolean isInfo() {
		return info;
	}
	
	public void setInfo(boolean info) {
		this.info = info;
	}
}
