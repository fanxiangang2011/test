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

#### 1、消息的同步发送和异步发送

ActiveMQ支持同步、异步两种发送模式将消息发送到broker上。

同步发送过程中，发送者发送一条消息会阻塞直到broker反馈一个确认消息，表示消息已经被broker处理。这个机制提供了消息的安全性保障，但是由于是阻塞的操作，会影响到客户端消息发送的性能

异步发送的过程中，发送者不需要等待broker提供反馈，所以性能相对较高。但是可能会出现消息丢失的情况。所以使用异步发送的前提是在某些情况下允许出现数据丢失的情况。

默认情况下，非持久化消息是异步发送的，持久化消息并且是在非事务模式下是同步发送的。

但是在开启事务的情况下，消息都是异步发送。由于异步发送的效率会比同步发送性能更高。所以在发送持久化消
息的时候，尽量去开启事务会话。

除了持久化消息和非持久化消息的同步和异步特性以外，我们还可以通过以下几种方式来设置异步发送

```java
1.ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.11.153:61616?
jms.useAsyncSend=true");
2.((ActiveMQConnectionFactory) connectionFactory).setUseAsyncSend(true);
3.((ActiveMQConnection)connection).setUseAsyncSend(true);
```

#### 2、消息的发送原理分析图解

**ProducerWindowSize的含义**

producer每发送一个消息，统计一下发送的字节数，当字节数达到ProducerWindowSize值时，需要等待broker的确认，才能继续发送。代码在：ActiveMQSession的1957行

主要用来约束在异步发送时producer端允许积压的(尚未ACK)的消息的大小，且只对异步发送有意义。每次发送消
息之后，都将会导致memoryUsage大小增加(+message.size)，当broker返回producerAck时，memoryUsage尺
寸减少(producerAck.size，此size表示先前发送消息的大小)。

可以通过如下2种方式设置:
Ø 在brokerUrl中设置: "tcp://localhost:61616?jms.producerWindowSize=1048576",这种设置将会对所有的
producer生效。
Ø 在destinationUri中设置: "test-queue?producer.windowSize=1048576",此参数只会对使用此Destination实例
的producer失效，将会覆盖brokerUrl中的producerWindowSize值。
注意：此值越大，意味着消耗Client端的内存就越大。

#### 3、消息发送的源码分析

**以producer.send为入口**

```java
public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive, AsyncCallback onComplete) throws JMSException {
        this.checkClosed();//检查session的状态，如果session以关闭则抛异常
        if (destination == null) {
            if (this.info.getDestination() == null) {
                throw new UnsupportedOperationException("A destination must be specified.");
            } else {
                throw new InvalidDestinationException("Don't understand null destinations");
            }
        } else {
            ActiveMQDestination dest;
            if (destination.equals(this.info.getDestination())) {//检查desination的类型，如果符合要求，就转变为ActiveMQDestination
                dest = (ActiveMQDestination)destination;
            } else {
                if (this.info.getDestination() != null) {
                    throw new UnsupportedOperationException("This producer can only send messages to: " + this.info.getDestination().getPhysicalName());
                }

                dest = ActiveMQDestination.transform(destination);
            }

            if (dest == null) {
                throw new JMSException("No destination specified");
            } else {
                if (this.transformer != null) {
                    Message transformedMessage = this.transformer.producerTransform(this.session, this, message);
                    if (transformedMessage != null) {
                        message = transformedMessage;
                    }
                }

                if (this.producerWindow != null) {//如果发送窗口大小不为空，则判断发送窗口的大小决定是否阻塞
                    try {
                        this.producerWindow.waitForSpace();
                    } catch (InterruptedException var10) {
                        throw new JMSException("Send aborted due to thread interrupt.");
                    }
                }
               //发送消息到broker的topic
                this.session.send(this, dest, message, deliveryMode, priority, timeToLive, this.producerWindow, this.sendTimeout, onComplete);
                this.stats.onMessage();
            }
        }
    }

```

**ActiveMQSession的send方法**

```java
protected void send(ActiveMQMessageProducer producer, ActiveMQDestination destination, Message
message, int deliveryMode, int priority, long timeToLive,
MemoryUsage producerWindow, int sendTimeout, AsyncCallback onComplete)
throws JMSException {
    checkClosed();
    if (destination.isTemporary() && connection.isDeleted(destination)) {
        throw new InvalidDestinationException("Cannot publish to a deleted Destination: " +
destination);
}
synchronized (sendMutex) { //互斥锁，如果一个session的多个producer发送消息到这里，会保证消息发送
的有序性
// tell the Broker we are about to start a new transaction
doStartTransaction();//告诉broker开始一个新事务，只有事务型会话中才会开启
TransactionId txid = transactionContext.getTransactionId();//从事务上下文中获取事务id
long sequenceNumber = producer.getMessageSequence();
//Set the "JMS" header fields on the original message, see 1.1 spec section 3.4.11
message.setJMSDeliveryMode(deliveryMode); //在JMS协议头中设置是否持久化标识
long expiration = 0L;//计算消息过期时间
if (!producer.getDisableMessageTimestamp()) {
long timeStamp = System.currentTimeMillis();
message.setJMSTimestamp(timeStamp);
if (timeToLive > 0) {
expiration = timeToLive + timeStamp;
}
}
message.setJMSExpiration(expiration);//设置消息过期时间
message.setJMSPriority(priority);//设置消息的优先级
message.setJMSRedelivered(false);//设置消息为非重发
    // transform to our own message format here
//将不通的消息格式统一转化为ActiveMQMessage
ActiveMQMessage msg = ActiveMQMessageTransformation.transformMessage(message,
connection);
msg.setDestination(destination);//设置目的地
//生成并设置消息id
msg.setMessageId(new MessageId(producer.getProducerInfo().getProducerId(),
sequenceNumber));
// Set the message id.
if (msg != message) {//如果消息是经过转化的，则更新原来的消息id和目的地
message.setJMSMessageID(msg.getMessageId().toString());
// Make sure the JMS destination is set on the foreign messages too.
message.setJMSDestination(destination);
}
//clear the brokerPath in case we are re-sending this message
msg.setBrokerPath(null);
msg.setTransactionId(txid);
if (connection.isCopyMessageOnSend()) {
msg = (ActiveMQMessage)msg.copy();
}
msg.setConnection(connection);
msg.onSend();//把消息属性和消息体都设置为只读，防止被修改
msg.setProducerId(msg.getMessageId().getProducerId());
if (LOG.isTraceEnabled()) {
	LOG.trace(getSessionId() + " sending message: " + msg);
}
    //如果onComplete没有设置，且发送超时时间小于0，且消息不需要反馈，且连接器不是同步发送模式，且消息非持久
化或者连接器是异步发送模式
//或者存在事务id的情况下，走异步发送，否则走同步发送
if (onComplete==null && sendTimeout <= 0 && !msg.isResponseRequired() &&
!connection.isAlwaysSyncSend() && (!msg.isPersistent() || connection.isUseAsyncSend() || txid !=
null)) {
this.connection.asyncSendPacket(msg);
if (producerWindow != null) {
// Since we defer lots of the marshaling till we hit the
// wire, this might not
// provide and accurate size. We may change over to doing
// more aggressive marshaling,
// to get more accurate sizes.. this is more important once
// users start using producer window
// flow control.
int size = msg.getSize(); //异步发送的情况下，需要设置producerWindow的大小
producerWindow.increaseUsage(size);
}
} else {
if (sendTimeout > 0 && onComplete==null) {
this.connection.syncSendPacket(msg,sendTimeout); //带超时时间的同步发送
}else {
this.connection.syncSendPacket(msg, onComplete); //带回调的同步发送
}
}
}
}
```

