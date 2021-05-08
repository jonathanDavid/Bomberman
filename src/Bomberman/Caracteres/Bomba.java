/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.Caracteres;

import Bomberman.GUI.Animacion;
import Bomberman.Objetos.Cuadricula;
import Bomberman.Objetos.Mapa;
import Bomberman.Sonido.Audio;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author jdavi
 */
public class Bomba{
    private int Arriba=0;
    private int Abajo=1;
    private int Izquierda=2;
    private int Derecha=3;
    
    private ArrayList<Animacion> animacionCentro;
    private ArrayList<Animacion> animacionBorde;
    private ArrayList<Animacion> animacionMedio;
    private ArrayList<Rectangle> explosion;
    
    private int rango;
    private long tiempoEspera,tiempoTotal,tiempoInicio;
    public boolean enEspera,bombaActiva;
    private Cuadricula bombaPosicion;
    private int posX,posY,Ancho,Alto;
    private String Ruta;
    private Audio explotarBomba;
    
    public Bomba(int Ancho,int Alto, int rango,int tiempoAnimacion){
        animacionCentro = new ArrayList();
        animacionBorde  = new ArrayList();
        animacionMedio  = new ArrayList();
        explosion= new ArrayList();
        this.rango=rango;
        this.Alto = Alto;
        this.Ancho= Ancho;
        Ruta="Imagenes/Explosiones";
        enEspera=true;
        bombaActiva=false;
        explotarBomba= new Audio("ExplotarBomba.wav", false,0);
        cargarSprites(tiempoAnimacion);
    }
    public  void desplegarBomba(int x, int y,long tiempo,Cuadricula a){
        tiempoInicio=tiempo;
        bombaPosicion=a;
        this.posX=x;
        this.posY=y;
        bombaActiva=true;
    }
    private void cargarSprites(long tiempoAnimacion){
        String[] nombreGeneral={"ExBorde","ExMedio"};
        String[] nombreEspecifico={"Arriba","Abajo","Izquierda","Derecha"};
        Animacion nuevaAnimacion =new Animacion(true);
        for(int i = 1; i <= 6; i++){
            nuevaAnimacion.añadirEscena(new ImageIcon(getClass().getResource(Ruta+"/"+"Bomba"+i+".png")).getImage(),tiempoAnimacion);
            tiempoEspera+=tiempoAnimacion;
        }
        tiempoEspera=tiempoEspera*2;
        animacionCentro.add(nuevaAnimacion);
        
        nuevaAnimacion =new Animacion(true);
        for(int i = 1; i <= 4; i++){
            nuevaAnimacion.añadirEscena(new ImageIcon(getClass().getResource(Ruta+"/"+"ExCentro"+i+".png")).getImage(),tiempoAnimacion);
            tiempoTotal+=tiempoAnimacion;
        }
        tiempoTotal*=2;
        tiempoTotal+=tiempoEspera;
        animacionCentro.add(nuevaAnimacion);
        
        for(int i =0;i<nombreGeneral.length; i++) {
            for (int k =0;k<nombreEspecifico.length; k++) {
                nuevaAnimacion =new Animacion(true);
                for(int j = 1; j <= 4; j++){
                    nuevaAnimacion.añadirEscena(new ImageIcon(getClass().getResource(Ruta+"/"+nombreGeneral[i]+nombreEspecifico[k]+j+".png")).getImage(),tiempoAnimacion);
                }
                if(i==0){
                    animacionBorde.add(nuevaAnimacion);
                }else{
                    animacionMedio.add(nuevaAnimacion);
                }          
            }
            
        } 
    } 
    public  void dibujarBomba(Graphics g, Mapa m){
        if (bombaActiva) {             
                if (!enEspera){
                g.drawImage(animacionCentro.get(1).getSprite(), posX, posY,Ancho,Alto, null);
                explosion.add(new Rectangle(posX, posY, Ancho,Alto));
                    for (int i = Ancho; i <= rango*Ancho; i+=Ancho){
                        if (!m.collisionObjetosSolidos(new Rectangle(posX+i, posY, Ancho,Alto),"Bomba")) {
                            explosion.add(new Rectangle(posX+i, posY, Ancho,Alto));
                            if (i==rango*Ancho){
                                g.drawImage(animacionBorde.get(Derecha).getSprite(),posX+i,posY,Ancho,Alto, null);
                            }else{
                                g.drawImage(animacionMedio.get(Derecha).getSprite(),posX+i,posY,Ancho,Alto, null);
                            }
                        }else{
                            break;
                        }
                    }    
                    for (int i = Ancho; i <= rango*Ancho; i+=Ancho){
                       if (!m.collisionObjetosSolidos(new Rectangle(posX-i, posY, Ancho,Alto),"Bomba")) {
                            explosion.add(new Rectangle(posX-i, posY, Ancho,Alto));
                            if (i==rango*Ancho) {
                               g.drawImage(animacionBorde.get(Izquierda).getSprite(),posX-i,posY,Ancho,Alto, null);
                            }else{
                               g.drawImage(animacionMedio.get(Izquierda).getSprite(),posX-i,posY,Ancho,Alto, null);
                            }
                       }else{
                            break;
                        }
                    }
                    for (int i = Ancho; i <= rango*Ancho; i+=Ancho){
                        if (!m.collisionObjetosSolidos(new Rectangle(posX, posY+i, Ancho,Alto),"Bomba")) {
                            explosion.add(new Rectangle(posX, posY+i, Ancho,Alto));
                            if (i==rango*Ancho) {
                               g.drawImage(animacionBorde.get(Abajo).getSprite(),posX,posY+i,Ancho,Alto, null);
                            }else{
                               g.drawImage(animacionMedio.get(Abajo).getSprite(),posX,posY+i,Ancho,Alto, null);
                            }
                        }else{
                            break;
                        }
                    }    
                    for (int i = Ancho; i <= rango*Ancho; i+=Ancho){
                        if (!m.collisionObjetosSolidos(new Rectangle(posX, posY-i, Ancho,Alto),"Bomba")) {
                            explosion.add(new Rectangle(posX, posY-i, Ancho,Alto));
                            if (i==rango*Ancho) {
                               g.drawImage(animacionBorde.get(Arriba).getSprite(),posX,posY-i,Ancho,Alto, null);
                            }else{
                              g.drawImage(animacionMedio.get(Arriba).getSprite(),posX,posY-i,Ancho,Alto, null);
                            }
                        }else{
                            break;
                        }
                    }
            }else{ 
                g.drawImage(animacionCentro.get(0).getSprite(), posX, posY,Ancho,Alto, null);
            }
        } else{
          
        }   
    }
    private boolean esSoloBorde(){
        if(rango==1) {
            return true;
        }else{
            return false;
        }
    }  
    public  boolean verificarDestruccionObjeto(Rectangle blanco){
        if (bombaActiva) {
            for (Rectangle i: explosion) {
                if (i.intersects(blanco)) {
                    return true;
                }
            }
        }
        return false;
    }
    public  synchronized void reproducirSonido(){
        explotarBomba.reproducir();
    }
    public  synchronized void actualizarAnimacion(long tiempo){
        if (bombaActiva){
            if (tiempo-tiempoInicio>tiempoTotal) {
                bombaActiva=false;
                explosion= new ArrayList();
            }
            if (tiempo-tiempoInicio>tiempoEspera){
                if (enEspera) {
                    explotarBomba.establecerActivo();
                }
                enEspera=false;                
                animacionCentro.get(1).actulizarAnimacion(tiempo);
                if (esSoloBorde()){
                    for (int i = 0; i < animacionBorde.size(); i++){
                        animacionBorde.get(i).actulizarAnimacion(tiempo);
                    }
                }else{
                    for (int i = 0; i <  animacionMedio.size(); i++) {
                         animacionMedio.get(i).actulizarAnimacion(tiempo);
                    }
                    for (int i = 0; i < animacionBorde.size(); i++) {
                        animacionBorde.get(i).actulizarAnimacion(tiempo);
                    }
                }
            }else{  
                enEspera=true;
                animacionCentro.get(0).actulizarAnimacion(tiempo);
            }    
        }       
    }  
    
    
}
