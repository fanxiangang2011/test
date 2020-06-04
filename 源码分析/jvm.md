

# 深入理解JVM

## java发展史

 - 1995年5月23日，Oak语言改名为java，并且在Sun World发布Java1.0版本
 - 1996年1月23日，发布JDK1.0，实现了一个纯解释执行的Java虚拟机实现（Sun Classic VM）。代表技术主要有Java虚拟机、Applet、AWT等。
 - 1997年2月19日，发布了JDK1.1，技术代表:JAR文件格式、JDBC、JavaBean、RMI等。java语法也有了一定的增强，如内部类（Inner Class）、反射(Reflection)
 - 1998年12月4日，发布了JDK1.2工程代号为Playground（竞技场），在这个版本中代表技术非常多，如EJB、Java  Plug-in、Java IDL、Swing等。Java虚拟机中内置了JIT（Just In Time）即时编译器，Java技术体系分为了三个方向：
    - 面向桌面应用开发的J2SE（Java 2 Platform,Standard Edition）
    - 面向企业级开发的J2EE(Java 2 Platform,Enterprise Edition)
    - 面向手机等移动终端开发的J2ME(Java 2 Platform,Micro Edition)
 - 2000年5月8号发布JDK1.3，改进主要体现在Java类库上，如数学算法、和新的Timer API等
 - 2002年2月13日发布JDK1.4，技术体现在正则表达式、异常链、NIO、日志类、XML解析器和XSLT转换器等。
 - 2004年9月30日，JDK5发布，工程代号Tiger(老虎)，技术体现：自动装箱、泛型、动态注解、枚举、可变长参数、遍历循环（foreach）等，在虚拟机和API层面上，改进了Java内存模型（Java Memory Model,JMM），提供了java.util.concurrent并发包等。
 - 2006年12月11日，发布JDK6，代号Mustang（野马）。技术体现：提供初步的动态语言支持（通过内置Mozilla JavaScript Rhino引擎实现）、提供编译期注解处理器和微型HTTP服务器API，等。对Java虚拟机内部做了大量改进，包括锁与同步、垃圾收集、类加载等。
 - 2009年2月19日，完成JDK7其第一个里程碑版本，代号Dolphin(海豚)
 - 2011年7月28日，发布JDK7，
 - 2014年3月18日，发布JDK8，主要包括
     - JEP 126：对Lambda表达式的支持，这让Java语言拥有了流畅的函数式表达能力。
     - JEP 104：内置Nashorn JavaScript引擎的支持
     - JEP 150：新的时间、日期API。
     - JEP 122：彻底移除HotSpot的永久代。
 - 2017年9月21日，发布JDK9，发布
     - Jigsaw
     - JS Shell
     - JLink
     - JHSDB
 - 2018年3月20日，JDK10发布，版本的主要目标是内部重构
     - 统一源仓库
     - 统一立即收集器接口
     - 统一即时编译器接口（JVMCI在JDK9已经有了，这里是引入新的Graal即时编译器）
 - 2018年9月25日，JDK11发布，包括
      - ZGC垃圾收集器
 - 2019年3月12日，JDK12发布
      - Switch表达式
      - Java 微测试套件（JMH）

## java虚拟机家族

 -  虚拟机始祖：Sun Classic/Exact VM
     -  只能使用纯解释器方式来执行Java代码，如果要使用即时编译器就必须外挂，外挂的编译器是Sum提供的sunwjit(Sun Workshop JIT)
     -  jdk1.2在Solaris平台发布了一款为Exact VM虚拟机。如：热点探测、两级即时编译、编译器与解释器混合工作模式等。
-  武林盟主：HotSpot VM
-  小家碧玉：Mobile/Embedded VM 
     -  Sun/Oracle公司
-  天下第二：BEA JRockit/IBM J9 VM
     -  三大商业Java虚拟机：
          -  BEA System公司的JRockit    ,号称世界上最快的虚拟机
          -  IBM公司的IBM J9
          -  Sun/Oracle的HotSpot
-  软硬合璧:BEA Liquid VM/Azul VM
-  挑战者：Apache Harmony/Google Android Dalvik VM



## 新一代即时编译器

HostSpot虚拟机中包含两个即时编译器，分别是编译耗时长，但是输出代码优化程度较低的客户端编译器（简称C1），以及编译耗时长，但输出代码优化质量也更高的服务端编译器（简称为C2），通常他们会在分层编译机制下与解释器互相配合来共同构成HotSpot虚拟机的执行子系统，

