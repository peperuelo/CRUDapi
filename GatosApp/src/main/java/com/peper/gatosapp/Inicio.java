
package com.peper.gatosapp;

import java.io.IOException;
import javax.swing.JOptionPane;


public class Inicio {
    
    public static void main (String[] args)throws IOException{
        int opcionMenu = -1;
        String[] botones = {"1. ver gatos","2.ver favoritos", "3. Salir"};
         
        do {
            //menu
            String opcion =(String) JOptionPane.showInputDialog(null,"gatitos","Menu Principal", JOptionPane.INFORMATION_MESSAGE,null,botones,botones[0]);
                  
            //opcion que seleciona el usuario
            for (int i = 0;i<botones.length;i++){
                if(opcion.equals(botones[i])){
                    opcionMenu = i;

                }
            }
            switch(opcionMenu){
            case 0:
                GatoService.verGato();
                break;
            case 1:
                Gato gato = new Gato();
                GatoService.verFavorito(gato.getApikey());
                break;
            default:
                break;   
            }
        }while (opcionMenu != 1);
                               
        
    }
}
