package evolutionarycomputation;

import java.security.SecureRandom;

/**
 *
 * @author Ziheng Cong
 */

public class GeneticAlgorithm {
    private Item it;
    private int numItems; 
    private int elitismCount; // Number of elite individuals
    private int knapsackCapacity;
    private int populationSize; // Population size
    private final int GENERATION_COUNT;
    private Population[] Generations;
    private double mutationRate; // Mutation probability
    private double crossoverRate; // Crossover probability
    
    /**
     * Constructor of GeneticAlgorithm
     * 
     * @param populationSize
     *               The number of individuals in the population
     * @param mutationRate
     *               The mutation rate of the population
     * @param crossoverRate
     *               The crossover rate of the population
     * @param elitismCount
     *               The strongest individuals in the population
     * @param knapsackCapacity
     *               The size and constrain of the population
     * @param generationCount
     *               The count of the generation
     * @param it
     *               The instance of Item class which stores fitness & volumne
     * @param numItems
     *               The size of each individual's chromosome
     */
    public GeneticAlgorithm(int populationSize, double mutationRate,
                double crossoverRate, int elitismCount, int knapsackCapacity, int generationCount,Item it, int numItems) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.knapsackCapacity = knapsackCapacity; 
        this.GENERATION_COUNT = generationCount; 
        this.Generations = new Population[GENERATION_COUNT]; 
        this.it = it;
        this.numItems = numItems; 
    }
    
    /**
     * Initialize the population 
     * 
     * @param chromosomeLength
     *             The length of the individuals chromosome
     * @return  
     *          population The initial population generated
     */
    public Population initPopulation(int chromosomeLength){
        // Initialize population
        Population population = new Population(this.populationSize, this.numItems, this.knapsackCapacity, this.it);
        return population;
    }
    
    /**
     * Get the count of the generation
     * 
     * @return the count of the generation
     */
    public int getGenerationCount () {
         return this.GENERATION_COUNT;
    }
    
    /**
     * Traverse each individual to evaluate the fitness & volumne
     * 
     * @param population 
     */
    public void evalPopulation(Population population) {
        // Volumne
        population.calculateAndSetVolumne(it); //The update of Volumne of each population count on this
        // Fitness
        population.calculateAndSetPopulationFitness(it);
        // Individuals normalised fitness
        population.calculateAndSetIndividualsNormalisedFitness();
    }

    /**
     * Judgment of termination conditions.
     * 
     * When the generation reaches the maxGeneration or the fitness is Stable then 
     * the temination condition is ture
     * 
     * @param generationsCount
     *              The count of the generation
     * @param maxGeneration
     *              Max generation the generation can go through
     * @param population
     *              The population to select parent from
     * @return 
     *       boolean True if termination condition met otherwise it's false
     */
    public boolean isTerminationConditionMetOrNot(int generationsCount, int maxGeneration, Population population){
        if (generationsCount == maxGeneration-1 || isFitnessStable(population)) {
                    return true;
		}
		return false;
    }
    
    /**
     * Judge that if the fitness get stable
     * 
     * @param population
     *              The population to select parent from
     * @return 
     */
    public boolean isFitnessStable(Population population){
        Individual [] individuals = population.getIndividuals();
        int parent1 , parent2;  
        parent1 =individuals[0].getFitness(); 
        for (int populationIndex = 1; populationIndex < 10; populationIndex++) {
                    parent2 =individuals[populationIndex].getFitness(); 
                    if (parent1 != parent2) {
                            return false;
                }
        }        
        return true;
    }

    /**
     * Use Roulette wheel selection to select crossover individuals
     * 
     * @param population
     *              The population to select parent from
     * @return The individual selected as a parent
     */
    public Population selectParentPopulation(Population population) {	
            //Population popOne = new Population(this.populationSize, this.knapsackCapacity, this.numItems);
            Population popOne = new Population(this.populationSize, this.knapsackCapacity);
            // Get individuals from the initial population
	    Individual individuals[] = population.getIndividuals();
            Individual chosen = null;
	    //create rouletteWheel with random wedge sizes
            double populationFitness = population.getPopulationFitness(); 
                for (int i =0; i< population.populationSize; i++) { 
                    double rouletteWheelPosition = Math.random();
                    double spinWheel =0;
                    for (int j=0;  j<individuals.length; j++) {                  
                          spinWheel += individuals[j].getNormalizedFitness();
                          if ( spinWheel >= rouletteWheelPosition ){
                             chosen = individuals[j];
                             break;
                          }
                }
                if (chosen == null){
                   chosen = individuals[population.size()-1];
                }
                  popOne.setIndividual(i, chosen);
                  chosen = null;
                }   
                  return popOne;
    }
    
