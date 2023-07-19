package de.daniel.barista;

import de.daniel.card.Menu;
import de.daniel.meals.Category;
import de.daniel.meals.Extra;
import de.daniel.meals.FoodItem;
import de.daniel.meals.Size;
import de.daniel.order.MoneyCalculator;
import de.daniel.order.Order;
import de.daniel.utils.PrintUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Barista {

    private final Menu menu;
    private final Order o;
    private final Scanner scanner;
    private List<FoodItem> foodItems;
    public Barista(Menu m) {
        scanner = new Scanner(System.in);
        foodItems = m.listMeals();
        this.menu = m;
        this.o = new Order();
    }
    public void handleCustomer() {
        greetCustomer();
        askForName();
        PrintUtil.printMenu(foodItems);
        System.out.println("");
        while (true) {
            System.out.println("Druecke (S) zum Suchen, (B) zum bestellen,(Z) zum zahlen, (P) um die karte erneut anzuzeigen");
            String s = scanner.nextLine().toUpperCase();
            switch (s) {
                case "S","SUCHEN" -> {
                    handleSearch();
                    continue;
                }
                case "B","BESTELLEN" -> {
                    handleOrder();
                    continue;
                }
                case "P","PRINT" -> {
                    PrintUtil.printMenu(foodItems);
                    continue;
                }
                case "Z","ZAHLEN" -> {
                    handlePayment();
                    return;
                }
            }
            System.out.println("Eingabe ungueltig, versuche es nocheinmal");
        }
    }

    private void handleSearch() {
        System.out.println("Wonach moechtst du suchen?");
        System.out.println("Du kannst nach mehreren Kategorien suchen, in dem du sie mit \",\" trennst");
        System.out.println("Schreibe \"reset\" um den filter zurückzusetzten");
        String s = scanner.nextLine();
        if (s.equalsIgnoreCase("reset")) {
            System.out.println("Deine SuchOptionen wurden zurückgesetzt");
            foodItems = menu.listMeals();
            PrintUtil.printMenu(foodItems);
        } else {
            List<String> strings = Arrays.asList(s.split(","));
            int dirty = 0;
            for (String s2 : strings) {
                try {
                    foodItems = menu.filterList(foodItems, Category.valueOf(s2.toUpperCase()));
                } catch (Exception e) {
                    System.out.println("Tut mir leid, wir haben keine " + s2);
                    dirty++;
                }
            }
            if (strings.size() - dirty > 0)
                PrintUtil.printMenu(foodItems);
        }

        System.out.println("Moechtest du sonst noch etwas tun?");
    }

    private void handleOrder() {
        System.out.println("Was möchtest du bestellen");
        FoodItem toOrder = untilValid(
                 (x)->menu.listMeals().stream().anyMatch((b)->b.getName().equalsIgnoreCase(x)) //check
                ,(x)->System.out.println("Wir haben " + x+ " nicht auf der karte, bestelle bitte etwas anderes") //if doesnt match
                ,(x)->menu.listMeals().stream().filter((b)->b.getName().equalsIgnoreCase(x)).findFirst().get()); //transform to result
        if (toOrder.getPossibleExtras().size() > 0) {
            System.out.println("Möchtest du Extras? (Y/N)");
            askForExtras(toOrder);
        }
        toOrder.setSize(askForSize());
        System.out.println("Wie oft moechtest du das bestellen");
        int amount = askForAmount();
        System.out.println("amount");
        for(int i=0;i<amount;++i)
            this.o.foodItems.add(toOrder);

        System.out.println("Moechtest du sonst noch etwas tun?");
    }

    private void askForExtras(FoodItem item) {
        String res = scanner.nextLine();
        if ( res.equalsIgnoreCase("N")) {
            return;
        }
        if (res.equalsIgnoreCase("Y")) {
            System.out.println("Diese extras sind möglich");
            PrintUtil.printExtras(item);
            System.out.println("Liste die extras die du haben möchtest");
            String s = untilValid((x) -> !x.contains(",,")&&x.length()>0, (x) -> System.out.println("Invalide eingabe, versuche es erneut"), (x) -> x);
            for (String s2 : s.split(",")) {
                boolean found = false;
                for(Extra extra : item.getPossibleExtras()){
                    if(extra.getName().equalsIgnoreCase(s2)){
                        item.getActualExtras().add(extra);
                        found=true;
                        break;
                    }
                }if(!found)
                System.out.println("Das extra " + s2 + " gibt es nicht, deswegen wird es ignoriert");
            }
            System.out.println("Deine Extras wurden hinzugefügt");
            return;
        }
        System.out.println("Du kannst nur mit Y/N antworten!, versuche es nocheinmal");
        askForExtras(item);
    }

    private Size askForSize() {
        System.out.print("Welche groeße darf es sein? ");
        String s = "(";
        for (Size size : Size.values()) {
            s += size.name() + ",";
        }
        s = s.substring(0, s.length() - 1);
        System.out.println(s + ")");
        return untilValid((x) -> {
            for (Size lamdasize : Size.values()) {
                if (lamdasize.name().equalsIgnoreCase(x)) return true;
            }
            return false;
        }, (x) -> System.out.println(x + " ist keine valide Groesse"), (x) -> {
            for (Size lamdasize : Size.values()) {
                if (lamdasize.name().equalsIgnoreCase(x)) return lamdasize;
            }
            return null;
        });
    }

    private void handlePayment() {

        BigDecimal price = o.calculatePrice();
        System.out.println("Das macht " + price + " €");
        System.out.println("Am besten bezahlst du das mit "  + MoneyCalculator.getBest(price));

        System.out.println("Danke fürs Einkaufen :)");
        System.exit(0);
    }

    private void greetCustomer() {
        System.out.println("Willkommen im MPP-Cafe");
    }

    private void askForName() {
        System.out.println("Wie heisst du?");
        String name = untilValid((x) -> x.length() > 0 && x.length() < 20,
                (x) -> {
                    if (x.length() == 0) {
                        System.out.println("Der name darf nicht leer gelassen werden");
                    } else {
                        System.out.println("Ich glaube nicht das du " + x + "heißt");
                    }
                }, (x) -> x);
        System.out.println("Hallo " + name);
        o.customerName = name;

    }

    private int askForAmount(){
        return untilValid(x->{
            int i;
            try{
                i = Integer.parseInt(x);
                if(i <= 0 || i > 100){
                    return false;
                }
                return true;
            }catch (Exception e){}return false;
        },(x)->System.out.println("Diese Anzahl ist nicht erlaubt"), Integer::parseInt);
    }

    private <T> T untilValid(Predicate<String> isValid, Consumer<String> errormessage, Function<String, T> transformer) {
        while (true) {
            String t = scanner.nextLine();
            if (isValid.test(t)) {
                return transformer.apply(t);
            } else {
                errormessage.accept(t);
            }
        }

    }


}
