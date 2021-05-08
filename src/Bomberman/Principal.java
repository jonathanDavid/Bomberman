 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman;

import Bomberman.GUI.Capa;
import Bomberman.GUI.Interfaz;
import Bomberman.GUI.Menu;
import Bomberman.Objetos.ColeccionNiveles;
import Bomberman.Objetos.Nivel;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 *
 * @author jdavi
 */
public class Principal {
     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
            Interfaz miInterfaz=null;
            int ancho=767;
            int alto=720;
            
            Menu miMenu = new Menu(20,ancho/2,alto/2);
            String[] botones={"Juego","Salir"};
            miMenu.crearBotones(botones);
            
            Capa capa = new Capa(1,-1,miMenu,KeyEvent.VK_UP,KeyEvent.VK_DOWN);
            
            miInterfaz= new Interfaz(capa);
           
            ColeccionNiveles nuevaColeccion = new ColeccionNiveles(10,50,160,100,ancho);
            Capa capa1= new Capa(2,1,nuevaColeccion,KeyEvent.VK_RIGHT,KeyEvent.VK_LEFT);
            miInterfaz.añadirCapa(capa1, 1);
             
            int j=3;
            for (Nivel i: nuevaColeccion.getMisNiveles()) {
                Capa c = new Capa(j,2,i,-1,-1);
                miInterfaz.añadirCapa(c, 2);
                j++;
            }
            Juego P = new Juego(miInterfaz,ancho , alto);
            P.setVisible(true);
            P.iniciarHilos();           
    }
    

}
