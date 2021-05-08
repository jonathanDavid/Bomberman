/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.GUI;

/**
 *
 * @author jdavi
 */
public class Label {
    private int posX;
    private int posY;
    
    private String Texto;
    
    
    public Label(String texto,int x, int y) {
        this.posX=x;
        this.posY=y;
        this.Texto=texto;
    }

    public String getTexto() {
        return Texto;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }
    public void establecerPosicion(int x, int y){
        posX=x;
        posY=y;
    }
    
    
    
    
    
    
    
    
}
