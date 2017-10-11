package fdi.maps.server;

import fdi.maps.client.GMapsService;
import fdi.maps.shared.ConstantsGeoLocal;
import fdi.maps.shared.FieldVerifier;
import fdi.maps.shared.MarkersParametre;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GMapsServiceImpl extends RemoteServiceServlet implements
		GMapsService {

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public ArrayList<MarkersParametre> getExtradata(String extradata, String datageturl, String protocol) {
		

		
		
		try {
			StringBuilder result = new StringBuilder();
		      URL url = new URL(protocol+"://"+datageturl+"?"+ConstantsGeoLocal.EXTRADATA+"="+extradata);
		      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		      conn.setRequestMethod("GET");
		      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		      String line;
		      while ((line = rd.readLine()) != null) {
		         result.append(line);
		      }
		      rd.close();
		      
		      
		      String Valor = result.toString();
		      Gson G=new Gson();
		      @SuppressWarnings("rawtypes")
			ArrayList ValoresLista = G.fromJson(Valor, ArrayList.class);
		      
//		      listaJson.add(structureId);
//			    listaJson.add(lisParam);
		      @SuppressWarnings({ "unchecked", "unused" })
			List<Long> structureId=(List<Long>) ValoresLista.get(0);
		      @SuppressWarnings("unchecked")
			List<LinkedTreeMap<String, String>> lisParam= (List<LinkedTreeMap<String, String>>) ValoresLista.get(1);
		      
		      ArrayList<MarkersParametre> Salida = new ArrayList<MarkersParametre>();
		      for (LinkedTreeMap<String, String> hashMap : lisParam) {
		    	  String Lat=hashMap.get(ConstantsGeoLocal.LATITUDE);
		    	  String Lng=hashMap.get(ConstantsGeoLocal.LONGITUDE);
		    	  if (Lat!=null&&!Lat.isEmpty()&&Lng!=null&&!Lng.isEmpty())
		    	  {
		    		  try {
		    			  MarkersParametre KM = new MarkersParametre(Double.parseDouble(Lat), Double.parseDouble(Lng));
				    	  Salida.add(KM);
					} catch (Exception e) {
						e.printStackTrace();
					}
		    	 
		    	  }
		      }
		      
		      
		      return Salida;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<MarkersParametre>();
		}
	}




}
