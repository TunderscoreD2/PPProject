package de.daniel.meals;

public enum Size {


    Small(0.75f),
    Medium(1f),
    Large(1.5f);

    private float priceMultiplicator;
    Size(float multiplactor){
        priceMultiplicator = multiplactor;
    }
    public float getPriceMultiplicator(){
        return priceMultiplicator;
    }
}
