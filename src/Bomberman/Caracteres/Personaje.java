/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.Caracteres;

import Bomberman.GUI.Animacion;
import Bomberman.GUI.Label;
import Bomberman.Objetos.Cuadricula;
import Bomberman.Objetos.Elemento;
import Bomberman.Objetos.Mapa;
import Bomberman.Sonido.Audio;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author jdavi
 */
public class Personaje {

    private String Ruta;
    private Rectangle areaJuagdor;
    private Movimiento miMovimiento;
    private ArrayList<Animacion> animacionesMovimiento;
    private Animacion animacionMuerte;
    private Animacion animacionEspera;
    private Animacion animacionPoderActivo;
    private Bomba bomba;   
    private Label PuntajeTexto;
    private Label VidasTexto;
    private Image imagenPrevia;
    private long puntajeGanado,tiempoInicioMuerte,tiempoInicioPoder,tiempoPoderActivo;
    private int vidas;
    private int vidasJuego;
    private int posX, posY, velocidad,ancho, alto;
    private int posDespiegueX, posDespiegueY;
    private boolean enMovimiento, estaVivo, bombaColocada,poderActivo; 
    private Audio colocarBomba;
    
    
    
    public Personaje(int x, int y, int vx,int ancho, int alto, int vidas) {
        VidasTexto=new Label("Vidas: ", 450,56);
        PuntajeTexto=new Label("Puntaje: ", 550,56);
        this.posX=x; 
        this.posY=y; 
        this.velocidad=vx;
        this.vidas=vidas;
        this.alto=alto;
        estaVivo=true;
        this.ancho=ancho;
        this.miMovimiento= new Movimiento();
        this.animacionesMovimiento= new ArrayList();
        this.Ruta="Imagenes/Movimiento";
        this.areaJuagdor= new Rectangle(posX, posY, alto, alto);
        bomba= new Bomba(40,40, 6,140);
        imagenPrevia= new ImageIcon(getClass().getResource(Ruta+"/ImagenPrevia.png")).getImage();
        colocarBomba= new Audio("ColocarBomba.wav", false,0);
        cargarSprites();
        establecerMovimiento(Movimiento.NINGUNO);
    }    
    private void cargarSprites(){
        String[] spritesNombre={"MoverAdelante","MoverDerecha","MoverAtras","MoverIzquierda"};
        for (int i =0;i<spritesNombre.length; i++) {
            Animacion nuevaAnimacion =new Animacion(true);
            for (int j = 1; j <= 3; j++){
                nuevaAnimacion.añadirEscena(new ImageIcon(getClass().getResource(Ruta+"/"+spritesNombre[i]+j+".png")).getImage(),100);
            }
            animacionesMovimiento.add(nuevaAnimacion);
        }
        animacionEspera= new Animacion(true);
        for (int i = 0; i < 8; i++) {
            animacionEspera.añadirEscena(new ImageIcon(getClass().getResource(Ruta+"/MoverQuieto"+i+".png")).getImage(), 160);
        }
        animacionMuerte= new Animacion(false);
        for(int i = 1; i <= 5; i++) {
            animacionMuerte.añadirEscena(new ImageIcon(getClass().getResource(Ruta+"/Muerte"+i+".png")).getImage(), 420-20*i);                 
        }     
        animacionPoderActivo= new Animacion(false);
        for (int i = 1; i <= 12; i++) {
            animacionPoderActivo.añadirEscena(new ImageIcon(getClass().getResource(Ruta+"/Efecto/Efecto"+i+".png")).getImage(), 50);                 
        }
    }    
    public void verificarLimitesMovimiento(Mapa m){
        areaJuagdor.setBounds(posX+alto, posY+velocidad*3, velocidad, alto-velocidad*5);
        if(m.collisionObjetosSolidos(areaJuagdor,"Jugador")){
            miMovimiento.disponibleDerecha=false;
        }else{
            miMovimiento.disponibleDerecha=true;
        }
        areaJuagdor.setBounds(posX+velocidad*3, posY-velocidad, alto-velocidad*6, velocidad);  
        if(m.collisionObjetosSolidos(areaJuagdor,"Jugador")){
            miMovimiento.disponibleArriba=false;
        }else{
            miMovimiento.disponibleArriba=true;
        }
        areaJuagdor.setBounds(posX+velocidad*3, posY+alto, alto-velocidad*6, velocidad);
        if(m.collisionObjetosSolidos(areaJuagdor,"Jugador")){
            miMovimiento.disponibleAbajo=false;
        }else{
            miMovimiento.disponibleAbajo=true;
        }
        areaJuagdor.setBounds(posX-velocidad, posY+velocidad*3, velocidad, alto-velocidad*5);
        if(m.collisionObjetosSolidos(areaJuagdor,"Jugador")){
            miMovimiento.disponibleIzquierda=false;
        }else{
            miMovimiento.disponibleIzquierda=true;
        }   
 
    }    
    public void moverse(long tiempo,Mapa m){
        if (estaVivo){
            verificarLimitesMovimiento(m);
            if(enMovimiento){
                switch(miMovimiento.direccionActual){
                    case Movimiento.IZQUIERDA:{              
                        if (miMovimiento.disponibleIzquierda)
                            posX-=velocidad;                          
                        break;
                    }
                    case Movimiento.DERECHA:{
                        if (miMovimiento.disponibleDerecha)
                            posX+=velocidad;             
                        break;
                    }
                    case Movimiento.ABAJO:{
                        if (miMovimiento.disponibleAbajo)
                              posY+=velocidad;         
                        break;
                    }
                    case Movimiento.ARRIBA:{
                        if (miMovimiento.disponibleArriba)
                            posY-=velocidad; 
                        break;
                    }
                }            
                animacionesMovimiento.get(miMovimiento.direccionActual).actulizarAnimacion(tiempo);

            }else{
                animacionEspera.actulizarAnimacion(tiempo);
                
            }
            if (poderActivo){
                animacionPoderActivo.actulizarAnimacion(tiempo);      
            }
            if (bombaColocada) {
                Cuadricula c=m.obtenerPosicionCercana(posX+ancho/2, posY);
                Nodo b= m.obtenerCampoEnemigos().obtenerNodo(c);
                if (b!=null) {
                    m.obtenerCampoEnemigos().establecerNodoActual(b);
                }  
                bomba.desplegarBomba(c.obtenerPosX(), c.obtenerPosY(), tiempo,c);
                bomba.bombaActiva=true;
                bombaColocada=false;
            }
        }else{
            if (tiempo-tiempoInicioMuerte>animacionMuerte.tiempoAnimacion*2) {
                this.reestablecerPersonaje();
            }
            animacionMuerte.actulizarAnimacion(tiempo);     
        }
        
        if (bomba.bombaActiva){
            bomba.actualizarAnimacion(tiempo);
            areaJuagdor.setBounds(posX+velocidad*4,posY+velocidad*4,ancho-velocidad*3,alto-velocidad*6);
            if (bomba.verificarDestruccionObjeto(areaJuagdor) && !bomba.enEspera) {  
               animacionMuerte.Inicio();
                estaVivo=false;   
                tiempoInicioMuerte=tiempo;
            }
        }
        
        if (poderActivo) {
            if (tiempo-tiempoInicioPoder>tiempoPoderActivo) {
                poderActivo=false;
            }
        }
        
    }
    public void destruirPersonaje(long t){
        if (estaVivo){
            animacionMuerte.Inicio();
            estaVivo=false;   
            tiempoInicioMuerte=t;
        }
    }
    public void establecerMovimiento(int direccion) {
        if(direccion!= Movimiento.NINGUNO){
            miMovimiento.estableDireccionActual(direccion);
            enMovimiento = true;
        }else{
            enMovimiento = false;
        }  
    } 
    public synchronized void reproducirSonido(){
        colocarBomba.reproducir();
        bomba.reproducirSonido();
    }
    public synchronized void controladorPersonaje(int codigoTecla,int tipoEvento){
       if(tipoEvento==1){
           if (estaVivo) {
               switch(codigoTecla){
                case KeyEvent.VK_UP:
                    this.establecerMovimiento(Movimiento.ARRIBA);
                    break;
                case KeyEvent.VK_DOWN:
                    this.establecerMovimiento(Movimiento.ABAJO);
                    break;
                case KeyEvent.VK_LEFT:
                    this.establecerMovimiento(Movimiento.IZQUIERDA);
                    break;
                case KeyEvent.VK_RIGHT:
                    this.establecerMovimiento(Movimiento.DERECHA);
                    break;
                case KeyEvent.VK_W:
                    if (!bomba.bombaActiva && !enMovimiento && estaVivo) {
                        colocarBomba.establecerActivo();
                        bombaColocada=true;
                    }
                    break;
                }
           }
            
       }else{
            this.establecerMovimiento(Movimiento.NINGUNO);
       }
    }   
    public synchronized void dibujarPersonaje(Graphics g,Mapa m){ 
        if (bomba.bombaActiva) {
            bomba.dibujarBomba(g, m);
        }
        if (estaVivo){
            Image i;
            if (enMovimiento) {
                i=animacionesMovimiento.get(miMovimiento.direccionActual).getSprite();
                g.drawImage(i, posX+i.getWidth(null)/2, posY,ancho,alto, null); 
            }else{
                i=animacionEspera.getSprite();
                g.drawImage(i, posX+i.getWidth(null)/2, posY,ancho,alto, null); 
            }
            if (poderActivo) {
                g.drawImage(animacionPoderActivo.getSprite(), posX+i.getWidth(null)/2, posY,ancho,alto, null); 
            }
        }else{
            Image i;
            i=animacionMuerte.getSprite();
            if (i.getWidth(null)==16) {
                g.drawImage(i, posX+i.getWidth(null)/2, posY,ancho,alto, null);
            }else{
                g.drawImage(i, posX,posY, alto,alto, null);  
           }    
        }
        g.setColor(Color.WHITE);
        g.drawImage(imagenPrevia, VidasTexto.getX()-50, VidasTexto.getY()-30, 50, 50, null);
        g.drawString(VidasTexto.getTexto()+vidasJuego, VidasTexto.getX(), VidasTexto.getY());
        g.drawString(PuntajeTexto.getTexto()+puntajeGanado, PuntajeTexto.getX(), PuntajeTexto.getY());   
    }    
    public synchronized void establecerPosicion(int x, int y){
        estaVivo=true;
        vidasJuego=vidas;
        bombaColocada=false;
        poderActivo=false;
        bomba.bombaActiva=false;
        this.posX=x;
        this.posY=y;
    }  
    public synchronized void aumentarPuntaje(long puntaje){
        puntajeGanado+=puntaje;
    }
    public synchronized void reiniciarPuntaje(){
        puntajeGanado=0;
    }
    public int obtenerPosX() {
        return posX;
    }
    public int obtenerPosY() {
        return posY;
    }
    public Rectangle obtenerArea(){
        areaJuagdor.setBounds(posX+velocidad*4,posY+velocidad*4,ancho-velocidad*3,alto-velocidad*6);
        return areaJuagdor;
    }
    public Movimiento obtenerNovimiento(){
        return miMovimiento;
    }
    private synchronized void reestablecerPersonaje(){
        this.posX=posDespiegueX;
        this.posY=posDespiegueY;
        bomba.bombaActiva=false;
        poderActivo=false;
        estaVivo=true;
        vidasJuego--;
    }
    public void establecerPosicionDespliegue(int x, int y){
        posDespiegueX=x;
        posDespiegueY=y;
    }
    public int obtenerVidas(){
        return vidasJuego;
    }
    public long obtenerPuntaje(){
        return puntajeGanado;
    }
    public boolean obtenerPoderActivo(){
        return poderActivo;
    }
    public void establecerItemRecojido(int itemCod,long tiempo){
        switch(itemCod){
            case Elemento.BOMBAROJA:
                tiempoInicioPoder=tiempo;
                establecerPoder(true);
                break;
           
        }
    }
    public void establecerPoder(boolean activo){
        poderActivo=activo;
    }
    public void establecerTiempoPoder(long tiempoPoderActivo){
        this.tiempoPoderActivo=tiempoPoderActivo;
    }
    
    
}
