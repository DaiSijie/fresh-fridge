package starthack.fridgetogo;

/*
 *	Author:      Gilbert Maystre
 *	Date:        Mar 18, 2017
 */

public class Classifier {

    private Double[] lastDish;
    private boolean providedFeedback = false;
    
    public Classifier(){

    }
    
    public boolean feedDish(Double[] dish){
        this.lastDish = dish;
        providedFeedback = false;
        return (f(dish) <= 0)? false : true;
    }
    
    
    /**
     * @param actual Give true if the person like the dish, else false
     */
    public void provideFeedback(boolean actual){
        if(!providedFeedback){
            providedFeedback = true;
            double guess = f(lastDish);
            double truth = (actual)? 1 : 0 ;            
            for(int j = 0; j < Database.getw().length; j++){
                Database.getw()[j] = Database.getw()[j] + (truth - guess) * lastDish[j];
            }
        }
    }
    
    private double f(Double[] input){
        double sum = 0;
        for(int i = 0; i < 6; i++)
            sum += input[i] * Database.getw()[i];
           
        sum -= 0.5;
        return (sum < 0)? 0 : 1;
    }

    
    /**
     * Grades the dish with the current estimator
     * 
     * @param dish the dish to grade
     * @return a grade. If Negative, the user does not want dish if positive, the user might be interested. The larger
     * The norm, the larger the confidence the prediction is.
     */
    public double grade(Double dish[]){
        double sum = 0;
        for(int i = 0; i < 6; i++)
            sum += dish[i] * Database.getw()[i];
        sum -= 0.5;
        return sum;
    }
    
    
}