JDK10起，HotSpot中加入了一个全新的即时编译器：Graal编译器，是以C2编译器替代者的身份登场的，

## 自动内存管理

### 第二章 Java内存区域与内存溢出异常

 * 运行时数据区
    * 方法区（Method Area）
    * 虚拟机栈(VM Stack)
    * 本地方法栈Native Method Stack
    * 堆(Heap)
    * 程序计数器（Program Counter Register）

#### 程序计数器（Program Counter Register）

​	程序计数器（Program Counter Register）是一块较小的内存空间，它可以看作是当前线程所执行的字节码的行号指示器。在Java虚拟机的概念模型里，字节码解释器工作时就是通过改变这个计数器的值来选取一条需要执行的字节码指令，它是程序控制流的指示器，分支、循环、跳转、异常处理、线程恢复等基础功能都需要依赖这个计数器来完成。

 * 每个线程，都有一个独立的程序计数器，独立存储，对这类内存区域为“线程私有”的内存。
 * 如果线程正在执行的一个Java方法，这个计数器记录的是正在执行的虚拟机字节码指令的地址；
 * 如果正在执行的是本地（Native）方法，这个计数器值则应为空（undefined）。此内存区域是唯一一个在《Java虚拟机规范》中没有规定任何OutOfMemoryError情况的区域。

#### java虚拟机栈(Java Virtual Machine Stack)

 * 也是线程私有的，它的生命周期和线程相同。
 * 虚拟机栈描述的是Java方法执行的线程内存模型：每个方法被执行的时候，都会同步创建一个栈帧用于存储局部变量表、操作数栈、动态连接、方法出口等信息。
 * 每一个方法被调用直至执行完毕的过程，就对应着一个栈帧在虚拟机栈中从入栈到出栈的过程。
 * 栈：通常指虚拟机栈，或者更多的情况下只是指虚拟机栈中局部变量表部分。
 * Java虚拟机基本数量类型：boolean、byte、char、short、int、float、long、double
 * 对象引用：
    * 强引用（Strong Reference）
    * 软引用（Soft Reference）
    * 弱引用（Weak Reference）
    * 虚拟引用（Phantom Reference）
* 数据类型在局部变量中的存储空间以局部变量槽（Slot）来表示。
  * 64位的long和double占据2个变量槽，其余的占用一个
  * 如果线程请求的栈深度大于虚拟机所允许的深度，将抛出StackOverflowError异常
  * 如果Java虚拟机栈容量可以动态扩展，当栈扩展时无法申请导足够的内存会抛出OutOfMemoryError异常

#### 本地方法栈（Native Method Stacks）

  * 本地方法栈和虚拟机栈区别
      * 虚拟机栈为虚拟机执行Java方法（也就是字节码）服务
      * 本地方法栈则是为虚拟机使用到的本地（Native）方法服务
* Hot-Spot虚拟机直接把本地方法栈和虚拟机栈合二为一。与虚拟机栈一样，本地方法栈也会在栈深度溢出或者栈扩展失败时分别抛出StackOverflowError和OutOfMemoryError异常

#### Java堆

Java堆是垃圾收集器管理的内存区域，一些资料也被称为“GC堆”（Garbage Collected Heap）

从分配内存的角度看，所有线程共享的Java堆中可以划分出多个线程私有的分配缓冲区（Thread Local Allocation Buffer,TLAB）

 * 根据《Java虚拟机规为范》的规定，Java堆可以处于物理上不连续的内存空间中，但在逻辑上它应该被视为连续的，
 * 如果在Java堆中没有内存完成实例分配，并且堆也无法再扩展时，Java虚拟机将会抛出OutOfMemoryError异常

#### 方法区（Method Area）

 * HostSpot虚拟机
    * jdk6 逐渐改为采用本地内存（Native Memory）来实现方法区的计划了
    * jdk7把原本放在永久代的字符串常量池、静态变量等移除。
    * jdk8，终于完全废弃了永久代的概念，改用在本地内存中实现的元空间（Meta-space）来代替，把JDK中还剩余的内容全部移到元空间中。

#### 运行时常量池（Runtime Constant Pool）

运行常量池是方法区的一部分。

Class文件中除了有类的版本、字段、方法、接口等描述信息外，还有一项信息是常量池表（Constant Pool Table）,用于存放编译器生产的各种子面量和符合引用，这部分内容将加载后存放到方法区的运行时常量池中。

#### 直接内存（Direct Memory）

JDK1.4 新加入了NIO（New Input/Output）

####  HostSpot虚拟机对象探秘

