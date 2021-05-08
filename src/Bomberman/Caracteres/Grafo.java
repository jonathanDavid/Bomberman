/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.Caracteres;

import Bomberman.Objetos.Cuadricula;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author jdavi
 */
public class Grafo {
    public ArrayList<Nodo> Nodos;
   
    public Grafo(){
        Nodos= new ArrayList();
    }
    
    public synchronized void añadirNodo(Cuadricula c){
        Nodo n;
        if (Nodos.size()>0) {
            n = new Nodo(c,Nodos.size());
        }else{
            n = new Nodo(c,0);
        }
        for (Nodo i:Nodos) {
            int X=Math.abs(n.getCasillaPosicion().obtenerIndiceFila()-i.getCasillaPosicion().obtenerIndiceFila());
            int Y=Math.abs(n.getCasillaPosicion().obtenerIndiceColumna()-i.getCasillaPosicion().obtenerIndiceColumna());     
            if ((X==1 && Y==0) || (Y==1 && X==0)) {
                n.añadirNodoAdyacente(i);
                i.añadirNodoAdyacente(n);
            }
        }
        Nodos.add(n); 
    }   
    public synchronized void actualizarGrafo(Cuadricula[][] zona, int filas, int columnas){
        Nodos=null;
        Nodos= new ArrayList();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (!zona[i][j].esSolido()) {
                    this.añadirNodo(zona[i][j]);
                }
            }
        }
    } 
    public synchronized Nodo posicionCercana(int x, int y){
        Nodo a = null;
        double disMenor=999999999;
        for (Nodo i:  Nodos) {
            Cuadricula c = i.getCasillaPosicion();
            double d = Math.pow(Math.pow((x-c.obtenerPosX()),2)+Math.pow(y-c.obtenerPosY(), 2), 0.5);
            if (d<disMenor){
                 a=i;
                 disMenor=d;
            }
        }
        return a;
    }
    public synchronized void establecerNodoActual(Nodo nodoActual){
        for (Nodo i :Nodos){
            i.obtenerNodosAdyacentes().remove(nodoActual);
        }
        Nodos.remove(nodoActual);
    }  
    public synchronized ArrayList<Nodo> BuscarCaminoBFS(Nodo Inicio,Nodo Final){
        ArrayList<Nodo> Camino= new ArrayList();
        limpiarNodos();
        Queue<Nodo> Cola = new LinkedList<>();
        int[] Padres = new int[Nodos.size()];
        Cola.add(Inicio);
        Padres[Inicio.obtenerId()]=-1;
        Inicio.establecerVisitado(true);
        Nodo Actual=null;
        
        while(!Cola.isEmpty()){
            Actual = Cola.remove();
            Actual.establecerVisitado(true);
            if(Actual.equals(Final)) {
                 break;
            }else{
                for (Nodo i: Actual.obtenerNodosAdyacentes()) {
                    if (!i.estaVisitado()) {
                        Cola.add(i);
                        try { 
                            Padres[i.obtenerId()]=Actual.obtenerId();
                        } catch (Exception e){}  
                    }
                }
            }
            
        }

        if (Actual.equals(Final)){
            int id=Final.obtenerId();
            while (id!=-1) {            
                Camino.add(Nodos.get(id));
                id=Padres[id];
            }
            
        }
        limpiarNodos();
        return Camino;
    }    
    public synchronized void limpiarNodos(){
        for (Nodo i: Nodos) {
            i.establecerVisitado(false);
        }
    } 
    public synchronized Nodo obtenerNodo(Cuadricula c){
        for (Nodo i: Nodos) {
            if (i.getCasillaPosicion().equals(c)) {
                return i;
            }
        }
        return null;
    }
}



