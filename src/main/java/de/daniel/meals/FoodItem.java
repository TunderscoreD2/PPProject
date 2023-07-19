package de.daniel.meals;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name="MEALS")
public class FoodItem {
    @NonNull
    @Id
    private String name;
    @NonNull
    private BigDecimal price;
    @ElementCollection(targetClass = Category.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="Categories")
    @Column(name="category",nullable = true)
    @NonNull
    private Collection<Category> categorys;

    @NonNull
    @CollectionTable(name = "Extras", joinColumns = @JoinColumn(name = "meal_name"))
    @Column(name = "extras",nullable = false)
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Extra> possibleExtras;


    @Transient
    private List<Extra> actualExtras = new ArrayList<>();
    @Transient
    private Size size;

    @Override
    public String toString() {
        return "FoodItem{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", categorys=" + categorys +
                ", possibleExtras=" + possibleExtras +
                ", actualExtras=" + actualExtras +
                ", size=" + size +
                '}';
    }
}


