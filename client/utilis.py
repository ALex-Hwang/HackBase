# thrift
import sys
import glob
sys.path.append('gen-py')

from masterService import masterSvc
from regionService import regionSvc
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

# zookeeper
from kazoo.client import KazooClient

# zookeeper server
ZookeeperHost = "10.162.42.0:2181"

# return the ip address of the master
# return None if no mater is found
def findMaster():
    zk = KazooClient(hosts=ZookeeperHost) 
    zk.start()
    if not zk.exists("/MasterInfo"):
        print("No master available right now...")
        return None
    data, _ = zk.get("/MasterInfo")
    zk.stop()
    return data

# desciption: get the region server's ip address of the table
# return the ip address of the region server
# return None if the tableName is not found
def whereTable(tableName):
    zk = KazooClient(hosts=ZookeeperHost)
    zk.start()

    if not zk.exists("/MetaDataServer"):
        print("no tables at all...")
        return None
    tables = zk.get_children("/MetaDataServer")
    for table in tables:
        if table == tableName:
            address, _ = zk.get("/MetaDataServer/"+table)
            if address == b'404':
                print('No region server available!')
                return None
            address = address.decode('utf-8')
            address = address.split('_')[1]
            address = address.encode('utf-8')
            zk.stop()
            return address
    print("no table called: "+tableName+" found")
    zk.stop()
    return None

def existsTable(tableName):
    zk = KazooClient(hosts=ZookeeperHost)
    zk.start()

    if not zk.exists("/MetaDataServer"):
        zk.stop()
        return False
    tables = zk.get_children("/MetaDataServer")
    for table in tables:
        if table == tableName:
            zk.stop()
            return True
    zk.stop()
    return False

# desciption: get the nodes in the root of the zookeeper
# return all the nodes in the root directory of the zookeeper
def zookeeperChecker():
    zk = KazooClient(hosts=ZookeeperHost)
    zk.start()

    nodes = zk.get_children("/MetaDataServer")
    zk.stop()
    return nodes

# description: parse the data from the zookeeper
# return ip, port
def parseAddress(rawData):
    if isinstance(rawData, str):
        data = rawData
    else:
        data = rawData.decode('utf-8')
    ipAndport = data.split(':')
    return ipAndport[0], ipAndport[1]

# description: api provided to interpreter, connects to the master
# return the result of the creation
def createTable(tableName):
    ip, port = parseAddress(findMaster())

    transport = TSocket.TSocket(ip, port)
    transport = TTransport.TBufferedTransport(transport)
    protocol = TBinaryProtocol.TBinaryProtocol(transport)
    client = masterSvc.Client(protocol)
    transport.open()

    ip = client.createTable(tableName)

    transport.close()
    return ip

# description: api provided to interpreter, connects to the master
# return the result of the drop
def dropTable(tableName):
    ip, port = parseAddress(findMaster())

    transport = TSocket.TSocket(ip, port)
    transport = TTransport.TBufferedTransport(transport)
    protocol = TBinaryProtocol.TBinaryProtocol(transport)
    client = masterSvc.Client(protocol)
    transport.open()

    address = whereTable(tableName)
    if address != None:
        msg = client.deleteTable(tableName)
    else:
        msg = "error"
    transport.close()
    return msg

def command(address, command):

    if address == None:
        return None
    
    ip, port = parseAddress(address)

    transport = TSocket.TSocket(ip, port)
    transport = TTransport.TBufferedTransport(transport)
    protocol = TBinaryProtocol.TBinaryProtocol(transport)
    client = regionSvc.Client(protocol)
    transport.open()

    msg = client.sendCommand(command)
    transport.close()
    return msg


if __name__ == "__main__":
    print("Debug")

