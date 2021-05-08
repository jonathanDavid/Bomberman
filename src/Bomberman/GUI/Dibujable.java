/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.GUI;

import java.awt.Font;
import java.awt.Graphics;

/**
 *
 * @author jdavi
 */
public interface Dibujable {
   
   void  dibujar(Graphics g,Font f);
   void accionEvento(int codigoTecla);
   void reproducirSonido();
}