​	当Java虚拟机遇到一条字节码new指令时，首先将去检查这个指令的参数是否能在常量池中定位到一个类的符号引用，并且检查这个符号引用代表的类是否已被加载、解析和初始化过程。如果没有，那么必须先执行相应的类加载过程，

 * 指针碰撞（Bump the Pointer）
 * 空闲列表（Free List）

选择哪种分配方式由Java堆是否规整决定，而Java堆是否规整又由所采用的垃圾收集器是否带有空间压缩整理（Compact）的能力决定。

 * 当使用Serial、ParNew等带压缩整理过程的收集器时，系统采用的分配算法是指针碰撞，既简单 又高效
 * 而当使用CMS这种基于清除（Sweep）算法的收集器时，理论上就只能采用较为复杂的空闲列表来分配内存
 * 线程并发，线程不安全，解决方案
    * 对分配内存空间的动作进行同步处理-------实际上虚拟机采用CAS配上失败重试的方式保证更新操作的原子性；
    * 百内存分配 动作按照线程划分在不同的空间之中进行，即每股线程在Java堆中预先分配一小块内存，称为本地线程分配缓冲（Thread Local Allocation Buffer,TLAB）,那个线程要分配内存，就在那个线程的本地缓冲区中分配，只有本地缓冲区用完了，分配新的缓冲区时才需要同步锁定。虚拟机是否使用TLAB，可以通过-XX：+/-UserTLAB参数来设定。

#### 对象的内存布局

对象在堆内存中的存储布局可以划分为三个部分：对象头（Header）、实例数据（Instance Data）和对齐填充（Padding）

 * 对象头
    * 一类是用于存储对象自身的运行时数据。如哈希码（HashCode）、GC分代年龄、锁状态标志、线程持有的锁、偏向线程ID、偏向时间戳等。官方称为“Mark Word”。
    * 另一类是类型指针，即对象指向它的类型元数据的指针，
* HotSport虚拟机默认的分配顺序为longs/doubles、ints、shorts/chars、bytes/booleans、oops（Ordinary Object Pointers,oops）
* 如果HotSpot虚拟机的+XX：CompactFields参数为true(默认就是true)，那子类之中较窄的变量也允许插入父类变量的空隙中，以节省出一点点空间。
* 对象的第三部分是对齐填充，起站位符的作用

#### 对象的访问定位

 * 主流的访问方式是句柄和直接指针两种

    * 句柄访问，划分一块内存来作为句柄池，reference中存储的就是对象的句柄地址，而句柄中包含了对象实例数据与类型数据各自具体的地址信息。

      java栈                     |                    句柄池                             |          java堆      |      实例池

      本地变量表             |            到对象实例数据的指针          |              |           |      对象实例数据

      int                            |           到对象类型数据的指针           |              |           |

      short                       |------------------------------------------------------------------------------------------------

      reference                |                                                  方法区

      int                             |                                        对象类型数据

      double                     |

      float                         |

      ...                              |

   * 直接指针，reference中存储的直接就是对象地址。如果只是访问对象本身的话，就不需要多一次间接访问的开销。

   * 使用句柄访问的最大好处就是reference中存储的是稳定句柄池地址，在对象被移动（垃圾收集时移动对象是非常普遍的行为）时只会改变句柄中的实例数据指针，而reference本身不需要被修改。

   * 使用直接指针访问的最大好处就是速度更快，它节省了一次指针定位的时间开销，由于对象访问在java中非常频繁，使用句柄来访问的情况也十分常见。

#### 实战：OutOfMemoryError异常

##### Java堆溢出

 * -verbose:gc  -Xms20M   -Xmx20M -Xmn10M -XX:+PrintGCDetails   -XX:survivorRatio=8
    * 将堆的最小值-Xms参数与最大值-Xmx参数设置为一样即可避免堆自动扩展
    * 通过参数-XX：+HeapDumpOnOutOf-MemoryError可以让虚拟机在出现内存溢出异常的时候Dump出当前的内存堆转储快照以便进行事后分析。
    * Java堆内存的OutOfMemoryError异常是实际应用中最常见的内存溢出异常情况。出现Java堆内存溢出时，异常堆栈信息“java.lang.OutOfMemoryError”会跟随进一步提示“Java heap space”。
    * 先分清是内存泄漏（Memroy Leak）还是内存溢出（Memory Overflow）
       * 如果是内存泄露，通过攻击查看泄漏对象到GCRoots的应用链，找到泄漏对象是通过怎样的引用路径、与那些GC Roots相关联，才导致垃圾收集器无法回收他们，通过泄漏对象的类型信息已经它到GC Roots引用链的信息，一般可以比较准确地定位到这些对象创建的位置，进而找出产生内存泄露的代码的具体位置
       * 如果不是内存泄漏，换句话说就是内存中的对象确实都是必须存活的，那就应当检查Java虚拟机的堆参数（-Xmx与-Xms）设置，与激情的内存相比，看看是否还有向上调整的空间。再从代码检查是否存在某些对象生命周期过长、持有状态时间过长、存储结构设计不合理等情况，尽量减少程序运行期的内存消耗。

