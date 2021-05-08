/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bomberman.Sonido;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 *
 * @author jdavi
 */
public class Audio {
    private String rutaArchivo;
    private String nombreArchivo;
    private Clip reproductor;
    private AudioInputStream entrada;
    private boolean enBucle;
    private boolean Activo;
    private float volumen;
    private FloatControl controlVolumen;
    
    public Audio(String nombreArchivo,boolean enBucle,float volumen){
        this.rutaArchivo="Multimedia";
        this.volumen=volumen;
        this.nombreArchivo= nombreArchivo;
        this.enBucle=enBucle;
        try {
           InputStream in = new FileInputStream(rutaArchivo+"/"+nombreArchivo);
           entrada= AudioSystem.getAudioInputStream(new BufferedInputStream(in));
           reproductor= AudioSystem.getClip();
           reproductor.open(entrada);
        } catch (Exception e){
             System.out.println(e.getMessage());
        }  
        controlVolumen=(FloatControl)reproductor.getControl(FloatControl.Type.MASTER_GAIN);
        
    }
    public synchronized void establecerActivo(){
        Activo=true;
    }
    public synchronized boolean estaActivo(){
        return Activo;
    }
    public synchronized void reproducir(){
        if (Activo) {
            try {
                reproductor.setFramePosition(0);
                if (enBucle) {
                    reproductor.loop(Clip.LOOP_CONTINUOUSLY);
                }
                controlVolumen.setValue(volumen);
                reproductor.start();
                
            } catch (Exception e) {
                 System.out.println(e.getMessage());
            }
            Activo=false;
           
        }
      
    }
}
