/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.GUI;

import java.awt.Image;
import java.util.ArrayList;

/**
 *
 * @author jdavi
 */
public class Animacion {
    private ArrayList<Escena> misEscenas;
    public int EscenaActual;
    public long tiempoAnimacion;
    public long tiempoTranscurrido;
    public boolean repetirAnimacion;
    private long tiempoInicio;
    public boolean iniciar;
    public Animacion(boolean repetirAnimacion) {
        misEscenas= new ArrayList();
        this.repetirAnimacion=repetirAnimacion;
        tiempoAnimacion=0;
        Inicio();
    }
    
    public synchronized void Inicio(){
        tiempoTranscurrido=0;
        EscenaActual=0;
    }
    
    public synchronized void añadirEscena(Image Sprite, long tiempoEscena){
        tiempoAnimacion+=tiempoEscena;
       // iniciar=true;
        misEscenas.add(new Escena(Sprite, tiempoAnimacion));
    }
    
    private synchronized Escena getEscena(int i){
        return misEscenas.get(i);
    }

    public synchronized void actulizarAnimacion(long tiempo){  
        if (!misEscenas.isEmpty()) {
            if (iniciar) {
                if (tiempo>tiempoAnimacion){
                    tiempoTranscurrido=tiempo%tiempoAnimacion;
                    tiempoInicio=tiempo;
                    EscenaActual=0;     
                }else{
                    tiempoTranscurrido=tiempo; 
                }
            }else{
                tiempoInicio=tiempo;
                iniciar=true;
                EscenaActual=0;     
            }              
            while(tiempoTranscurrido>getEscena(EscenaActual).tiempoFinal){
                EscenaActual++;
            }  
        }
    }
    
    public synchronized Image getSprite(){
        if (!misEscenas.isEmpty()) {      
            return getEscena(EscenaActual).sprite;       
        }else{
            return null;
        }
    }
}

class Escena{
        Image sprite;
        long tiempoFinal;

    public Escena(Image sprite, long tiempo) {
        this.sprite = sprite;
        this.tiempoFinal = tiempo;
    }
}