**ActiveMQConnection.doAsyncSendPacket**

```java
private void doAsyncSendPacket(Command command) throws JMSException {
    try {
   		 this.transport.oneway(command);
    } catch (IOException e) {
   		 throw JMSExceptionSupport.create(e);
    }
}
```

这个地方问题来了，this.transport是什么东西？在哪里实例化的？按照以前看源码的惯例来看，它肯定不是一个
单纯的对象。

按照以往我们看源码的经验来看，一定是在创建连接的过程中初始化的。所以我们定位到代码

**transprot的实例化过程**

从connection=connectionFactory.createConnection();这行代码作为入口，一直跟踪到
ActiveMQConnectionFactory. createActiveMQConnection这个方法中。代码如下

```java
protected ActiveMQConnection createActiveMQConnection(String userName, String password) throws JMSException {
        if (this.brokerURL == null) {
            throw new ConfigurationException("brokerURL not set.");
        } else {
            ActiveMQConnection connection = null;

            try {
                Transport transport = this.createTransport();
                connection = this.createActiveMQConnection(transport, this.factoryStats);
                connection.setUserName(userName);
                connection.setPassword(password);
                this.configureConnection(connection);
                transport.start();
                if (this.clientID != null) {
                    connection.setDefaultClientID(this.clientID);
                }

                return connection;
            } catch (JMSException var8) {
                try {
                    connection.close();
                } catch (Throwable var6) {
                }

                throw var8;
            } catch (Exception var9) {
                try {
                    connection.close();
                } catch (Throwable var7) {
                }

                throw JMSExceptionSupport.create("Could not connect to broker URL: " + this.brokerURL + ". Reason: " + var9, var9);
            }
        }
    }
```



**createTransport**

调用ActiveMQConnectionFactory.createTransport方法，去创建一个transport对象。

1. 构建一个URI
2. 根据URL去创建一个连接TransportFactory.connect

Ø 默认使用的是tcp的协议

```java
protected Transport createTransport() throws JMSException {
    try {
   	 	URI connectBrokerUL = brokerURL;
    	String scheme = brokerURL.getScheme();
        if (scheme == null) {
       	 throw new IOException("Transport not scheme specified: [" + brokerURL + "]");
        }
        if (scheme.equals("auto")) {
        	connectBrokerUL = new URI(brokerURL.toString().replace("auto", "tcp"));
        } else if (scheme.equals("auto+ssl")) {
        	connectBrokerUL = new URI(brokerURL.toString().replace("auto+ssl", "ssl"));
        } else if (scheme.equals("auto+nio")) {
        	connectBrokerUL = new URI(brokerURL.toString().replace("auto+nio", "nio"));
        } else if (scheme.equals("auto+nio+ssl")) {
        	connectBrokerUL = new URI(brokerURL.toString().replace("auto+nio+ssl", "nio+ssl"));
        }
    	return TransportFactory.connect(connectBrokerUL);
    } catch (Exception e) {
    	throw JMSExceptionSupport.create("Could not create Transport. Reason: " + e, e);
    }
}
```

**TransportFacotry.findTransportFactory**

1. 从TRANSPORT_FACTORYS这个Map集合中，根据scheme去获得一个TransportFactory指定的实例对象
2. 如果Map集合中不存在，则通过TRANSPORT_FACTORY_FINDER去找一个并且构建实例

Ø 这个地方又有点类似于我们之前所学过的SPI的思想吧？他会从METAINF/

services/org/apache/activemq/transport/ 这个路径下，根据URI组装的scheme去找到匹配的class对象并且
实例化，所以根据tcp为key去对应的路径下可以找到TcpTransportFactory

```java
public static TransportFactory findTransportFactory(URI location) throws IOException {
String scheme = location.getScheme();
if (scheme == null) {
throw new IOException("Transport not scheme specified: [" + location + "]");
}
TransportFactory tf = TRANSPORT_FACTORYS.get(scheme);
if (tf == null) {
// Try to load if from a META-INF property.
try {
tf = (TransportFactory)TRANSPORT_FACTORY_FINDER.newInstance(scheme);
TRANSPORT_FACTORYS.put(scheme, tf);
} catch (Throwable e) {
throw IOExceptionSupport.create("Transport scheme NOT recognized: [" + scheme + "]",
e);
}
}
return tf;
}
```

**调用TransportFactory.doConnect去构建一个连接**

```java
public Transport doConnect(URI location) throws Exception {
    try {
    Map<String, String> options = new HashMap<String, String>
    (URISupport.parseParameters(location));
    if( !options.containsKey("wireFormat.host") ) {
    options.put("wireFormat.host", location.getHost());
    }
    WireFormat wf = createWireFormat(options);
    Transport transport = createTransport(location, wf); //创建一个Transport，创建一个socket连
    接 -> 终于找到真相了
    Transport rc = configure(transport, wf, options);//配置configure，这个里面是对Transport做
    链路包装
    //remove auto
    IntrospectionSupport.extractProperties(options, "auto.");
    if (!options.isEmpty()) {
    throw new IllegalArgumentException("Invalid connect parameters: " + options);
    }
        return rc;
} catch (URISyntaxException e) {
throw IOExceptionSupport.create(e);
}
}
```

**configure**

```java
public Transport configure(Transport transport, WireFormat wf, Map options) throws Exception {
//组装一个复合的transport，这里会包装两层，一个是IactivityMonitor.另一个是WireFormatNegotiator
transport = compositeConfigure(transport, wf, options);
transport = new MutexTransport(transport); //再做一层包装,MutexTransport
transport = new ResponseCorrelator(transport); //包装ResponseCorrelator
return transport;
}
```

