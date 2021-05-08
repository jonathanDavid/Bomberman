/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.Objetos;

import Bomberman.GUI.Animacion;
import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 *
 * @author jdavi
 */
public class Elemento {
    public final static int BOMBAROJA=1;
    
    private String Ruta;
    private boolean enEspera;
    private int tipoItem;
    
    private boolean  empezar;
    private long tiempoActivo;
    
    private long tiempoEspera,tiempoActivacion;
    private Animacion miAnimacionItem;
    private Cuadricula cuadriculaPosicion;
    private int ancho,alto;
    public Elemento(int tipoItem, long tiempoEspera,long tiempoActivo,int ancho,int alto) {
        Ruta="Imagenes/Objetos/";
        this.tiempoEspera= tiempoEspera;
        this.tipoItem = tipoItem;
        this.tiempoActivo=tiempoActivo;
        this.miAnimacionItem = new Animacion(true);   
        empezar=true;
        enEspera=true;
        this.ancho=ancho;
        this.alto=alto;
        cargarSprites();
    }
    
    public void cargarSprites(){
        String item="";
        switch(tipoItem){
            case BOMBAROJA:
                item="ItemRoja";
                break;
        }
        for (int i = 1; i <= 2; i++) {
            miAnimacionItem.añadirEscena(new ImageIcon(getClass().getResource(Ruta+item+i+".png")).getImage(),150);
        }    
    }
    public int obtenerTipoItem() {
        return tipoItem;
    }
    public long obtenerTiempoActivo() {
        return tiempoActivo;
    }
    public void dibujarElemento(Graphics g){
        if(enEspera) {
            g.drawImage(miAnimacionItem.getSprite(), cuadriculaPosicion.obtenerPosX(), cuadriculaPosicion.obtenerPosY(),ancho , alto, null);
        }        
    }
    public void establecerCuadriculaPosicion(Cuadricula cuadriculaPosicion) {
        this.cuadriculaPosicion = cuadriculaPosicion;
    }
    
    public Cuadricula  obtenerMiCuadricula() {
        return cuadriculaPosicion;
    }
    public void esteblecerActivo(boolean estaActivo){
        this.enEspera=estaActivo;
    }
    public void actualizarItem(long tiempo){
        if(empezar){
            tiempoActivacion=tiempo;
            empezar=false;
        }
        if (tiempo-tiempoActivacion<tiempoEspera) {
            miAnimacionItem.actulizarAnimacion(tiempo);
        }else{
            enEspera=false;
        }
    }
    public boolean obtenerActivo(){
        return enEspera;
    }
    
    
    
    
    
}
