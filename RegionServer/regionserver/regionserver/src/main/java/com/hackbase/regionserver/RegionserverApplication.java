package com.hackbase.regionserver;

import com.hackbase.regionserver.Controller.*;

public class RegionserverApplication {

	private static String DOWNIP = "http://10.214.241.124:8000/test_zoo.py";

	public static void main(String[] args) throws Exception {
		ZookeeperNode.connect();
		ThriftListener.start();
	}

}
