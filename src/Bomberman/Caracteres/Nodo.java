/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.Caracteres;

import Bomberman.Objetos.Cuadricula;
import java.util.ArrayList;

/**
 *
 * @author jdavi
 */
public class Nodo {
   private Cuadricula casillaPosicion;
   private ArrayList<Nodo> nodosAdyacentes;
   private boolean visitado;
   private int id;
   
    public Nodo(Cuadricula casillaPosicion,int id) {
        this.casillaPosicion = casillaPosicion;
        this.id=id;
        nodosAdyacentes= new ArrayList();
    }
    public boolean estaVisitado(){
        return visitado;
    }
    public void establecerVisitado(boolean estaVisitado){
        visitado=estaVisitado;
    }
    public int obtenerId(){
        return id;
    }
    
    public synchronized ArrayList<Nodo> obtenerNodosAdyacentes(){
        return nodosAdyacentes;
    }

    public Cuadricula getCasillaPosicion() {
        return casillaPosicion;
    }
    public void añadirNodoAdyacente(Nodo n){
        nodosAdyacentes.add(n);
    }
      
}
