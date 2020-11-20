package evolutionarycomputation;

/**
 *
 * @author Ziheng Cong
 */

public class Item {
    private Item[] item;
    private int VOLUMNE;
    private int BENEFIT;
    
    public Item(int volumne, int benefit){
        this.VOLUMNE = volumne;
        this.BENEFIT = benefit;
    }
    
    public Item(){
        this.item = item;
    }
    
    public Item[] getItem() {
        return item;
    }

    public void setItem(Item[] item) {
        this.item = item;
    }
    
    public int getVOLUMNE() {
        return VOLUMNE;
    }

    public void setVOLUMNE(int VOLUMNE) {
        this.VOLUMNE = VOLUMNE;
    }

    public int getBENEFIT() {
        return BENEFIT;
    }

    public void setBENEFIT(int BENEFIT) {
        this.BENEFIT = BENEFIT;
    }
    
}
