/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import com.jme3.system.AppSettings;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.Game;
import org.jgap.*;

/**
 *
 * @author James
 */
public class RacerFitnessFunction extends FitnessFunction {

    private static Game g;
    private static Semaphore s;

    @Override
    protected double evaluate(IChromosome a_subject) {
        // Ejecutamos el juego
        double resdoubl;
        if (g == null) {
            s = new Semaphore(0, true);
            g = new Game((Integer) a_subject.getGene(0).getAllele(), (Integer) a_subject.getGene(1).getAllele(), s);

            AppSettings gameSettings = null;
            gameSettings = new AppSettings(false);          
            gameSettings.setUseInput(true);
            gameSettings.setResolution(1280, 720);
            gameSettings.setFullscreen(false);
            gameSettings.setVSync(false);
            gameSettings.setTitle("Stellar Conquest");
            gameSettings.setUseInput(true);
            gameSettings.setFrameRate(500);
            gameSettings.setSamples(0);
            gameSettings.setRenderer("LWJGL-OpenGL2");
            gameSettings.setVSync(true);
            g.setSettings(gameSettings);
            g.setShowSettings(false);
            g.start();

        } else {
            g.setAcceleration((Integer) a_subject.getGene(0).getAllele());
            g.setSteerAngle((Integer) a_subject.getGene(1).getAllele());
          //  System.out.println("RESETEO");
            g.reset();
        }

        try {
            s.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(RacerFitnessFunction.class.getName()).log(Level.SEVERE, null, ex);
        }

//        System.out.println("HOLA");
//        Semaphore s = new Semaphore(0, true);
//        int accel = (Integer) a_subject.getGene(0).getAllele();
//        int angle = (Integer) a_subject.getGene(1).getAllele();
//
//        Game game = new Game(accel, angle, s);
//
//        AppSettings gameSettings = null;
//        gameSettings = new AppSettings(false);
//        gameSettings.setUseInput(true);
//      
//
//        game.setSettings(gameSettings);
//        game.setShowSettings(false);
//        game.start();
//
//        try {
//            s.acquire();
//        } catch (InterruptedException ex) {
//            Logger.getLogger(RacerFitnessFunction.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        double res = game.getFitness();
//        game.stop();
//        game.
//        game = null;
//        
//        System.out.println(res);
        return g.getFitness();
    }

}
