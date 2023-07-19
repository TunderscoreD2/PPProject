package de.daniel.order;

import de.daniel.meals.Extra;
import de.daniel.meals.FoodItem;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Order {

    public String customerName;

    public Order(){
        foodItems = new ArrayList<>();
    }

    public ArrayList<FoodItem> foodItems;

    public BigDecimal calculatePrice(){
        BigDecimal price = BigDecimal.valueOf(0);
        for(FoodItem f : foodItems){
            price =price.add(f.getPrice().multiply(BigDecimal.valueOf(f.getSize().getPriceMultiplicator())));
            for(Extra extra : f.getActualExtras()){
                price=price.add(extra.getPrice());
            }
        }
        return price;
    }


}
