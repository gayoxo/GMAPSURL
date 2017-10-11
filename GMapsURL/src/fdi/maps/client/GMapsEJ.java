package fdi.maps.client;

import java.util.ArrayList;
import java.util.LinkedList;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.InputElement;
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
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.GoogleMap.DblClickHandler;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.google.maps.gwt.client.places.Autocomplete;
import com.google.maps.gwt.client.places.AutocompleteOptions;
import com.google.maps.gwt.client.places.PlaceGeometry;
import com.google.maps.gwt.client.places.PlaceResult;
import com.google.maps.gwt.client.places.Autocomplete.PlaceChangedHandler;
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
import fdi.maps.shared.MarkersParametre;



public class GMapsEJ implements EntryPoint {
	protected static final String ERROR_SETTING_DATA= "Error sending data to edition, try again and test asociated systems";
	public static final String GEOICONRED = "Geo/IconoRojo.png";
	public static final String GEOICOBLUE = "Geo/IconoAzul.png";
	public static final String GEOICONYEL = "Geo/IconoAmarillo.png";
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
//	private String multy;
//	private DirectionsService DS;
	private ArrayList<Coordinates> Coordenada;
private Autocomplete autoComplete;
private static GMapsServiceAsync serviceGMaps = GWT.create(GMapsService.class);
private String extradata;
private String datageturl;
	

	
	public void onModuleLoad() {

		//Captura de Parametros http://localhost:8080/GMapsURL/?latitude=36.5008762&longitude=-6.2684345
		
		
		
//		Coordinates P=null;
		
		passId = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.PASSID);
//		String passlatitude = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.LATITUDE);
//		String passlongitude = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.LONGITUDE);
		String edit = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.EDIT);
		postUrl = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.POSTURL);
		protocol = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.PROTOCOL);
		extradata  = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.EXTRADATA);
		datageturl = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.DATAGETURL);
		
