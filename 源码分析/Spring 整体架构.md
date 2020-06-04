# Spring 整体架构

**数据访问/集成**：包括JDBC、ORM、OXM、JMS、TRANSCATIONS(事务)

**WEB**  包括：WebSocket、Servlet、WEB、Protlet

**AOP** 

**Aspects**

**基础组件**

**消息**

**核心容器**     包括：Bean、Core、Context、Expression Language (表达语言)

**Test**

## (1）Core Container(核心容器)

**Core Container(核心容器)**包含有Core、Bean、Context和Expression Language模块。Core和Bean模块是框架的基础部分，提供IOC（反转控制）和依赖注入特性。这里的基础概念是BeanFactory，它提供对Factory模式的经典来消除对程序性单例模式的需要，并真正地允许你从程序逻辑中分离依赖关系和配置。

 - Core模块 主要包括Spring框架的基本的核心工具类，Spring的其他组件要都要使用到这个包里的类，Core模块是其他组件的基本核心。当然你也可以在自己的应用系统中使用这些工具类。
 - Bean模块是所有模块都要用到的，它包含访问配置文件、创建和管理bean以及进行Inversion of Controller / Dependency Injection(IOC/DI)操作相关的所有类。
 - Context模块构建于Core和Beans模块基础之上，提供了一种类似于JDNI注册器的框架式的对象访问方法。Context模块继承了Beans特性，为Spring核心提供了大量扩展，添加了对国际化（例如资源绑定）、事件传播、资源加载和对Context的透明创建的支持。Context模块同时也支持J2EE的一些特性，例如EJB、JMX和基础的远程处理。ApplicationContext接口是Context模块的关键。
 - Expression Language 模块提供了一个强大的表达式语言用于在运行时查询和操作对象。它是JSP2.1规范中定义的unifed expression language的一个扩展。该语言支持设置/获取属性的值，属性的分配，方法的调用，访问数组上下文（accessiong the context of arrays）、容器和索引器、逻辑和算术运算符、命名变量以及从Spring的IOC容器中根据名称检索对象。它也支持list投影、选择和一般的list聚合。

## (2)Data Access/Integration(数据访问/集成)

Data Access/Integration层包含有JDBC、ORM、OXM、JMS和Transaction模块，其中

- JDBC模块提供了一个JDBC抽象层，它可以消除冗长的JDBC编码和解析数据库厂商特有的错误代码。这个模块包含了Spring对JDBC数据访问进行封装的所有类。

- ORM模块为流行的对象-关系映射API，如JPA、JDO、Hibernae、iBatis等，提供了一个交互层。利用ORM封装包，可以混合使用所有Spring提供的特性进行O/R映射。

  Spring框架插入了若干个ORM框架，从而提供了ORM的对象关系工具，其他包括JDO、Hibernate和iBatisSQL Map。所有这些都遵从Spring的通用事务和DAO异常层次机构。

- ORM模块提供了一个对Object/XML映射实现的抽象层，Object/XML映射实现包括JAXB、Castor、XMLBeans、JIBX和XStream。

- JMS（java Messaging Service）模块主要包含了一些制造和消费消息的特性。

- Transaction模块支持编程和声明性的事物管理，这些事物类必须实现特定的接口，并且对所有的POJO都适用

## (3）WEB

Web上下文模块建立在应用程序上下文模块之上，为基于Web的应用程序提供了上下文。所有，Spring框架支持与Jakarta Struts的集成。Web模块还简化了处理多部分请求以及将请求参数绑定到域对象的工作。Web层包括了WEb、Web-Servlet、Web-Struts和Web-Porlet模块，具体说明如下。

- WEB模块：提供了基础的面向Web的集成特性。例如，多文件上传、使用servlet listeners 初始化IOC容器以及一个面向Web的应用上下文。它还包含Spring远程支持中Web的相关部分。
- Web-Servlet模块 Web.servlet.jar：该模块包含Spring的model-view-controller（MVC）实现。Spring的MVC框架使得模型范围内的代码和Web form之间能够清楚地分离开来，并与Spring框架的其他特性集成在一起。
- Web-Struts模块：该模块提供了对Struts的支持，使得类在Spring应用中能够与一个典型的Struts Web层集成在一起。注意，该支持在Spring3.0中是deprecated的。
- Web-Porlet模块：提供了用于Portlet环境和Web-Servlet模块的MVC的实现。

## (4）AOP

AOP模块提供了一个符合AOP联盟标准的面向切面编程的实现，它让你可以定义例如方法拦截器和切点，从而将逻辑代码分开，降低他们之间的耦合性。利用source-level的元数据功能，还可以将各种行为信息合并到你的代码中，这有点像.Net技术中的attribute概念。

