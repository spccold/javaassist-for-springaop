package com.geek.javaassist;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识一个spring bean method是否需要javaassist增强
 * 
 * @author jileng
 * @version $Id: JavaAssistEnhancer.java, v 0.1 2016年1月22日 下午2:07:06 jileng Exp $
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JavaAssistEnhancer {

}