//		multy = com.google.gwt.user.client.Window.Location.getParameter(ConstantsGeoLocal.MULTI);
		
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
		
		if (extradata!=null&&datageturl!=null)
		{
			GWT.log(extradata
					+"-"+datageturl);
			serviceGMaps.getExtradata(extradata,datageturl,protocol,new AsyncCallback<ArrayList<MarkersParametre>>() {

				
				
				private Coordinates P=null;
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Error obnteniendo los datos extra de la posicion");
					
				}

				@Override
				public void onSuccess(ArrayList<MarkersParametre> result) {
					
					boolean multiActivado = false;
					double DLatitude;
					double DLongitude;
					ArrayList<Coordinates> Coordenadainterna=new ArrayList<Coordinates>();
					
					String DURL;
					if (result.size()>1)
					{
					
						
						
							
							
							try {
	
								for (int i = 0; i < result.size(); i++) {
									MarkersParametre markersParametreA = result.get(i);
									Coordinates nueva=new CoordinatesGeo(markersParametreA.getLat(),markersParametreA.getLng(),markersParametreA.getUrlFrame());
									Coordenadainterna.add(nueva);
								}
								
								if (Coordenadainterna.size()>1)
									multiActivado=true;
								else
									if (Coordenadainterna.size()==1)
										{
										DLatitude=Coordenadainterna.get(0).getLatitude();
										DLatitude=Coordenadainterna.get(0).getLongitude();
										DURL=((CoordinatesGeo)Coordenadainterna.get(0)).getUrlFrame();
										}

								
							} catch (Exception e) {
								Window.alert(e.getMessage());
							}
							
						
					}
						
						if (multiActivado)
						{
							Coordenada=Coordenadainterna;
							processMap(Coordenadainterna.get(0));
							processRoute();
							
						}
						else
						{
						
						
						
						if (!result.isEmpty())
						{
							
							MarkersParametre markersParametreA = result.get(0);	
							
						
						
						try {
							
							
							
							
							DLatitude = markersParametreA.getLat();
							DLongitude = markersParametreA.getLng();
							DURL= markersParametreA.getUrlFrame();
							
							
							P= new CoordinatesGeo(DLatitude,DLongitude,DURL);
							
							
						} catch (Exception e) {
							P=null;
						}
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
								
								
								


								private Marker marker;

								@Override
								public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
									GeocoderResult result = a.shift();
//									Window.alert(result.getFormattedAddress());
									 MarkerOptions mOpts = MarkerOptions.create();
//								        mOpts.setIcon(markerImage);
								        mOpts.setPosition(result.getGeometry().getLocation());
								        mOpts.setIcon(MarkerImage.create(GEOICONRED));
								        
								        marker = Marker.create(mOpts);
								        marker.setTitle(result.getFormattedAddress());
								        marker.setMap(gMap);
								        
								        
								        if (P instanceof CoordinatesGeo)
								        {
								        	marker.addClickListener(new ClickHandler() {
												
												@Override
												public void handle(MouseEvent event) {
													drawInfoWindow(marker, event,((CoordinatesGeo) P).getUrlFrame(),gMap);
													
												}
											});
								        }
								        
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


			});
		}else
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

		}
		
		
		
		
		/*
		
		
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
				        mOpts.setIcon(MarkerImage.create(GEOICONRED));
				        
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
        */
	}
	
	
	
	
	
	public static void drawInfoWindow(final Marker marker, MouseEvent mouseEvent,String URL,GoogleMap gMap) {
	    if (marker == null || mouseEvent == null) {
	      return;
	    }

	    Frame F=new Frame(URL);
	    
	    
	    InfoWindowOptions options = InfoWindowOptions.create();
	    options.setContent(F.toString());

	    InfoWindow iw = InfoWindow.create(options);
	    iw.open(gMap, marker);


	  }

	private void processRoute() {
		
		
		
		
		int cc=1;
        for (int i = 0; i < Coordenada.size(); i++) { 
        	
        	Coordinates lng=Coordenada.get(i);
        	
        	new MarkerCoordGeoMap(lng,Coordenada.size()-1,gMap,cc,i);
        	
			cc++;
			
			
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
        
        TextBox textea=null;
        if (editableAction){
    		textea = new TextBox();
    		textea.setWidth("90%");
    		RootPanel.get("centered").add(textea);
        }
        
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
        	
        	if (textea!=null)
        	{
        	 InputElement element = InputElement.as(textea.getElement());
             
             AutocompleteOptions options5 = AutocompleteOptions.create();
             
//     		options5.setTypes(types);
             options5.setBounds(gMap.getBounds());
             
             autoComplete = Autocomplete.create(element, options5);

             autoComplete.addPlaceChangedListener(new PlaceChangedHandler() {


     		@Override
     		public void handle() {
     			  PlaceResult result = autoComplete.getPlace();

     	            PlaceGeometry geomtry = result.getGeometry();
     	            LatLng center = geomtry.getLocation();

     	            gMap.panTo(center);
     	            setPoint(center);
     	            // mapWidget.setZoom(8);

     	            GWT.log("place changed center=" + center);
     			
     		}
             });
        	
        	}
        	
        gMap.addDblClickListener(new DblClickHandler() {
			
        	
        	

			@Override
			public void handle(MouseEvent event) {
				
				
				
				setPoint(event.getLatLng());
				
				
				
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
	
	
	protected void setPoint(LatLng latLng) {
		GeocoderRequest GReq = GeocoderRequest.create();
		GReq.setLocation(latLng);
		
		fCoder.geocode(GReq, new Geocoder.Callback() {
			
			
			


			@Override
			public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
				GeocoderResult result = a.shift();
//				Window.alert(result.getFormattedAddress());
				 MarkerOptions mOpts = MarkerOptions.create();
//			        mOpts.setIcon(markerImage);
			        mOpts.setPosition(result.getGeometry().getLocation());
			        
			        Marker marker = Marker.create(mOpts);
			        marker.setTitle(result.getFormattedAddress());
			        marker.setMap(gMap);
			        mOpts.setIcon(MarkerImage.create(GEOICONRED));
			        
			        if (ActualMarked!=null)
			        {
			        GoogleMap Nulo=null;
					ActualMarked.setMap(Nulo);
			        }
			        
			        
					ActualMarked=marker;
					
//					if (postUrl!=null&&!postUrl.isEmpty())
//					 {
//					
//					String URLPOSTF = protocol+"://"+postUrl+"?"+ConstantsGeoLocal.LATITUDE+"="+ActualMarked.getPosition().lat()+"&"+ConstantsGeoLocal.LONGITUDE+"="+ActualMarked.getPosition().lng();
//					
//					if (passId!=null&&!passId.isEmpty())
//						URLPOSTF=URLPOSTF+"&"+ConstantsGeoLocal.PASSID+"="+passId;
////					Window.alert(URLPOSTF);
////					panel.setAction(URLPOSTF);
////					panel.submit();
//					
//					doGet(URLPOSTF);
//			
//					
//					 }
					
					
			        
			}
		});
		
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