# gdata
jdbc,jdbctemplate bean

  
Maven
--------
[![Maven Central](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/com/gsralex/gdata/maven-metadata.xml.svg)](http://mvnrepository.com/artifact/com.gsralex/gdata)

 ``` java
 <dependency>
      <groupId>com.gsralex</groupId>
      <artifactId>gdata-bean</artifactId>
      <version>${lastest.version}</version>
  </dependency>
```

例子
--------
api层面尽量和jdbctemplate保持一致  
PS：插一句其他的，我个人不喜欢MyBatis的sql语句的处理方式，无论是xml还是注解sql。主要原因是xml和注解的表达能力都较弱，在动态查询方面捉襟见肘，灵活性较差。所以比较推崇jdbctemplate的sql直接写在代码的设计，灵活性无疑是最重要的。


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

//也支持sql insert
String sql="insert into t_foo(foo_1,foo_2,foo_3) values(?,?,?)";
jdbcUtils.executeUpdate(sql,new Object[]{foo.getFoo1(),foo.getFoo2(),foo.getFoo3());

foo.setFoo4(123123);
jdbcUtils.update(foo);

//也可以这样update
String sql="update t_foo set foo4=? where id=?";
jdbcUtils.executeUpdate(sql,new Object[]{foo.getFoo4(),foo.getId()});
  
//queryForObject 支持复杂类型
Foo data=jdbcUtils.queryForObject("select * from t_foo where id=?",new Object[]{1},Foo.class); 

List<Foo> list=jdbcUtils.queryForList("select * from t_foo",null,Foo.class);

//支持Vo

public class Vo {

    @Column(name = "foo_1")
    private String foo1;//db field foo_1

    @Column(name = "foo_2")
    private Double foo2;//db field foo_2

    @Column(name = "foo_3")
    private Date foo3;//db field foo_3

    @Column(name = "foo_4")
    private Integer foo4;//db field foo_4
   
    private Integer foo5;//db field foo5
	
    private Integer nodb_foo6;//not a db field 不会赋值
    
    private Double nodb_foo7;//not a db field 不会赋值
}

List<Vo> list=jdbcUtils.queryForList("select * from vo where id=?",new Object[]{1},Vo.class);

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
### v1.077(2018-11-02)
- 修复了bug，移出对springtemplate的支持  
### v1.06(2018-04-05)
- 支持手动事务，支持mysql,oralce,sqlserver，支持占位符语法
### v1.05(2018-03-20)
- 加入删除
### v1.03(2018-03-20)
- 加入了get,getList返回Integer.class等常用类型