##### 虚拟机栈和本地方法栈溢出 

* 栈容量只能由-Xss参数来设定，对于HotSpot来说，-Xoss参数虽然存在，但实际上是没有任何效果的。
* 《Java虚拟机规范》两种异常
  * 如果线程请求的栈深度大于虚拟机所运行 最大深度，将抛出StackOverflowError异常。
  * 如果虚拟机的栈内存允许动态扩展，当扩展栈容量无法申请导足够的内存时，将抛出OutOfMemoryError异常。
  * 《Java虚拟机规范》明确运行Java虚拟机实现自行选择是否支持栈的动态扩展，而HotSpot虚拟机的选择时不支持扩展，所以除非在创建线程申请内存时就因无法获得足够内存而出现OutOfMemoryError异常，否则在线程运行时是不会因为扩展而导致内存溢出的，只会因为栈容量无法容纳新的栈帧而导致StackOverflowError异常。

##### 方法区和运行时常量池溢出

* 由于运行时常量池是方法区的一部分，所有这两个区域的溢出测试可以放到一起进行。

* JDK7开始逐步“去永久代”的计划，并在JDK中完全使用JDK8中完全使用元空间来代替永久代。

  * String::intern()是一个本地方法，它的作用是如果字符串常量池中已经包含一个等于此String对象的字符串，则返回代表池中这个字符串的String对象的引用；否则，会将此String对象包含的字符串添加到常量池中，并且返回此字符串对象的引用。

  * 在JDK6-- 常量池分配在永久代中，我们可以通过-XX:PermSize和-XX：MaxPermSize限制永久代的大小，即可间接限制其中常量池的容量。

  * java.lang.OutOfMemoryError: PermGen space    JDK6 运行时常量池是属于方法区（即JDK 6的HotSpot虚拟机中的永久代）

  * java.lang.OutOfMemoryError: Java heap space JDK 7起，原本存放在永久代的字符串常量池被移至Java堆之中

  * ```java
    public static void main(String[] args) {
    String str1 = new StringBuilder("计算机").append("软件").toString();
    System.out.println(str1.intern() == str1);
    String str2 = new StringBuilder("ja").append("va").toString();
    System.out.println(str2.intern() == str2);
    }
    ```

    运行代码在JDK6中运行，会得到两个false，而在JDk6中会得到一个true和false 

    - 在JDK6中，intern()方法会首次遇到的字符串实例复制到永久代的字符串常量池中存储，返回的也是永久代里面这个字符串实例的引用，而有StringBuilder创建的字符串对象实例在Java堆上，所有不可能是同一个引用，结果将返回false。

    - 在JDK7中，intern()方法实现就不需要在拷贝字符串的实例到永久代了，既然字符串常量池以及移动到了Java堆中，那只需要在常量池里记录一下首次出现的实例引用即可，因此intern()返回的引用和由StringBuilder创建的那个字符串实例就是同一个。而对str2比较返回false，就是因为"Java"这个字符串在执行String-Builder.toString()之前就以及出现过了，字符串常量池中已经有它的引用了，不符合intern()方法要求“首次遇到”的原则，“计算机软件”这个字符串则是首次出现的，因此返回true.

    - ```
      注：intern 首次遇到返回true，对已经存在的则返回false.(JDK7++)
      ```

  * 方法区的主要职责是用于存放类型的相关信息，如类名、访问修饰符、常量池、字段描述、方法描述等。

  * 方法区溢出也是一种常见的内存溢出异常。

  * 在JDK8，永久代退出，元空间替代。元空间的防御措施：

    - -XX：MaxMetaspaceSize:设置元空间最大值，默认是-1，即不限制，或者说只受限本地内存大小
    - -XX：MetaspaceSize：指定元空间的初始空间大小，以字节为单位，达到该值就会触发垃圾收集进行类型卸载，同时收集器会对该值进行调整：如果释放了大量的空间，就适当较低该值；如果释放了很少的空间，那么就不超过-XX：MaxMetaspaceSize(如果设置的话)的情况下，适当提高该值。
    - -XX：MinMetaspaceFreeRatio:作用是在垃圾收集之后控制最小的元空间剩余容量的百分比，可以较少因为元空间不足导致的垃圾收集的频率。类似的还有-XX：Max-MetaspaceFreeRatio,用于控制最大的元空间剩余容量的百分比。

