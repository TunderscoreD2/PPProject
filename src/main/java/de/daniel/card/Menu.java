package de.daniel.card;

import de.daniel.meals.Category;
import de.daniel.meals.Extra;
import de.daniel.meals.FoodItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Menu {

    private Session dbSession;

    public Menu(){
      setupConnection();
      initMenu();
    }

    //Simulate DB because of inmemory db
    private void initMenu(){

        addFood(new FoodItem("Pommes", BigDecimal.valueOf(15), Arrays.asList(Category.MEAL,Category.VEGETARIAN),Arrays.asList(new Extra("Ketchup",BigDecimal.valueOf(0.5)),new Extra("Mayo",BigDecimal.valueOf(0.5)))));
        addFood(new FoodItem("Kaffee", BigDecimal.valueOf(4), Arrays.asList(Category.DRINK,Category.WARM_DRINK),Arrays.asList(new Extra("Zucker",BigDecimal.valueOf(0.5)),new Extra("Milch",BigDecimal.valueOf(1.5)))));
        addFood(new FoodItem("Kuchen", BigDecimal.valueOf(9), Arrays.asList(Category.DESSERT),new ArrayList<>()));
        addFood(new FoodItem("Schnitzel", BigDecimal.valueOf(27), Arrays.asList(Category.MEAL),Arrays.asList(new Extra("Kartoffelsalat",BigDecimal.valueOf(1.5)),new Extra("Preiselbeeren",BigDecimal.valueOf(1.5)))));
        addFood(new FoodItem("Rum", BigDecimal.valueOf(10), Arrays.asList(Category.DRINK,Category.COLD_DRINK,Category.ALCOHOLIC_DRINK),Arrays.asList(new Extra("Eiswuerfel",BigDecimal.valueOf(1.5)))));

    }

    public void terminateConnection(){
        dbSession.close();
    }

    private void setupConnection(){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        dbSession = sessionFactory.openSession();
    }



    public void addFood(FoodItem item){
        Transaction t = dbSession.beginTransaction();
        dbSession.persist(item);
        t.commit();
    }

    public List<FoodItem> listMeals(){
        return  dbSession.createQuery(" FROM FoodItem ", FoodItem.class).list();
    }

    public List<FoodItem> filterList(List<FoodItem> list,Category c){
        //probably better to implement in sql
        return list.stream().filter(x->x.getCategorys().contains(c)).collect(Collectors.toList());
    }


}
