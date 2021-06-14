package com.hackbase.regionserver.Controller;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.File;
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
        getZookeeper();

        createE(RegionServerManagerPath, "");
        int num = Integer.valueOf(new String(getData("/RegionServerManager")));
        setData("/RegionServerManager", String.valueOf(num + 1));
        createP(ServerInfoPath, userName + "@" + IP + PORT);
        createP(ServerInfoPath + "/path", LogPath);
        createP(ServerInfoPath + "/tableNum", String.valueOf(count()));
        createP(ServerInfoPath + "/tables", "");
    }

    public static int count() {
        String basePath = "./";
        String[] list = new File(basePath).list();
        int num = 0;
        for (String s : list) {
            if (s.startsWith("TABLE_FILE")) {
                num++;
            }
        }
        return num;
    }

    /**
     * 获取zookeeper实例
     * 
     * @return
     * @throws Exception
     */
    public static void getZookeeper() throws Exception {
        zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("事件类型" + event.getType() + "，路径" + event.getPath());
            }
        });
        System.out.println("完成");
    }

    /**
     * 创建一个节点，返回创建好的路径; name：节点名称 ; content：节点data;
     * createP()为创建持久节点，createE()为创建临时节点
     * 
     * @throws Exception
     */
    public static void createP(String name, String content) throws Exception {
        if (!isExist(name)) {
            String znodePath = zk.create(name, content.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("返回的路径 为：" + znodePath);
        }
    }

    public static void createE(String name, String content) throws Exception {
        if (!isExist(name)) {
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
        // ZooKeeper zk = getZookeeper();
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
        // ZooKeeper zk = getZookeeper();
        // 获取子节点
        List<String> children = zk.getChildren(name, true);
        for (String string : children) {
            System.out.println("子节点:" + string);
        }
        // 阻塞，测试监听器,且只监听"/"目录
        // Thread.sleep(Long.MAX_VALUE);
    }

    public static void getChildren() throws Exception {
        // ZooKeeper zk = getZookeeper();
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
        // ZooKeeper zk = getZookeeper();
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
        // ZooKeeper zk = getZookeeper();
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
        // ZooKeeper zk = getZookeeper();

        // 修改znode的值
        zk.setData(name, content.getBytes(), -1);

        // 测试是否修改成功
        System.out.println(new String(zk.getData(name, false, null)));

    }

}
