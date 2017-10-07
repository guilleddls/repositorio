package com.clinica.veterinaria.util;

import java.io.File;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * 
 */
public class UploadFile {
    public final String MASCOTA_UPLOAD_URL = "http://json-veterinaria.16mb.com/mascotas/fileUpload.php";
    public final String CONSULTA_UPLOAD_URL = "http://json-veterinaria.16mb.com/solicitudes/fileUpload.php";
    public final String PRODUCTO_UPLOAD_URL = "http://json-veterinaria.16mb.com/productos/fileUpload.php";
    public final String FICHERO_UPLOAD_URL = "http://json-veterinaria.16mb.com/ficheros/fileUpload.php";
    
    public void imagenMascota(final String path){
        uploadFile(path, MASCOTA_UPLOAD_URL);
    }
    public void imagenProducto(String path){
        uploadFile(path, PRODUCTO_UPLOAD_URL);
          
    }   
    public void imagenFichero(String path){
        uploadFile(path, FICHERO_UPLOAD_URL);        
    }
    
    private synchronized void uploadFile(final String path, final String url){
        Thread thread = new Thread(){
            @Override
            public void run(){
                uploadFile(new File(path), url);
            }
        };
        thread.start();
    }
    
    private void uploadFile(File file, String url) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(url);
        try 
        {
            FileBody archivo = new FileBody(file);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();   
            HttpEntity entity = builder.addPart("image", archivo).build();
            httppost.setEntity(entity);
            httpclient.execute(httppost);
        }
        catch (Exception e) { } 
    }
    
    
}
