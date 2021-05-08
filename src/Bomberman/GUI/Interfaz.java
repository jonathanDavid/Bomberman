/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.GUI;


import java.util.ArrayList;

/**
 *
 * @author jdavi
 */
public class Interfaz {
    
    private ArrayList<Capa> misCapas;
    private Capa capaPrincipal;
    private int capaSeleccionada;

    public Interfaz(Capa Raiz) {
        misCapas= new ArrayList();
        capaPrincipal=Raiz;
        misCapas.add(Raiz);
        capaSeleccionada =0;
    }
    
    public void añadirCapa(Capa a, int id){
        for (Capa i: misCapas) {
            if (i.getId()==id) {
                i.añadirCapa(a);
            }
        }
         misCapas.add(a);
    }
    public Capa obtenerCapaPrincipal(){
        return capaPrincipal;
    }
    public Capa obtenerCapa(int id){
        for (Capa i: misCapas) {
            if (i.getId()==id) {
                return i;
            }
        } 
        return null;
    }
    public void cambiarSeleccion(int seleccion){
        this.capaSeleccionada=seleccion;
    }
    
   
   
    
}
