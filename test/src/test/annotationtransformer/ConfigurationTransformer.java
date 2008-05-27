package test.annotationtransformer;

import org.testng.internal.annotations.IAnnotationTransformer2;
import org.testng.internal.annotations.IConfiguration;
import org.testng.internal.annotations.IDataProvider;
import org.testng.internal.annotations.IFactory;
import org.testng.internal.annotations.ITest;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ConfigurationTransformer implements IAnnotationTransformer2 {

  public void transform(ITest annotation, Class testClass,
      Constructor testConstructor, Method testMethod)
  {
  }

  public void transform(IConfiguration annotation, Class testClass,
      Constructor testConstructor, Method testMethod)
  {
    if (annotation.getBeforeTestMethod()) {
      System.out.println("disabling " + testMethod + " "
          + testMethod.hashCode() 
          + " " + annotation.hashCode());
      annotation.setEnabled(false);
    }
  }

  public void transform(IDataProvider annotation, Method testMethod) {
  }

  public void transform(IFactory annotation, Method testMethod) {
  }

}