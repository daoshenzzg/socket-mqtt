# 自定义变长消息协议
Header定义
Socket变长协议的Header总长度为20 Bytes，具体含义参见下表：

| 序号  | 字段  | 长度  | 说明  |
| --- | --- | --- | --- |
| 1   | version | 1 Byte | 版本号。 |
| 2   | messageType | 1 Byte | 消息类型。0-心跳；1-请求；2-响应。 |
| 3   | bodyLength | 4 Bytes | Body长度。 |
| 4   | seqID | 4 Bytes | 消息ID（SequenceID，由客户端传入，服务端使用客户端传入的值进行响应，服务端也可以指定值）。 |
| 5   | reserved | 10 Bytes | 保留。 |
