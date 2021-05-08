/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.Objetos;

import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author jdavi
 */
public class Cuadricula {
    private String id;
    private int posX,posY;
    private int i,j;
    private boolean esSolido;
    private boolean esDestruible;
    
    public Cuadricula(String id,boolean esSolido, int i, int j) {
        this.i = i;
        this.j = j;
        this.id = id;
        this.esSolido=esSolido;
    }
    

    public void establecerSolido(boolean esSolido){
        this.esSolido=esSolido;
    }
    public void establecerSprite(String sprite) {
        this.id= sprite;
    }
    public String obtenerId() {
        return id;
    }
    

    public int obtenerPosX() {
        return posX;
    }

    public int obtenerPosY() {
        return posY;
    }
    
    public int obtenerIndiceFila() {
        return i;
    }

    public int obtenerIndiceColumna() {
        return j;
    }
    

    public void establecerPosicion(int x , int y){
        this.posX=x;
        this.posY=y;
    }
    public void establecerDestruible(){
     esDestruible=true;
    }
    
    public boolean esDestructible(){
        return esDestruible;
    }
    public boolean esSolido(){
        return esSolido;
    }
}
