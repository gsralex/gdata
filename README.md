# gdata
jdbc,jdbctemplate bean

  
Maven
--------
[![Maven Central](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/com/gsralex/gdata/maven-metadata.xml.svg)](http://mvnrepository.com/artifact/com.gsralex/gdata)

 ``` java
 <dependency>
      <groupId>com.gsralex</groupId>
      <artifactId>gdata</artifactId>
      <version>${lastest.version}</version>
  </dependency>
```

例子
--------
api层面尽量和jdbctemplate保持一致
 ``` java
 JdbcUtils jdbcUtils = new JdbcUtils(DataSourceConfg.getDataSource());
 @Table(name = "t_foo")
 public class Foo {

    @IdField
    private Long id;

    @LabelField(name = "foo_1")
    private String foo1;//db field foo_1

    @LabelField(name = "foo_2")
    private Double foo2;//db field foo_2

    @LabelField(name = "foo_3")
    private Date foo3;//db field foo_3

    @LabelField(name = "foo_4")
    private Integer foo4;//db field foo_4
   
    private Integer foo5;//db field foo5
	
    @IgnoreField
    private Integer foo6;//not a db field
}
 
Foo foo=new Foo();
jdbcUtils.insert(foo,true);
Assert.assertNotEquals(0, foo.getId()); //ok

foo.setFoo4(123123);
jdbcUtils.update(foo);
  
Foo data=jdbcUtils.queryForObject("select * from t_foo where id=?",new Object[]{1},Foo.class); //queryForObject 支持复杂类型

List<Foo> list=jdbcUtils.queryForList("select * from t_foo",null,Foo.class);

Integer cnt=jdbcUtils.queryForObject("select count(1) from t_foo",null,Integer.class); //queryForObject 支持简单类型


boolean ok=jdbcUtils.delete(foo);


 ```

ChangeLog
--------
### v1.06(2018-03-20)
- 支持手动事务
### v1.05(2018-03-20)
- 加入删除
### v1.03(2018-03-20)
- 加入了get,getList返回Integer.class等常用类型




