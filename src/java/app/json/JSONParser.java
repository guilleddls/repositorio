package app.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.zkoss.json.JSONObject;
import org.apache.http.impl.client.HttpClientBuilder;
import org.zkoss.json.JSONValue;


public class JSONParser{

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    /**
     *  Función que consigue JSON de la url
     *  haciendolo por metodos de HTTP POST o GET
     * @param url la direccion donde esta el JSON
     * @param method metodo acción POST o GET
     * @param params parametros de la url
     * @return un Objeto JSON
     */
    public JSONObject makeHttpRequest(String url, String method, List params) {

        // Making HTTP request
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            
            HttpResponse httpResponse;
            HttpEntity httpEntity;
            // check for request method
            switch (method) {
                case "POST":
                    // request method is POST
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                    httpResponse = httpClient.execute(httpPost);
                    break;
                default: //case "GET":
                    // request method is GET
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                    HttpGet httpGet = new HttpGet(url);
                    httpResponse = httpClient.execute(httpGet);
                    break;
            }
            httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(System.err);
        } catch (ClientProtocolException e) {
            e.printStackTrace(System.err);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            System.err.println("Buffer Error: Error converting result : " + e.getMessage());
        }

        // try parse the string to a JSON object
        try {
            jObj = (JSONObject) JSONValue.parse(json);//new JSONObject(json);
            
            //new JSONParser().makeHttpRequest(url, method, params);
//            for(int i=0; i<jObj.size() ; i++){
//                jObj.get(i);
//            }
        } catch (Exception e) {
            System.err.println("JSON Parser: Error parsing data " + e.getMessage());
        }

        // return JSON String
        return jObj;

    }
}