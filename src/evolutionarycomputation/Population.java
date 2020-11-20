package evolutionarycomputation;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author Ziheng Cong
 */

public class Population {
    private Item it;
    private int numItems;
    final int populationSize; // =numofIndividual
    final int knapsackCapacity; 
    public Individual[] population; // Population: the collection of all individuals
    public double populationFitness = -1; // Population fitness
    
    /**
     * Initializes blank population of individuals
     * 
     * @param populationSize
     *             The number of individuals in the population
     * @param knapsackCapacity 
     *             The size and constrain of the population
     */            
    public Population(int populationSize, int knapsackCapacity){
        this.populationSize = populationSize;
        this.knapsackCapacity = knapsackCapacity;
        this.population = new Individual[populationSize];
    }
    
    /**
     * Initializes population of individuals
     * 
     * @param populationSize
     *            The number of individuals in the population
     * @param numItems
     *            The size of each individual's chromosome
     * @param knapsackCapacity
     *            The size and constrain of the population
     * @param it 
     *            The instance of Item class which stores fitness & volumne
     */
    public Population(int populationSize,int numItems, int knapsackCapacity, Item it){ 
       this.numItems = numItems; 
       this.knapsackCapacity = knapsackCapacity; 
       this.populationSize = populationSize;
       this.population = new Individual[populationSize];
       // Create every individual in the population 
       for (int individualCount = 0; individualCount < populationSize; individualCount++) {
            // Create an individual, initializing its chromosome to the given length
            this.population[individualCount] = new Individual(this.numItems, knapsackCapacity);
            // Set fitness & volumne
            this.population[individualCount].calculateAndSetVolumne(it); 
            this.population[individualCount].calculateAndSetFitness(it);  
        }
        // Calculate the sum of individual fitness
        int popFitness =0;
        for (int i=0; i < this.populationSize; i++ ){
            popFitness+= this.population[i].getFitness();
         }
        this.populationFitness = popFitness;
        // Calculate the normalized fitness for each individual
        for (int i=0; i < this.populationSize; i++){
            this.population[i].normalizedFitness = (this.population[i].getFitness()/this.populationFitness);
        }
        // Calculate the sum of normalized fitness
        for (int i=0; i < this.populationSize; i++) {
            for (int j=0; j <= i; j++)
                this.population[i].cumNormFitness  += this.population[j].normalizedFitness;
        }
    }
    
    /**
     * Get individuals from the population
     * 
     * @return Individuals in population
     */
    public Individual[] getIndividuals(){
        return this.population;
    }
    
    /**
     * Find an individual in the population by its fitness
     * 
     * @param offset
     *            The offset of the individual you want, sorted by fitness. 0 is
     *            the strongest, population.length - 1 is the weakest.
     * @return individual Individual at offset
     */
    public Individual getFittest(int offset){
        // Sort the individuals in the population according to their fitness
        Arrays.sort(this.population, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                if(o1.getFitness()>o2.getFitness()){
                    return -1;
                }
                else if(o1.getFitness()<o2.getFitness()){
                    return 1;
                }
                return 0;
            }        
        });
        // Returns the individual in the sorted offset
        return this.population[offset];
    }
    
    /**
     * Get population's group fitness
     * 
     * @return populationFitness The population's total fitness
     */
    public double getPopulationFitness() {
        return this.populationFitness; 
    }
    
    /**
     * Set population's group fitness
     * 
     * @param populationFitness
     *              The population's total fitness
     */
    public void setPopulationFitness(double populationFitness) {
        this.populationFitness = populationFitness;
    }
    
    /**
     * Calculate and set population fitness
     * 
     * @param it 
     *          Store the fitness & volumen of each individual
     */
    public void calculateAndSetPopulationFitness(Item it) {
            double popfitness =0.0;
            for (int i=0; i< this.populationSize; i++) {
                    this.population[i].calculateAndSetFitness(it);
			popfitness += this.population[i].getFitness();
            }
            this.setPopulationFitness(popfitness);
    }  
    
    /**
     * Run in GA and update each time volume of each chromosome
     * 
     * @param it
     *          Store the fitness & volumen of each individual
     */
    public void calculateAndSetVolumne(Item it) {
            for (int i=0; i< this.populationSize; i++) {
                    this.population[i].calculateAndSetVolumne(it);
            }
    } 
    
    /**
     * Calculate and set individuals normalised fitnes
     * 
     */
    public void calculateAndSetIndividualsNormalisedFitness()
    {
        for (int i=0; i < this.populationSize; i++) 
            {
               this.population[i].normalizedFitness = (this.population[i].getFitness()/this.populationFitness);
            }
    }

    /**
     * Get population's size
     * 
     * @return The population's size
     */
    public int size(){
        return this.populationSize;
    }
    
    /**
     * Set individual at offset
     * 
     * @param offset
     * @param individual the individual is being set
     */
    public void setIndividual(int offset,Individual individual){
        population[offset] = individual;
    }
    
    /**
     * Get individual at offset
     * 
     * @param offset
     * @return individual the individual that you want to get from population
     */
    public Individual getIndividual(int offset){
        return population[offset];
    }
    
    /**
     * @shuffle Random shuffle
     */
    public void shuffle() {
        Random rnd = new Random();
        for (int i = population.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Individual a = population[index];
            population[index] = population[i];
            population[i] = a;
        }
    }
    
    /**
     * This method is to display all the values of the individuals in a matrix format. 
     * 
     */
    @Override
	public String toString() {
            StringBuilder popString = new StringBuilder();
            for (int i=0; i< this.populationSize; i++) {
                    popString.append(this.population[i].toString());
                    popString.append("  ");
                    popString.append(String.format("Fitness: %d   Volumne: %d \n", 
                                population[i].getFitness(), this.population[i].getVolume(), this.population[i].normalizedFitness));
            }
            return popString.toString();
        }
}
