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


#### bean操作

 ``` java
 JdbcUtils jdbcUtils = new JdbcUtils(DataSourceConfg.getDataSource());
 @Table(name = "t_foo")
 public class Foo {

    @Id
    @Column(name = "id") //如果数据库字段名与类字段名相同，可以不用写@Column注解
    private Long id;

    @Column(name = "foo_1")
    private String foo1;//db field foo_1

    @Column(name = "foo_2")
    private Double foo2;//db field foo_2

    @Column(name = "foo_3")
    private Date foo3;//db field foo_3

    @Column(name = "foo_4")
    private Integer foo4;//db field foo_4
   
    private Integer foo5;//db field foo5
	
    @Ignore
    private Integer foo6;//not a db field
}
 
Foo foo=new Foo();
jdbcUtils.insert(foo,true);
Assert.assertNotEquals(0, foo.getId()); //ok

foo.setFoo4(123123);
jdbcUtils.update(foo);
  
//queryForObject 支持复杂类型
Foo data=jdbcUtils.queryForObject("select * from t_foo where id=?",new Object[]{1},Foo.class); 

List<Foo> list=jdbcUtils.queryForList("select * from t_foo",null,Foo.class);

//queryForObject 支持简单类型
Integer cnt=jdbcUtils.queryForObject("select count(1) from t_foo",null,Integer.class); 


boolean ok=jdbcUtils.delete(foo);

 ```
 
 
 
 
 
#### 占位符语法
 
 第一种写法，使用Map<String,Object>，mapkey不区分大小写，sql不区分大小写
 ``` java
 Map<String,Object> paramMap=new HashMap<>();
 paramMap.put("Foo1","123");
 List<Foo> list= jdbcUtils.queryForListP("select * from t_foo where foo_1=:foo1",paramMap,Foo.class);
 ```
 第二种写法，设置BeanSource
 ``` java
 Foo foo=new Foo();
 foo.setFoo1("123");
 BeanSource beanSource=new BeanSource(foo);
 List<Foo> list= jdbcUtils.queryForListP("select * from t_foo where foo_1=:foo1",beanSource,Foo.class);
```
 
 
 
 
#### 支持手动事务

 ``` java

  jdbcUtils.setAutoCommit(false);
  
  jdbcUtils.insert(foo, true); id:1
  jdbcUtils.insert(foo1, true); id:2
  
  jdbcUtils.rollback();//没有实际插入
  
  jdbcUtils.insert(foo2, true); id:3
  jdbcUtils.commmit();//只插入一条id:3
  
```

修改日志
--------
### v1.06(2018-04-05)
- 支持手动事务，支持mysql,oralce,sqlserver，支持占位符语法
### v1.05(2018-03-20)
- 加入删除
### v1.03(2018-03-20)
- 加入了get,getList返回Integer.class等常用类型




