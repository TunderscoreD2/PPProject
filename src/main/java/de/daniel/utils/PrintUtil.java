package de.daniel.utils;

import de.daniel.meals.Category;
import de.daniel.meals.Extra;
import de.daniel.meals.FoodItem;
import dnl.utils.text.table.TextTable;

import java.util.List;

public class PrintUtil {

    public static void printExtras(FoodItem item){
        Object[][] tableData = new Object[item.getPossibleExtras().size()][2];
        int index = 0;
        for(Extra extra : item.getPossibleExtras()){
            tableData[index][0] = extra.getName();
            tableData[index++][1] = extra.getPrice();
        }
        TextTable tt = new TextTable(new String[]{"Extra","Price"},tableData);
        tt.printTable();
    }



     public static void printMenu(List<FoodItem> foodItems){
        Object[][] tableData = new Object[foodItems.size()][4];
        int index = 0;
        for(FoodItem item : foodItems){
            tableData[index][0] = item.getName();
            //Api kommt mit € nicht klar...
            tableData[index][1] = item.getPrice()+" Euro ";
            if(item.getPossibleExtras().isEmpty()){
                tableData[index][2] ="Keine";
            }else{
                tableData[index][2] ="";
                for(Extra extra : item.getPossibleExtras()){
                    tableData[index][2] +=extra.getName() +" ";
                }
            }
            tableData[index][3] ="";
            for(Category category : item.getCategorys()){
                tableData[index][3] +=category.name() +" ";
            }
            index++;
        }
        TextTable tt = new TextTable(new String[]{"Meal","Price","Extras","Category"},tableData);
        tt.printTable();
    }


}
