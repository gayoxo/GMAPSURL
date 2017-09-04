package fdi.maps.server;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import fdi.ucm.server.interconect.model.Interconect;
import fdi.ucm.server.interconect.model.Parameter;


@Path("Param")
public class ServiceParamRest  extends RemoteServiceServlet {


	private static final long serialVersionUID = -5608185730468273983L;
	public static final String GEOLOCALIZATION = "geolocalization";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String ICONPATH = "gmaps.png";
	
	@Context
	UriInfo uri;
	
	@Context 
	ServletContext context;
	

		//http://localhost:8080/GMapsURL/rest/Param/HolaMundo
		@Path("HolaMundo")
		@GET
		public Response doGreet() {
			/**
			 * OK 
  			 * 200 OK, see HTTP/1.1 documentation.
			 */
			return Response.status(200)
					.entity("Hola Mundo")
					.build();
		}
		
		//http://localhost:8080/GMapsURL/rest/Param/active
				@Path("active")
				@GET
				public Response active() {
					/**
					 * OK 
		  			 * 200 OK, see HTTP/1.1 documentation.
					 */
					return Response.status(200)
							.entity("Activo")
							.build();
				}
		
				
				
				//http://localhost:8080/GMapsURL/rest/Param/getParam
				@Path("getParam")
				@GET
				public String getParam() {
					
					Interconect IT=new Interconect();

					String pathS="http://"+uri.getBaseUri().getHost()+":"+uri.getBaseUri().getPort()+context.getContextPath();
					
					IT.setIcon(pathS+"/"+ICONPATH);
					IT.setName(GEOLOCALIZATION);
					
			
					
					List<Parameter> list=new ArrayList<Parameter>();
					
					
					list.add(new Parameter(GEOLOCALIZATION,LATITUDE));
					list.add(new Parameter(GEOLOCALIZATION,LONGITUDE));
					
					
					IT.setParametros(list);
					
					
					
					Gson G=new Gson();
					String Salida = G.toJson(IT);
					return Salida;
					

				}
				
				
				
}
