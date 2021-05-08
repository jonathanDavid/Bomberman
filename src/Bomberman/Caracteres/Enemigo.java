/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.Caracteres;

import Bomberman.GUI.Animacion;
import Bomberman.Objetos.Mapa;
import Bomberman.Sonido.Audio;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author jdavi
 */
public class Enemigo {
    private Animacion animacionMovimiento;
    private Animacion animacionMuerte;
    private Animacion animacionAlerta;
    private String Ruta;
    private Movimiento miMovimiento;
    private ArrayList<Nodo> caminoJugador;
    private Grafo zonaDisponible;
    private String nombreEnemigo;
    private Rectangle areaEnemigo;
    private Nodo nodoActual;
    private long tiempoInicio;
    private long timepoMuerte;
    private long puntosDestruccion;
    private boolean estaVivo, alerta, enemigoMuere, desaparecer, asustado;
    int posX, posY,velocidad,ancho, alto;
    int rango;
    private Audio sonidoAlerta;
    
    public Enemigo(String nombreEnemigo,int x, int y,int velocidad, int ancho, int alto,long Puntos){
        Ruta="Imagenes/Enemigos/"+nombreEnemigo;
        this.nombreEnemigo= nombreEnemigo;
        puntosDestruccion=Puntos;
        posX=x;
        posY=y;
        this.velocidad=velocidad;
        this.ancho=ancho;
        this.alto=alto;
        animacionMovimiento= new Animacion(true);
        animacionAlerta= new Animacion(true);
        miMovimiento= new Movimiento();
        miMovimiento.estableDireccionActual(Movimiento.NINGUNO);
        animacionMuerte= new Animacion(true);
        estaVivo=true;
        areaEnemigo= new Rectangle();     
        caminoJugador= new ArrayList();
        rango=12;
        alerta=false;
        asustado=false;
        enemigoMuere=true;
        sonidoAlerta = new Audio("Alerta.wav", false, 0);
    } 
    public void cargarSprites(int numAnimaciones){
        for (int i = 1; i <= numAnimaciones; i++) {          
            animacionMovimiento.añadirEscena(new ImageIcon(getClass().getResource(Ruta+"/"+nombreEnemigo+i+".png")).getImage(), 150);
        }
        for (int i = 1; i <= 5; i++) {
            animacionMuerte.añadirEscena(new ImageIcon(getClass().getResource(Ruta+"/Muerte"+i+".png")).getImage(), 150);
            timepoMuerte+=150;
        }
        for (int i = 1; i <= 3; i++) {
            animacionAlerta.añadirEscena(new ImageIcon(getClass().getResource(Ruta+"/Alerta/Alerta"+i+".png")).getImage(),100);
        }
    } 
    public void verificarLimitesMovimiento(Mapa m){
        areaEnemigo.setBounds(posX+alto, posY+velocidad*3, velocidad, alto-velocidad*5);
            if(m.collisionObjetosSolidos(areaEnemigo,"Enemigo")){
                miMovimiento.disponibleDerecha=false;
            }else{
                miMovimiento.disponibleDerecha=true;
            }
        areaEnemigo.setBounds(posX+velocidad*3, posY-velocidad, alto-velocidad*6, velocidad);  
            if(m.collisionObjetosSolidos(areaEnemigo,"Enemigo")){
                miMovimiento.disponibleArriba=false;
            }else{
                miMovimiento.disponibleArriba=true;
            }
        areaEnemigo.setBounds(posX+velocidad*3, posY+alto, alto-velocidad*6, velocidad);
        if(m.collisionObjetosSolidos(areaEnemigo,"Enemigo")){
            miMovimiento.disponibleAbajo=false;
        }else{
            miMovimiento.disponibleAbajo=true;
        }
        areaEnemigo.setBounds(posX-velocidad, posY+velocidad*3, velocidad, alto-velocidad*5);
        if(m.collisionObjetosSolidos(areaEnemigo,"Enemigo")){
            miMovimiento.disponibleIzquierda=false;
        }else{
            miMovimiento.disponibleIzquierda=true;
        }   
 
    }    
    public void establecerPosicion(int x, int y){
        posX=x;
        posY=y;
    }   
    public synchronized void dibujarEnemigo(Graphics g,Font f){  
        if (!desaparecer) {
            if (!estaVivo) {
                g.drawImage(animacionMuerte.getSprite(),posX,posY,ancho, alto, null);  
                g.setFont(f.deriveFont(Font.TRUETYPE_FONT, 10));
                g.setColor(Color.BLACK);
                g.drawString(puntosDestruccion+"", posX+f.getSize(), posY+f.getSize());
                g.setFont(f);
            }else{
                g.drawImage(animacionMovimiento.getSprite(),posX,posY,ancho, alto, null);
                if (alerta) {
                    g.drawImage(animacionAlerta.getSprite(), posX+ancho-20, posY, 20, 20, null);
                }
            }
//            g.setColor(Color.red);
//            for (Nodo i: caminoJugador){
//                g.fillOval(i.getCasillaPosicion().obtenerPosX(), i.getCasillaPosicion().obtenerPosY(), 5, 5);
//            }

        }  
    }
    public synchronized void destruirEnemigo(){
        animacionMuerte.Inicio();
        estaVivo=false;  
    }
    public synchronized void escogerDireccionMovimientoPersecucion(){
        Nodo a= nodoActual;
        boolean entro=false;
        for (Nodo i: a.obtenerNodosAdyacentes()){
            for (Nodo j: caminoJugador) {
                if (i.equals(j)) {
                    entro=true;
                        if(a.getCasillaPosicion().obtenerIndiceFila()==i.getCasillaPosicion().obtenerIndiceFila()){
                            if (a.getCasillaPosicion().obtenerIndiceColumna()+1==i.getCasillaPosicion().obtenerIndiceColumna()) {
                                if (miMovimiento.disponibleDerecha) {
                                    miMovimiento.estableDireccionActual(Movimiento.DERECHA);
                                }
                            }
                            if (a.getCasillaPosicion().obtenerIndiceColumna()-1==i.getCasillaPosicion().obtenerIndiceColumna()) {
                                if (miMovimiento.disponibleIzquierda) {
                                    miMovimiento.estableDireccionActual(Movimiento.IZQUIERDA);
                                }
                            }                        
                        }else if(a.getCasillaPosicion().obtenerIndiceColumna()==i.getCasillaPosicion().obtenerIndiceColumna()){
                            if (a.getCasillaPosicion().obtenerIndiceFila()+1==i.getCasillaPosicion().obtenerIndiceFila()) {
                                if (miMovimiento.disponibleAbajo) {
                                    miMovimiento.estableDireccionActual(Movimiento.ABAJO);
                                }
                            }
                            if (a.getCasillaPosicion().obtenerIndiceFila()-1==i.getCasillaPosicion().obtenerIndiceFila()) {
                                if (miMovimiento.disponibleArriba) {
                                    miMovimiento.estableDireccionActual(Movimiento.ARRIBA);
                                }
                            }
                        } 
                }
            }
           
        }
         if (!entro) {
                 miMovimiento.estableDireccionActual(Movimiento.NINGUNO);
            }
    }
    public synchronized void escogerDireccionMovimientoAleatorio(Nodo jugador){
        Nodo a= nodoActual;
        if (!a.obtenerNodosAdyacentes().isEmpty()) {
            Nodo i;
            i = a.obtenerNodosAdyacentes().get((int)(Math.random()*(a.obtenerNodosAdyacentes().size())));
            if (asustado) {
                double dist,mayor=0;
                Nodo tem=null;
                for (Nodo k: a.obtenerNodosAdyacentes()) {
                    dist=Math.pow(Math.pow(k.getCasillaPosicion().obtenerPosX()-jugador.getCasillaPosicion().obtenerPosX(), 2)+Math.pow(k.getCasillaPosicion().obtenerPosY()-jugador.getCasillaPosicion().obtenerPosY(), 2), 0.5);
                    if (dist>mayor) {
                        mayor=dist;
                        tem=k;
                    }
                    
                }
                i=tem;
            }

                    if(a.getCasillaPosicion().obtenerIndiceFila()==i.getCasillaPosicion().obtenerIndiceFila()){
                        if (a.getCasillaPosicion().obtenerIndiceColumna()+1==i.getCasillaPosicion().obtenerIndiceColumna()) {
                            if(miMovimiento.disponibleDerecha) {
                                miMovimiento.estableDireccionActual(Movimiento.DERECHA);
                            }
                        }
                        if (a.getCasillaPosicion().obtenerIndiceColumna()-1==i.getCasillaPosicion().obtenerIndiceColumna()) {
                            if (miMovimiento.disponibleIzquierda) {
                                miMovimiento.estableDireccionActual(Movimiento.IZQUIERDA);
                            }
                        }                        
                    }else if(a.getCasillaPosicion().obtenerIndiceColumna()==i.getCasillaPosicion().obtenerIndiceColumna()){
                        if (a.getCasillaPosicion().obtenerIndiceFila()+1==i.getCasillaPosicion().obtenerIndiceFila()) {
                            if(miMovimiento.disponibleAbajo) {
                                miMovimiento.estableDireccionActual(Movimiento.ABAJO);
                            }
                        }
                        if (a.getCasillaPosicion().obtenerIndiceFila()-1==i.getCasillaPosicion().obtenerIndiceFila()) {
                            if(miMovimiento.disponibleArriba) {
                                miMovimiento.estableDireccionActual(Movimiento.ARRIBA);
                            }
                        }
                    } 
             
        }else{
            miMovimiento.estableDireccionActual(Movimiento.NINGUNO);
        }
        
    }      
    public synchronized void moverse(long tiempo,Mapa m){ 
        verificarLimitesMovimiento(m);
        if (!estaVivo){
            if (enemigoMuere) {
                tiempoInicio=tiempo;
                enemigoMuere=false;
            }
            if (tiempo-tiempoInicio<timepoMuerte*3) {
                animacionMuerte.actulizarAnimacion(tiempo);
            }else{
                desaparecer=true;
            }
        }else{
            Nodo a=zonaDisponible.posicionCercana(posX, posY);
            if (Math.abs(posX-a.getCasillaPosicion().obtenerPosX())==0 && Math.abs(posY-a.getCasillaPosicion().obtenerPosY())==0) {
                nodoActual=a; 
                ArrayList<Nodo> caminoTem;
                try {
                    caminoTem=zonaDisponible.BuscarCaminoBFS(nodoActual,m.obtenerNodoJugador());
                }catch(Exception e){
                    caminoTem= new ArrayList();
                }
                caminoJugador=caminoTem;
                if (caminoJugador.size()>=rango || asustado) {
                    alerta=false;
                    escogerDireccionMovimientoAleatorio(m.obtenerNodoJugador());
                }else{
                    if (!alerta && !caminoJugador.isEmpty()) {
                       sonidoAlerta.establecerActivo();
                       alerta=true;
                    }      
                    escogerDireccionMovimientoPersecucion();
                    caminoJugador.remove(nodoActual);
                    if (caminoJugador.isEmpty()) {
                        escogerDireccionMovimientoAleatorio(m.obtenerNodoJugador());
                    }
                } 
            } 
          
            switch(miMovimiento.direccionActual){
            case Movimiento.IZQUIERDA:{   
                if (miMovimiento.disponibleIzquierda) {
                     posX-=velocidad;   
                }                  
                break;
            }
            case Movimiento.DERECHA:{
                if (miMovimiento.disponibleDerecha) {
                posX+=velocidad;     
                }
                break;
            }
            case Movimiento.ABAJO:{
                if (miMovimiento.disponibleAbajo) {
                posY+=velocidad; 
                }
                break;
            }
            case Movimiento.ARRIBA:{
                if (miMovimiento.disponibleArriba) {
                posY-=velocidad; 
                }
                break;
            }
            default:
                break;
            }  
            animacionAlerta.actulizarAnimacion(tiempo);
            animacionMovimiento.actulizarAnimacion(tiempo);
        }
    
    }
    public boolean verificarColision(Rectangle area){
        areaEnemigo.setBounds(posX+velocidad*4,posY+velocidad*4,ancho-velocidad*7,alto-velocidad*7);
        if (estaVivo) {
            return areaEnemigo.intersects(area);
        }else{
            return false;
        }   
    }
    public void establecerGrafo(Grafo g){
        zonaDisponible= g;
    }
    public  void reproducirSonido(){
        sonidoAlerta.reproducir();
    }
    public boolean obtenerDesaparecer(){
        return desaparecer;
    }      
    public long obtenerPuntosDestruccion(){
        return puntosDestruccion;
    }
    public void establecerAsustado(boolean asustado){
       this.asustado=asustado; 
    }
}
