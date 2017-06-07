/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.io.FileReader;
import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.TwoWayMutationOperator;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

/**
 *
 * @author James
 */
public class Learning {

    public static void main(String[] args) throws InvalidConfigurationException {
        Configuration conf = new DefaultConfiguration();
        // Add custom mutation operator
        conf.getGeneticOperators().clear();

        TwoWayMutationOperator mutOp = new TwoWayMutationOperator(conf, 6);
        conf.addGeneticOperator(mutOp);

        conf.addGeneticOperator(new CrossoverOperator(conf));
        conf.setPreservFittestIndividual(true);
        conf.setKeepPopulationSizeConstant(false);

        RacerFitnessFunction myFunc = new RacerFitnessFunction();
        System.out.println("Ha creado la fitness function.");

        conf.setFitnessFunction(myFunc);

        Gene[] sampleGene = new Gene[2];

        sampleGene[0] = new IntegerGene(conf, 100, 500);
        sampleGene[1] = new IntegerGene(conf, 3, 15);

        Chromosome sampleChromosome = new Chromosome(conf, sampleGene);
        conf.setSampleChromosome(sampleChromosome);
        conf.setPopulationSize(10);

        Genotype population = Genotype.randomInitialGenotype(conf);

        for (int i = 0; i < 25; i++) {
            System.out.println("Iteracion " + i + ", poblacion " + population.getPopulation().size());
            population.evolve();
        }

        IChromosome bestSolutionSoFar = population.getFittestChromosome();

        System.out.println("The best solution contained the following: ");
        System.out.println("fitness " + bestSolutionSoFar.getFitnessValue());
        System.out.println("aceleracion " + bestSolutionSoFar.getGene(0));
        System.out.println("steer " + bestSolutionSoFar.getGene(1));
        System.out.println("POR FIN");
    }

}
