package xsh.jdk8;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xsh.App;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Collect():将流 转为 集合或字符串返回
 * Collectors：工具类，是JDK预实现Collector的工具类
 *
 * 参考文档：
 *      https://www.jianshu.com/p/7eaa0969b424
 *      https://blog.csdn.net/candyguy242/article/details/80718855
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/1/27 9:44
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)  //启动类
public class CollectTest {


    /**
     * 将流中的元素 存到 集合/字符串 返回
     */
    @Test
    public void toCollection() {
        List<String> items = Arrays.asList("11", "22", "33","aa", "22", "bb", "33");
        /**
         * toCollection(集合类) 将流中的元素 放到 集合[指定类型]
         * LinkedList::new 引用构造方法。方法引用【类::new】
         */
        List<String> collection = items.stream().collect(
//                Collectors.toCollection(LinkedList::new)
                Collectors.toCollection(ArrayList::new)
        );
        //结果：[11, 22, 33, aa, 22, bb, 33]
        System.out.println(collection);


        /**
         * toList()：将流中的元素 存到 列表集合[默认ArrayList]
         */
        List<String> list = items.stream().collect(
                Collectors.toList()
        );
        //结果：[11, 22, 33, aa, 22, bb, 33]
        System.out.println(list);


        /**
         * toSet(): 将流中的元素 存到 去重无序集set[默认HashSet]
         */
        Set<String> set = items.stream().collect(
                Collectors.toSet()
        );
        //结果：[11, 22, 33, aa, bb]
        System.out.println(set);


        /**
         * toMap(key, value, key重复时处理方式, map生成方式):
         *      将key和value存到map并返回
         * toConcurrentMap和toMap相识
         */
        Map<String,String> map = items.stream().limit(3).collect(
                Collectors.toMap(e -> e.substring(0,1),e -> e)
        );
        Map<String,String> map1 = items.stream().collect(
                Collectors.toMap(e -> e.substring(0,1),e->e,(a,b)-> b)
        );
        Map<String,String> map2 = items.stream().collect(
                Collectors.toMap(e -> e.substring(0,1),e->e,(a,b)-> b,HashMap::new)
        );
        //[11, 22, 33, aa, 22, bb, 33]
        //map结果：{1=11, 2=22, 3=33}
        //map1结果：{a=aa, 1=11, b=bb, 2=22, 3=33}
        //map2结果：{a=aa, 1=11, b=bb, 2=22, 3=33}
        System.out.println(map.toString() + "\n" + map1.toString() + "\n" + map2.toString());


        /**
         * joining(): 将流中的元素 以字符方式连接起来
         */
        String s1 = list.stream().collect(Collectors.joining());//无参方法
        String s2 = list.stream().collect(Collectors.joining("-"));//指定连接符
        String s3 = list.stream().collect(Collectors.joining("-","S","E")); //指定连接符和前后缀
        //[11, 22, 33, aa, 22, bb, 33]
        //s1结果：112233aa22bb33
        //s2结果：11-22-33-aa-22-bb-33
        //s3结果: S11-22-33-aa-22-bb-33E
        System.out.println(s1 + "\n" + s2 + "\n" + s3);
    }