到目前为止，这个transport实际上就是一个调用链了，他的链结构为
ResponseCorrelator(MutexTransport(WireFormatNegotiator(IactivityMonitor(TcpTransport()))
每一层包装表示什么意思呢？
ResponseCorrelator 用于实现异步请求。
MutexTransport 实现写锁，表示同一时间只允许发送一个请求
WireFormatNegotiator 实现了客户端连接broker的时候先发送数据解析相关的协议信息，比如解析版本号，是否
使用缓存等
InactivityMonitor 用于实现连接成功成功后的心跳检查机制，客户端每10s发送一次心跳信息。服务端每30s读取
一次心跳信息。

**同步发送和异步发送的区别**

```java
public Object request(Object command, int timeout) throws IOException {
FutureResponse response = asyncRequest(command, null);
return response.getResult(timeout); // 从future方法阻塞等待返回
}
```

在ResponseCorrelator的request方法中，需要通过response.getResult去获得broker的反馈，否则会阻塞

#### 4、持久化消息和非持久化消息的存储原理

正常情况下，非持久化消息是存储在内存中的，持久化消息是存储在文件中的。能够存储的最大消息数据在
${ActiveMQ_HOME}/conf/activemq.xml文件中的systemUsage节点

SystemUsage配置设置了一些系统内存和硬盘容量

```xml
<systemUsage>
    <systemUsage>
        	<memoryUsage>
        //该子标记设置整个ActiveMQ节点的“可用内存限制”。这个值不能超过ActiveMQ本身设置的最大内存大小。其中的percentOfJvmHeap属性表示百分比。占用70%的堆内存
           		 <memoryUsage percentOfJvmHeap="70" />
            </memoryUsage>
        <storeUsage>
        //该标记设置整个ActiveMQ节点，用于存储“持久化消息”的“可用磁盘空间”。该子标记的limit属性必须要进行设置
            <storeUsage limit="100 gb"/>
            </storeUsage>
            <tempUsage>
            //一旦ActiveMQ服务节点存储的消息达到了memoryUsage的限制，非持久化消息就会被转储到 temp store区域，虽然
            我们说过非持久化消息不进行持久化存储，但是ActiveMQ为了防止“数据洪峰”出现时非持久化消息大量堆积致使内存耗
            尽的情况出现，还是会将非持久化消息写入到磁盘的临时区域——temp store。这个子标记就是为了设置这个temp
            store区域的“可用磁盘空间限制”
            <tempUsage limit="50 gb"/>
        </tempUsage>
    </systemUsage>
</systemUsage>
```

Ø 从上面的配置我们需要get到一个结论，当非持久化消息堆积到一定程度的时候，也就是内存超过指定的设置阀
值时，ActiveMQ会将内存中的非持久化消息写入到临时文件，以便腾出内存。但是它和持久化消息的区别是，重
启之后，持久化消息会从文件中恢复，非持久化的临时文件会直接删除



### 消息从持久化策略分析

消息持久性对于可靠消息传递来说是一种比较好的方法，即时发送者和接受者不是同时在线或者消息中心在发送者
发送消息后宕机了，在消息中心重启后仍然可以将消息发送出去。消息持久性的原理很简单，就是在发送消息出去
后，消息中心首先将消息存储在本地文件、内存或者远程数据库，然后把消息发送给接受者，发送成功后再把消息
从存储中删除，失败则继续尝试。接下来我们来了解一下消息在broker上的持久化存储实现方式

#### 1、持久化存储支持类型

ActiveMQ支持多种不同的持久化方式，主要有以下几种，不过，无论使用哪种持久化方式，消息的存储逻辑都是
一致的。
Ø KahaDB存储（默认存储方式）
Ø JDBC存储
Ø Memory存储
Ø LevelDB存储

Ø JDBC With ActiveMQ Journal

#### 2、KahaDB存储

KahaDB是目前默认的存储方式,可用于任何场景,提高了性能和恢复能力。消息存储使用一个事务日志和仅仅用一个索引文件来存储它所有的地址。
KahaDB是一个专门针对消息持久化的解决方案,它对典型的消息使用模式进行了优化。在Kaha中,数据被追加到
data logs中。当不再需要log文件中的数据的时候,log文件会被丢弃。

**KahaDB的配置方式**

```java
<persistenceAdapter>
	<kahaDB directory="${activemq.data}/kahadb"/>
</persistenceAdapter>
```

**KahaDB的存储原理**

在data/kahadb这个目录下，会生成四个文件
Ø db.data 它是消息的索引文件，本质上是B-Tree（B树），使用B-Tree作为索引指向db-*.log里面存储的消息
Ø db.redo 用来进行消息恢复
Ø db-*.log 存储消息内容。新的数据以APPEND的方式追加到日志文件末尾。属于顺序写入，因此消息存储是比较
快的。默认是32M，达到阀值会自动递增
Ø lock文件 锁，表示当前获得kahadb读写权限的broker

#### 3、JDBC存储

使用JDBC持久化方式，数据库会创建3个表：activemq_msgs，activemq_acks和activemq_lock。
ACTIVEMQ_MSGS 消息表，queue和topic都存在这个表中
ACTIVEMQ_ACKS 存储持久订阅的信息和最后一个持久订阅接收的消息ID
ACTIVEMQ_LOCKS 锁表，用来确保某一时刻，只能有一个ActiveMQ broker实例来访问数据库

**JDBC存储实践**

```properties
<persistenceAdapter>
<jdbcPersistenceAdapter dataSource="# MySQL-DS " createTablesOnStartup="true" />
</persistenceAdapter>
```

dataSource指定持久化数据库的bean，createTablesOnStartup是否在启动的时候创建数据表，默认值是true，这样每次启动都会去创建数据表了，一般是第一次启动的时候设置为true，之后改成false

```xml
<bean id="Mysql-DS" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://192.168.11.156:3306/activemq?
    relaxAutoCommit=true"/>
    <property name="username" value="root"/>
    <property name="password" value="root"/>
</bean>
```

添加jar包依赖

* common-pool-1.6.jar
* mysql-connector-java-5.1.35.jar
* commons-dbcp-1.4.jar

#### 4、LevelDB存储

LevelDB持久化性能高于KahaDB，虽然目前默认的持久化方式仍然是KahaDB。并且，在ActiveMQ 5.9版本提供
了基于LevelDB和Zookeeper的数据复制方式，用于Master-slave方式的首选数据复制方案。

不过，据ActiveMQ官网对LevelDB的表述：LevelDB官方建议使用以及不再支持，推荐使用的是KahaDB

```xml
<persistenceAdapter>
<levelDBdirectory="activemq-data"/>
</persistenceAdapter>
```



#### 5、Memory消息存储

基于内存的消息存储，内存消息存储主要是存储所有的持久化的消息在内存中。persistent=”false”,表示不设置持
久化存储，直接存储到内存中

```xml
<beans>
<broker brokerName="test-broker" persistent="false"
xmlns="http://activemq.apache.org/schema/core">
<transportConnectors>
<transportConnector uri="tcp://localhost:61635"/>
</transportConnectors> </broker>
</beans>
```



#### 6、JDBC Message store with ActiveMQ Journal

这种方式克服了JDBC Store的不足，JDBC每次消息过来，都需要去写库和读库。
ActiveMQ Journal，使用高速缓存写入技术，大大提高了性能。
当消费者的消费速度能够及时跟上生产者消息的生产速度时，journal文件能够大大减少需要写入到DB中的消息。
举个例子，生产者生产了1000条消息，这1000条消息会保存到journal文件，如果消费者的消费速度很快的情况
下，在journal文件还没有同步到DB之前，消费者已经消费了90%的以上的消息，那么这个时候只需要同步剩余的
10%的消息到DB。
如果消费者的消费速度很慢，这个时候journal文件可以使消息以批量方式写到DB。
Ø 将原来的标签注释掉
Ø 添加如下标签

```xml
<persistenceFactory>
<journalPersistenceAdapterFactory dataSource="#Mysql-DS" dataDirectory="activemqdata"/>
</persistenceFactory>
```

Ø 在服务端循环发送消息。可以看到数据是延迟同步到数据库的

### 消费端消费消息的原理

我们通过上一节课的讲解，知道有两种方法可以接收消息，一种是使用同步阻塞的MessageConsumer#receive方
法。另一种是使用消息监听器MessageListener。这里需要注意的是，在同一个session下，这两者不能同时工
作，也就是说不能针对不同消息采用不同的接收方式。否则会抛出异常。
至于为什么这么做，最大的原因还是在事务性会话中，两种消费模式的事务不好管控

#### 消费端消费消息源码分析

##### 1、ActiveMQMessageConsumer.receive

消费端同步接收消息的源码入口

```java 
public Message receive() throws JMSException {
    checkClosed();
    checkMessageListener(); //检查receive和MessageListener是否同时配置在当前的会话中
    sendPullCommand(0); //如果PrefetchSizeSize为0并且unconsumerMessage为空，则发起pull命令
    MessageDispatch md = dequeue(-1); //从unconsumerMessage出队列获取消息
    if (md == null) {
    return null;
    }
    beforeMessageIsConsumed(md);
    afterMessageIsConsumed(md, false); //发送ack给到broker
    return createActiveMQMessage(md);//获取消息并返回
}
```



##### 2、sendPullCommand

发送pull命令从broker上获取消息，前提是prefetchSize=0并且unconsumedMessages为空。
unconsumedMessage表示未消费的消息，这里面预读取的消息大小为prefetchSize的值

```java
protected void sendPullCommand(long timeout) throws JMSException {
    clearDeliveredList();
    if (info.getCurrentPrefetchSize() == 0 && unconsumedMessages.isEmpty()) {
        MessagePull messagePull = new MessagePull();
        messagePull.configure(info);
        messagePull.setTimeout(timeout);
        session.asyncSendPacket(messagePull); //向服务端异步发送messagePull指令
    }
}
```

**clearDeliveredList**

在上面的sendPullCommand方法中，会先调用clearDeliveredList方法，主要用来清理已经分发的消息链表
deliveredMessages
deliveredMessages，存储分发给消费者但还为应答的消息链表
Ø 如果session是事务的，则会遍历deliveredMessage中的消息放入到previouslyDeliveredMessage中来做重发
Ø 如果session是非事务的，根据ACK的模式来选择不同的应答操作

```java 
private void clearDeliveredList() {
    if (clearDeliveredList) {
    synchronized (deliveredMessages) {
    if (clearDeliveredList) {
    if (!deliveredMessages.isEmpty()) {
    if (session.isTransacted()) {
        if (previouslyDeliveredMessages == null) {
previouslyDeliveredMessages = new PreviouslyDeliveredMap<MessageId,
Boolean>(session.getTransactionContext().getTransactionId());
}
for (MessageDispatch delivered : deliveredMessages) {
previouslyDeliveredMessages.put(delivered.getMessage().getMessageId(), false);
}
LOG.debug("{} tracking existing transacted {} delivered list ({}) on
transport interrupt",
getConsumerId(), previouslyDeliveredMessages.transactionId,
deliveredMessages.size());
} else {
if (session.isClientAcknowledge()) {
LOG.debug("{} rolling back delivered list ({}) on transport
interrupt", getConsumerId(), deliveredMessages.size());
// allow redelivery
          if (!this.info.isBrowser()) {
for (MessageDispatch md: deliveredMessages) {
this.session.connection.rollbackDuplicate(this,
md.getMessage());
}
}
}
LOG.debug("{} clearing delivered list ({}) on transport interrupt",
getConsumerId(), deliveredMessages.size());
deliveredMessages.clear();
pendingAck = null;
}
}
clearDeliveredList = false;
}
}
}
}
```

##### 3、dequeue

从unconsumedMessage中取出一个消息，在创建一个消费者时，就会未这个消费者创建一个未消费的消息通道，这个通道分为两种，一种是简单优先级队列分发通道SimplePriorityMessageDispatchChannel ；另一种是先进先出的分发通道FifoMessageDispatchChannel.
至于为什么要存在这样一个消息分发通道，大家可以想象一下，如果消费者每次去消费完一个消息以后再去broker拿一个消息，效率是比较低的。所以通过这样的设计可以允许session能够一次性将多条消息分发给一个消费者。
默认情况下对于queue来说，prefetchSize的值是1000

##### 4、beforeMessageConsumed

这里面主要是做消息消费之前的一些准备工作，如果ACK类型不是DUPS_OK_ACKNOWLEDGE或者队列模式（简单来说就是除了Topic和DupAck这两种情况），所有的消息先放到deliveredMessages链表的开头。并且如果当前是事务类型的会话，则判断transactedIndividualAck，如果为true，表示单条消息直接返回ack。
否则，调用ackLater，批量应答, client端在消费消息后暂且不发送ACK，而是把它缓存下来(pendingACK)，等到这些消息的条数达到一定阀值时，只需要通过一个ACK指令把它们全部确认；这比对每条消息都逐个确认，在性能上要提高很多

```java
private void beforeMessageIsConsumed(MessageDispatch md) throws JMSException {
    md.setDeliverySequenceId(session.getNextDeliveryId());
    lastDeliveredSequenceId = md.getMessage().getMessageId().getBrokerSequenceId();
    if (!isAutoAcknowledgeBatch()) {
        synchronized(deliveredMessages) {
        deliveredMessages.addFirst(md);
        }
        if (session.getTransacted()) {
            if (transactedIndividualAck) {
                immediateIndividualTransactedAck(md);
            } else {
                ackLater(md, MessageAck.DELIVERED_ACK_TYPE);
            }
        }
    }
}
```

##### 5、afterMessagelsConsumed

这个方法的主要作用是执行应答操作，这里面做以下几个操作
Ø 如果消息过期，则返回消息过期的ack
Ø 如果是事务类型的会话，则不做任何处理
Ø 如果是AUTOACK或者（DUPS_OK_ACK且是队列），并且是优化ack操作，则走批量确认ack
Ø 如果是DUPS_OK_ACK，则走ackLater逻辑
Ø 如果是CLIENT_ACK，则执行ackLater

```java
private void afterMessageIsConsumed(MessageDispatch md, boolean messageExpired) throws
JMSException {
    if (unconsumedMessages.isClosed()) {
    return;
    }
if (messageExpired) {
acknowledge(md, MessageAck.EXPIRED_ACK_TYPE);
stats.getExpiredMessageCount().increment();
} else {
stats.onMessage();
if (session.getTransacted()) {
// Do nothing.
} else if (isAutoAcknowledgeEach()) {
if (deliveryingAcknowledgements.compareAndSet(false, true)) {
synchronized (deliveredMessages) {
if (!deliveredMessages.isEmpty()) {
if (optimizeAcknowledge) {
	ackCounter++;
// AMQ-3956 evaluate both expired and normal msgs as
// otherwise consumer may get stalled
    if (ackCounter + deliveredCounter >= (info.getPrefetchSize() * .65)
    || (optimizeAcknowledgeTimeOut > 0 && System.currentTimeMillis() >= (optimizeAckTimestamp +
    optimizeAcknowledgeTimeOut))) {
        MessageAck ack =
        makeAckForAllDeliveredMessages(MessageAck.STANDARD_ACK_TYPE);
        if (ack != null) {
            deliveredMessages.clear();
            ackCounter = 0;
            session.sendAck(ack);
            optimizeAckTimestamp = System.currentTimeMillis();
        }
// AMQ-3956 - as further optimization send
// ack for expired msgs when there are any.
// This resets the deliveredCounter to 0 so that
// we won't sent standard acks with every msg just
// because the deliveredCounter just below
// 0.5 * prefetch as used in ackLater()
    if (pendingAck != null && deliveredCounter > 0) {
        session.sendAck(pendingAck);
        pendingAck = null;
        deliveredCounter = 0;
    }
}
    } else {
        MessageAck ack =
        makeAckForAllDeliveredMessages(MessageAck.STANDARD_ACK_TYPE);
        if (ack!=null) {
        deliveredMessages.clear();
        session.sendAck(ack);
    }
}
}
}
deliveryingAcknowledgements.set(false);
}
} else if (isAutoAcknowledgeBatch()) {
	ackLater(md, MessageAck.STANDARD_ACK_TYPE);
} else if (session.isClientAcknowledge()||session.isIndividualAcknowledge()) {
    boolean messageUnackedByConsumer = false;
    synchronized (deliveredMessages) {
    messageUnackedByConsumer = deliveredMessages.contains(md);
}
    if (messageUnackedByConsumer) {
    	ackLater(md, MessageAck.DELIVERED_ACK_TYPE);
    }
}else {
	throw new IllegalStateException("Invalid session state.");
}
}
}
```

## 第三节

### unconsumedMessages数据的获取过程

那我们来看看ActiveMQConnectionFactory.createConnection里面做了什么事情

```java
 public Connection createConnection() throws JMSException {
        return this.createActiveMQConnection();
 }
```

1、动态创建一个传输协议

2、创建一个连接

3、通过transprot.start()

```java
protected ActiveMQConnection createActiveMQConnection(String userName, String password) throws JMSException {
        if (this.brokerURL == null) {
            throw new ConfigurationException("brokerURL not set.");
        } else {
            ActiveMQConnection connection = null;

            try {
                Transport transport = this.createTransport();//动态创建一个协议
                //创建一个连接
                connection = this.createActiveMQConnection(transport, this.factoryStats);
                connection.setUserName(userName);
                connection.setPassword(password);
                this.configureConnection(connection);
                transport.start();
                if (this.clientID != null) {
                    connection.setDefaultClientID(this.clientID);
                }

                return connection;
            } catch (JMSException var8) {
                try {
                    connection.close();
                } catch (Throwable var6) {
                }

                throw var8;
            } catch (Exception var9) {
                try {
                    connection.close();
                } catch (Throwable var7) {
                }

                throw JMSExceptionSupport.create("Could not connect to broker URL: " + this.brokerURL + ". Reason: " + var9, var9);
            }
        }
    }
```

#### transprot.start()

我们前面在分析消息发送的时候，已经知道transprot是一个链式 的调用，是一个多层包装的对象

ResonseCorrelator(MutexTransprot(WireFormatNegotiator(InactivityMonitor(TcpTransprot()))))

最终调用TcpTransprot.start()方法，然而这个类中并没有start，而是在父类ServiceSupport.start()中。

```java
public void start() throws Exception {
        if (this.started.compareAndSet(false, true)) {
            boolean success = false;
            this.stopped.set(false);

            try {
                this.preStart();
                this.doStart();
                success = true;
            } finally {
                this.started.set(success);
            }

            Iterator i$ = this.serviceListeners.iterator();

            while(i$.hasNext()) {
                ServiceListener l = (ServiceListener)i$.next();
                l.started(this);
            }
        }

    }
```

这块代码看起来就比较熟悉了，我们之前看过的中间件的源码，通信层都是独立来实现及解耦的。而ActiveMQ也是一样，提供了Transport接口和TransportSupport类。这个接口的主要作用是为了让客户端有消息被异步发送、同步发送和被消费的能力。接下来沿着doStart()往下看，又调用TcpTransport.doStart() ,接着通过super.doStart(),调用TransportThreadSupport.doStart(). 创建了一个线程，传入的是this，调用子类的run方法，也就是TcpTransport.run().

```java
TcpTransport:
protected void doStart() throws Exception {
        this.connect();
        this.stoppedLatch.set(new CountDownLatch(1));
        super.doStart();
    }
```

```java
TransportThreadSupport： 
protected void doStart() throws Exception {
        this.runner = new Thread((ThreadGroup)null, this, "ActiveMQ Transport: " + this.toString(), this.stackSize);
        this.runner.setDaemon(this.daemon);
        this.runner.start();
    }
```



#### TcpTransport.run

run方法主要是从socket中读取数据包，只要TcpTransprot没有停止，它就会不断去调用doRun.

```java
public void run() {
        LOG.trace("TCP consumer thread for " + this + " starting");
        this.runnerThread = Thread.currentThread();

        try {
            while(!this.isStopped()) {
                this.doRun();
            }
        } catch (IOException var7) {
            ((CountDownLatch)this.stoppedLatch.get()).countDown();
            this.onException(var7);
        } catch (Throwable var8) {
            ((CountDownLatch)this.stoppedLatch.get()).countDown();
            IOException ioe = new IOException("Unexpected error occured: " + var8);
            ioe.initCause(var8);
            this.onException(ioe);
        } finally {
            ((CountDownLatch)this.stoppedLatch.get()).countDown();
        }

    }
```



#### TcpTransport.doRun

doRun中，通过readCommand中读取数据

```java
TcpTransport:
protected void doRun() throws IOException {
        try {
            Object command = this.readCommand();
            this.doConsume(command);
        } catch (SocketTimeoutException var2) {
        } catch (InterruptedIOException var3) {
        }

    }
```



#### TcpTransprot.readCommend

这里面，通过wireFormat对数据进行格式化，可以认为这是一个反序列化过程。wireFormat默认实现是OpenWireFormat，activeMQ自定义的跨语言的wire协议

```java
  protected Object readCommand() throws IOException {
        return this.wireFormat.unmarshal(this.dataIn);
    }
```

分析到这，我们差不多明白了传输层的主要工作是获得数据并且把数据转换为对象，再把对象对象传给ActiveMQConnection

#### TransprotSupport.doConsume

TransportSupport类中最重要的方法是doConsume，它的作用就是用来“消费消息”

```java
  public void doConsume(Object command) {
        if (command != null) {
            if (this.transportListener != null) {
                this.transportListener.onCommand(command);
            } else {
                LOG.error("No transportListener available to process inbound command: " + command);
            }
        }

    }
```

TransportSupport类中唯一的成员变量是TransportListener

transportListener;，这也意味着一个Transport支持类绑定一个传送监听器类，传送监听器接口TransportListener 最重要的方法就是 void onCommand(Object command);，它用来处理命令，
这个transportListener是在哪里赋值的呢？再回到ActiveMQConnection的构造方法中。->246行
传递了ActiveMQConnection自己本身，(ActiveMQConnection是TransportListener接口的实现类之一)
于是，消息就这样从传送层到达了我们的连接层上。

```java
protected ActiveMQConnection(final Transport transport, IdGenerator clientIdGenerator, IdGenerator connectionIdGenerator, JMSStatsImpl factoryStats) throws Exception {
        this.maxThreadPoolSize = DEFAULT_THREAD_POOL_SIZE;
        this.rejectedTaskHandler = null;
        this.transport = transport;
        this.clientIdGenerator = clientIdGenerator;
        this.factoryStats = factoryStats;
        this.executor = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "ActiveMQ Connection Executor: " + transport);
                return thread;
            }
        });
        String uniqueId = connectionIdGenerator.generateId();
        this.info = new ConnectionInfo(new ConnectionId(uniqueId));
        this.info.setManageable(true);
        this.info.setFaultTolerant(transport.isFaultTolerant());
        this.connectionSessionId = new SessionId(this.info.getConnectionId(), -1L);
        this.transport.setTransportListener(this);
        this.stats = new JMSConnectionStatsImpl(this.sessions, this instanceof XAConnection);
        this.factoryStats.addConnection(this);
        this.timeCreated = System.currentTimeMillis();
        this.connectionAudit.setCheckForDuplicates(transport.isFaultTolerant());
    }
```

从构造函数可以看出，创建ActiveMQConnection对象时，除了和Transport相互绑定，还对线程池执行器executor进行了初始化。下面我们看看该类的核心方法

#### onCommand

Ø 这里面会针对不同的消息做分发，比如传入的command是MessageDispatch，那么这个command的visit方法就会调用processMessageDispatch方法

```java
ActiveMQConnection:
public void onCommand(Object o) {
    final Command command = (Command)o;
    if (!this.closed.get() && command != null) {
        try {
            command.visit(new CommandVisitorAdapter() {
                public Response processMessageDispatch(MessageDispatch md) throws Exception {
                    // 等待Transport中断处理完成
                    ActiveMQConnection.this.waitForTransportInterruptionProcessingToComplete();
                    //通过消防者ID来获取消费者对象
                    //（ActiveMQMessageConsumer实现了ActiveMQDispatcher接口），所以MessageDispatch包含了消息应该被分配到那个消费者的映射信息
                    //在创建MessageConsumer的时候，调用ActiveMQMessageConsumer的第282行，调用ActiveMQSession的1798行将当前的消费者绑定到dispatchers中 所以这里拿到的是ActiveMQSession
                    ActiveMQDispatcher dispatcher = (ActiveMQDispatcher)ActiveMQConnection.this.dispatchers.get(md.getConsumerId());
                    if (dispatcher != null) {
                        Message msg = md.getMessage();
                        if (msg != null) {
                            msg = msg.copy();
                            msg.setReadOnlyBody(true);
                            msg.setReadOnlyProperties(true);
                            msg.setRedeliveryCounter(md.getRedeliveryCounter());
                            msg.setConnection(ActiveMQConnection.this);
                            msg.setMemoryUsage((MemoryUsage)null);
                            md.setMessage(msg);
                        }
//调用会话ActiveMQSession自己的dispatch方法来处理这条消息
                        dispatcher.dispatch(md);
                    }

                    return null;
                }
//如果传入的是ProducerAck，则调用的是下面这个方法，这里我们仅仅关注MessageDispatch就行了
                public Response processProducerAck(ProducerAck pa) throws Exception {
                    if (pa != null && pa.getProducerId() != null) {
                        ActiveMQMessageProducer producer = (ActiveMQMessageProducer)ActiveMQConnection.this.producers.get(pa.getProducerId());
                        if (producer != null) {
                            producer.onProducerAck(pa);
                        }
                    }

                    return null;
                }

                public Response processBrokerInfo(BrokerInfo info) throws Exception {
                    ActiveMQConnection.this.brokerInfo = info;
                    ActiveMQConnection.this.brokerInfoReceived.countDown();
                    ActiveMQConnection.access$772(ActiveMQConnection.this, !ActiveMQConnection.this.brokerInfo.isFaultTolerantConfiguration() ? 1 : 0);
                    ActiveMQConnection.this.getBlobTransferPolicy().setBrokerUploadUrl(info.getBrokerUploadUrl());
                    return null;
                }

                public Response processConnectionError(final ConnectionError error) throws Exception {
                    ActiveMQConnection.this.executor.execute(new Runnable() {
                        public void run() {
                            ActiveMQConnection.this.onAsyncException(error.getException());
                        }
                    });
                    return null;
                }

                public Response processControlCommand(ControlCommand commandx) throws Exception {
                    ActiveMQConnection.this.onControlCommand(commandx);
                    return null;
                }

                public Response processConnectionControl(ConnectionControl control) throws Exception {
                    ActiveMQConnection.this.onConnectionControl((ConnectionControl)command);
                    return null;
                }

                public Response processConsumerControl(ConsumerControl control) throws Exception {
                    ActiveMQConnection.this.onConsumerControl((ConsumerControl)command);
                    return null;
                }

                public Response processWireFormat(WireFormatInfo info) throws Exception {
                    ActiveMQConnection.this.onWireFormatInfo((WireFormatInfo)command);
                    return null;
                }
            });
        } catch (Exception var5) {
            this.onClientInternalException(var5);
        }
    }

    Iterator iter = this.transportListeners.iterator();

    while(iter.hasNext()) {
        TransportListener listener = (TransportListener)iter.next();
        listener.onCommand(command);
    }

}
```

在现在这个场景中，我们只关注processMessageDispatch方法，在这个方法中，只是简单的去调用ActiveMQSession的dispatch方法来处理消息,

Ø tips: command.visit, 这里使用了适配器模式，如果command是一个MessageDispatch，那么它就会调用processMessageDispatch方法，其他方法他不会关心，代码如下：MessageDispatch.visit

```java
@Override 
public Response visit(CommandVisitor visitor) throws Exception { 
    return visitor.processMessageDispatch(this);
}
```



#### ActiveMQSession.dispatch(md)

executor这个对象其实是一个成员对象ActiveMQSessionExecutor，专门负责来处理消息分发

```java
 public void dispatch(MessageDispatch messageDispatch) {
        try {
            this.executor.execute(messageDispatch);
        } catch (InterruptedException var3) {
            Thread.currentThread().interrupt();
            this.connection.onClientInternalException(var3);
        }

    }
```



#### ActiveMQSessionExecutor.execute

Ø 这个方法的核心功能就是处理消息的分发。

```java
void execute(MessageDispatch message) throws InterruptedException {
        if (!this.startedOrWarnedThatNotStarted) {
            ActiveMQConnection connection = this.session.connection;
            long aboutUnstartedConnectionTimeout = connection.getWarnAboutUnstartedConnectionTimeout();
            if (!connection.isStarted() && aboutUnstartedConnectionTimeout >= 0L) {
                long elapsedTime = System.currentTimeMillis() - connection.getTimeCreated();
                if (elapsedTime > aboutUnstartedConnectionTimeout) {
                    LOG.warn("Received a message on a connection which is not yet started. Have you forgotten to call Connection.start()? Connection: " + connection + " Received: " + message);
                    this.startedOrWarnedThatNotStarted = true;
                }
            } else {
                this.startedOrWarnedThatNotStarted = true;
            }
        }

        if (!this.session.isSessionAsyncDispatch() && !this.dispatchedBySessionPool) {
            this.dispatch(message);
        } else {
            this.messageQueue.enqueue(message);
            this.wakeup();
        }

    }
```

默认是采用异步消息分发。所以，直接调用messageQueue.enqueue，把消息放到队列中，并且调用wakeup方法

#### 异步分发的流程

```java
public void wakeup() {
        if (!this.dispatchedBySessionPool) {//进一步验证
            if (this.session.isSessionAsyncDispatch()) {//判断session是否为异步分发
                try {
                    TaskRunner taskRunner = this.taskRunner;
                    if (taskRunner == null) {
                        synchronized(this) {
                            if (this.taskRunner == null) {
                                if (!this.isRunning()) {
                                    return;
                                }
//通过TaskRunnerFactory创建了一个任务运行类taskRunner，这里把自己作为一个task传入到createTaskRunner中，说明当前 //的类一定是实现了Task接口的. 简单来说，就是通过线程池去执行一个任务，完成异步调度，简单吧
                                this.taskRunner = this.session.connection.getSessionTaskRunner().createTaskRunner(this, "ActiveMQ Session: " + this.session.getSessionId());
                            }

                            taskRunner = this.taskRunner;
                        }
                    }

                    taskRunner.wakeup();
                } catch (InterruptedException var5) {
                    Thread.currentThread().interrupt();
                }
            } else {
                while(true) {
                    if (this.iterate()) {
                        continue;
                    }
                }
            }
        }

    }
```

所以，对于异步分发的方式，会调用ActiveMQSessionExecutor中的iterate方法，我们来看看这个方法的代码

##### iterate

这个方法里面做两个事
Ø 把消费者监听的所有消息转存到待消费队列中
Ø 如果messageQueue还存在遗留消息，同样把消息分发出去

```java
public boolean iterate() { 
    // Deliver any messages queued on the consumer to their listeners. 
    for (ActiveMQMessageConsumer consumer : this.session.consumers) { 
        if (consumer.iterate()) {
            return true;
        }
    } 
    // No messages left queued on the listeners.. so now dispatch messages 
    // queued on the session
    MessageDispatch message = messageQueue.dequeueNoWait(); 
    if (message == null) { 
        return false;
    } else { 
        dispatch(message); 
        return !messageQueue.isEmpty();
    }
}
```



##### ActiveMQMessageConsumer.dispatch

```java
public boolean iterate() {
        MessageListener listener = (MessageListener)this.messageListener.get();
        if (listener != null) {
            MessageDispatch md = this.unconsumedMessages.dequeueNoWait();
            if (md != null) {
                this.dispatch(md);
                return true;
            }
        }

        return false;
    }
```



#### 同步分发的流程

同步分发的流程，直接调用ActiveMQSessionExcutor中的dispatch方法，代码如下

```java 
void dispatch(MessageDispatch message) { 
    // TODO - we should use a Map for this indexed by consumerId 
    for (ActiveMQMessageConsumer consumer : this.session.consumers) { 
        ConsumerId consumerId = message.getConsumerId(); 
        if (consumerId.equals(consumer.getConsumerId())) { 
            consumer.dispatch(message); break; 
        }
    }
}
```

#### ActiveMQMessageConsumer.dispathc

调用ActiveMQMessageConsumer.dispatch方法，把消息转存到unconsumedMessages消息队列中。

```java
public void dispatch(MessageDispatch md) {
        MessageListener listener = (MessageListener)this.messageListener.get();

        try {
            this.clearMessagesInProgress();
            this.clearDeliveredList();
            synchronized(this.unconsumedMessages.getMutex()) {
                if (!this.unconsumedMessages.isClosed()) {
                    if (!this.info.isBrowser() && this.session.connection.isDuplicate(this, md.getMessage())) {
                        if (!this.session.isTransacted()) {
                            LOG.warn("Duplicate non transacted dispatch to consumer: " + this.getConsumerId() + ", poison acking: " + md);
                            MessageAck poisonAck = new MessageAck(md, (byte)1, 1);
                            poisonAck.setFirstMessageId(md.getMessage().getMessageId());
                            poisonAck.setPoisonCause(new Throwable("Duplicate non transacted delivery to " + this.getConsumerId()));
                            this.session.sendAck(poisonAck);
                        } else {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(this.getConsumerId() + " tracking transacted redelivery of duplicate: " + md.getMessage());
                            }

                            boolean needsPoisonAck = false;
                            synchronized(this.deliveredMessages) {
                                if (this.previouslyDeliveredMessages != null) {
                                    this.previouslyDeliveredMessages.put(md.getMessage().getMessageId(), true);
                                } else {
                                    needsPoisonAck = true;
                                }
                            }

                            if (needsPoisonAck) {
                                MessageAck poisonAck = new MessageAck(md, (byte)1, 1);
                                poisonAck.setFirstMessageId(md.getMessage().getMessageId());
                                poisonAck.setPoisonCause(new JMSException("Duplicate dispatch with transacted redeliver pending on another consumer, connection: " + this.session.getConnection().getConnectionInfo().getConnectionId()));
                                LOG.warn("acking duplicate delivery as poison, redelivery must be pending to another consumer on this connection, failoverRedeliveryWaitPeriod=" + this.failoverRedeliveryWaitPeriod + ". Message: " + md + ", poisonAck: " + poisonAck);
                                this.session.sendAck(poisonAck);
                            } else if (this.transactedIndividualAck) {
                                this.immediateIndividualTransactedAck(md);
                            } else {
                                this.session.sendAck(new MessageAck(md, (byte)0, 1));
                            }
                        }
                    } else if (listener != null && this.unconsumedMessages.isRunning()) {
                        ActiveMQMessage message = this.createActiveMQMessage(md);
                        this.beforeMessageIsConsumed(md);

                        try {
                            boolean expired = message.isExpired();
                            if (!expired) {
                                listener.onMessage(message);
                            }

                            this.afterMessageIsConsumed(md, expired);
                        } catch (RuntimeException var9) {
                            LOG.error(this.getConsumerId() + " Exception while processing message: " + md.getMessage().getMessageId(), var9);
                            if (!this.isAutoAcknowledgeBatch() && !this.isAutoAcknowledgeEach() && !this.session.isIndividualAcknowledge()) {
                                this.afterMessageIsConsumed(md, false);
                            } else {
                                md.setRollbackCause(var9);
                                this.rollback();
                            }
                        }
                    } else {
                        if (!this.unconsumedMessages.isRunning()) {
                            this.session.connection.rollbackDuplicate(this, md.getMessage());
                        }

                        this.unconsumedMessages.enqueue(md);
                        if (this.availableListener != null) {
                            this.availableListener.onMessageAvailable(this);
                        }
                    }
                }
            }

            if (++this.dispatchedCount % 1000 == 0) {
                this.dispatchedCount = 0;
                Thread.yield();
            }
        } catch (Exception var11) {
            this.session.connection.onClientInternalException(var11);
        }

    }
```

Ø 到这里为止，消息如何接受以及他的处理方式的流程，我们已经搞清楚了，希望对大家理解activeMQ的核心机制有一定的帮助

### 消费端的PrefetchSize

#### 原理剖析

activemq的consumer端也有窗口机制，通过prefetchSize就可以设置窗口大小。不同的类型的队列，prefetchSize的默认值也是不一样的

Ø 持久化队列和非持久化队列的默认值为 1000

Ø 持久化topic默认值为100
Ø 非持久化队列的默认值为Short.MAX_VALUE-1
通过上面的例子，我们基本上应该知道prefetchSize的作用了，消费端会根据prefetchSize的大小批量获取数据，比如默认值是1000，那么消费端会预先加载1000条数据到本地的内存中。

##### prefetchSize的设置方法

在createQueue中添加consumer.prefetchSize，就可以看到效果

```java 
Destination destination=session.createQueue("myQueue?consumer.prefetchSize=10");
```

既然有批量加载，那么一定有批量确认，这样才算是彻底的优化

##### optimizeAcknowledge

ActiveMQ提供了optimizeAcknowledge来优化确认，它表示是否开启“优化ACK”，只有在为true的情况下，prefetchSize以及optimizeAcknowledgeTimeout参数才会有意义
优化确认一方面可以减轻client负担（不需要频繁的确认消息）、减少通信开销，另一方面由于延迟了确认（默认ack了0.65*prefetchSize个消息才确认），broker再次发送消息时又可以批量发送
如果只是开启了prefetchSize，每条消息都去确认的话，broker在收到确认后也只是发送一条消息，并不是批量发布，当然也可以通过设置DUPS_OK_ACK来手动延迟确认， 我们需要在brokerUrl指定optimizeACK选项ConnectionFactory connectionFactory= new ActiveMQConnectionFactory ("tcp://192.168.11.153:61616?jms.optimizeAcknowledge=true&jms.optimizeAcknowledgeTimeOut=10000");
Ø 注意，如果optimizeAcknowledge为true，那么prefetchSize必须大于0. 当prefetchSize=0的时候，表示consumer通过PULL方式从broker获取消息

#### 总结

到目前为止，我们知道了optimizeAcknowledge和prefetchSize的作用，两者协同工作，通过批量获取消息、并延迟批量确认，来达到一个高效的消息消费模型。它比仅减少了客户端在获取消息时的阻塞次数，还能减少每次获取消息时的网络通信开销
Ø 需要注意的是，如果消费端的消费速度比较高，通过这两者组合是能大大提升consumer的性能。如果consumer的消费性能本身就比较慢，设置比较大的prefetchSize反而不能有效的达到提升消费性能的目的。因为过大的prefetchSize不利于consumer端消息的负载均衡。因为通常情况下，我们都会部署多个consumer节点来提升消费端的消费性能。
这个优化方案还会存在另外一个潜在风险，当消息被消费之后还没有来得及确认时，client端发生故障，那么这些消息就有可能会被重新发送给其他consumer，那么这种风险就需要client端能够容忍“重复”消息。

### 消息的确认过程

#### ACK_MODE

通过前面的源码分析，基本上已经知道了消息的消费过程，以及消息的批量获取和批量确认，那么接下来再了解下消息的确认过程
从第一节课的学习过程中，我们知道，消息确认有四种ACK_MODE，分别是
AUTO_ACKNOWLEDGE = 1 自动确认
CLIENT_ACKNOWLEDGE = 2 客户端手动确认
DUPS_OK_ACKNOWLEDGE = 3 自动批量确认
SESSION_TRANSACTED = 0 事务提交并确认
虽然Client端指定了ACK模式,但是在Client与broker在交换ACK指令的时候,还需要告知ACK_TYPE,ACK_TYPE表示此确认指令的类型，不同的ACK_TYPE将传递着消息的状态，broker可以根据不同的ACK_TYPE对消息进行不同的操作。

#### ACK_TYPE

DELIVERED_ACK_TYPE = 0 消息"已接收"，但尚未处理结束
STANDARD_ACK_TYPE = 2 "标准"类型,通常表示为消息"处理成功"，broker端可以删除消息了
POSION_ACK_TYPE = 1 消息"错误",通常表示"抛弃"此消息，比如消息重发多次后，都无法正确处理时，消息将会被删除或者DLQ(死信队列)
REDELIVERED_ACK_TYPE = 3 消息需"重发"，比如consumer处理消息时抛出了异常，broker稍后会重新发送此消息INDIVIDUAL_ACK_TYPE = 4 表示只确认"单条消息",无论在任何ACK_MODE下
UNMATCHED_ACK_TYPE = 5 在Topic中，如果一条消息在转发给“订阅者”时，发现此消息不符合Selector过滤条件，那么此消息将 不会转发给订阅者，消息将会被存储引擎删除(相当于在Broker上确认了消息)。
Client端在不同的ACK模式时,将意味着在不同的时机发送ACK指令,每个ACK Command中会包含ACK_TYPE,那么broker端就可以根据ACK_TYPE来决定此消息的后续操作

### 消息的重发机制原理

#### 消息重发的情况

在正常情况下，有几中情况会导致消息重新发送
Ø 在事务性会话中，没有调用session.commit确认消息或者调用session.rollback方法回滚消息
Ø 在非事务性会话中，ACK模式为CLIENT_ACKNOWLEDGE的情况下，没有调用acknowledge或者调用了recover方法；

一个消息被redelivedred超过默认的最大重发次数（默认6次）时，消费端会给broker发送一个”poison ack”(ActiveMQMessageConsumer#dispatch：1460行)，表示这个消息有毒，告诉broker不要再发了。这个时候broker会把这个消息放到DLQ（死信队列）。

#### 死信队列

ActiveMQ中默认的死信队列是ActiveMQ.DLQ，如果没有特别的配置，有毒的消息都会被发送到这个队列。默认情况下，如果持久消息过期以后，也会被送到DLQ中。

#### 死信队列配置策略

缺省所有队列的死信消息都被发送到同一个缺省死信队列，不便于管理，可以通过individualDeadLetterStrategy或sharedDeadLetterStrategy策略来进行修改

```xml
<destinationPolicy> 
    <policyMap> 
        <policyEntries> 
            <policyEntry topic=">" > 
                <pendingMessageLimitStrategy> 
                    <constantPendingMessageLimitStrategy limit="1000"/> 	                					</pendingMessageLimitStrategy>
            </policyEntry> // “>”表示对所有队列生效，如果需要设置指定队列，则直接写队列名称 					<policyEntry queue=">">
            <deadLetterStrategy> //queuePrefix:设置死信队列前缀 //useQueueForQueueMessage 设置队列保存到死信。 <individualDeadLetterStrategy queuePrefix="DLQ." useQueueForQueueMessages="true"/> 				</deadLetterStrategy> 
            </policyEntry> 
        </policyEntries> 
    </policyMap> 
</destinationPolicy>
```



#### 自动丢弃过期消息

```xml
<deadLetterStrategy> 
    <sharedDeadLetterStrategy processExpired="false" /> 
</deadLetterStrategy>
```



#### 死信队列的再次消费

当定位到消息不能消费的原因后，就可以在解决掉这个问题之后，再次消费死信队列中的消息。因为死信队列仍然是一个队列

### ActiveMQ静态网络配置

#### 配置说明

修改activeMQ服务器的activeMQ.xml, 增加如下配置

```xml
<networkConnectors> 
	<networkConnector uri="static://(tcp://192.168.11.153:61616,tcp://192.168.11.154:61616)"/> </networkConnectors>
```

两个Brokers通过一个static的协议来进行网络连接。一个Consumer连接到BrokerB的一个地址上，当Producer在BrokerA上以相同的地址发送消息是，此时消息会被转移到BrokerB上，也就是说BrokerA会转发消息到BrokerB上

#### 消息回流

从5.6版本开始，在destinationPolicy上新增了一个选项replayWhenNoConsumers属性，这个属性可以用来解决当broker1上有需要转发的消息但是没有消费者时，把消息回流到它原始的broker。同时把enableAudit设置为false，为了防止消息回流后被当作重复消息而不被分发
通过如下配置，在activeMQ.xml中。 分别在两台服务器都配置。即可完成消息回流处理

```xml
<policyEntry queue=">" enableAudit="false"> 
    <networkBridgeFilterFactory> 
        <conditionalNetworkBridgeFilterFactory replayWhenNoConsumers="true"/> 				       </networkBridgeFilterFactory>
</policyEntry>
```

#### 动态网络连接

ActiveMQ使用Multicast协议将一个Service和其他的Broker的Service连接起来。Multicast能够自动的发现其他broker，从而替代了使用static功能列表brokers。用multicast协议可以在网络中频繁
multicast://ipadaddress:port?transportOptions

### 基于zookeeper+levelDB的HA集群搭建

activeMQ5.9以后推出的基于zookeeper的master/slave主从实现。虽然ActiveMQ不建议使用LevelDB作为存储，主要原因是，社区的主要精力都几种在kahadb的维护上，包括bug修复等。所以并没有对LevelDB做太多的关注，所以他在是不做为推荐商用。但实际上在很多公司，仍然采用了LevelDB+zookeeper的高可用集群方案。而实际推荐的方案，仍然是基于KahaDB的文件共享以及Jdbc的方式来实现。

#### 配置

在三台机器上安装activemq，通过三个实例组成集群。

#### 修改配置

**directory**：表示LevelDB所在的主工作目录
**replicas**:表示总的节点数。比如我们的及群众有3个节点，且最多允许一个节点出现故障，那么这个值可以设置为2，也可以设置为3. 因为计算公式为 (replicas/2)+1. 如果我们设置为4， 就表示不允许3个节点的任何一个节点出错。
**bind**：当当前的节点为master时，它会根据绑定好的地址和端口来进行主从复制协议
**zkAddress**：zk的地址
**hostname**：本机IP

**sync**：在认为消息被消费完成前，同步信息所存储的策略。 local_mem/local_disk
ActiveMQ

### ActiveMQ的优缺点

ActiveMQ采用消息推送方式，所以最适合的场景是默认消息都可在短时间内被消费。数据量越大，查找和消费消息就越慢，消息积压程度与消息速度成反比。

#### 优点

1.吞吐量低。由于ActiveMQ需要建立索引，导致吞吐量下降。这是无法克服的缺点，只要使用完全符合JMS规范的消息中间件，就要接受这个级别的TPS。
2.无分片功能。这是一个功能缺失，JMS并没有规定消息中间件的集群、分片机制。而由于ActiveMQ是伟企业级开发设计的消息中间件，初衷并不是为了处理海量消息和高并发请求。如果一台服务器不能承受更多消息，则需要横向拆分。ActiveMQ官方不提供分片机制，需要自己实现。

#### 适用场景

对TPS要求比较低的系统，可以使用ActiveMQ来实现，一方面比较简单，能够快速上手开发，另一方面可控性也比较好，还有比较好的监控机制和界面

#### 不适用的场景

消息量巨大的场景。ActiveMQ不支持消息自动分片机制，如果消息量巨大，导致一台服务器不能处理全部消息，就需要自己开发消息分片功能。



















