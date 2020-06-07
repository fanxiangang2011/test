# 2017年小马哥微服务

## 1、Spring Web MVC

​                  DispatcherServlet

​           Servlet WebApplicationContext

​            controller      viewResolver    HandleMapping

------------------------------------------------------------------------

​				Root WebApplicationContext

​            Services         Repositories 

c:controller-->dispatchServlet

Font Controller==DispatchServlet

Application Contrller=@Contrller or Controller

ServletContextLinstener->ContextLoaderLister->Root Web ApplicationContext

DispatcherServlet-->Servlet WebApplicationContext

* 映射处理
  * Servlet URL Pattern
  * Filter URL Pattern

* DispatcherServlet  extends FrameworkServlet  <HttpServletBean <HttpServlet

  自动装配

  org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration 	

  Sring Web MVC的配置Bean：WebMvcProperties

* HandleMapping 会寻找Request URL匹配的Handler

Handler是处理的方法，当然是一种实例

整体流程：Request->Handler->执行结果->返回（REST）->普通的文本

HandlerMapping->RequestMappingHandlerMapping 

getMapping(RequestMapping(method=RequestMethod.GET))

拦截器：HandlerInterceptor 可以理解Handler到底是什么

是包名+类名+方法名

处理逻辑：

perHandle(返回true)->HandlerMethod执行(Method#invoke)->postHandle->afterCompletion

* 异常处理

  * 1.Servlet标准
  * Spring Web MVC
  * Spring Boot

  Spring Boot错误处理页面

  	* 实现ErrorPageRegistrar
  	* 注册ErrorPage对象
  	* 实现ErrorPage对象中的Path路径

* 视图技术

  * view

    render方法是处理页面渲染的逻辑，Velocity，jsp,Thymeleaf

  * ViewResolver=页面+解析器（resolve）

    方法：resolveViewName, 寻找对于的View对象

    过程：RequestURL->RequestMappingHandleMapping->HandleMethod->return "viewName"-> 完整的页面名称=perfix+"viewName"+suffix->ViewResovler->View->rander->HTML 

  * Thymeleaf

    自动装配类：ThymeleafAutoConfiguration

  * 配置项的前缀：spring.thymeleaf

  * 模板寻找后缀：spring.thymeleaf.prefix

* 国际化

  * Local

## 2、Spring Rest









 



