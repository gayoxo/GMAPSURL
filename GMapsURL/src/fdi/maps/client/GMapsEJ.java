package fdi.maps.client;

import java.util.ArrayList;
import java.util.LinkedList;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.GoogleMap.DblClickHandler;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MVCArray;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerImage;
//import com.google.maps.gwt.client.MarkerImage;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Polyline;
import com.google.maps.gwt.client.PolylineOptions;
import fdi.maps.shared.ConstantsGeoLocal;


public class GMapsEJ implements EntryPoint {
	protected static final String ERROR_SETTING_DATA= "Error sending data to edition, try again and test asociated systems";
	LinkedList<Marker> listaMarked;
	private Geocoder fCoder;
	private GoogleMap gMap;
	private Marker ActualMarked;
	
//	private String PUNTO_NO_SETEADO="Point not seted yet, click on map to set the position";
//	private double DLatitude;
//	private double DLongitude;
	private FormPanel panel;
	private boolean editableAction;
	private String postUrl;
	private String passId;
	private String protocol;

	private boolean Calculado;
	private String multy;
//	private DirectionsService DS;
	private ArrayList<Coordinates> Coordenada;
	

	
	public void onModuleLoad() {

		//Captura de Parametros http://localhost:8080/GMapsURL/?latitude=36.5008762&longitude=-6.2684345
		
		
		
		Coordinates P=null;
		
		passId = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.PASSID);
		String passlatitude = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.LATITUDE);
		String passlongitude = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.LONGITUDE);
		String edit = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.EDIT);
		postUrl = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.POSTURL);
		protocol = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.PROTOCOL);
		
		multy = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.MULTI);
		
		
		if (protocol!=null)
			{
			protocol=protocol.toLowerCase();
			if (!protocol.toLowerCase().equals("http")||protocol.toLowerCase().equals("https"))
				protocol="http";
			}
		else
			protocol="http";
		
		
		try {
			editableAction=Boolean.parseBoolean(edit);
		} catch (Exception e) {
			editableAction=false;
		}
		
		
		boolean multiActivado = false;
		ArrayList<Coordinates> Coordenada=new ArrayList<Coordinates>();
		if (multy!=null)
		{
			try {
				JSONValue jsonValue = JSONParser.parseStrict(multy);
				JSONArray jsonArray = jsonValue.isArray();
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject point = jsonArray.get(i).isObject();
					String latii = point.get(ConstantsGeoLocal.LATITUDE).isString().stringValue();
					String longii = point.get(ConstantsGeoLocal.LONGITUDE).isString().stringValue();
					Coordinates nueva=new CoordinatesGeo(Double.parseDouble(latii),Double.parseDouble(longii));
					Coordenada.add(nueva);
				}
				
				if (Coordenada.size()>1)
					multiActivado=true;
				else
					if (Coordenada.size()==1)
						{
						passlatitude=Double.toString(Coordenada.get(0).getLatitude());
						passlongitude=Double.toString(Coordenada.get(0).getLongitude());
						}

				
			} catch (Exception e) {
				Window.alert(e.getMessage());
			}
			
		}
		
		
		if (multiActivado)
		{
			this.Coordenada=Coordenada;
			processMap(Coordenada.get(0));
			processRoute();
			
		}
		else
		{
		
		try {
			
			
			
			
			double DLatitude = Double.parseDouble(passlatitude);
			double DLongitude = Double.parseDouble(passlongitude);
			
			
			
			P= new CoordinatesGeo(DLatitude,DLongitude);
			
			
		} catch (Exception e) {
			P=null;
		}
		
		
		if (P==null)
		{
			Calculado=false;
		Geolocation geolocation = Geolocation.getIfSupported();
		if (geolocation != null) {
		    geolocation.watchPosition(new Callback<Position, PositionError>() {
		      

			@Override
		      public void onFailure(PositionError reason) {
		        //TODO handle error
		      }

		      @Override
		      public void onSuccess(Position result) {
		    	  
		    	  if (!Calculado)
		    		  {
		    		  processMap(result.getCoordinates());
		    	 	  Calculado=true;
		    		  }
		        
		        
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
				        mOpts.setIcon(MarkerImage.create("IconoRojo.png"));
				        
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
        
	}

	private void processRoute() {
		
		
		
		
		int cc=1;
        for (int i = 0; i < Coordenada.size(); i++) {        	 
			Coordinates lng=Coordenada.get(i);
			MarkerOptions mOptsT = MarkerOptions.create();
			 mOptsT.setPosition(LatLng.create(lng.getLatitude(), lng.getLongitude()));
			if (i==0)
				 mOptsT.setIcon(MarkerImage.create("IconoRojo.png"));
			else
				if (i==Coordenada.size()-1)
					 mOptsT.setIcon(MarkerImage.create("IconoAzul.png"));
				else
					 mOptsT.setIcon(MarkerImage.create("IconoAmarillo.png"));
			  Marker marker2 = Marker.create(mOptsT);
		        marker2.setTitle(Integer.toString(cc));
		        cc++;
		        
		        marker2.setMap(gMap);
			
		}
        
        
        MVCArray<LatLng> latLngArray = MVCArray.create();  
        for (Coordinates lng : Coordenada) {  
            latLngArray.push(LatLng.create(lng.getLatitude(), lng.getLongitude()));  
        }  
        PolylineOptions polyOpts = PolylineOptions.create();  
        polyOpts.setPath(latLngArray);  
        polyOpts.setStrokeColor("red");  
        polyOpts.setStrokeOpacity(0.5);  
        polyOpts.setStrokeWeight(5);  
        Polyline path = Polyline.create(polyOpts);  
        path.setMap(gMap);
		
		
		
		/*
		
		DirectionsRendererOptions options2 = DirectionsRendererOptions.create();
        final DirectionsRenderer directionsDisplay = DirectionsRenderer.create(options2);
        directionsDisplay.setMap(gMap);
        
        
        
        DirectionsRequest DRWalk = DirectionsRequest.create();
        
        
        setAllData(DRWalk,TravelMode.WALKING);
        
        
        
        DS=DirectionsService.create();
               
        DS.route(DRWalk, new DirectionsService.Callback() {
			
			@Override
			public void handle(DirectionsResult result, DirectionsStatus status) {
				if (status == DirectionsStatus.OK) 
			          directionsDisplay.setDirections(result);
			    else
			          {
			    	
			    	 DirectionsRequest DRWalking = DirectionsRequest.create();
			    	 
			    	 setAllData(DRWalking,TravelMode.BICYCLING);
			    	
			    	 DS.route(DRWalking, new DirectionsService.Callback() {
			 			
			 			@Override
			 			public void handle(DirectionsResult result, DirectionsStatus status) {
			 				if (status == DirectionsStatus.OK) 
			 			          directionsDisplay.setDirections(result);
			 			    else
			 			          {
			 			    	 DirectionsRequest DRDrive = DirectionsRequest.create();
						    	 
						    	 setAllData(DRDrive,TravelMode.DRIVING);
						    	
						    	 DS.route(DRDrive, new DirectionsService.Callback() {
						 			


									@Override
						 			public void handle(DirectionsResult result, DirectionsStatus status) {
						 				if (status == DirectionsStatus.OK) 
						 			          directionsDisplay.setDirections(result);
						 			    else
						 			          {
						 			    		Window.alert("I cant find a Route");
						 			    		
						 			    		int P=1;
						 			    		for (Coordinates coordinates : Coordenada)
						 			    		{
				 			   					 MarkerOptions mOpts = MarkerOptions.create();
				 			   				        mOpts.setPosition(LatLng.create(coordinates.getLatitude(), coordinates.getLongitude()));
				 			   				        
				 			   				        Marker marker = Marker.create(mOpts);
				 			   				        marker.setTitle(Integer.toString(P));
				 			   				        P++;
				 			   				        marker.setMap(gMap);
						 			    		}
						 			    		
						 			    		

						 			    		
						 			    		
						 			    		
//						 			    		for (Coordinates coordinates : Coordenada) {
//						 			    		GeocoderRequest GReq = GeocoderRequest.create();
//						 			   			GReq.setLocation(LatLng.create(coordinates.getLatitude(), coordinates.getLongitude()));
//						 			   			fCoder.geocode(GReq, new Geocoder.Callback() {
//						 			   				
//						 			   				
//						 			   				
//
//
//						 			   				@Override
//						 			   				public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
//						 			   					GeocoderResult result = a.shift();
////						 			   					Window.alert(result.getFormattedAddress());
//						 			   					 MarkerOptions mOpts = MarkerOptions.create();
////						 			   				        mOpts.setIcon(markerImage);
//						 			   				        mOpts.setPosition(result.getGeometry().getLocation());
//						 			   				        
//						 			   				        Marker marker = Marker.create(mOpts);
//						 			   				        marker.setTitle(result.getFormattedAddress());
//						 			   				        marker.setMap(gMap);
//						 			   				        
////						 			   				        if (ActualMarked!=null)
////						 			   				        {
////						 			   				        GoogleMap Nulo=null;
////						 			   						ActualMarked.setMap(Nulo);
////						 			   				        }
////						 			   				        
////						 			   				        
////						 			   						ActualMarked=marker;
//						 			   				        
//						 			   				}
//						 			   			});
//												}
						 			          }
						 			        
						 				
						 			}


						 		});
			 			          }
			 			        
			 				
			 			}
			 		});
			          }
			        
				
			}
		});
		*/
	}

//	private void setAllData(DirectionsRequest DRWalk, TravelMode mode) {
//		Coordinates Origin = Coordenada.get(0);
//        Coordinates Destiny = Coordenada.get(Coordenada.size()-1);
//        
//        DRWalk.setOrigin(LatLng.create(Origin.getLatitude(),Origin.getLongitude()));
//        DRWalk.setDestination(LatLng.create(Destiny.getLatitude(),Destiny.getLongitude()));
//        DRWalk.setTravelMode(mode);
//        
//        
//        JsArray<DirectionsWaypoint> waypoints = JsArray.createArray().cast();
//        for (int i = 1; i < Coordenada.size()-1; i++) {
//        	 Coordinates Stop = Coordenada.get(i);
//        	DirectionsWaypoint DW=DirectionsWaypoint.create();
//        	 DW.setLocation(LatLng.create(Stop.getLatitude(),Stop.getLongitude()));
//             DW.setStopover(true);
//             waypoints.push(DW);
//		}
//        
//        DRWalk.setWaypoints(waypoints);
//		
//	}

	protected void processMap(Coordinates coor) {
	
		
        panel = new FormPanel();
        panel.setMethod(FormPanel.METHOD_GET);
        panel.setWidth("100%");
        panel.setHeight((Window.getClientHeight()-20)+"px");
//        panel.setHeight( "600px");
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
        
        if (editableAction){
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
					        mOpts.setIcon(MarkerImage.create("IconoRojo.png"));
					        
					        if (ActualMarked!=null)
					        {
					        GoogleMap Nulo=null;
							ActualMarked.setMap(Nulo);
					        }
					        
					        
							ActualMarked=marker;
							
//							if (postUrl!=null&&!postUrl.isEmpty())
//							 {
//							
//							String URLPOSTF = protocol+"://"+postUrl+"?"+ConstantsGeoLocal.LATITUDE+"="+ActualMarked.getPosition().lat()+"&"+ConstantsGeoLocal.LONGITUDE+"="+ActualMarked.getPosition().lng();
//							
//							if (passId!=null&&!passId.isEmpty())
//								URLPOSTF=URLPOSTF+"&"+ConstantsGeoLocal.PASSID+"="+passId;
////							Window.alert(URLPOSTF);
////							panel.setAction(URLPOSTF);
////							panel.submit();
//							
//							doGet(URLPOSTF);
//					
//							
//							 }
							
							
					        
					}
				});
				
			}
		});     
        


        Window.addCloseHandler( 
        	    new CloseHandler<Window>() 
        	    {
        	       

					public void onClose( CloseEvent<Window> windowCloseEvent ) 
        	        {
	
							
						
        	        	 if (postUrl!=null&&!postUrl.isEmpty())
						 {
						
						String URLPOSTF = protocol+"://"+postUrl+"?";
						
						if (ActualMarked!=null)
							URLPOSTF = URLPOSTF+ConstantsGeoLocal.LATITUDE+"="+ActualMarked.getPosition().lat()+"&"+ConstantsGeoLocal.LONGITUDE+"="+ActualMarked.getPosition().lng();
						
							
						if (passId!=null&&!passId.isEmpty())
							{
							if (ActualMarked!=null)
								URLPOSTF=URLPOSTF+"&";
							URLPOSTF=URLPOSTF+ConstantsGeoLocal.PASSID+"="+passId;
							}
//						Window.alert(URLPOSTF);
//						panel.setAction(URLPOSTF);
//						panel.submit();
						
						doGet(URLPOSTF);
						
	
						
						 }
        	        }
        	    } ); 
        

        }
	}
	
	
	public void doGet(String url) {
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

        try {
            builder.sendRequest(null, new RequestCallback() {
            	
                public void onError(Request request, Throwable exception) {
                    // Code omitted for clarity
                	Window.alert(ERROR_SETTING_DATA+" to " +postUrl+"->"+exception.getCause());
                }

                public void onResponseReceived(Request request, Response response) {
                    // Code omitted for clarity
                }
            });

        } catch (RequestException e) {
            // Code omitted for clarity
        }
    }
	
	
	

}