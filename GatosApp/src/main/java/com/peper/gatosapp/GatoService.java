/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peper.gatosapp;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author peper
 */
public class GatoService {
    
    public static void verGato() throws IOException{ 
        // se traen datos de la api
            OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").get().build();

        Response response = client.newCall(request).execute();
        
        String elJson = response.body().string();
    
        //Cortar los []
        elJson = elJson.substring(1,elJson.length());
        elJson = elJson.substring(0,elJson.length()-1);
        
        //Creamos objeto Gson

        Gson gson = new Gson();
        Gato gatos = gson.fromJson(elJson, Gato.class);
        
        // redimensionar imagen
        Image image =  null;
        try{
            URL url = new URL(gatos.getUrl());
            image = ImageIO.read(url);
            ImageIcon fondoGato = new ImageIcon(image);
            if(fondoGato.getIconWidth()>800){
                //redimensionamos
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                fondoGato = new  ImageIcon(modificada);
            }
            
            String  menu = "Opciones:  \n"
                    + "1. Ver otra imagen \n"
                    + "2. Favoritos \n"
                    + "3. Volver \n";
                   
            
            String [] botones  = {"ver ota imagen", "favorito", "volver"}; 
            String idGato = String.valueOf(gatos.getId());
            String opcion = (String) JOptionPane.showInputDialog(null,menu,idGato,JOptionPane.INFORMATION_MESSAGE, fondoGato,botones,botones[0]);
            
            int seleccion = -1;
            
            
              //opcion que seleciona el usuario
            for (int i = 0;i<botones.length;i++){
                if(opcion.equals(botones[i])){
                    seleccion = i;
                }
            }
            
           switch(seleccion){
            case 0:
                verGato();
                break;
            case 1:
                favoritoGato(gatos);
                break; 
            case 2:
                
            default:
                break;
        }
                    
        }catch(IOException e){
            System.out.println(e);
        }
          
    }
    public static void favoritoGato(Gato gato){
        try{
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n\t\"image_id\":\""+gato.getId()+"\"\n}");           
            Request request = new Request.Builder()
            .url("https://api.thecatapi.com/v1/favourites")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", gato.getApikey())
            .build();
            Response response = client.newCall(request).execute();
            System.out.println("siii"); 
            
        }catch(IOException e) {
            System.out.println(e);            
            
        }
    }

    public static void verFavorito(String apiKey)throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
        .url("https://api.thecatapi.com/v1/favourites")
        .method("GET", null)
        .addHeader("x-api-key", apiKey)
        .build();
        Response response = client.newCall(request).execute();
        
         String elJson = response.body().string();
         
           //Creamos objeto Gson

        Gson gson = new Gson();
        
        
           GatoFav[] gatosArray= gson.fromJson(elJson, GatoFav[].class);
           
           if (gatosArray.length>0){
               int min = 1;
               int max = gatosArray.length;
               int aleatorio = (int) (Math.random() * ((max-min)+1)) + min;
            int index = aleatorio-1;
               System.out.println(index);
               
               GatoFav gatofav = gatosArray[index];
               
               
                       // redimensionar imagen
        Image image =  null;
        try{
            URL url = new URL(gatofav.image.getUrl());
            image = ImageIO.read(url);
            ImageIcon fondoGato = new ImageIcon(image);
            if(fondoGato.getIconWidth()>800){
                //redimensionamos
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                fondoGato = new  ImageIcon(modificada);
            }
            
            String  menu = "Opciones:  \n"
                    + "1. Ver otra imagen \n"
                    + "2. Eliminar Favoritos \n"
                    + "3. Volver \n";
                   
            
            String [] botones  = {"ver ota imagen", "Eliminar Favorito", "volver"}; 
            String idGato = gatofav.getId();
            String opcion = (String) JOptionPane.showInputDialog(null,menu,idGato,JOptionPane.INFORMATION_MESSAGE, fondoGato,botones,botones[0]);
            
            int seleccion = -1;
            
            
              //opcion que seleciona el usuario
            for (int i = 0;i<botones.length;i++){
                if(opcion.equals(botones[i])){
                    seleccion = i;
                }
            }
            
            switch(seleccion){
                case 0:
                    verFavorito(apiKey);
                    break;
                case 1:
                 borrarFavorito(gatofav);
                    break; 
                default:
                    Inicio.main(botones);
                    break;
        }
                    
        }catch(IOException e){
            System.out.println(e);
        }
                  
           }
    }

    private static void borrarFavorito(GatoFav gatofav){
        
        
        try{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
        .url("https://api.thecatapi.com/v1/favourites/"+gatofav.getId()+"")
        .method("DELETE", body)
        .addHeader("Content-Type", "application/json")
        .addHeader("x-api-key", gatofav.getPikey())
        .build();
        Response response = client.newCall(request).execute();
        
        }catch(IOException e){
        System.out.println(e);
        }
    }
}