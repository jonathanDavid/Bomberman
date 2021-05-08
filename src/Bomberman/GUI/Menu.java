/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.GUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;


/**
 *
 * @author jdavi
 */
public class Menu implements Dibujable{
    
    private int posX,posY;
    private int seleccion, AltoBotones;
    private ArrayList<Label>  Elementos;
    private Image puntero;
    private Image Portada; 
    
    public Menu(int tamañoBotones, int x, int y){
        puntero = new ImageIcon(getClass().getResource("Imagenes/Puntero.png")).getImage();
        Portada = new ImageIcon(getClass().getResource("Imagenes/Portada.png")).getImage();   
        this.AltoBotones=tamañoBotones;
        this.posX=x;
        this.posY=y;
        this.seleccion=0;        
        Elementos= new ArrayList();
    }   
    public void crearBotones(String[] textoBotones){
        for (int i = 0; i < textoBotones.length; i++) {
            Label b= new Label(textoBotones[i], posX-40,posY+AltoBotones*i);
            Elementos.add(b);
        }
    }      
    public void cambiarPosicion(int x, int y){
        posX=x;
        posY=y;
        int j=0;
        for (Label i: Elementos ) {
            i.establecerPosicion(posX , (posY+AltoBotones*j));
            j++;
        }
    }
    @Override
    public void dibujar(Graphics g,Font f){    
        g.drawImage(Portada, posX-Portada.getWidth(null)/2, (posY-Portada.getHeight(null))/2, null);
        int index=0;
        for(Label Elemento : Elementos){
           g.setFont(f);
           if(index==seleccion){    
               g.setFont(f.deriveFont(Font.TRUETYPE_FONT,(float)(f.getSize()*1.5)));
               g.setColor(Color.RED);
               g.drawImage(puntero, (Elementos.get(seleccion).getX()-(Elementos.get(seleccion).getTexto().length()*f.getSize()/2))-24, (Elementos.get(seleccion).getY()-f.getSize()),16,16, null);
           }else{
               g.setFont(f.deriveFont(Font.TRUETYPE_FONT,14));
               g.setColor(Color.WHITE);
           }
           g.drawString(Elemento.getTexto(), Elemento.getX()-(Elemento.getTexto().length()*g.getFont().getSize()/3), Elemento.getY());
           index++;
        }
    }   
    @Override
    public void accionEvento(int codigoTecla){
        switch(codigoTecla){
            case KeyEvent.VK_UP:{
                    seleccion--;
                    if (seleccion<0) {
                        seleccion=Elementos.size()-1;
                    }          
                break;
            }
            case KeyEvent.VK_DOWN:{
                    seleccion++;
                    if (seleccion>Elementos.size()-1) {
                        seleccion=0;
                    }              
                break;
            }
            case KeyEvent.VK_ENTER:{
                    if (seleccion==Elementos.size()-1) {
                        System.exit(0);
                    }              
                break;
            }
            default:
                break;
        }
    }  
    @Override
    public void reproducirSonido(){
    
    }
    
    
    
}
