package fdi.maps.client;

import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerImage;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Marker.ClickHandler;

public class MarkerCoordGeoMap {

	private Marker marker2;
	private Coordinates lngt;
	private GoogleMap gMapt;

	public MarkerCoordGeoMap(Coordinates lng, int size, GoogleMap gMap, int cc, int actual) {
		this.lngt=lng;
		this.gMapt=gMap;
		MarkerOptions mOptsT = MarkerOptions.create();
		 mOptsT.setPosition(LatLng.create(lng.getLatitude(), lng.getLongitude()));
		if (actual==0)
			 mOptsT.setIcon(MarkerImage.create(GMapsEJ.GEOICONRED));
		else
			if (actual==size)
				 mOptsT.setIcon(MarkerImage.create(GMapsEJ.GEOICOBLUE));
			else
				 mOptsT.setIcon(MarkerImage.create(GMapsEJ.GEOICONYEL));
		  marker2 = Marker.create(mOptsT);
	        marker2.setTitle(Integer.toString(cc));
	        
	        
	       
	        	marker2.addClickListener(new ClickHandler() {
					
					@Override
					public void handle(MouseEvent event) {
						 if (lngt instanceof CoordinatesGeo)
					        {
							 GMapsEJ.drawInfoWindow(marker2, event,((CoordinatesGeo) lngt).getUrlFrame(),gMapt);
					        }
						
					}
				});
	        
	        
	        marker2.setMap(gMap);
	}

}
