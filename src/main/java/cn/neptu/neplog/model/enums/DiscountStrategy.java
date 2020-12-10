package cn.neptu.neplog.model.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
class Item{
    private double price;
    private int count;
    private List<Item> gifts;
    private Discount discountStrategy;
}
interface Discount {
    void apply(Item item, double total);
}

public class DiscountStrategy{

    public static void main(String[] args) {
        Item item = new Item();
        item.setCount(1);
        item.setPrice(1.11);
        item.setDiscountStrategy((item1, total) -> {
            if(total > 100){
                item1.setPrice(item1.getPrice() - 20);
            }
        });
        Item item2 = new Item();
        item2.setCount(1);
        item2.setPrice(1.11);
        item2.setDiscountStrategy((item1, total) -> {
            if(total > 100){
                item1.setPrice(item1.getPrice() * 0.8);
            }
        });
        Item item3 = new Item();
        item3.setCount(1);
        item3.setPrice(1.11);
        item3.setDiscountStrategy((item1, total) -> {
            if(total > 100){
                item1.getGifts().add(new Item());
            }
        });
        List<Item> items = Arrays.asList(item,item2);
        // calculate total price
        items.forEach(i -> {
            if(i.getDiscountStrategy() != null)
                i.getDiscountStrategy().apply(i, 2323);
        });
        // re-calculate total price
    }
}




