/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.Objetos;

import Bomberman.Caracteres.Enemigo;
import Bomberman.Caracteres.Grafo;
import Bomberman.Caracteres.Nodo;
import Bomberman.Caracteres.Personaje;
import Bomberman.GUI.Animacion;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author jdavi
 */
public class Mapa {
   
    private boolean[][] objetoSolido;
    private boolean[][] objetoSolidoTem;
    private boolean animacionActiva;
    private boolean puertaNivelActiva,establecerPosicionPuerta;
    private int filasCuadricula,columnasCuadricula,altoCuadricula,posX,posY,anchoCuadricula,puntosDestruccion;
    private long tiempoTotal, tiempoInicio,tiempoActualizacion,tiempoUltimaActializacion, puntajeTotal;
    private ArrayList<Cuadricula> bloquesDestruir;
    private ArrayList<Enemigo> misEnemigos;
    private ArrayList<Elemento> misElementos;
    private ArrayList<Long> tiempoDespliegue;   
    private Animacion destruirCuad;
    private Animacion puertaNivel;
    private Cuadricula puertaPosicion;
    
    private Cuadricula[][] misCuadriculas;
    private ArrayList<Image> misImagenes;
    private ArrayList<String> codigosImagenes;
    
    private Grafo CampoEnemigos;
    private Image bloqueDestructible;
    private Nodo nodoJuagdor;
    private Rectangle areaCuadricula;
    private String rutaImagenes, nombreAnimacion;
    private String[][] codSprite;


    public Mapa(int puntos,int filasCuadricula, int columnasCuadricula, int altoCuadricula, int anchoCuadricula,int posX,int posY,String carptaNivel,String nombreAnimacion){
        this.posX=posX; this.posY=posY;
        this.filasCuadricula = filasCuadricula;
        this.areaCuadricula=  new Rectangle(posX, posY,anchoCuadricula,altoCuadricula);
        this.columnasCuadricula = columnasCuadricula;
        this.altoCuadricula = altoCuadricula;
        this.anchoCuadricula = anchoCuadricula;
        this.nombreAnimacion=nombreAnimacion; 
        rutaImagenes="Imagenes"+carptaNivel;
        puertaNivel= new Animacion(false);
        objetoSolidoTem= new boolean[filasCuadricula][columnasCuadricula];
        bloquesDestruir= new ArrayList();
        misEnemigos= new ArrayList();
        puntosDestruccion=puntos;
        CampoEnemigos= new Grafo();
        puertaNivelActiva=false;
        establecerPosicionPuerta=true;
        misElementos= new ArrayList();
        tiempoDespliegue=new ArrayList();
    }
    public void establecerPuntageGuardado(long puntaje){
        puntajeTotal+=puntaje;
        CrearItems(puntajeTotal*2);
    }

