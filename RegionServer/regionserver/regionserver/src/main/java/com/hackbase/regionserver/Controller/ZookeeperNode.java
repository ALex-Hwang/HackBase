package com.hackbase.regionserver.Controller;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import java.util.List;
import java.util.Map;

public class ZookeeperNode {

    private static final String connectString = "10.162.42.0:2181";

    private static Map<String, String> map = System.getenv();

    private static String userName = map.get("USERNAME");

    private static final int sessionTimeout = 10000;

    private static ZooKeeper zk = null;

    private static String RegionServerManagerPath = "/RegionServerManager/" + userName;

    private static String ServerInfoPath = "/ServerInfo/" + userName;

    private static String LogPath = "D:\\Repository\\Java\\demo\\log.txt";

    private static String IP = "10.112.196.177";

    private static String PORT = ":9090";

    public static void connect() throws Exception {
        createE(RegionServerManagerPath, "");
        createP(ServerInfoPath, userName + "@" + IP + PORT);
        createP(ServerInfoPath + "/path", LogPath);
    }

    /**
     * 获取zookeeper实例
     * 
     * @return
     * @throws Exception
     */
    public static ZooKeeper getZookeeper() throws Exception {
        zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // 收到watch通知后的回调函数
                System.out.println("事件类型" + event.getType() + "，路径" + event.getPath());

                // 因为监听器只会监听一次，这样可以一直监听,且只监听"/"目录
                // try {
                // zk.getChildren("/", true);
                // } catch (Exception e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
            }
        });
        System.out.println("完成");
        return zk;
    }

    /**
     * 创建数据
     * 
     * @throws Exception
     */
    public static void createP(String name, String content) throws Exception {
        if (!isExist(name)) {
            ZooKeeper zk = getZookeeper();
            // 创建一个节点，返回创建好的路径 ，且上传的数据可以为任意类型，需要转换成byte[]
            // 参数1 路径，参数2 内容，参数3 权限，参数4 类型
            String znodePath = zk.create(name, content.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("返回的路径 为：" + znodePath);
        }
    }

    public static void createE(String name, String content) throws Exception {
        if (!isExist(name)) {
            ZooKeeper zk = getZookeeper();
            // 创建一个节点，返回创建好的路径 ，且上传的数据可以为任意类型，需要转换成byte[]
            // 参数1 路径，参数2 内容，参数3 权限，参数4 类型
            String znodePath = zk.create(name, content.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("返回的路径 为：" + znodePath);
        }
    }

    /**
     * 判断znode是否存在
     * 
     * @throws Exception
     */
    public static boolean isExist(String name) throws Exception {
        ZooKeeper zk = getZookeeper();
        Stat exists = zk.exists(name, false);
        if (exists == null) {
            System.out.println("不存在");
        } else {
            System.out.println("存在");
        }

        return exists == null ? false : true;
    }

    /**
     * 获取子节点
     * 
     * @throws Exception
     */
    public static void getChildren(String name) throws Exception {
        ZooKeeper zk = getZookeeper();
        // 获取子节点
        List<String> children = zk.getChildren(name, true);
        for (String string : children) {
            System.out.println("子节点:" + string);
        }
        // 阻塞，测试监听器,且只监听"/"目录
        // Thread.sleep(Long.MAX_VALUE);
    }

    public static void getChildren() throws Exception {
        ZooKeeper zk = getZookeeper();
        // 获取子节点
        List<String> children = zk.getChildren("/", true);
        for (String string : children) {
            System.out.println("子节点:" + string);
        }
        // 阻塞，测试监听器,且只监听"/"目录
        // Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 获取znode数据
     * 
     * @throws Exception
     */
    public static byte[] getData(String name) throws Exception {
        ZooKeeper zk = getZookeeper();
        byte[] data = zk.getData(name, false, new Stat());
        System.out.println(new String(data));
        return data;
    }

    /**
     * 删除数据
     * 
     * @throws Exception
     */
    public static void delete(String name) throws Exception {
        ZooKeeper zk = getZookeeper();
        // 第二个参数为version，-1表示删除所有版本
        // 它不支持删除的节点下面还有子节点，只能递归删除
        zk.delete(name, -1);
    }

    /**
     * 修改znode的值
     * 
     * @throws Exception
     */
    public static void setData(String name, String content) throws Exception {
        ZooKeeper zk = getZookeeper();

        // 修改znode的值
        zk.setData(name, content.getBytes(), -1);

        // 测试是否修改成功
        System.out.println(new String(zk.getData(name, false, null)));

    }

}
