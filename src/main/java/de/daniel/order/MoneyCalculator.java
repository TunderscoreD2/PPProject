package de.daniel.order;

import java.math.BigDecimal;

public class MoneyCalculator {


    public static String getBest(BigDecimal price){
        long amount = price.multiply(new BigDecimal("100")).longValue();
        if(amount <=0){
            throw new IllegalArgumentException("amount not valid");
        }
        long[] possibleAmounts = {50000,20000,10000,5000,2000,1000,500,200,100,50,20,15,10,5,2,1};
        long[] toPay = new long[possibleAmounts.length];
        for(int i = 0; i < possibleAmounts.length;++i){
            long fits = amount/possibleAmounts[i];
            toPay[i]=fits;
            amount-=fits*possibleAmounts[i];
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < toPay.length;++i){
            if(toPay[i] != 0){
                stringBuilder.append(toPay[i]);
                stringBuilder.append("*");
                stringBuilder.append(possibleAmounts[i]/100f);
                stringBuilder.append(possibleAmounts[i] > 200 ? " Schein"  :" Muenze");
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

}
