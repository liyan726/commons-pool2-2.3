import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * 实现的工厂类
 * 这个类提供一些自定义的核心同能接口。在框架运行时，会接口回调的方式来调用执行   回调模式
 * <p/>
 * Created by yan_li on 2017/1/13.
 */
public class MyPoolableObjectFactory extends BasePooledObjectFactory<Resource> {

    /**
     * 创建一个对象实例
     */
    @Override
    public Resource create() throws Exception {
        return new Resource();
    }

    /**
     * 包裹创建的对象实例，返回一个pooledobject
     */
    @Override
    public PooledObject<Resource> wrap(Resource obj) {
        return new DefaultPooledObject<Resource>(obj);
    }

    @Override
    public PooledObject<Resource> makeObject() throws Exception {
        return super.makeObject();
    }

    @Override
    public void destroyObject(PooledObject<Resource> p) throws Exception {
        super.destroyObject(p);
    }

    /**
     * 校验对象的有效性，通俗讲是否是可用的 有效返回 true，无效返回 false
     * <p/>
     * 1：从资源池中获取资源的时候，参数 testOnBorrow 或者 testOnCreate 中有一个 配置 为 true 时，则调用  factory.validateObject() 方法
     * 2：将资源返还给资源池的时候，参数 testOnReturn，配置为 true 时，调用此方法
     * 3：资源回收线程，回收资源的时候，参数 testWhileIdle，配置为 true 时，调用此方法
     */
    @Override
    public boolean validateObject(PooledObject<Resource> p) {
        Resource resource = p.getObject();
        return resource.getRid() == 1 ? false : true;
    }

    /**
     * 激活资源对象
     * <p/>
     * 什么时候会调用此方法
     * 1：从资源池中获取资源的时候
     * 2：资源回收线程，回收资源的时候，根据配置的 testWhileIdle 参数，判断 是否执行 factory.activateObject()方法，true 执行，false 不执行
     */
    @Override
    public void activateObject(PooledObject<Resource> p) throws Exception {
        super.activateObject(p);
    }

    /**
     * 功能描述：钝化资源对象
     * <p/>
     * 1：将资源返还给资源池时，调用此方法。
     */
    @Override
    public void passivateObject(PooledObject<Resource> p) throws Exception {
        super.passivateObject(p);
    }
}
