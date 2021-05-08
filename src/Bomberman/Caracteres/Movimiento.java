/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.Caracteres;

/**
 *
 * @author jdavi
 */
public class Movimiento {
    public static final int ARRIBA=2;
    public static final int DERECHA=1;
    public static final int ABAJO=0;
    public static final int IZQUIERDA=3;
    public static final int NINGUNO=-1;
    
    public boolean disponibleArriba;
    public boolean disponibleAbajo;
    public boolean disponibleIzquierda;
    public boolean disponibleDerecha;
    
    public int direccionActual;

    public void estableDireccionActual(int direccion){
      direccionActual=direccion;
    }
}
