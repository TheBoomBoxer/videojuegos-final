/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.Game;
import org.jgap.*;
import org.jgap.impl.*;

/**
 *
 * @author James
 */
public class RacerFitnessFunction extends FitnessFunction {
    

    @Override
    protected synchronized double  evaluate(IChromosome a_subject) {
       // Ejecutamos el juego
       Game game = new Game((Integer) a_subject.getGene(0).getAllele(),(Integer)  a_subject.getGene(1).getAllele(),this);      
       game.start();
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(RacerFitnessFunction.class.getName()).log(Level.SEVERE, null, ex);
        }
       return game.getFitness();
    }
    
    
    
}
