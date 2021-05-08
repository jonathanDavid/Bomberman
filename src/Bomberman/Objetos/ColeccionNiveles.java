/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.Objetos;

import Bomberman.GUI.Dibujable;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author jdavi
 */
public class ColeccionNiveles implements Dibujable{
    public static final String SeparadorDATOS="@";
    public static final String SeparadorSPRITES="-";
     
    private ArrayList<Nivel> misNiveles;
    private String archivoMapas;
    private int seleccion;
    private int posX, posY;
    private int anchoVistaPrevia, altoVistaPrevia, anchoPantalla;
    
    public ColeccionNiveles(int x,int y,int anchoVistaPrevia, int altoVistaPrevia, int anchoPantalla) throws IOException {
        archivoMapas="Niveles.txt";
        posX=x+(anchoPantalla-3*(anchoVistaPrevia+14))/2;
        posY=y;
        seleccion=0;
        this.anchoPantalla=anchoPantalla;
        misNiveles= new ArrayList();
        this.anchoVistaPrevia=anchoVistaPrevia;
        this.altoVistaPrevia=altoVistaPrevia;
        cargarNiveles();
    }
    
    private void cargarNiveles() throws FileNotFoundException, IOException{
        String linea;
        Mapa miMapa=null;
        FileReader f = new FileReader(archivoMapas); 
        BufferedReader b = new BufferedReader(f);
        
        while ((linea=b.readLine())!=null) {            
            String[] Elementos= linea.split(SeparadorDATOS);
            int fil=Integer.parseInt(Elementos[1]);
            miMapa= new Mapa(10,Integer.parseInt(Elementos[1]),
                    Integer.parseInt(Elementos[2]),
                    Integer.parseInt(Elementos[3]),
                    Integer.parseInt(Elementos[4]),
                    Integer.parseInt(Elementos[5]),
                    Integer.parseInt(Elementos[6]),
                    "/"+Elementos[0],
                    Elementos[10]);
            
            String[][] codigos= new String[Integer.parseInt(Elementos[1])][Integer.parseInt(Elementos[2])];
            boolean[][] objetoSolido= new boolean[Integer.parseInt(Elementos[1])][Integer.parseInt(Elementos[2])];
            for (int i = 0; i < fil; i++) {
                linea=b.readLine();
                String[] Matriz= linea.split(SeparadorSPRITES);
                for (int j = 0; j < Matriz.length; j++) {
                    codigos[i][j]=Matriz[j];
                    if (!Matriz[j].equals("a") && !Matriz[j].equals("e")) {
                        objetoSolido[i][j]=true;
                    }
                }
            }
            miMapa.establecerCodigosEsprites(codigos);
            miMapa.establecerObjetosSolidos(objetoSolido);
            Image imagenPrevia = new ImageIcon(getClass().getResource("Imagenes/"+Elementos[0]+"/"+Elementos[9])).getImage();
            Nivel nuevoNivel =  new Nivel(miMapa,Elementos[0],imagenPrevia, Integer.parseInt(Elementos[7]), Integer.parseInt(Elementos[8]),this);  
            misNiveles.add(nuevoNivel);                       
        }
        b.close();
    }
    
    public void guardarNivel(Nivel nuevoNivel){
        try {
            FileWriter archivo= new FileWriter(archivoMapas,true);
            PrintWriter pw= new PrintWriter(archivo);
            pw.println(nuevoNivel.obtenerInformacionNivel(archivoMapas));
            
            archivo.close();
        } catch (IOException ex){}
        
    }
    
    public void crearNivel(int i){
       misNiveles.get(i).cargarNivel();
    }
    
    @Override
    public synchronized void dibujar(Graphics g,Font f){
        int j=0,k=0,index=0;
        for(Nivel i: misNiveles){
            if (index==seleccion) {
                i.dibujarVistaPrevia(posX+(anchoVistaPrevia+f.getSize()*2)*j,posY+(altoVistaPrevia+f.getSize()*3)*k,(int)(anchoVistaPrevia*1.1),(int)(altoVistaPrevia*1.1),f.deriveFont(Font.TRUETYPE_FONT, (float)(f.getSize()*1.2)), g); 
            }else{
                i.dibujarVistaPrevia(posX+(anchoVistaPrevia+f.getSize()*2)*j,posY+(altoVistaPrevia+f.getSize()*3)*k,anchoVistaPrevia,altoVistaPrevia,f, g);
            }
            
            if (misNiveles.get(seleccion).equals(i)) {
                Graphics2D g2= (Graphics2D)g;
                g2.setStroke(new BasicStroke(1f));
                g2.setColor(Color.WHITE);
                g2.drawRect(posX+(anchoVistaPrevia+f.getSize()*2)*j-5, posY+(altoVistaPrevia+f.getSize()*3)*k-5, anchoVistaPrevia+(int)(f.getSize()*1.8), altoVistaPrevia+f.getSize()*3);
            }  
     
            if (posX+(anchoVistaPrevia+f.getSize()*2)*(j+1)+anchoVistaPrevia > anchoPantalla){
                j=0;
                k++;
            }else{
               j++;
            } 
            index++;
        }
       
            
  
        
    }
    @Override
    public synchronized void accionEvento(int codigoTecla){
        switch(codigoTecla){
            case KeyEvent.VK_LEFT:{   
                seleccion--;
                if (seleccion<0) {
                    seleccion=misNiveles.size()-1;
                }    
                break;
            }
            case KeyEvent.VK_RIGHT:{  
                seleccion++;
                if (seleccion> misNiveles.size()-1) {
                    seleccion=0;
                }     
                break;
            }
            case KeyEvent.VK_ENTER:{  
                crearNivel(seleccion);
                break;
            }
            default:
                break;
        }
    }
    @Override
    public void reproducirSonido(){
    
    }
    public synchronized ArrayList<Nivel> getMisNiveles() {
        return misNiveles;
    }
    
    public void cambiarNivel(){
            seleccion++;
            crearNivel(seleccion);
    }
    
}
