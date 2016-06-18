# javaassist-for-springaop

#spring 通过jdk/cglib实现aop的在每次调用的时候都要通过反射，性能不高，通过javaassist在spring bean未初始化之前修改beandifintion对应的class，被修改后的proxy初始化后会放入spring容器中，每次调用再也不用通过反射了!   
