/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.GUI;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author jdavi
 */
public class Temporizador {
    private int horas;
    private int minutos;
    private int segundos;
    private Thread tiempo;
    private Image icono;
    
    public Temporizador(){
        icono= new ImageIcon(getClass().getResource("Imagenes/Tiempo.png")).getImage();
        tiempo= new Thread(new Runnable() {
            @Override
            public void run() {        
                    while(true){ 
                        try{
                            EjecutarTemporizador();
                            Thread.sleep(1000); 
                        }catch(Exception e){}
                    }
            }
        });
    }
    public Image obtenerIcono(){
        return icono;
    }
    public void EjecutarTemporizador(){
        segundos++;
        if(segundos==60){
               segundos=0;
               minutos++;
            if(minutos==60){
                minutos=0;
                horas++;
            }
        }
    }   
    public  void iniciarTemporizador(){
            tiempo.start();
           
    }
    public  void pausarTemporizador(){
            tiempo.interrupt();  
    }

    public void reiniciarTemporizador(){
        segundos=0;
        minutos=0;
        horas=0;
    }
    public String mostarTiempo(){
       String horaStr="0", minutosStr="0", segundosStr="0";
        if (horas/10!=0)
            horaStr="";
        if (minutos/10!=0)
            minutosStr="";
        if (segundos/10!=0) 
           segundosStr="";
        return "Tiempo: "+ horaStr+horas+":"+minutosStr+minutos+":"+segundosStr+segundos;
    }    
}
