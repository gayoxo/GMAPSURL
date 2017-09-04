package fdi.maps.client;

import java.util.LinkedList;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.GoogleMap.DblClickHandler;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
//import com.google.maps.gwt.client.MarkerImage;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import fdi.maps.shared.ConstantsGeoLocal;


public class GMapsEJ implements EntryPoint {
	LinkedList<Marker> listaMarked;
	private Geocoder fCoder;
	private GoogleMap gMap;
	private Marker ActualMarked;
	
	private String PUNTO_NO_SETEADO="Point not seted yet, click on map to set the position";
	private double DLatitude;
	private double DLongitude;
	private FormPanel panel;
	
	public void onModuleLoad() {
		
		
		//Captura de Parametros http://localhost:8080/GMapsURL/?latitude=36.5008762&longitude=-6.2684345
		
		Coordinates P=null;
		try {
			String passId = com.google.gwt.user.client.Window.Location.getParameter("passId");
			String passlatitude = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.LATITUDE);
			String passlongitude = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.LONGITUDE);
			
			DLatitude = Double.parseDouble(passlatitude);
			DLongitude = Double.parseDouble(passlongitude);
			
			P= new Coordinates() {
				
				@Override
				public Double getSpeed() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public double getLongitude() {
					// TODO Auto-generated method stub
					return DLongitude;
				}
				
				@Override
				public double getLatitude() {
					// TODO Auto-generated method stub
					return DLatitude;
				}
				
				@Override
				public Double getHeading() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Double getAltitudeAccuracy() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Double getAltitude() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public double getAccuracy() {
					// TODO Auto-generated method stub
					return 0;
				}
			};
			
			
		} catch (Exception e) {
			P=null;
		}
		
		
		if (P==null)
		{
		Geolocation geolocation = Geolocation.getIfSupported();
		if (geolocation != null) {
		    geolocation.watchPosition(new Callback<Position, PositionError>() {
		      

			@Override
		      public void onFailure(PositionError reason) {
		        //TODO handle error
		      }

		      @Override
		      public void onSuccess(Position result) {
		    	  
		    	  processMap(result.getCoordinates());
		    	  
		        
		        
		      }
		    });
		  }

		}else
		{
			processMap(P);
			
			
			GeocoderRequest GReq = GeocoderRequest.create();
			GReq.setLocation(LatLng.create(P.getLatitude(), P.getLongitude()));
			fCoder.geocode(GReq, new Geocoder.Callback() {
				
				
				


				@Override
				public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
					GeocoderResult result = a.shift();
//					Window.alert(result.getFormattedAddress());
					 MarkerOptions mOpts = MarkerOptions.create();
//				        mOpts.setIcon(markerImage);
				        mOpts.setPosition(result.getGeometry().getLocation());
				        
				        Marker marker = Marker.create(mOpts);
				        marker.setTitle(result.getFormattedAddress());
				        marker.setMap(gMap);
				        
				        if (ActualMarked!=null)
				        {
				        GoogleMap Nulo=null;
						ActualMarked.setMap(Nulo);
				        }
				        
				        
						ActualMarked=marker;
				        
				}
			});
			

		}
  
        
	}

	protected void processMap(Coordinates coor) {
        panel = new FormPanel();
        panel.setWidth("100%");
        panel.setHeight("600px");
        MapOptions options = MapOptions.create();
        options.setCenter(LatLng.create(coor.getLatitude(), coor.getLongitude()));
        options.setZoom(14);
        options.setMapTypeId(MapTypeId.ROADMAP);
        options.setDraggable(true);
        options.setMapTypeControl(true);
        options.setScaleControl(true);
        options.setScrollwheel(true);
        options.setDisableDoubleClickZoom(true);
        options.setMapMaker(true);
        gMap = GoogleMap.create(panel.getElement(), options);
        RootPanel.get("centered").add(panel);
        fCoder = Geocoder.create();
        
        
        gMap.addDblClickListener(new DblClickHandler() {
			
        	
        	

			@Override
			public void handle(MouseEvent event) {
				
				GeocoderRequest GReq = GeocoderRequest.create();
				GReq.setLocation(event.getLatLng());
				fCoder.geocode(GReq, new Geocoder.Callback() {
					
					
					


					@Override
					public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
						GeocoderResult result = a.shift();
//						Window.alert(result.getFormattedAddress());
						 MarkerOptions mOpts = MarkerOptions.create();
//					        mOpts.setIcon(markerImage);
					        mOpts.setPosition(result.getGeometry().getLocation());
					        
					        Marker marker = Marker.create(mOpts);
					        marker.setTitle(result.getFormattedAddress());
					        marker.setMap(gMap);
					        
					        if (ActualMarked!=null)
					        {
					        GoogleMap Nulo=null;
							ActualMarked.setMap(Nulo);
					        }
					        
					        
							ActualMarked=marker;
					        
					}
				});
				
			}
		});        


	       Button submitButton =new Button("Submit");
	       RootPanel.get("centered").add(submitButton);
	       
	       
	       submitButton.addClickHandler(new ClickHandler() {
			

			@Override
			public void onClick(ClickEvent event) {
				if (ActualMarked!=null)
					{
					panel.setAction("/Holapa?lat="+ActualMarked.getPosition().lat()+"&lng="+ActualMarked.getPosition().lng());
					panel.submit();
					}
				else Window.alert(PUNTO_NO_SETEADO);
				
			}
		});
		
	}
	
	

	
	
	

}