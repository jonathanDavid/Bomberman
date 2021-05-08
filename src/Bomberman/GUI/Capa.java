/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.GUI;


import Bomberman.Sonido.Audio;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 * @author jdavi
 */
public class Capa {
    
    private Dibujable Elemento;
    private ArrayList<Capa> CapasAdyacentes;
    private int id;
    private int idAntecesor;
    private int seleccion;
    private final int aumentarSeleccion;
    private final int disminuirSeleccion;
    private Audio cambiarSeleccion;
    private Audio entrarSeleccion;
    private Audio salirSeleccion;
    
    public Capa(int id,int idAntecesor, Dibujable Elemento,int aumentarSeleccion,int disminuirSeleccion){
        this.id=id;
        this.idAntecesor=idAntecesor;
        this.Elemento=Elemento;
        this.aumentarSeleccion=aumentarSeleccion;
        this.disminuirSeleccion=disminuirSeleccion; 
        this.seleccion=0;
        cambiarSeleccion= new Audio("Seleccion.wav", false,0);
        entrarSeleccion= new Audio("Confirmar.wav",false,0);
        salirSeleccion= new Audio("Atras.wav",false,0);
        CapasAdyacentes= new ArrayList();
    }
    
    public void dibujarCapa(Graphics g,Font f){
        g.setColor(Color.WHITE);
        Elemento.dibujar(g,f);  
    }
    public void añadirCapa(Capa p){
        CapasAdyacentes.add(p);
    }
    public Dibujable obtenerElelmento(){
        return Elemento;
    }
    
    public synchronized Capa RealizarEvento(int entrada,Interfaz miIntrfaz){
        Elemento.accionEvento(entrada);
        if (CapasAdyacentes.size()>0) {
            if(entrada==disminuirSeleccion){
                cambiarSeleccion.establecerActivo();
                seleccion--;
                if (seleccion<0) {
                    seleccion=CapasAdyacentes.size()-1;
                }          
            }else if(entrada==aumentarSeleccion){
                cambiarSeleccion.establecerActivo();
                seleccion++;
                if (seleccion> CapasAdyacentes.size()-1) {
                    seleccion=0;
                }
            }  

           if (entrada==KeyEvent.VK_ENTER){
               entrarSeleccion.establecerActivo();
               return this.CapasAdyacentes.get(seleccion);
           }
        }  
        if (entrada==KeyEvent.VK_ESCAPE){
            if (idAntecesor!=-1) {
                salirSeleccion.establecerActivo();
                return miIntrfaz.obtenerCapa(idAntecesor); 
            }
        }
        return null;
    }
    public synchronized void reproducirSonido(){
        cambiarSeleccion.reproducir();
        entrarSeleccion.reproducir();
        salirSeleccion.reproducir();
        Elemento.reproducirSonido();
    }

    public int getId() {
        return id;
    }
    public int getIdAnterior() {
        return idAntecesor;
    }
    
    public int obtenerAumentarSeleccion(){
        return aumentarSeleccion;
    }
}
