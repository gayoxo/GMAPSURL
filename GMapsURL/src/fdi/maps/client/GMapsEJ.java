package fdi.maps.client;

import java.util.LinkedList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.maps.gwt.client.DirectionsRenderer;
import com.google.maps.gwt.client.DirectionsRendererOptions;
import com.google.maps.gwt.client.DirectionsRequest;
import com.google.maps.gwt.client.DirectionsResult;
import com.google.maps.gwt.client.DirectionsService;
import com.google.maps.gwt.client.DirectionsStatus;
import com.google.maps.gwt.client.DirectionsWaypoint;
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
import com.google.maps.gwt.client.TravelMode;


public class GMapsEJ implements EntryPoint {
	private ListBox L;
	LinkedList<Marker> listaMarked;
	private Geocoder fCoder;
	private GoogleMap gMap;

	public void onModuleLoad() {
		listaMarked=new LinkedList<>();
		FormPanel panel = new FormPanel();
        panel.setWidth("100%");
        panel.setHeight("600px");
        MapOptions options = MapOptions.create();
        options.setCenter(LatLng.create(40.4169,-3.7033));
        options.setZoom(13);
        options.setMapTypeId(MapTypeId.ROADMAP);
        options.setDraggable(true);
        options.setMapTypeControl(true);
        options.setScaleControl(true);
        options.setScrollwheel(true);
        options.setMapMaker(true);
//        Button btn = new Button();
        gMap = GoogleMap.create(panel.getElement(), options);
        RootPanel.get("centered").add(panel);
//        RootPanel.get("centered").add(btn);

        gMap.addDblClickListener(new DblClickHandler() {
			
        	
        	

			@Override
			public void handle(MouseEvent event) {
//				Window.alert(event.getLatLng().toString());
//		        MarkerImage markerImage = MarkerImage.create();
				
				GeocoderRequest GReq = GeocoderRequest.create();
				GReq.setLocation(event.getLatLng());
				fCoder.geocode(GReq, new Geocoder.Callback() {
					
					
					
					private Marker marker;
					private int Position;

					@Override
					public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
						GeocoderResult result = a.shift();
//						Window.alert(result.getFormattedAddress());
						 MarkerOptions mOpts = MarkerOptions.create();
//					        mOpts.setIcon(markerImage);
					        mOpts.setPosition(result.getGeometry().getLocation());
					        
					        marker = Marker.create(mOpts);
					        marker.setTitle(result.getFormattedAddress());
					        marker.setMap(gMap);
					        
					        listaMarked.add(marker);
					        L.addItem(result.getFormattedAddress());
					        Position=L.getItemCount()-1;
					        marker.addDblClickListener(new Marker.DblClickHandler() {
								
								@Override
								public void handle(MouseEvent event) {
									
									BorrarPunto(marker,Position);

								}
							});
					}
				});
				
				
		       
				
			}
		});
        
        LatLng centerIcon = LatLng.create(40.4169,-3.7033);
//        MarkerImage markerImage = MarkerImage.create();
        MarkerOptions mOpts = MarkerOptions.create();
//        mOpts.setIcon(markerImage);
        mOpts.setPosition(centerIcon);
        
        Marker marker = Marker.create(mOpts);
        marker.setMap(gMap);
        
//        gMap.addIdleListener(new GoogleMap.IdleHandler() {
//
//            public void handle() {
//                Window.alert("Idle");
//
//            }
//        });
        fCoder = Geocoder.create();
        L=new ListBox();
        L.setVisibleItemCount(5);
        L.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				int Selecctioado = L.getSelectedIndex()-1;
				if (!listaMarked.isEmpty())
					{
					
					Marker Marked=listaMarked.get(Selecctioado);
					BorrarPunto(Marked,Selecctioado);
					}
				
			}
		});
        
        
        
        
        
        
        
        RootPanel.get("centered").add(L);
        
        
        FormPanel panel2 = new FormPanel();
        panel2.setWidth("100%");
        panel2.setHeight("600px");
        MapOptions options3 = MapOptions.create();
        options3.setCenter(LatLng.create(40.4169,-3.7033));
        options3.setZoom(13);
        options3.setMapTypeId(MapTypeId.ROADMAP);
        options3.setDraggable(true);
        options3.setMapTypeControl(true);
        options3.setScaleControl(true);
        options3.setScrollwheel(true);
        options3.setMapMaker(true);
