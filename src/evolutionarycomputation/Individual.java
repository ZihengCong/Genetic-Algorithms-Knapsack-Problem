package evolutionarycomputation;

/**
 *
 * @author Ziheng Cong
 */

public class Individual {
    private Item it; 
    private Item[] items; 
    final int knapsackCapacity;
    final int CHROMOSOME_LENGTH;
    private int numItems; //=chromosome
    private int fitness = -1;
    private int volumne = -1;
    double normalizedFitness = 0.0 ;
    double cumNormFitness = 0.0;
    private int[] chromosome;//Define a chromosome
 
    /**
     * Initializes individual with specific chromosome
     * 
     * @param chromosome
     *             The chromosome to give individual
     * @param knapsackCapacity 
     *             The size and constrain of the population
     */            
    public Individual(int[] chromosome, int knapsackCapacity) {
            this.chromosome = chromosome;
            this.CHROMOSOME_LENGTH = chromosome.length; 
            this.knapsackCapacity = knapsackCapacity;  
    }
    
    /**
     * Initializes random individual.
     *
     * @param chromosomeLength
     *              The chromosome to give individual
     * @param knapsackCapacity 
     *              The size and constrain of the population
     */
    public Individual(int chromosomeLength, int knapsackCapacity){
        this.knapsackCapacity = knapsackCapacity; 
        this.CHROMOSOME_LENGTH = chromosomeLength; 
        this.chromosome = new int[CHROMOSOME_LENGTH];
        for (int gene = 0; gene < CHROMOSOME_LENGTH; gene++) { 
            if(0.5<Math.random()){
                this.setGene(gene, 1);
            }
            else {
                this.setGene(gene, 0);
            }
        }
    }
    
    /**
     * Gets individual's chromosome
     * 
     * @return The individual's chromosome
     */
    public int[] getChromosome(){
        return this.chromosome;
    }
    
    /**
     * Gets individual's chromosome length
     * 
     * @return The individual's chromosome length
     */
    public int getChromosomeLength(){
        return this.chromosome.length;
    }
    
    /**
     * Set gene at offset
     * 
     * @param offset
     * @param gene
     */
    public void setGene(int offset,int gene){
        this.chromosome[offset] = gene;
    }
    
    /**
     * Get gene at offset
     * 
     * @param offset
     * @return gene
     */
    public int getGene(int offset){
        return this.chromosome[offset];
    }
    
    /**
     * Calculate and set  individual's fitness
     * 
     * @param it 
     *         Store the fitness & volumen of each individual
     */
    public void calculateAndSetFitness(Item it){ 
        this.fitness = 0;
        this.items = it.getItem(); 
        for (int gene = 0; gene < this.chromosome.length; gene++) {
            if (this.getVolume() <= this.knapsackCapacity ) {
                // Multiply the fitness and item to get the fitness of each individual
                this.fitness = this.fitness + this.chromosome[gene] * items[gene].getBENEFIT();
            } else{
                // If the volume of the individual exceed its capacity then the fitness is 0
                this.fitness = 0;
            }
        }
        this.setFitness(this.fitness);
    }
    
    /**
     * Calculate and set individual's volumne
     * 
     * @param it 
     *         Store the fitness & volumen of each individual
     */
    public void calculateAndSetVolumne(Item it){  
        this.volumne = 0;
        this.items = it.getItem();
        for (int gene = 0; gene < this.chromosome.length; gene++) {
            // Multiply the volumne and item to get the volumne of each individual
            this.volumne = this.volumne + this.chromosome[gene] * items[gene].getVOLUMNE(); 
        }
        this.setVolume(this.volumne);
    }
    
    /**
     * Save individual's fitness
     * 
     * @param fitness 
     */
    public void setFitness(int fitness){
        this.fitness = fitness;
    }
    
    /**
     * Gets individual's fitness
     * 
     * @return The individual's fitness
     */
    public int getFitness(){
        return this.fitness;
    }
    
    /**
     * Save individual's volumne
     * 
     * @param volumne 
     */
    public void setVolume(int volumne){
        this.volumne = volumne;
    }
    
    /**
     * Gets individual's volumne
     * 
     * @return The individual's volumne
     */
    public int getVolume(){
        return this.volumne;
    }
    
    /**
     * Get individual's normalised fitness
     * 
     * @return normalizedFitness
     */
    public double getNormalizedFitness() {                          
        return this.normalizedFitness;
    }
    
    /**
     * Get individual's cumulative normalised fitness
     * 
     * @return cumNormFitness
     */
    public double getCumNormFitness() {
        return this.cumNormFitness;
    }
    
    /**
     * Set individual's normalised fitness
     * 
     * @param normalizedFitness 
     */
    public void setNormalizedFitness(double normalizedFitness) {
        this.normalizedFitness = normalizedFitness;
    }
    
    /**
     * Set individual's cumulative normalised fitness
     * 
     * @param cumFitness 
     */
    public void setCumNormFitness(double cumFitness) {
       this.cumNormFitness = cumFitness;                             
    }
    
    /**
     * Display the chromosome as a string.
     * 
     * @return string representation of the chromosome
     */
    public String toString(){
        String output = "";
        for (int gene = 0; gene < this.chromosome.length; gene++) {
            output += this.chromosome[gene] + " "; 
        }
        return output;
    }
}