    /**
     * 获取常量的函数
     */
    @Test
    public void tem() {
        List<String> items = Arrays.asList("11", "4", "33","aaaaaaa", "22", "111", "33");
        /**
         * mapping(item,collector):
         *      对流中的元素[items]进行映射[类型转换]，并交由Collector归纳。
         * .limit(3): 截取items集合前3个元素
         */
        List<Integer> list1 = items.stream().limit(3).collect(
                Collectors.mapping(Integer::valueOf, Collectors.toList())
        );
        //结果：[11, 4, 33]
        System.out.println(list1) ;


        /**
         * collectingAndThen(collectors, 处理函数): 对归纳的结果collectors再处理
         * e -> e.size()：获取列表e大小，【lambda表达式】
         */
        int length = items.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toList(),e -> e.size()
                )
        );
        //结果：7
        System.out.println(length);


        /**
         * maxBy()：列表中最大元素
         * minBy(): 列表中最小元素
         */
        Optional<String> max = items.stream().collect(
                Collectors.maxBy((a, b) -> b.length() - a.length())
        );
        Optional<String> min = items.stream().collect(
                Collectors.minBy((a, b) -> b.length() - a.length())
        );
        //max结果：Optional[4]
        //min结果：Optional[aaaaaaa]
        System.out.println(max + "\n" + min);

        /**
         * limit(3)：截取列表前3个元素 [11, 4, 33]
         * summingInt(元素)：列表元素值 求和
         * Integer::valueOf：元素值转为int
         */
        int summing = items.stream().limit(3).collect(Collectors.summingInt(Integer::valueOf));
        //averagingInt(): 列表元素值 求平均值
        double average = items.stream().limit(3).collect(Collectors.averagingInt(Integer::valueOf));
        System.out.println(summing) ; //48
        System.out.println(average) ; //16.0

        /**
         * [11, 4, 33, aaaaaaa]
         *   2  1   2   7
         *
         * map(String::length): 获取集合中元素长度
         * Collectors.reducing(初始值, 元素, 统计)：流中的元素做统计
         */
        Optional<Integer> reducing1 = items.stream().limit(4).map(String::length).collect(
                Collectors.reducing(Integer::sum)
        );
        Integer reducing2 = items.stream().limit(3).map(String::length).collect(
                Collectors.reducing(0, Integer::sum)
        );
        Integer reducing3 = items.stream().limit(4).collect(
                Collectors.reducing(0, String::length, Integer::sum)
        );
        System.out.println(reducing1) ; //Optional[12]
        System.out.println(reducing2) ; //5
        System.out.println(reducing3) ; //12
    }



    /**
     * 分组Collectors.groupingBy(key, map生成方式, value)
     *      得到map<key,value>集合,默认value为对应items集合
     * */
    @Test
    public void groupingBy1() {
        List<String> items = Arrays.asList("apple", "apple", "banana",
                "apple", "orange", "banana", "papaya");
        /**
         * Function.identity(): 流中的元素[名]
         * Collectors.counting(): 元素个数
         */
        Map<String, Long> result = items.stream().collect(
                Collectors.groupingBy(
                    Function.identity(), Collectors.counting()
                )
        );
        //结果：{papaya=1, orange=1, banana=2, apple=3}
        System.out.println(result);

        Map<Integer,List<String>> s = items.stream().collect(
                Collectors.groupingBy(String::length)
        );
        Map<Integer,List<String>> ss = items.stream().collect(
                Collectors.groupingBy(String::length, Collectors.toList())
        );
        Map<Integer,Set<String>> sss = items.stream().collect(
                Collectors.groupingBy(String::length,HashMap::new,Collectors.toSet())
        );
        //s结果：{5=[apple, apple, apple], 6=[banana, orange, banana, papaya]}
        //ss结果：{5=[apple, apple, apple], 6=[banana, orange, banana, papaya]}
        //sss结果：{5=[apple], 6=[banana, orange, papaya]}
        System.out.println(s.toString() + "\n" + ss.toString() + "\n" + sss.toString());

        /**
         * partitioningBy(判断规则)：
         *   将流中的元素 按校验规则 分为两个部分[false和true]，放到一个map<Boolean值,list>返回
         */
        Map<Boolean,List<String>> map = items.stream().collect(
                Collectors.partitioningBy(e -> e.length()>5)
        );
        Map<Boolean,Set<String>> map2 = items.stream().collect(
                Collectors.partitioningBy(e -> e.length()>=6,Collectors.toSet())
        );
        //map结果：{false=[apple, apple, apple], true=[banana, orange, banana, papaya]}
        //map2结果：{false=[apple], true=[banana, orange, papaya]}
        System.out.println(map.toString() + "\n" + map2.toString());
    }


    //对象
    @Test
    public void groupingBy2() {
        //3 apple, 2 banana, others 1
        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orang", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("9.99"))
        );

        /**
         * Item::getName: 元素名称
         * summingInt(字段): 列表中该元素值 求和
         */
        Map<String, Integer> summingInt = items.stream().collect(
                Collectors.groupingBy(
                        Item::getName,  Collectors.summingInt(Item::getQty)
                )
        );
        //结果：{papaya=20, banana=30, apple=40, orang=10, watermelon=10}
        System.out.println(summingInt) ;


        /**
         * mapping(item,Collector): 对流中的元素[item]映射，并交由Collector归纳
         * toSet(): 将流中数据 存到 集合set(去重，无序)
         */
        Map<BigDecimal, Set<String>> result = items.stream().collect(
                Collectors.groupingBy(
                        Item::getPrice, Collectors.mapping(Item::getName, Collectors.toSet())
                )
        );
        //结果：{19.99=[banana], 29.99=[orang, watermelon], 9.99=[papaya, apple]}
        System.out.println(result);

    }
}