    private void asignarMatriz(boolean[][] a,boolean[][] b){
        for(int i = 0; i <filasCuadricula; i++) {
              for(int j = 0; j < columnasCuadricula; j++) {
                  a[i][j]=b[i][j];
              }
        }
    } 
    public void establecerCodigosEsprites(String[][] misCodigos){
        this.codSprite=misCodigos;
    }
    public void establecerObjetosSolidos(boolean[][] objetoSolido){
        this.objetoSolido=objetoSolido;
        asignarMatriz(objetoSolidoTem, objetoSolido);
          
    }   
    public synchronized void reiniciarMapa(){
        misEnemigos=null;
        tiempoTotal=0;
        puntajeTotal=0;
        tiempoInicio=0;
        misCuadriculas=null;
        bloquesDestruir= new ArrayList();
        destruirCuad=null;
        puertaNivelActiva=false;
        establecerPosicionPuerta=true;
        misImagenes=null;
        codigosImagenes=null;
        puertaPosicion=null;
        asignarMatriz(objetoSolido, objetoSolidoTem);
    }   
    public synchronized void cargarSprites(){
        puertaNivelActiva=false;
        puertaPosicion=null;
        misCuadriculas = new Cuadricula[filasCuadricula][columnasCuadricula];
        misImagenes= new ArrayList();
        codigosImagenes= new ArrayList();
        for (int i = 1; i <= 5; i++) {
            puertaNivel.añadirEscena(new ImageIcon(getClass().getResource("Imagenes/Objetos/Salir"+i+".png")).getImage(), 100);
        }
        for (int i = 0; i < filasCuadricula; i++){
            for (int j = 0; j < columnasCuadricula; j++){
               misCuadriculas[i][j]=new Cuadricula(codSprite[i][j],objetoSolido[i][j],i,j);
                if (misImagenes.isEmpty()) {
                    codigosImagenes.add(codSprite[i][j]);
                    misImagenes.add(new ImageIcon( getClass().getResource(rutaImagenes+"/"+codSprite[i][j]+".png")).getImage());
                }else{
                    boolean agregar=true;
                    for (int k = 0; k < codigosImagenes.size(); k++) {
                        if (codigosImagenes.get(k).equals(codSprite[i][j])) {
                            agregar=false;
                        }
                    }
                    if (agregar) {
                        codigosImagenes.add(codSprite[i][j]);
                        misImagenes.add(new ImageIcon( getClass().getResource(rutaImagenes+"/"+codSprite[i][j]+".png")).getImage());
                    }
                
                }
            }
        }
        
        bloqueDestructible= new ImageIcon(getClass().getResource(rutaImagenes+"/"+nombreAnimacion+".png")).getImage();
        codigosImagenes.add(nombreAnimacion);
        misImagenes.add(bloqueDestructible);
        destruirCuad= new Animacion(true);
        for (int i = 1; i <= 6; i++) {
            destruirCuad.añadirEscena(new ImageIcon(getClass().getResource(rutaImagenes+"/"+nombreAnimacion+i+".png")).getImage(),160);
            tiempoTotal+=160;
        }
        crearBloquesDestructibles();
        establecerPosiciones();
        String[] enemigosNombre= {"Enemigo1","Enemigo2","Enemigo3","Enemigo4"};
        misEnemigos= new ArrayList();
        CampoEnemigos.actualizarGrafo(misCuadriculas,filasCuadricula,columnasCuadricula);
       
        for (int i=0;i<=4;i++){    
            int enemigoEscojido= (int)(Math.random()*(enemigosNombre.length));
            Enemigo j = new Enemigo(enemigosNombre[enemigoEscojido], 0, 0, 2,anchoCuadricula, altoCuadricula,150);
            if (enemigoEscojido==enemigosNombre.length-1) {
                j.cargarSprites(4);
            }else{
                j.cargarSprites(8);
            } 
            puntajeTotal+=150;
            misEnemigos.add(j);     
        }
        posicionarEnemigos();
        CrearItems(puntajeTotal);

    }  
    public void CrearItems(long puntaje){
        misElementos=new ArrayList();
        for (int i = 2; i < 4; i++) {          
            Elemento item= new Elemento(Elemento.BOMBAROJA,12000,12000,anchoCuadricula,altoCuadricula);
            misElementos.add(item);
            tiempoDespliegue.add(puntajeTotal/i);
        }
    }
    public synchronized void dibujarMapa(Graphics g){     
        for (int i = 0; i < filasCuadricula; i++) {
            for (int j = 0; j < columnasCuadricula; j++) {
                Cuadricula c= misCuadriculas[i][j];
                for (int k = 0; k < misImagenes.size(); k++) {
                    if (c.obtenerId().equals(codigosImagenes.get(k))) {
                        g.drawImage(misImagenes.get(k), c.obtenerPosX(), c.obtenerPosY(), anchoCuadricula, altoCuadricula, null);
                    }
                }
            }
        }
        if (puertaNivelActiva) {
            g.drawImage(puertaNivel.getSprite(),puertaPosicion.obtenerPosX(),puertaPosicion.obtenerPosY(), anchoCuadricula, altoCuadricula,null);
        }
        
//        for (Nodo i: CampoEnemigos.Nodos) {
//            g.fillOval(i.getCasillaPosicion().obtenerPosX(), i.getCasillaPosicion().obtenerPosY(), 5, 5);
//        }
       
    }
    public synchronized void actualizarMapa(long tiempo,Graphics g,Font f,Personaje jugador,Nivel miNivel){
       
        if (animacionActiva) {
            if (tiempo-tiempoInicio>tiempoTotal) {
                animacionActiva=false;
            }else{ 
                for (Cuadricula i: bloquesDestruir) {
                    destruirCuad.actulizarAnimacion(tiempo);
                    g.drawImage(destruirCuad.getSprite(), i.obtenerPosX(), i.obtenerPosY(),anchoCuadricula,altoCuadricula, null);
                    i.establecerSprite(/*new ImageIcon(getClass().getResource(rutaImagenes+"/"+codSprite[i.obtenerIndiceFila()][i.obtenerIndiceColumna()]+".png")).getImage()*/codSprite[i.obtenerIndiceFila()][i.obtenerIndiceColumna()]);
                    g.setFont(f.deriveFont(Font.TRUETYPE_FONT, 12));
                    g.setColor(Color.BLACK);     
                    g.drawString(""+puntosDestruccion,(int)(i.obtenerPosX()+f.getSize()*1.5), i.obtenerPosY()+f.getSize()); 
                    g.setFont(f);
                }
            }
        }else{
            tiempoInicio=tiempo;   
            boolean a= false;
            for (Cuadricula i: bloquesDestruir){
                i.establecerSolido(false);
                objetoSolido[i.obtenerIndiceFila()][i.obtenerIndiceColumna()]=false;
                bloquesDestruir.remove(i);
                jugador.aumentarPuntaje(puntosDestruccion);  
                a=true;
            }

        }
        if (jugador.obtenerPuntaje()==puntajeTotal) {
            puertaNivel.actulizarAnimacion(tiempo);
            puertaNivelActiva=true;
            if (establecerPosicionPuerta) {
                puertaPosicion= asignarCasilla();
                establecerPosicionPuerta=false;
            }
        }
        
        if (tiempo-tiempoUltimaActializacion>tiempoActualizacion) {
                CampoEnemigos.actualizarGrafo(misCuadriculas,filasCuadricula,columnasCuadricula);
                tiempoUltimaActializacion=tiempo;
                tiempoActualizacion= (long)(Math.random()*(3000))+2000;
        }
        
        nodoJuagdor = CampoEnemigos.posicionCercana(jugador.obtenerPosX(), jugador.obtenerPosY());
        for (Enemigo i: misEnemigos) {
            i.dibujarEnemigo(g,f);
            if (jugador.obtenerPoderActivo()) {
                i.establecerAsustado(true);
            }else{
                i.establecerAsustado(false);
            }
            i.moverse(tiempo, this);
            if (i.verificarColision(jugador.obtenerArea())) {
                if (!jugador.obtenerPoderActivo()) {
                    jugador.destruirPersonaje(tiempo);
                }else{
                    i.destruirEnemigo();
                }
                
            }
            if (i.obtenerDesaparecer()) {
                jugador.aumentarPuntaje(i.obtenerPuntosDestruccion());
                misEnemigos.remove(i);
            }
        }
        
        if (puertaPosicion!=null) {
            if (puertaPosicion.equals(nodoJuagdor.getCasillaPosicion())) {
                miNivel.establecerCambiarSiguienteNivel(true);
                puertaPosicion=null;
                establecerPosicionPuerta=true;
            }
        }
        
        for (int i = 0; i < misElementos.size(); i++) {
            if (jugador.obtenerPuntaje()>=tiempoDespliegue.get(i)) {
                if (misElementos.get(i).obtenerMiCuadricula()==null){
                    misElementos.get(i).establecerCuadriculaPosicion(asignarCasilla());
                }
                if (nodoJuagdor.getCasillaPosicion().equals(misElementos.get(i).obtenerMiCuadricula())) {
                    jugador.establecerItemRecojido(misElementos.get(i).obtenerTipoItem(),tiempo);
                    jugador.establecerTiempoPoder(misElementos.get(i).obtenerTiempoActivo());
                    misElementos.get(i).esteblecerActivo(false);
                }
                misElementos.get(i).actualizarItem(tiempo);
                misElementos.get(i).dibujarElemento(g);
            }
            if (!misElementos.get(i).obtenerActivo()) {
                misElementos.remove(misElementos.get(i));     
            }
        }

    }
    public synchronized boolean collisionObjetosSolidos(Rectangle area,String etiqueta){
        for (int i = 0; i < filasCuadricula; i++) {
            for (int j = 0; j <columnasCuadricula; j++) {
                if (misCuadriculas[i][j].esSolido()) {
                    Cuadricula c=misCuadriculas[i][j];
                    areaCuadricula.setBounds(c.obtenerPosX(),c.obtenerPosY(),anchoCuadricula,altoCuadricula);
                    if(area.intersects(areaCuadricula)) {
                        if (etiqueta.equals("Bomba")) {
                            if(c.esDestructible()) {
                                animacionActiva=true;
                                boolean colocar=true;
                                for (Cuadricula k: bloquesDestruir) {
                                    if (k.equals(c)) {
                                        colocar=false;
                                    }
                                }
                                if (colocar) {
                                    bloquesDestruir.add(c); 
                                }                         
                            }     
                        }                      
                        return true;
                    }
                }else{
                    if (etiqueta.equals("Bomba")){
                        for(Enemigo k: misEnemigos) {
                            if(k.verificarColision(area)){
                                k.destruirEnemigo();
                            }
                        }
                    }
                }
                
            }
        }
        destruirCuad.Inicio();
        return false;
    }   
    public void crearBloquesDestructibles(){
        for (int i = 1; i < filasCuadricula; i++) {
            int numBloques=(int)(Math.random()*(columnasCuadricula-3)+3);
            int k=0;
            while(k<=numBloques){
                int j=(int)(Math.random()*(columnasCuadricula-1)+1);
                if (!objetoSolido[i][j]){    
                    if ((i!=1 || j!=1) && (i!=1 || j!=2) && (i!=2 || j!=1)) {
                        Cuadricula c = new Cuadricula(/*bloqueDestructible,*/nombreAnimacion,true,i,j);
                        c.establecerDestruible();
                        misCuadriculas[i][j]=c; 
                        objetoSolido[i][j]=true;
                        puntajeTotal+=puntosDestruccion;
                    }
                } 
                k++;
            }
        }
        
    }
    public void setTamaño(int altoCuadricula,int anchoCuadricula) {
        this.altoCuadricula = altoCuadricula;
        this.anchoCuadricula = anchoCuadricula;
    }   
    public void establecerPosiciones(){
        for (int i = 0; i < filasCuadricula; i++) {
            for (int j = 0; j < columnasCuadricula; j++) {
                misCuadriculas[i][j].establecerPosicion(posX+anchoCuadricula*j,posY+altoCuadricula*i);
            }
        }
    }
    public void posicionarEnemigos(){
        int desplazarFilas= filasCuadricula/misEnemigos.size();
        int j,k=1,cont=1;
        for (Enemigo i: misEnemigos) {
            ArrayList<Cuadricula> posicionesPosibles= new ArrayList();
            j=(desplazarFilas*(cont-1))+1;
            while(j<=(desplazarFilas*cont-1)){
                k=1;
                while(k<=columnasCuadricula-1){
                    if (!objetoSolido[j][k] && ((j!=1 || k!=1) && (j!=1 || k!=2) && (j!=2 || k!=1))){
                       posicionesPosibles.add(misCuadriculas[j][k]);
                    }
                    k++;
                }
                j++;
            }
            cont++;
            int c1= (int)(Math.random()*(posicionesPosibles.size()-1));
            if (posicionesPosibles.size()>=0) {
                Cuadricula c =posicionesPosibles.get(c1);
                i.establecerPosicion(c.obtenerPosX(), c.obtenerPosY());
                
            } 
            i.establecerGrafo(CampoEnemigos); 
            
        }
    }   
    public synchronized Cuadricula obtenerPosicionCercana(int x, int y){
        double disMenor=999999999;
        Cuadricula cuadCercana=null;
        for (int i = 0; i < filasCuadricula; i++) {
            for (int j = 0; j <columnasCuadricula; j++) {
                if(!objetoSolido[i][j]) {
                    Cuadricula c = misCuadriculas[i][j];
                    double d = Math.pow(Math.pow((x-c.obtenerPosX()),2)+Math.pow(y-c.obtenerPosY(), 2), 0.5);
                    if (d<disMenor) {
                         cuadCercana=c;
                         disMenor=d;
                    }
                }
            }
        }
        return cuadCercana;
    }
    public synchronized void reproducirSonido(){
        if (misEnemigos!=null) {
            for (Enemigo i: misEnemigos) {
                i.reproducirSonido();
            }
        } 
    }
    public synchronized Nodo obtenerNodoJugador(){
        return nodoJuagdor;
    }
    public synchronized Grafo obtenerCampoEnemigos(){
        return CampoEnemigos;
    }
    public Cuadricula asignarCasilla(){
        int i=(int)(Math.random()*(filasCuadricula-1));
        int j=(int)(Math.random()*(columnasCuadricula-1));
        Cuadricula a=misCuadriculas[i][j];
        while (a.esSolido()) {            
            i=(int)(Math.random()*(filasCuadricula-1));
            j=(int)(Math.random()*(columnasCuadricula-1));
            a=misCuadriculas[i][j];
        }          
        return a;            
    }
    
    public int obtenerFilasCuadricula() {
        return filasCuadricula;
    }

    public int obtenerColumnasCuadricula() {
        return columnasCuadricula;
    }

    public int obtenerAltoCuadricula() {
        return altoCuadricula;
    }
    public int obtenerPosX(){
        return posX;
    }
    public int obtenerPosY(){
        return  posY;
    }

    public int obtenerAnchoCuadricula() {
        return anchoCuadricula;
    }

    public String obtenerNombreAnimacion() {
        return nombreAnimacion;
    }
}
