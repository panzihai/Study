package xsh.jdk8;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 实体类
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/1/27 11:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private String name;
    private int qty;
    private BigDecimal price;


    @FunctionalInterface
    public interface Supplier<T> {
        T get();
    }

    //Supplier是jdk1.8的接口，这里和lamda一起使用了
    public static Item create(final Supplier<Item> supplier) {
        return supplier.get();
    }
    
    
    public static void sleep() {
        System.out.println("我是静态方法sleep") ;
    }
    
    public void move() {
        System.out.println("我是一般方法move") ;
    }

}
