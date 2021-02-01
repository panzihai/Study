package xsh.jdk8;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Arrays;
import java.util.List;

/**
 * JDK8新特性
 * 参考文档：https://www.cnblogs.com/liuxiaozhi23/p/10880147.html
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/1/27 17:01
 */
@RunWith(SpringRunner.class)
public class JDK8Test {

    /**
     * lambda表达式
     *    函数作为参数 传给方法
     *    (parameters) -> expression
     *    1)类型声明：不需要声明，编译器可统一识别参数值
     *    2)参数圆括号：一个参数无需定义，若多个参数需要定义
     *    3)大括号：若主体只有一个语句，就不需要使用
     *    4)关键字return：若主体只有一个表达式,则编译器会自动返回值;
     *          若有大括号,则需要return返回;
     */
    @Test
    public void lambda() {

    }


    /**
     * 方法引用：
     *    使用一对冒号 ::
     */
    @Test
    public  void menth() {
        //构造器引用：【类名::new】
        Item item = Item.create(Item::new);
        //一般方法 和 静态方法
        List<Item> items = Arrays.asList(item);
        items.forEach(System.out::println);
    }

    /**
     * 函数式接口
     *  只有一个抽象方法，可以有多个非抽象方法
     */
    @Test
    public void inter() {

    }

}