//        Button btn = new Button();
        GoogleMap gMap2 = GoogleMap.create(panel2.getElement(), options3);
        RootPanel.get("centered").add(panel2);
//      
        
        
        
        DirectionsRendererOptions options2 = DirectionsRendererOptions.create();
        final DirectionsRenderer directionsDisplay = DirectionsRenderer.create(options2);
        directionsDisplay.setMap(gMap2);
        
        DirectionsRequest DR = DirectionsRequest.create();
        DR.setOrigin(LatLng.create(40.4169,-3.7033));
        DR.setDestination(LatLng.create(37.8550964,-4.7086738));
        DR.setTravelMode(TravelMode.WALKING);
        
        DirectionsWaypoint DW=DirectionsWaypoint.create();
        
        DW.setLocation(LatLng.create(39.8676536,-4.0098788));
        DW.setStopover(true);

        DirectionsWaypoint DW2=DirectionsWaypoint.create();
        
        DW2.setLocation(LatLng.create(38.9554156,-3.9809874));
        DW2.setStopover(true);
        
        JsArray<DirectionsWaypoint> waypoints = JsArray.createArray().cast();
        waypoints.push(DW);
        waypoints.push(DW2);
        
        
        DR.setWaypoints(waypoints);
        
        DirectionsService DS=DirectionsService.create();
        
        DS.route(DR, new DirectionsService.Callback() {
			
			@Override
			public void handle(DirectionsResult result, DirectionsStatus status) {
				if (status == DirectionsStatus.OK) {
			          directionsDisplay.setDirections(result);
			        } else if (status == DirectionsStatus.INVALID_REQUEST) {
			        	Window.alert(status.toString());
			        } else if (status == DirectionsStatus.MAX_WAYPOINTS_EXCEEDED) {
			        	Window.alert(status.toString());
			        } else if (status == DirectionsStatus.NOT_FOUND) {
			        	Window.alert(status.toString());
			        } else if (status == DirectionsStatus.OVER_QUERY_LIMIT) {
			        	Window.alert(status.toString());
			        } else if (status == DirectionsStatus.REQUEST_DENIED) {
			        	Window.alert(status.toString());
			        } else if (status == DirectionsStatus.UNKNOWN_ERROR) {
			        	Window.alert(status.toString());
			        } else if (status == DirectionsStatus.ZERO_RESULTS) {
			        	Window.alert(status.toString());
			        }
				
			}
		});
  
        
	}

	protected void BorrarPunto(Marker Marked, int Selecctioado) {
		
		if (Window.confirm("Esta seguro de que desea borrar el punto"))
		{
		GoogleMap Nulo=null;
		Marked.setMap(Nulo);
		L.removeItem(Selecctioado);
		listaMarked.remove(Selecctioado);
		}
		
	}
	
	
//	private void addHandlers() {
//        fMap.addMapDoubleClickHandler(new MapDoubleClickHandler() {
//
//            @Override
//            public void onDoubleClick(MapDoubleClickEvent event) {
//                if (event.getLatLng() != null) {
//                    performReverseLookup(event.getLatLng());
//                }
//            }
//
//        });
//
//        fMarker.addMarkerDragEndHandler(new MarkerDragEndHandler() {
//
//            @Override
//            public void onDragEnd(MarkerDragEndEvent event) {
//                LatLng point = event.getSender().getLatLng();
//                if (point != null) {
//                    performReverseLookup(point);
//                }
//            }
//
//        });
//    }
//
//    private void performReverseLookup(final LatLng point) {
//        fCoder.getLocations(point, new LocationCallback() {
//
//            @Override
//            public void onSuccess(JsArray<Placemark> locations) {
//                if (locations.length() > 0) {
//                    LatLng point = locations.get(0).getPoint();
//                    fMarker.setLatLng(point);
//                    fMarker.setVisible(true);
//                    fMap.getInfoWindow().open(point, new InfoWindowContent(locations.get(0).getAddress()));
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode) {}
//        });
//    }
	
	
	

}