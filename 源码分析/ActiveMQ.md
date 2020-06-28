# 2018

## 第一节

### 消息中间件的初步认识

#### 什么是消息中间件？

消息中间件是值 利用高效可靠的消息传递机制进行平台无关的数据交流， 并 基于数据通信来进行分布式系统的集成。通过提供消息传递和消息排队模型，可以在分布式架构下扩展进程之间的通信。

#### 消息中间件能做什么？

消息中间件主要解决的就是分布式系统之间消息传递的问题， 它能够屏蔽各种平台以及协议之间的特性，实现应用程序之间的协同。 举个非常简单的例子 ，就拿一个电商平台的注册功能来简单分析下，用户注册这一个服务， 不单单只是insert 一条数据到数据库里面就完事了，还需要发送激活邮件、发送新人红包或者积分、发送营销短信等一系列操作。 假如说这里面的每一个操作，都需要消耗 1s那么整个注册过程就需要耗时 4s 才能响应给用户。

但是我们从注册这个服务可以看到，每一个子操作都是相对独立的，同时，基于领域划分以后，发送激活邮件、发送营销短信、赠送积分及红 包都属于不同的子域。 所以我们可以对这些子操作进行来实现异步化执行，类似于多线程并行处理的概念。如何实现异步化呢？用多线程 能实现吗？多线程当然可以实现，只是，消息的持久化、消息的重发这些条件，多线程并不能满足。所以需要借助一些开源中间件来解决。而分布式消息队列就是一个非常好的解决办法，引入分布式消息队列以后，架构图就变成这样了 下图是异步消息队列的场景 。 通过引入分布式队列，就能够 大大提升程序的处理效率，并且还 解决 了各个模块之间的耦合 问题。

我们再来展开一种场景， 通过分布式消息队列来实现 流量整形， 比如在电商平台的秒杀场景下，流量会非常大。 通过消息队列的方式可以很好的缓解高流量的问题。

1、用户提交过来的请求，先写入到消息队列。消息队列是有长度的，如果消息队列长度超过指定长度，直接抛弃

2、秒杀的具体核心处理业务，接收消息队列中消息进行处理，这里的消息处理能力取决于消费端本身的吞吐量

当然，消息中间件还有更多应用场景，比如在弱一致性事务模型中，可以采用分布式消息队列的实现最大能力通知
做技术人的指路明灯，做职场生涯的精神导师方式来实现数据的最终一致性等等方式来实现数据的最终一致性等等

### ActiveMQ简介

ActiveMQ是完全基于 JMS 规范实现的一个消息中间件产品。 是 Apache 开源基金会研发的消息中间件 。 ActiveMQ主要应用在分布式系统架构中，帮助构建高可用、 高性能、可伸缩的企业级面向消息服务的系统

**ActiveMQ特性**

1、对语言和协议编写客户端

语言：java、c、c++、c#、Ruby、Perl、python、PHP

应用协议：openwire/stomp/REST/ws/notification/XMPP/AMQP

2、完全支持 jms1.1 和 J2ee1.4 规范

3、对 spring 的支持， ActiveMQ 可以很容易内嵌到 spring模块中

### 从JMS规范来了解ActiveMQ

#### JMS定义

Java 消息服务（ Java Message Service ）是 java 平台中关于面向消息中间件的 API ，用于在两个应用程序之间，或者分布式系统中发送消息，进行异步通信 。

JMS是一个与具体平台无关的 API ，绝大多数 MOMMessage Oriented Middleware ）（面向消息中间件）提供商都对 JMS 提供了支持。今天给大家讲的 ActiveMQ 就是其中一个实现
**什么是MOM**

MOM是 面向消息的中间件，使用消息传送提供者来协调消息传送操作。 MOM 需要提供 API 和管理工具。客户端
使用 api 调用，把消息发送到由提供者管理的目的地。在发送消息之后，客 户端会继续执行其他工作，并且在接收方收到这个消息确认之前，提供者一直保留该消息。

**MOM特点**

1、消息异步接收，发送者不需要等待消息接受响应

2、消息可靠接收，确保消息在中间件可靠保存。只有接收方收到后才删除消息

Java消息传送服务规范最初的开发目的是为了使 Java应用程序能够访问现有 MOM 系统。引入该规范之后，它
已被许多现有的 MOM 供应商采用并且已经凭借自身的功能实现为异步消息传送系统。

#### JMS规范

我们已经知道了JMS 规范的目的是为了 使得 Java 应用程序能够访问现有 MOM 消息中间件 系统 形成一套统一
的标准规范，解决不同消息中间件之间的协作问题。 在创建 JMS 规范时，设计者希望能 够结合现有的消息传送的精髓，比如说

