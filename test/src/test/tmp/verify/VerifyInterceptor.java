package test.tmp.verify;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.TestNGUtils;
import org.testng.collections.Maps;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VerifyInterceptor implements IMethodInterceptor {

  /**
   * This method does two things:
   * - Find all the methods annotated with @Verify in the classes that contain test methods
   * - Insert these verify methods after each method passed in parameter
   * These @Verify methods are stored in a map keyed by the class in order to avoid looking them
   * up more than once on the same class.
   */
  public List<IMethodInstance> intercept(List<IMethodInstance> methods,
      ITestContext context) {

    List<IMethodInstance> result = new ArrayList<IMethodInstance>();
    Map<Class, List<IMethodInstance>> verifyMethods = Maps.newHashMap();
    for (IMethodInstance mi : methods) {
      ITestNGMethod tm = mi.getMethod();
      List<IMethodInstance> v = verifyMethods.get(tm.getRealClass());
      if (v == null) {
        v = findVerifyMethods(tm.getRealClass(), tm);
      }
      result.add(mi);
      result.addAll(v);
    }

    return result;
  }

  /**
   * @return all the @Verify methods found on @code{realClass}
   */
  private List<IMethodInstance> findVerifyMethods(Class realClass, final ITestNGMethod tm) {
    List<IMethodInstance> result = new ArrayList<IMethodInstance>();
    for (final Method m : realClass.getDeclaredMethods()) {
      Annotation a = m.getAnnotation(Verify.class);
      if (a != null) {
        final ITestNGMethod vm = TestNGUtils.createITestNGMethod(tm, m);
        result.add(new IMethodInstance() {

          public Object[] getInstances() {
            return tm.getInstances();
          }

          public ITestNGMethod getMethod() {
            return vm;
          }

        });
      }
    }

    return result;
  }
}