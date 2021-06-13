package com.hackbase.regionserver;

import java.io.File;

import com.hackbase.regionserver.Controller.*;

public class RegionserverApplication {

	public static void main(String[] args) throws Exception {
		ZookeeperNode.connect();
		ThriftListener.start();
	}

}
