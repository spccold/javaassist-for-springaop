package com.geek.javaassist;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.util.ClassUtils;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
/**
 * 
 * 
 * @author jileng
 * @version $Id: JavaassistEnhancerFactoryPostProcessor.java, v 0.1 2016年1月22日 下午6:55:26 jileng Exp $
 */
public class JavaassistEnhancerFactoryPostProcessor implements BeanFactoryPostProcessor {
    private static final String    ENHANCERCLASS_SUFFIX = "Child";

    private static final ClassPool POOL;

    static {
        POOL = ClassPool.getDefault();
        //兼容web容器运行
        POOL.insertClassPath(new ClassClassPath(JavaassistEnhancerFactoryPostProcessor.class));
    }

    /**
     * reset all beanClass for beanDefiniton which method has JavaAssistEnhancer Annotation present
     * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
     */
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        AbstractBeanDefinition beanDefinition = null;
        if (null != beanDefinitionNames && beanDefinitionNames.length > 0) {
            for (String beanDefinitionName : beanDefinitionNames) {
                beanDefinition = (AbstractBeanDefinition) beanFactory.getBeanDefinition(beanDefinitionName);
                enhanceAndResetBeanClass(beanDefinition);
            }
        }
    }

    private void enhanceAndResetBeanClass(AbstractBeanDefinition beanDefinition) {
        List<String> annotatedMethodsName = getAllJavaassistEnhancerMethodsName(beanDefinition);

        if(null == annotatedMethodsName || annotatedMethodsName.size()==0){
            return;
        }
        
        try {
            String beanName = beanDefinition.getBeanClassName();
            String enhancerBeanName = beanName + ENHANCERCLASS_SUFFIX;
            // original bean class
            CtClass ccOrigin = POOL.get(beanName);
            // make proxy class
            CtClass ccProxy = POOL.makeClass(enhancerBeanName);
            // because jvm not all to reload class(unless open jpda), so proxy need to extends original bean class
            ccProxy.setSuperclass(ccOrigin);
            
            CtMethod newMethod = null;
            for (String methodName : annotatedMethodsName) {
                //委托子类调用到父类
                newMethod = CtNewMethod.delegator(ccOrigin.getDeclaredMethod(methodName), ccProxy);
                /***********************************do somthing on origin method*******************************************/
                final String beforeMethod = "{long startTime = System.currentTimeMillis(); System.out.println(\""+methodName+" start \");";
                final String afterMethod = "finally {long diff = System.currentTimeMillis() - startTime; System.out.println(\""+methodName+" completed in:\" + diff);}}";

                newMethod.instrument(new ExprEditor() {
                    public void edit(MethodCall m) throws CannotCompileException {
                        m.replace(beforeMethod + " try {$_ = $proceed($$); } " + afterMethod);
                    }
                });

                /******************************************************************************/
                ccProxy.addMethod(newMethod);
            }
            beanDefinition.setBeanClass(ccProxy.toClass(ClassUtils.getDefaultClassLoader(),null));
            //TODO  delay to decide
            //beanDefinition.setBeanClassName(enhancerBeanName);
        } catch (Throwable e) {
            //should do something meaningful
            e.printStackTrace();
        }
    }

    /**
     * 返回当前beandefinition对应beanclass中所包含注解(@JavaAssistEnhancer)的方法名称列表
     * 
     * @param beanDefinition
     * @return
     */
    public List<String> getAllJavaassistEnhancerMethodsName(AbstractBeanDefinition beanDefinition) {
        List<String> methodsName = new ArrayList<String>();
        Class<?> tempOriginalBeanClass = null;
        // Make sure bean class is actually resolved at this point.
        if (!beanDefinition.hasBeanClass()) {
            try {
                tempOriginalBeanClass = ClassUtils.forName(beanDefinition.getBeanClassName(), ClassUtils.getDefaultClassLoader());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (LinkageError e) {
                e.printStackTrace();
            }
        }
        
        Method[] methods = tempOriginalBeanClass.getMethods();
        if (null != methods && methods.length > 0) {
            for (Method m : methods) {
                if (m.isAnnotationPresent(JavaAssistEnhancer.class)) {
                    methodsName.add(m.getName());
                }
            }
        }
        return methodsName;
    }

    /**
     * 判断一个方法的返回类型是否为Void
     * 
     * @param cm
     * @return
     */
    //    private boolean isVoid(CtMethod cm) {
    //        boolean isVoid = false;
    //        try {
    //            CtClass returnType = cm.getReturnType();
    //            if (returnType.isPrimitive()) {
    //                CtPrimitiveType primitiveType = (CtPrimitiveType) returnType;
    //                isVoid = primitiveType.getName().equalsIgnoreCase("void");
    //            }
    //
    //        } catch (Throwable e) {
    //            e.printStackTrace();
    //        }
    //        return isVoid;
    //    }
}
