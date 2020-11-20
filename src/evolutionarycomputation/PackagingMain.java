package evolutionarycomputation;

/**
 *
 * @author Ziheng Cong
 */

public class PackagingMain {
    public static int maxGenerations = 50; //initialize to given generations
    public static void main(String[] args) {
        int numItems = 10; //initialize number of items
        int knapsack_Capacity = 40;
        int populationSize  = 10;
        int elitism_count = 1;
        double mutationRate = 0.1;
        double crossoverRate = 0.7;

        System.out.printf("int maxGenerations = %d;\n" 
                            + "int numItmes = %d;\n"
                            + "int populationSize = %d;\n" 
                            + "double mutationRate = %.2f;\n"
                            + "double crossoverRate = %.2f;\n"
                            + "double constraint = %d;\n"
                            + "int elitism_count = %d;\n" 
                            ,maxGenerations, numItems,populationSize, mutationRate,
                                      crossoverRate, knapsack_Capacity, elitism_count);
        
        // Generate random Volumne & Benefit for Item
        Item[] items = new Item[numItems]; // Create a new array of size numItems
        Item it = new Item();// Instantiate the Item Class as it
        // Loop to create Items with random Volumne and Benefit.
        for (int itemIndex = 0; itemIndex < numItems; itemIndex++) {
            int volumne = (int) (10 * Math.random()+1); // <= 10
            int benefit = (int) (10 * Math.random()+1); // <= 10
            items[itemIndex] = new Item(volumne, benefit); 
        }
        it.setItem(items); // Save to Item class
        Item[] items1 = it.getItem(); // Import the stored in it into items1
        
        // Transfer the Item instance "it" which store volumne & benefit to other class
        Individual indi = new Individual(numItems, knapsack_Capacity);
        indi.calculateAndSetFitness(it);
        indi.calculateAndSetVolumne(it);
        
        Population bestPopulation = new Population(populationSize,numItems, knapsack_Capacity,it);
        
        // Intial GA object
        GeneticAlgorithm ga = new GeneticAlgorithm(populationSize, mutationRate, 
                            crossoverRate, elitism_count, knapsack_Capacity, maxGenerations, it, numItems); 

        // Initialize population
        Population one = ga.initPopulation(numItems);                                                       
        System.out.println ("\nInitial \n" + one.toString());
        System.out.println ("       Population fitness: " + one.getPopulationFitness()/one.populationSize + "\n");

        // Evaluate population
        ga.evalPopulation(one);

        // Keep track of current generation
	int generationCount = 0;

        // Add population to generations
        ga.addPopulationToGeneration (generationCount, one);

        // Start evolution loop
        while (ga.isTerminationConditionMetOrNot(generationCount, maxGenerations, bestPopulation) == false) {
                    Population GenNext = ga.GetAGeneration(generationCount);
                    
                    // Apply Select Paren Population
                    GenNext = ga.selectParentPopulation(GenNext);

                    // Apply crossover    
                    GenNext = ga.crossoverPopuation(GenNext); 

                    // Apply mutation
                    GenNext = ga.mutatePopulation(GenNext);
                    
                    // Evaluate population
                    ga.evalPopulation(GenNext);   
                    
                    // Increment the current generation
                    generationCount++;
                    
                    // Store the latest Population in Generation
                    ga.addPopulationToGeneration (generationCount, GenNext);   

                    // Display appropriate output for each iteration
                    bestPopulation = GenNext;// Store the best population
                    System.out.println ("Current Population:\n" + GenNext.toString());
                    System.out.println("Best solution so far: " + GenNext.getFittest(0).toString() 
                                                        +"  Fit: " + GenNext.getFittest(0).getFitness()
                                                            +"  Vol: " + GenNext.getFittest(0).getVolume());
                    System.out.println("Population fitness:  " + GenNext.getPopulationFitness()/GenNext.populationSize + "\n");
	}
        
        // Display results
        System.out.println("BEST POPULATION");
        System.out.println(bestPopulation.toString());
        System.out.println("BEST POPULATION FITNESS: " +  bestPopulation.getFittest(0).getFitness());
        System.out.println("BEST SOLUTION: " + bestPopulation.getFittest(0).toString() + ": " 
                                        + bestPopulation.getFittest(0).getFitness() + " " + bestPopulation.getFittest(0).getVolume());
    } 
}