1. 不同的消息传递模式或域，例如点对点消息传递和发布、订阅消息传递
2. 提供与接收同步和异步消息的工具
3. 对可靠消息传递的支持
4. 常见消息格式，例如流、文本和字节

**细化JMS的基本功能**

通过前面的内容讲解以及案例演示，我们已经知道了 JMS规范以及他的基本功能是用于和面向消息中间件相互通信
的应用程序的接口，那么 JMS 提供的具体标准有哪些呢？我们来仔细去研究下

* 消息传递域

JMS规范中定义了两种消息传递域：点对点（ point to point ）消息传递域 和发布 订阅 消息传递域(publish/subscribe)简单理解就是：有点类似于我们通过qq 聊天的时候，在群里面发消息和给其中一个同学私聊消息。在群里发消息，所有群成员都能收到 消息。私聊消息只能被私聊的学员能收到消息，

**点对点消息传递域**

1. 每个消息只能有一个消费者
2. 消息的生产者和消费者没有时间上的相关性。无论消费者在生产者发送消息的时候是否处于运行状态，都可以提取消息

**发布定义消息传递域**

1. 每个消息可以有多个消费者
2. 生产者和消费者之间有时间上的相关性。订阅一个主题的消费者只能消费它订阅之后发布的消息。JMS规范允许客户创建持久订阅，这在一定程度上降低了时间上的相关性要求。持久订阅允许消费者消费它在未处于激活状态时发送的消息。

* 消息结构组成

JMS消息由及部分组成：消息头、属性、消息体

**消息头**

消息头(Header) 消息头包含消息的识别信息和路由信息，消息头包含一些标准的属性如：
JMSDestination 消息发送的目的地， queue 或者 topic
JMSDeliveryMode 传送模式。持久模式和非持久模式
JMS Priority 消息优先级（优先级分为 10 个级别，从 0( 最低 到 9 最高 ). 如果不设定优先级，默认级别是 4 。
需要注意的是， JMS provider 并不一定保证按照优先级的顺序提交消息）
JMSMessage ID 唯一识别每个消息的标识

**属性**

按类型可以分为应用设置的属性，标准属性和消息中间件定义的属性