##### 本机直接内存溢出

- 直接内存（Direct Memory）的容量大小可通过-XX：MaxDirectMemorySize参数来指定，如果不去指定，则
- 默认与Java堆最大值（由-Xmx指定）一致。
  - 申请分配内存的方法使Unsafe::allocateMemory()

### 第三章垃圾收集器与内存分配策略

#### 3.1概述

垃圾收集（Garbage Collection,GC）

Java堆和方法区这两个区域则有着很显著的不确定性：一个接口的多个实现类需要的内存可能会不一样，一个方法所执行的不同条件分支所需要的内存也可能不一样，只有处于运行期间，我们才能知道程序究竟会创建哪些对象，创建多少个对象，这部分内存的分配和回收是动态的。

#### 对象已死？

* 引用计数算法  但是java虚拟机不是通过引用计数算法来判断对象是否存活的。

* 可达性分析算法 (Reachability Analysis)

  * 通过“GC Roots”的根对象作为起始节点集，根据引用关系向下搜索，搜索过程所走过的路径称为“引用链”（Reference Chain）,如果某个对象到GC Roots间没有任何引用链相连，或者用图论的话来说就是从GC Roots到这个对象不可达时，则证明此对象是不可能再被使用的。
  * 在Java技术体系里面，固定可作为GC Roots 的对象包括以下几种：

  1. 在虚拟机栈（栈帧中的本地变量表）中引用的对象，譬如各个线程被调用的方法堆栈中使用到的参数、局部变量、临时变量等。
  2. 在方法区中类静态属性引用的对象，譬如Java类的引用类型静态变量
  3. 在方法区中常量引用的对象，譬如字符串常量池（String Table）里的引用。
  4. 在本地方法栈中JNI（即通常所说的Native方法）引用的对象。
  5. Java虚拟机内部的引用，如基本数据类型对应的Class对象，一些常驻的异常对象（比如 NullPointExcepiton、OutOfMemoryError）等，还有系统类加载器。
  6. 所有被同步锁（synchronized关键字）持有的对象。
  7. 反映Java虚拟机内部情况的JMXBean、JVMTI中注册的回调、本地代码缓存等

* 再谈对象

  * JDK1.2版之后，分强引用（Strongly Re-ference）、软引用（Soft Reference）、弱引用（Weak Reference）、虚引用（Phantom Reference）,强度依次逐渐较弱。
    * 强引用是最传统的“引用”的定义，是指在程序代码之中普遍存在的引用赋值，即类似“Object
      obj=new Object()”这种引用关系。无论任何情况下，只要强引用关系还存在，垃圾收集器就永远不会回收掉被引用的对象。
    * 软引用是用来描述一些还有用，但非必须的对象。只被软引用关联着的对象，在系统将要发生内
      存溢出异常前，会把这些对象列进回收范围之中进行第二次回收，如果这次回收还没有足够的内存，
      才会抛出内存溢出异常。在JDK 1.2版之后提供了SoftReference类来实现软引用。
    * 弱引用也是用来描述那些非必须对象，但是它的强度比软引用更弱一些，被弱引用关联的对象只能生存到下一次垃圾收集发生为止。当垃圾收集器开始工作，无论当前内存是否足够，都会回收掉只被弱引用关联的对象。在JDK 1.2版之后提供了WeakReference类来实现弱引用。
    * 虚引用也称为“幽灵引用”或者“幻影引用”，它是最弱的一种引用关系。一个对象是否有虚引用的存在，完全不会对其生存时间构成影响，也无法通过虚引用来取得一个对象实例。为一个对象设置虚引用关联的唯一目的只是为了能在这个对象被收集器回收时收到一个系统通知。在JDK 1.2版之后提供了PhantomReference类来实现虚引用。
  * 生存还是死亡？
    * 筛选条件是此对象是否有必要执行finalize(）方法，如果对象没有覆盖finalize()方法，或者finalize（）方法已经被虚拟机调用过，那么虚拟机将这两种情况都视为“没有必要执行”。



### 第四章虚拟机收集器与内存分配策略

### 第五章调优案例分析与实战









### 





















































​	







