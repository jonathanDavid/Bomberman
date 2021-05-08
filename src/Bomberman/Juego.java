/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman;

import Bomberman.Caracteres.Personaje;
import Bomberman.GUI.Capa;
import Bomberman.GUI.Interfaz;
import Bomberman.Objetos.Nivel;
import Bomberman.Sonido.Audio;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author jdavi
 */
public class Juego extends JFrame{
    
    private int tamañoFuente;
    private Capa capaActual;  
    private Canvas Lienzo;
    private Font estiloFuente;
    private Interfaz miInterfaz;   
    private Personaje Jugador;
    private Thread estructuraCentral;
    private Thread estructuraAudio;
    private Audio musicaFondo;
    
    public Juego(Interfaz miInterfaz, int ancho, int alto) throws IOException{
        inicializarComponentes(ancho,alto,miInterfaz);
        musicaFondo= new Audio("MusicaFondo.wav",true,0);
        musicaFondo.establecerActivo();
        estructuraCentral = new Thread(new Runnable() {
            @Override
            public void run() {
                Lienzo.createBufferStrategy(2);
                long start=System.currentTimeMillis();
                while(true){
                    try{
                        Graphics g= Lienzo.getBufferStrategy().getDrawGraphics();
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, Lienzo.getWidth(), Lienzo.getHeight());                                                
                        capaActual.dibujarCapa(g,estiloFuente);
                        if (capaActual.obtenerElelmento() instanceof Nivel) {
                            Nivel nivelActual= (Nivel)capaActual.obtenerElelmento();
                            nivelActual.ActualizarNivel(g, Jugador,System.currentTimeMillis()- start,estiloFuente,Lienzo.getWidth(), Lienzo.getHeight());
                            Jugador.moverse(System.currentTimeMillis()- start,nivelActual.obtenerMapa());
                            if (nivelActual.obtenerCambiarSiguienteNivel()) {
                                capaActual=nivelActual.cambiarNivel(miInterfaz.obtenerCapa(capaActual.getIdAnterior()),Jugador);
                            }
                        }else{
                            Jugador.reiniciarPuntaje();
                        } 
                        Lienzo.getBufferStrategy().show();
                        Thread.sleep(30);
                    }catch(Exception e){}
                }
            }
        });
        
        estructuraAudio = new Thread(new Runnable() {
            @Override
            public void run() {              
                musicaFondo.reproducir(); 
                while (true){                    
                    capaActual.reproducirSonido();
                    Jugador.reproducirSonido();
                }
              
            }
        });
        estructuraCentral.setPriority(Thread.MAX_PRIORITY);
        estructuraAudio.setPriority(Thread.NORM_PRIORITY);
        
    }   
    private void controladorEventos(){
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e){}
            @Override
            public void keyPressed(KeyEvent e) {
                Capa c = capaActual.RealizarEvento(e.getKeyCode(),miInterfaz);
                if (c!=null){
                    capaActual=c;
                }
                Jugador.controladorPersonaje(e.getKeyCode(),1);              
            }
            @Override
            public void keyReleased(KeyEvent e){
                Jugador.controladorPersonaje(e.getKeyCode(),0);  
            }
         });
        addWindowStateListener(new WindowStateListener(){
            @Override
            public void windowStateChanged(WindowEvent arg){
                EstadoVentanaCambiado(arg);
            }
        });
        
    } 
    private void EstadoVentanaCambiado(WindowEvent e){} 
    private void inicializarComponentes(int ancho, int alto,Interfaz miInterfaz){
        Lienzo =new Canvas();
        add(Lienzo,BorderLayout.CENTER);  
        setResizable(false);
        setBounds((Toolkit.getDefaultToolkit().getScreenSize().width-ancho)/2,0,ancho, alto);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        capaActual=miInterfaz.obtenerCapaPrincipal();
        Jugador = new Personaje(40,40,3,24,40,3); 
        controladorEventos();
        tamañoFuente=14;
        try{
            estiloFuente = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("FuenteB.TTF")).deriveFont(Font.TRUETYPE_FONT, tamañoFuente);
        }catch(Exception e){}
        
        this.miInterfaz=miInterfaz; 
           
    }
    public void iniciarHilos(){
        estructuraCentral.start();
        estructuraAudio.start();
    }
}