1. 应用程序设置和添加的属性，比如Message.setStringProperty(setStringProperty(“key”,”通过下面的代码可以获得自定义属性的，在接收端的代码中编写在发送端，定义消息属性message.setStringProperty("Mic","Hello World");

   在接收端接收数据

   ```java
   Enumeration enumeration=message.getPropertyNames(); 
   while(enumeration.hasMoreElements()){ 
       String name=enumeration.nextElement().toString(); 			        System.out.println("name:"+name+":"+message.getStringProperty(name)); 
       System.out.println(); }
   ```

   

2.  JMS 定义的属性

   使用“JMSX ”作为属性名的前缀，通过下面这段代码可以返回所有连接支持的 JMSX 属性的名字

   ```java
   Enumeration names=connection.getMetaData().getJMSXPropertyNames();
   while(names.hasMoreElements()){
       String name=(String) names.nextElement();
       System.out.println(name);
   }
   ```

   

3. JMS provider 特定的属性

**消息体**

就是我们需要传递的消息内容，JMS API 定义了 5 中消息体格式，可以使用不同形式数据接收数据，并可以兼容现有的消息格式，其中包括

* TextMessage                  java.lang.String对象，如 xml 文件内容
* MapMessage                 名/值对的集合，名是String对象，值类型可以是Java可以是任何基本类型
* BytesMessage                字节流
* StreamMessage              java中的输入输出流
* ObjectMessage               Java中的可序列化对象

* Message                           没有消息体，只有消息头和属性。

**持久订阅**

持久订阅 的 概念， 也 很容 易 理解， 比如 还是 以 Q Q 为例，我们 把 Q Q 退出 了， 但是 下次 登录 的 时候， 仍然 能 收到 离线 的 消息。

1. 持久订阅者和非持久订阅者针对的 Domain 是 Pub/Sub而不是 P2P

2. 当 Broker 发送消息给订阅者时，如果订阅者处于 未激活状态 状态：持久订阅者可以收到消息，而非持久订阅者则收不到消息。

  当然这种方式也有一定的影响：当持久订阅者处于 未激活状态时， Broker 需要为持久订阅者保存消息；如果持久订阅者订阅的消息太多则会溢出。

**消费端改动**

```java
connection=connectionFactory.createConnection(); connection.setClientID("Mic-001"); connection.start(); Session session=connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE); Topic destination=session.createTopic("myTopic"); MessageConsumer consumer=session.createDurableSubscriber(destination,"Mic-001"); TextMessage message=(TextMessage)consumer.receive(); System.out.println(message.getText());
```

修改三处地方，然后先启动消费端去注册一个持久订阅。

持久订阅时，客户端向JMS 服务器注册一个自己身份的 ID当这个客户端处于离线时， JMS Provider 会为这个 ID 保存所有发送到主题的消息，当客户再次连接到 JMSProvider 时，会根据自己的 ID 得到所有当自己处于离线时
发送到主题的消息。

这个身份 ID 在 代码 中 的 体现 就是 connect ion 的 ClientID这个其实很好理解，你要想收到朋友发送的 qq 消息，前提就是你得先注册个 QQ 号，而且还要有台能上网的设备，电脑或手机。设备就相当于是 c lientId 是唯一的； qq 号相当于是订阅者的名称，在同一台设备上，不能用同一个 qq号挂 2 个客户端。连接的 clientId 必须是唯一的，订阅者的名称在同一个连接内必须唯一。这样才能唯一的确定连接和订阅者。

### JMS消息的可靠性机制

理论上来说，我们需要保证消息中间件上的消息，只有被消费者确认过以后才会被签收，相当于我们寄一个快递出
去，收件人没有收到快递，就认为这个包裹还是属于待签收状态，这样才能保证包裹能够安全达到收件人手里。消
息中间件也是一样。消息的消费通常包含3 个阶段：客户接收消息、客户处理消息、消息被确认首先，来简单了解
JMS 的事务性会话和非事务性会话的概念

JMS Session接口提供了 commit 和 rollback 方法。事务提交意味着生产的所有消息被发送，消费的所有消息被确认；事务回滚意味着生产的所有消息被销毁，消费的所 有消息被恢复并重新提交，除非它们已经过期。 事务性的会话总是牵涉到事务处理中， commit 或 rollback 方法一旦被调用，一个事务就结束了，而另一个事务被开始。关闭事务性会话将回滚其中的事务。

**在事务型会话中**

在事务状态下进行发送操作，消息并未真正投递到中间件，而只有进行 session.commit 操作之后，消息才会发送到中间件，再转发到适当的消费者进行处理。如果是调用rollback 操作，则表明，当前事务期间内所发送的消息都取消掉。 通过在创建 session 的时候使用 true or false 来决定当前的会 话是事务性还是非事务性connection.createSession(Boolean.TRUE ,Session.ACKNOWLEDGE);

在事务性会话中，消息的确认是自动进行，也就是通过session . 以后，消息会自动确认。
➢ 必须保证发送端和接收端都是事务性会话
**在非在非事务型会话中**

消息何时被确认取决于创建会话时的应答模式(acknowledgement mode). 有三个可选项Session.AUTO_ACKNOWLEDGE

当客户成功的从receive 方法返回的时候，或者从MessageListenner.onMessage 方法成功返回的时候，会话
自动确认客户收到消息。

Session.CLIENT_ACKNOWLEDGE

客户通过调用消息的acknowledge 方法确认消息。

CLIENT_ACKNOWLEDGE特性

在这种模式中，确认是在会话层上进行，确认一个被消费的消息将自动确认所有已被会话消费的消息。列如，如果
一个消息消费者消费了 10 个消息，然后确认了第 5 个消息， 那么 0 5 的 消息 都会 被 确认
演示如下 

发送端 发送 1 0 个 消息， 接收端 接收 1 0 个 消息，但是 在 i ==5 的 时候， 调用 message . acknowledge()进行确认 ,会 发现 0 ~4 的 消息 都会 被 确认 Session.DUPS_ACKNOWLEDGE

消息延迟确认 。 指定 消息 提供者 在 消息 接收 者 没有 确认 发送时 重新 发送 消息， 这种 模式 不在乎 接受者 收到 重复 的 消息 。

**消息的 持久化 存储**

消息的 持久化 存储 也是 保证 可靠性 最重要 的 机制 之一， 也就是 消息 发送 到 Broker 上 以后， 如果 broker 出现 故障 宕机 了， 那么 存储 在 broker 上 的 消息 不应该 丢失 。 可以 通过下面 的 代码 来 设置 消息 发送 端 的 持久化 和 非持久化 特性

```java
MessageProducer producer=session.createProducer(destination);
producer.setDeliverMode(DeliveryMode.PERSISTENT);
```

对于非持久的消息， JMS provider 不会将它存到文件 数据库等稳定的存储介质中。也就是说非持久消息驻留在
内存中，如果 jms provider 宕机，那么内存中的非持久消息会丢失

对于持久消息，消息提供者会使用存储 转发机制，先将消息存储到稳定介质中，等消息发送成功后再删除。如
果 jms provider 挂掉了，那么这些未送达的消息不会丢失； jms provider 恢复正常后，会重新读取这些消息，
并传送给对应的消费者。

## 第二节

### 持久化消息和非持久化消息的发送策略

### 消息从持久化策略分析

### 消费端消费消息的原理