​	通过配置管理特性，SpringAOP模块直接将面向切面的编程功能集成到了Spring框架中，所有可以很容易地使Spring框架管理的任何对象支持AOP，不用依赖EJB组件，就可以将声明性事务管理服务。通过使用SpringAOP，不用依赖EJB组件，就可以将声明性事务管理集成到应用程序中。

- Aspects模块提供了对AspectJ的集成支持。
- Instrumentation模块提供了class instrumentation支持和classloader实现，使得可以在特定的应用服务器上使用。

## (5)Test

​	Test模块支持使用JUnit和TestNG对Spring组件进行测试。

# 核心类介绍

## 1、DefaultListableBeanFactory

XmlBeanFactory继承了DefaultListableBeanFactory，而DefaultListableBeanFactory是整个Bean加载的核心部分，是Spring注册及加载Bean的默认实现，而对于XMLBeanFactory与DefaultListableBeanFactory不同的地方其实是在XmlBeanFactory中使用了自定义的XML读取器XmlBeanDefinitionReader,实现了个性化的BeanDefinitionRead读取，DefaultListableBeanFactory继承了AbstractAutowireCapableBeanFactory并实现了ConfigurableListableBeanFactory以及BeanDefinitionRegisry接口。

- AliasRegistry:定义对alias的简单增删改等操作

- SimpleAliasRegistry:主要使用map作为alias的缓存，并对接口AliasRegistry进行实现

- SingletonBeanRegistry:定义对单例的注册及获取

- BeanFactory:定义获取Bean及Bean的各种属性

- DefaultSingletonBeanRegistry:对接口SingletionBeanRegistry各函数的实现。

- HierarchicalBeanFactory:继承BeanFactory,也就是在BeanFactory定义的功能的基础上增加了对parentFactory的支持

- BeanDefinitionRegistry:定义对BeanDefinition的各种增删改操作

- FactoryBeanRegistrySupport:在DefaultSingletonBeanRegistry基础上增加了对FactoryBean的特殊处理功能。

- ConfigurableBeanFactory:提供配置Factory的各种方法。

- ListableBeanFactory:根据各种条件获取bean的配置清单。

- AbstractBeanFactory:综合FactoryBeanRegistrySupport和ConfigurableBeanFactory的功能

- AutowireCapableBeanFactory:提供创建bean、自动注入、初始化以及应用bean的后处理器。

- AbstractAutowireCapableBeanFactory:综合AbstractBeanFactory并对接口Autowire Capable BeanFactory进行实现。

- ConfigurableListBeanFactory：BeanFactory配置清单，指定忽略类型及接口等。

- DefaultListableBeanFactory:综合上面所以功能，主要是对Bean注册后的处理。

  XmlBeanFactory对DefaultListableBeanFactory进行了扩展，主要用于从XML文档中读取BeanDefinition，对于注册及获取Bean都是使用从父类DefaultListableBeanFactory继承的方法去实现，而唯独与父类不同的个性化实现就是增加了XmlBeanDefinitionReader类型的reader属性。在XmlBeanFactory中主要使用reader属性对资源文件进行读取和注册。

## 2、XmlBeanDefinitionReader

XML配置文件的读取是Spring中最重要的功能，因为Spring的大部分功能都是以配置作为切入点，那么我们可以从XmlBeanDefinitionReader中梳理一下资源文件读取、解析及注册的大致脉络，首先我们看看各个类的功能。

* ResourceLoader: 定义资源加载器，主要应用于根据定义的资源文件地址返回对应的Resource。
* BeanDefinitionReader: 主要定义资源文件读取并转换为BeanDefinition的各个功能。
* EnvironmentCapable: 定义获取Environment方法
* DecumentLoader: 定义从资源文件加载到转换为Document的功能。
* AbstractBeanDefinitionReader: 对EnvironmentCapable、BeanDefinitionReader类定义的功能进行实现。
* BeanDefinitionDefinitionReader: 定义读取Document并注册BeanDefinition功能。
* BeanDefinitionParserDelegate: 定义分析Element的各种方法。

（1）通过继承自AbstractBeanDefinitionReader中的方法，来使用ResourLoader将资源文件路径转换为对应的Resource文件。

（2）通过DocumentLoader对Resource文件进行转换，将Resource文件转换为Document文件

（3）通过实现接口BeanDefinitionDecumentReader的DefaultBeanDefinitionDocumentReader类对Document进行解析，并使用BeanDefinitionParserDelegate对Element进行解析。

































































































































































