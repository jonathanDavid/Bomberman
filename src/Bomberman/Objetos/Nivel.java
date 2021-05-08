/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.Objetos;

import Bomberman.Caracteres.Personaje;
import Bomberman.GUI.Capa;
import Bomberman.GUI.Dibujable;
import Bomberman.GUI.Label;
import Bomberman.GUI.Temporizador;
import Bomberman.Sonido.Audio;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

/**
 *
 * @author jdavi
 */
public class Nivel implements Dibujable{
    private Mapa miMapa;
    private Image vistaPrevia;
    private Image imagenFondo;
    
    private String nombre;
    private String nombreVistaPrevia;
    
    private boolean nivelInactivo,cambiarSiguienteNivel;
    private int desplegarPosX, desplegarPosY;
    private int id;
    private Temporizador temporizador;
    private Label nivelTerminado;
    private boolean juegoTerminado;
    private long tiempoJuegoTerminado, tiempoInicioFin;
    private Audio AudioJuegoTerminado;
    private ColeccionNiveles miColeccion;

    public Nivel(Mapa miMapa,String nombre,Image vistaPrevia, int desplegarPosX,int  desplegarPosY,ColeccionNiveles miColeccion) {
        this.miMapa = miMapa;
        this.nombre=nombre;
        this.vistaPrevia=vistaPrevia;
        this.nivelInactivo=true;
        this.desplegarPosX=desplegarPosX;
        this.desplegarPosY=desplegarPosY;
        nivelTerminado=new Label("Juego Terminado", 384, 360);
        imagenFondo= new ImageIcon(getClass().getResource("Imagenes/Objetos/Banner.png")).getImage();
        tiempoJuegoTerminado=9000;
        temporizador= new Temporizador();
        AudioJuegoTerminado= new Audio("JuegoTerminado.wav", false, 0);
        this.miColeccion=miColeccion;
    }
    public void cargarNivel(){
        miMapa.cargarSprites();
    }  
    public Mapa obtenerMapa(){
        return miMapa;
    }
    public String  obtenerNombre(){
        return nombre;
    }
    public synchronized void dibujarVistaPrevia(int x, int y,int anchoVistaPrevia,int altoVistaPrevia,Font f, Graphics g){
        g.drawImage(vistaPrevia, x, y,anchoVistaPrevia,altoVistaPrevia, null);
        g.setFont(f);
        g.drawString(nombre, x+(anchoVistaPrevia-f.getSize()*(nombre.length()-2))/2, y+altoVistaPrevia+f.getSize());
    }
    
    @Override
    public synchronized void accionEvento(int codigoTecla){
        switch(codigoTecla){
            case KeyEvent.VK_ESCAPE:{    
                    miMapa.reiniciarMapa();
                    temporizador.pausarTemporizador();
                    nivelInactivo=true;   
                break;
            }
            default:
                break;
        }
    }
    @Override
    public  synchronized void dibujar(Graphics g,Font f){    
        if(!juegoTerminado) {
            miMapa.dibujarMapa(g);
            g.setFont(f);
            g.drawImage(imagenFondo, 0, 0, 760, 90,null);
            g.drawImage(temporizador.obtenerIcono(),  f.getSize()*3,(int)(f.getSize()*1.5),50,50,null);
            g.drawString(temporizador.mostarTiempo(), f.getSize()*8, f.getSize()*4);          
        }
    }
    public synchronized void ActualizarNivel(Graphics g,Personaje jugador,long tiempo,Font f,int ancho, int alto){
        if (!juegoTerminado) {
            if(nivelInactivo){               
                jugador.establecerPosicion(desplegarPosX,desplegarPosY);
                jugador.establecerPosicionDespliegue(desplegarPosX, desplegarPosY);
                nivelInactivo=false;
                temporizador.reiniciarTemporizador();    
                temporizador.iniciarTemporizador();
      
            }
            g.setFont(f.deriveFont(Font.TRUETYPE_FONT, 20));
            g.drawString(nombre, (ancho-nombre.length()*f.getSize())/2-30, f.getSize()*2);
            g.setFont(f);
            miMapa.actualizarMapa(tiempo, g,f,jugador,this);
            jugador.dibujarPersonaje(g,miMapa); 
            
            if (jugador.obtenerVidas()==0) {
                juegoTerminado=true;
                AudioJuegoTerminado.establecerActivo();
                tiempoInicioFin=tiempo;
            }
        }else{
            if (tiempo-tiempoInicioFin<tiempoJuegoTerminado) {              
                g.setColor(Color.BLACK);
                g.fillRect(0,0, ancho, alto);
                g.setColor(Color.WHITE);
                g.setFont(f.deriveFont(Font.TRUETYPE_FONT, 32));
                String texto=nivelTerminado.getTexto();
                g.drawString(texto, nivelTerminado.getX()-(f.getSize()*(texto.length())),nivelTerminado.getY());
            }else{
                reiniciarNivel();
                jugador.reiniciarPuntaje();
            }     
        }        
    }
    @Override
    public void reproducirSonido(){
        AudioJuegoTerminado.reproducir();
        miMapa.reproducirSonido();
    }
    private void reiniciarNivel(){
        miMapa.reiniciarMapa();
        this.cargarNivel();
        juegoTerminado=false;
        nivelInactivo=true;   
    }
    public synchronized void establecerCambiarSiguienteNivel(boolean a){
        cambiarSiguienteNivel=a;
    }
    public synchronized boolean obtenerCambiarSiguienteNivel(){
       return cambiarSiguienteNivel;
    }
    public synchronized Capa cambiarNivel(Capa anterior,Personaje jugador) throws InterruptedException{
        miMapa.reiniciarMapa();
        temporizador.pausarTemporizador();
        anterior.RealizarEvento(anterior.obtenerAumentarSeleccion(), null);
        cambiarSiguienteNivel=false;
        nivelInactivo=true;     
        Capa nuevoNivel=anterior.RealizarEvento(KeyEvent.VK_ENTER, null);
        Nivel a =(Nivel)nuevoNivel.obtenerElelmento();
        a.obtenerMapa().establecerPuntageGuardado(jugador.obtenerPuntaje());
        return nuevoNivel;
    }
    
    public  String obtenerInformacionNivel(String s){
        return nombre+s+miMapa.obtenerFilasCuadricula()+s+miMapa.obtenerColumnasCuadricula()+s+miMapa.obtenerAltoCuadricula()+s+miMapa.obtenerAnchoCuadricula()+s+miMapa.obtenerPosX()+s+miMapa.obtenerPosY()+s+desplegarPosX+s+desplegarPosY+s+nombreVistaPrevia+s+miMapa.obtenerNombreAnimacion();
    }
    
    
    
}
