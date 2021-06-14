package com.hackbase.regionserver.Controller;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;

public class ThriftListener implements regionSvc.Iface {

    public static void start() throws Exception {
        System.out.println("服务端开启....");
        TProcessor tprocessor = new regionSvc.Processor<regionSvc.Iface>(new ThriftListener());
        TServerSocket serverTransport = new TServerSocket(9090);
        TServer.Args tArgs = new TServer.Args(serverTransport);
        tArgs.processor(tprocessor);
        tArgs.protocolFactory(new TBinaryProtocol.Factory());
        TServer server = new TSimpleServer(tArgs);
        server.serve();
    }

    @Override
    public String sendCommand(String para) throws TException {
        String result = "";
        try {
            result = Minisql.exec(para);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
