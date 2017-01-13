import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created by yan_li on 2017/1/13.
 */
public class TestRun {
    public static void main(String[] args) throws Exception {
        // 创建池对象工厂
        PooledObjectFactory<Resource> factory = new MyPoolableObjectFactory();

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        // 最大空闲数
        poolConfig.setMaxIdle(5);
        // 最小空闲数, 池中只有一个空闲对象的时候，池会在创建一个对象，并借出一个对象，从而保证池中最小空闲数为1
        poolConfig.setMinIdle(1);
        // 最大池对象总数
        poolConfig.setMaxTotal(4);
        // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        poolConfig.setMinEvictableIdleTimeMillis(1800000);
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        poolConfig.setTimeBetweenEvictionRunsMillis(1800000 * 2L);
        // 在获取对象的时候检查有效性, 默认false
        poolConfig.setTestOnBorrow(true);
        // 在归还对象的时候检查有效性, 默认false
        poolConfig.setTestOnReturn(false);
        // 在空闲时检查有效性, 默认false
        poolConfig.setTestWhileIdle(false);
        // 最大等待时间， 默认的值为-1，表示无限等待。
        poolConfig.setMaxWaitMillis(5000);
        // 是否启用后进先出, 默认true
        poolConfig.setLifo(true);
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        poolConfig.setBlockWhenExhausted(true);
        // 每次逐出检查时 逐出的最大数目 默认3
        poolConfig.setNumTestsPerEvictionRun(3);
        // 创建对象池

        final GenericObjectPool<Resource> pool = new GenericObjectPool<Resource>(factory, poolConfig);

        /**
         * GenericObjectPool 是 Apache Commons Pool 提供的对象池，使用的时候需要调用 borrowObject 获取一个对象，使用完以后需要调用 returnObject 归还对象，或者调用 invalidateObject 将这个对象标记为不可再用。
         *  实际应用中由于程序实现的问题，可能造成在一些极端的情况下出现 borrowObject／invalidateObject 没有被调用导致的泄漏问题。
         *  对象泄漏会导致对象池中的对象数量一直上升，达到设置的上限以后再调用 borrowObject 就会永远等待或者抛出。应用检查或者开启自检
         *
         * 设置了抛弃时间以后还需要打开泄漏清理才会生效。泄漏判断的开启可以通过两种方式：
         *    1、从对象池中获取对象的时候进行清理如果当前对象池中少于2个idle状态的对象或者 active数量>最大对象数-3 的时候，在borrow对象的时候启动泄漏清理。
         *    通过 AbandonedConfig.setRemoveAbandonedOnBorrow 为 true 进行开启。
         *    2、启动定时任务进行清理AbandonedConfig.setRemoveAbandonedOnMaintenance 设置为 true 以后，在维护任务运行的时候会进行泄漏对象的清理，
         *    可以通过 GenericObjectPool.setTimeBetweenEvictionRunsMillis 设置维护任务执行的时间间隔。
         */
        //        AbandonedConfig abandonedConfig = new AbandonedConfig();
        //        abandonedConfig.setRemoveAbandonedOnMaintenance(true); //在Maintenance的时候检查是否有泄漏
        //        abandonedConfig.setRemoveAbandonedOnBorrow(true); //borrow 的时候检查泄漏
        //        abandonedConfig.setRemoveAbandonedTimeout(10); //如果一个对象borrow之后10秒还没有返还给pool，认为是泄漏的对象
        //        pool.setAbandonedConfig(abandonedConfig);
        //        pool.setTimeBetweenEvictionRunsMillis(1000); //5秒运行一次维护任务

        //        for (int i = 0; i < 15; i++) {
        //            new Thread(new Runnable() {
        //                @Override
        //                public void run() {
        //                    try {
        //                        Resource resource = pool.borrowObject();// 注意，如果对象池没有空余的对象，那么这里会block，可以设置block的超时时间
        //                        System.out.println(resource);
        //                        Thread.sleep(13*1000);
        //                        pool.returnObject(resource);// 申请的资源用完了记得归还，不然其他人要申请时可能就没有资源用了
        //                    } catch (Exception e) {
        //                        e.printStackTrace();
        //                    }
        //                }
        //            }).start();

        Resource resource = pool.borrowObject();// 注意，如果对象池没有空余的对象，那么这里会block，可以设置block的超时时间
        System.out.println(resource);
       // pool.returnObject(resource);
        //空闲对象为1
        System.out.println(pool.getNumIdle());
        Resource resource2 = null;// 注意，如果对象池没有空余的对象，那么这里会block，可以设置block的超时时间
        try {
            resource2 = pool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(resource2);
        //空闲对象为1
        System.out.println(pool.getNumIdle());
        System.out.println("所有对象集合:"+pool.getMapSize());
        Thread.sleep(13 * 1000);
        //pool.returnObject(resource);// 申请的资源用完了记
    }
}