/**
 * Selects parent for crossover using tournament selection
 * 
 * @param population
 *              The population to select parent
 * @return The individual selected as a parent
 */
    public Individual selectParent(Population population){
         // Create tournament
        Population tournament = new Population(this.populationSize, this.knapsackCapacity);
        // Add random individuals to the tournament
        population.shuffle();
        // Randomly insert individuals into the tournament
        for (int i = 0; i < this.populationSize; i++) {
            Individual tournamentIndividual = population.getIndividual(i);
            tournament.setIndividual(i, tournamentIndividual);
        }
        // Return the best
        return tournament.getFittest(0);
    }
   
    /**
     * Add a population to the generation
     * 
     * @param offset
     * @param pop The population that be added 
     */
    public void addPopulationToGeneration(int offset, Population pop) {
        this.Generations[offset] = pop;
    }
    
    /**
     * Use two-points cross-over to apply crossover to population
     * 
     * @param population
     *              The population to apply crossover to
     * @return The new population
     */
    public Population crossoverPopuation(Population population) {
            // Create new population
            Population newPopulation = new Population(population.size(), this.knapsackCapacity);
            // Loop over current population by fitness
            for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
                    Individual parent1 = population.getFittest(populationIndex);
                    if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
                            // Initialize offspring
                            Individual offspring = new Individual(parent1.getChromosomeLength(), this.knapsackCapacity);
                            // Find second parent
                            Individual parent2 = this.selectParent(population);
                            
                            // Use two-points cross-over
                            int first, second, idx, temp;
                            SecureRandom rand = new SecureRandom();
                            first = (int) (Math.random() * (parent1.getChromosomeLength() + 1)); 
                            second = (int) (Math.random() * (parent1.getChromosomeLength() + 1));
                            
                            // Else-if is used for incase the first point equals to the second
                            if (first > second) { 
                               temp = first; 
                               first =second;
                               second =temp; 
                            } else if (first == second) {
                                first = (int) (Math.random() * (parent1.getChromosomeLength() + 1));
                                second = (int) (Math.random() * (parent1.getChromosomeLength() + 1));
                            } else if (first == second) {
                                first = (int) (Math.random() * (parent1.getChromosomeLength() + 1));
                                second = (int) (Math.random() * (parent1.getChromosomeLength() + 1));
                            }  
                            // Create offSpring to get genes from parents
                            Individual offSpring = new Individual(parent1.getChromosomeLength(), this.knapsackCapacity);
                            
                            // Apply two-points cross-over to the individual
                            for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
                                if (geneIndex <= first) {
                                    offSpring.setGene(geneIndex, parent1.getGene(geneIndex));
                                }else if (geneIndex > first && geneIndex < second) {
                                    offSpring.setGene(geneIndex, parent2.getGene(geneIndex));
                                }else if (geneIndex >= second) {
                                    offSpring.setGene(geneIndex, parent1.getGene(geneIndex));
                                }
                            }
                            // Add new individual to the new population
                            newPopulation.setIndividual(populationIndex, offSpring);
                    } else {
                            // Add individual to new population without applying crossover
                            newPopulation.setIndividual(populationIndex, parent1);
                    }
            }
            return newPopulation;
    }

    /**
     * Apply mutation to population
     * 
     * @param population
     *              The population to apply mutation to
     * @return The mutated population
     */
    public Population mutatePopulation(Population population){
        // Initialize new population
        Population newPopulation = new Population(this.populationSize, this.knapsackCapacity);
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            // Loop over current population by fitness
            Individual individual = population.getFittest(populationIndex);
            // Loop over individual's genes
            for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
                // Skip mutation if this is an elite individual
                if(populationIndex>this.elitismCount){
                    // Determine whether the gene is mutated
                    if(this.mutationRate>Math.random()){
                        int newGene = 1;
                        if(individual.getGene(geneIndex)==1){
                            newGene = 0;
                        }
                        // Mutate gene
                        individual.setGene(geneIndex, newGene);
                    }
                }
            }
            // Add individual to population
            newPopulation.setIndividual(populationIndex, individual);
        }
        // Return mutated population
        return newPopulation;
    }
    
    /**
     * Get a generation
     * 
     * @param offset
     * @return The population of the generation you choose
     */
    public Population GetAGeneration(int offset) {
        return this.Generations[offset];
    }    
}
