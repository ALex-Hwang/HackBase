//
// Created by alex on 2021/6/12.
//
#include "./gen-cpp/rpc.h"
#include <thrift/transport/TSocket.h>
#include <thrift/transport/TBufferTransports.h>
#include <thrift/protocol/TBinaryProtocol.h>
#include <memory>
#include <iostream>

using namespace apache::thrift::transport;
using namespace apache::thrift::protocol;
using std::shared_ptr;
using std::make_shared;

int main() {
    shared_ptr<TTransport> trans;
    trans = make_shared<TSocket>("localhost", 9090);
    trans = make_shared<TBufferedTransport> (trans);
    auto proto = make_shared<TBinaryProtocol> (trans);
    rpcClient client(proto);

    try {
        trans->open();
        std::string res;
        client.sendCommand(res, "create table test(num1 int, num2 float, str char(20));");
        std::cout << res << std::endl;
    } catch (...) {
        std::cout << "Client caught an exception" << std::endl;
    }
    trans->close();
}

