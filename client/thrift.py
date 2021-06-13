import sys
import glob
sys.path.append('gen-py')

from masterService import masterSvc

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

if __name__ == "__main__":
    transport = TSocket.TSocket('10.162.42.0', 9090)

    transport = TTransport.TBufferedTransport(transport)

    protocol = TBinaryProtocol.TBinaryProtocol(transport)

    client = masterSvc.Client(protocol)

    transport.open()

    test = client.createTable("test")
    print(test)

    transport.close()
