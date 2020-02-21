package org.tron.trongeventquery.monitor;


import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MonitorInfo {
  private int status;
  private String msg;
  private DataInfo data;

  public int getStatus() {
    return this.status;
  }

  public MonitorInfo setStatus(int status) {
    this.status = status;
    return this;
  }

  public String getMsg() {
    return this.msg;
  }

  public MonitorInfo setMsg(String msg) {
    this.msg = msg;
    return this;
  }

  public DataInfo getDataInfo() {
    return this.data;
  }

  public MonitorInfo setDataInfo(DataInfo data) {
    this.data = data;
    return this;
  }

//  public MonitorInfo ToProtoEntity() {
//    Protocol.MonitorInfo.Builder builder = Protocol.MonitorInfo.newBuilder();
//    builder.setStatus(getStatus());
//    builder.setMsg(getMsg());
//    Protocol.MonitorInfo.DataInfo.Builder dataInfo = Protocol.MonitorInfo.DataInfo.newBuilder();
//    DataInfo data = getDataInfo();
//    dataInfo.setInterval(data.getInterval());
//
//    Protocol.MonitorInfo.DataInfo.NodeInfo.Builder nodeInfo =
//        Protocol.MonitorInfo.DataInfo.NodeInfo.newBuilder();
//    DataInfo.NodeInfo node = data.getNodeInfo();
//    nodeInfo.setIp(node.getIp());
//    nodeInfo.setType(node.getType());
//    nodeInfo.setStatus(node.getType());
//    nodeInfo.setVersion(node.getVersion());
//    nodeInfo.setNoUpgradedSRCount(node.getNoUpgradedSRCount());
//
//    for (DataInfo.NodeInfo.NoUpgradedSR noUpgradedSR : node.getNoUpgradedSRList()) {
//      Protocol.MonitorInfo.DataInfo.NodeInfo.NoUpgradedSR.Builder noUpgradeSRProto =
//          Protocol.MonitorInfo.DataInfo.NodeInfo.NoUpgradedSR.newBuilder();
//      noUpgradeSRProto.setAddress(noUpgradedSR.getAddress());
//      noUpgradeSRProto.setUrl(noUpgradedSR.getUrl());
//      nodeInfo.addNoUpgradedSRList(noUpgradeSRProto.build());
//    }
//    // set node info
//    dataInfo.setNode(nodeInfo.build());
//
//    Protocol.MonitorInfo.DataInfo.BlockChainInfo.Builder blockChain =
//        Protocol.MonitorInfo.DataInfo.BlockChainInfo.newBuilder();
//    DataInfo.BlochainInfo BlockChain = data.getBlockchainInfo();
//    blockChain.setHeadBlockTimestamp(BlockChain.getHeadBlockTimestamp());
//    blockChain.setHeadBlockHash(BlockChain.getHeadBlockHash());
//    blockChain.setBlockProcessTime(BlockChain.getBlockProcessTime());
//    blockChain.setForkCount(BlockChain.getForkCount());
//    blockChain.setHeadBlockNum(BlockChain.getHeadBlockNum());
//    blockChain.setTxCacheSize(BlockChain.getTxCacheSize());
//    blockChain.setMissedTxCount(BlockChain.getMissTxCount());
//
//    Protocol.MonitorInfo.DataInfo.BlockChainInfo.TPSInfo.Builder tpsInfo =
//        Protocol.MonitorInfo.DataInfo.BlockChainInfo.TPSInfo.newBuilder();
//    DataInfo.BlochainInfo.TPSInfo TpsInfo = BlockChain.getTPS();
//    tpsInfo.setMeanRate(TpsInfo.getMeanRate());
//    tpsInfo.setOneMinuteRate(TpsInfo.getOneMinuteRate());
//    tpsInfo.setFiveMinuteRate(TpsInfo.getFiveMinuteRate());
//    tpsInfo.setFifteenMinuteRate(TpsInfo.getFifteenMinuteRate());
//    blockChain.setTPS(tpsInfo.build());
//    // set blockchain info
//    dataInfo.setBlockchain(blockChain.build());
//
//
//    Protocol.MonitorInfo.DataInfo.NetInfo.Builder netInfo =
//        Protocol.MonitorInfo.DataInfo.NetInfo.newBuilder();
//    DataInfo.NetInfo NetInfo = data.getNetInfo();
//    netInfo.setConnectionCount(NetInfo.getConnectionCount());
//    netInfo.setValidConnectionCount(NetInfo.getValidConnectionCount());
//    netInfo.setErrorProtoCount(NetInfo.getErrorProtoCount());
//    netInfo.setTCPInTraffic(NetInfo.getTCPInTraffic());
//    netInfo.setTCPOutTraffic(NetInfo.getTCPOutTraffic());
//    netInfo.setDisconnectionCount(NetInfo.getDisconnectionCount());
//    netInfo.setUDPInTraffic(NetInfo.getUDPInTraffic());
//    netInfo.setUDPOutTraffic(NetInfo.getUDPOutTraffic());
//
//    Protocol.MonitorInfo.DataInfo.NetInfo.ApiInfo.Builder apiInfo =
//        Protocol.MonitorInfo.DataInfo.NetInfo.ApiInfo.newBuilder();
//    apiInfo.setTotalCount(NetInfo.getApi().getTotalCount());
//    apiInfo.setTotalFailCount(NetInfo.getApi().getTotalFailCount());
//    for (DataInfo.NetInfo.ApiInfo.ApiDetailInfo ApiDetail : NetInfo.getApi().getApiDetailInfo()) {
//      Protocol.MonitorInfo.DataInfo.NetInfo.ApiInfo.ApiDetailInfo.Builder apiDetail =
//          Protocol.MonitorInfo.DataInfo.NetInfo.ApiInfo.ApiDetailInfo.newBuilder();
//      apiDetail.setName(ApiDetail.getName());
//      apiDetail.setCount(ApiDetail.getCount());
//      apiDetail.setFailCount(ApiDetail.getFailCount());
//      apiInfo.addDetail(apiDetail.build());
//    }
//    netInfo.setApi(apiInfo.build());
//
//
//    for (DataInfo.NetInfo.DisconnectionDetailInfo DisconnectionDetail : NetInfo
//        .getDisconnectionDetail()) {
//      Protocol.MonitorInfo.DataInfo.NetInfo.DisconnectionDetailInfo.Builder disconnectionDetail =
//          Protocol.MonitorInfo.DataInfo.NetInfo.DisconnectionDetailInfo.newBuilder();
//      disconnectionDetail.setReason(DisconnectionDetail.getReason());
//      disconnectionDetail.setCount(DisconnectionDetail.getCount());
//      netInfo.addDisconnectionDetail(disconnectionDetail.build());
//    }
//
//    Protocol.MonitorInfo.DataInfo.NetInfo.LatencyInfo.Builder latencyInfo =
//        Protocol.MonitorInfo.DataInfo.NetInfo.LatencyInfo.newBuilder();
//    latencyInfo.setDelay1S(NetInfo.getLatency().getDelay1S());
//    latencyInfo.setDelay2S(NetInfo.getLatency().getDelay2S());
//    latencyInfo.setDelay3S(NetInfo.getLatency().getDelay3S());
//    latencyInfo.setTop99(NetInfo.getLatency().getTop99());
//    latencyInfo.setTop95(NetInfo.getLatency().getTop95());
//    latencyInfo.setTotalCount(NetInfo.getLatency().getTotalCount());
//
//    for (DataInfo.NetInfo.LatencyInfo.LatencyDetailInfo LatencyDetailInfo : NetInfo.getLatency()
//        .getLatencyDetailInfo()) {
//      Protocol.MonitorInfo.DataInfo.NetInfo.LatencyInfo.LatencyDetailInfo.Builder latencyDetail =
//          Protocol.MonitorInfo.DataInfo.NetInfo.LatencyInfo.LatencyDetailInfo.newBuilder();
//      latencyDetail.setCount(LatencyDetailInfo.getCount());
//      latencyDetail.setWitness(LatencyDetailInfo.getWitness());
//      latencyDetail.setTop99(LatencyDetailInfo.getTop99());
//      latencyDetail.setTop95(LatencyDetailInfo.getTop95());
//      latencyDetail.setDelay1S(LatencyDetailInfo.getDelay1S());
//      latencyDetail.setDelay2S(LatencyDetailInfo.getDelay2S());
//      latencyDetail.setDelay3S(LatencyDetailInfo.getDelay3S());
//      latencyInfo.addDetail(latencyDetail.build());
//    }
//
//    // set latency info
//    netInfo.setLatency(latencyInfo.build());
//    // set net info
//    dataInfo.setNet(netInfo.build());
//    // set data info
//    builder.setData(dataInfo.build());
//    return builder.build();
//  }


  public static class DataInfo {
    private int interval;
    private NetInfo net;

    public int getInterval() {
      return this.interval;
    }

    public DataInfo setInterval(int interval) {
      this.interval = interval;
      return this;
    }
    public NetInfo getNetInfo() {
      return this.net;
    }

    public DataInfo setNetInfo(NetInfo net) {
      this.net = net;
      return this;
    }

    // network monitor information
    public static class NetInfo {
      private int errorProtoCount;
      private ApiInfo api;
      private int connectionCount;
      private int validConnectionCount;
      private long TCPInTraffic;
      private long TCPOutTraffic;
      private int disconnectionCount;
      private List<DisconnectionDetailInfo> disconnectionDetail = new ArrayList<>();
      private long UDPInTraffic;
      private long UDPOutTraffic;
      private LatencyInfo latency;

      public int getErrorProtoCount() {
        return this.errorProtoCount;
      }

      public NetInfo setErrorProtoCount(int errorProtoCount) {
        this.errorProtoCount = errorProtoCount;
        return this;
      }

      public ApiInfo getApi() {
        return this.api;
      }

      public NetInfo setApi(ApiInfo api) {
        this.api = api;
        return this;
      }

      public int getConnectionCount() {
        return this.connectionCount;
      }

      public NetInfo setConnectionCount(int connectionCount) {
        this.connectionCount = connectionCount;
        return this;
      }

      public int getValidConnectionCount() {
        return this.validConnectionCount;
      }

      public NetInfo setValidConnectionCount(int validConnectionCount) {
        this.validConnectionCount = validConnectionCount;
        return this;
      }

      public long getTCPInTraffic() {
        return this.TCPInTraffic;
      }

      public NetInfo setTCPInTraffic(long TCPInTraffic) {
        this.TCPInTraffic = TCPInTraffic;
        return this;
      }

      public long getTCPOutTraffic() {
        return this.TCPOutTraffic;
      }

      public NetInfo setTCPOutTraffic(long TCPOutTraffic) {
        this.TCPOutTraffic = TCPOutTraffic;
        return this;
      }

      public int getDisconnectionCount() {
        return this.disconnectionCount;
      }

      public NetInfo setDisconnectionCount(int disconnectionCount) {
        this.disconnectionCount = disconnectionCount;
        return this;
      }

      public List<DisconnectionDetailInfo> getDisconnectionDetail() {
        return this.disconnectionDetail;
      }

      public NetInfo setDisconnectionDetail(List<DisconnectionDetailInfo> disconnectionDetail) {
        this.disconnectionDetail = disconnectionDetail;
        return this;
      }

      public long getUDPInTraffic() {
        return this.UDPInTraffic;
      }

      public NetInfo setUDPInTraffic(long UDPInTraffic) {
        this.UDPInTraffic = UDPInTraffic;
        return this;
      }

      public long getUDPOutTraffic() {
        return this.UDPOutTraffic;
      }

      public NetInfo setUDPOutTraffic(long UDPOutTraffic) {
        this.UDPOutTraffic = UDPOutTraffic;
        return this;
      }

      public LatencyInfo getLatency() {
        return this.latency;
      }

      public NetInfo setLatency(LatencyInfo latency) {
        this.latency = latency;
        return this;
      }

      // API monitor information
      public static class ApiInfo {
        private int totalCount;
        private int totalFailCount;
        private List<ApiDetailInfo> detail = new ArrayList<>();

        public int getTotalCount() {
          return this.totalCount;
        }

        public ApiInfo setTotalCount(int totalCount) {
          this.totalCount = totalCount;
          return this;
        }

        public int getTotalFailCount() {
          return this.totalFailCount;
        }

        public ApiInfo setTotalFailCount(int totalFailCount) {
          this.totalFailCount = totalFailCount;
          return this;
        }

        public List<ApiDetailInfo> getApiDetailInfo() {
          return this.detail;
        }

        public ApiInfo setApiDetailInfo(List<ApiDetailInfo> detail) {
          this.detail = detail;
          return this;
        }

        public static class ApiDetailInfo {
          private String name;
          private int count;
          private int failCount;

          public String getName() {
            return this.name;
          }

          public ApiDetailInfo setName(String name) {
            this.name = name;
            return this;
          }

          public int getCount() {
            return this.count;
          }

          public ApiDetailInfo setCount(int count) {
            this.count = count;
            return this;
          }

          public int getFailCount() {
            return this.failCount;
          }

          public ApiDetailInfo setFailCount(int failCount) {
            this.failCount = failCount;
            return this;
          }
        }
      }

      // disconnection monitor information
      public static class DisconnectionDetailInfo {
        private String reason;
        private int count;

        public String getReason() {
          return this.reason;
        }

        public DisconnectionDetailInfo setReason(String reason) {
          this.reason = reason;
          return this;
        }

        public int getCount() {
          return this.count;
        }

        public DisconnectionDetailInfo setCount(int count) {
          this.count = count;
          return this;
        }

      }

      // latency monitor information
      public static class LatencyInfo {
        private int top99;
        private int top95;
        private int totalCount;
        private int delay1S;
        private int delay2S;
        private int delay3S;
        private List<LatencyDetailInfo> detail = new ArrayList<>();

        public int getTop99() {
          return this.top99;
        }

        public LatencyInfo setTop99(int top99) {
          this.top99 = top99;
          return this;
        }

        public int getTop95() {
          return this.top95;
        }

        public LatencyInfo setTop95(int top95) {
          this.top95 = top95;
          return this;
        }

        public int getTotalCount() {
          return this.totalCount;
        }

        public LatencyInfo setTotalCount(int totalCount) {
          this.totalCount = totalCount;
          return this;
        }

        public int getDelay1S() {
          return this.delay1S;
        }

        public LatencyInfo setDelay1S(int delay1S) {
          this.delay1S = delay1S;
          return this;
        }

        public int getDelay2S() {
          return this.delay2S;
        }

        public LatencyInfo setDelay2S(int delay2S) {
          this.delay2S = delay2S;
          return this;
        }

        public int getDelay3S() {
          return this.delay3S;
        }

        public LatencyInfo setDelay3S(int delay3S) {
          this.delay3S = delay3S;
          return this;
        }

        public List<LatencyDetailInfo> getLatencyDetailInfo() {
          return this.detail;
        }

        public LatencyInfo setLatencyDetailInfo(List<LatencyDetailInfo> detail) {
          this.detail = detail;
          return this;
        }

        public static class LatencyDetailInfo {
          private String witness;
          private int top99;
          private int top95;
          private int count;
          private int delay1S;
          private int delay2S;
          private int delay3S;

          public String getWitness() {
            return this.witness;
          }

          public LatencyDetailInfo setWitness(String witness) {
            this.witness = witness;
            return this;
          }

          public int getTop99() {
            return this.top99;
          }

          public LatencyDetailInfo setTop99(int top99) {
            this.top99 = top99;
            return this;
          }

          public int getTop95() {
            return this.top95;
          }

          public LatencyDetailInfo setTop95(int top95) {
            this.top95 = top95;
            return this;
          }

          public int getCount() {
            return this.count;
          }

          public LatencyDetailInfo setCount(int count) {
            this.count = count;
            return this;
          }

          public int getDelay1S() {
            return this.delay1S;
          }

          public LatencyDetailInfo setDelay1S(int delay1S) {
            this.delay1S = delay1S;
            return this;
          }

          public int getDelay2S() {
            return this.delay2S;
          }

          public LatencyDetailInfo setDelay2S(int delay2S) {
            this.delay2S = delay2S;
            return this;
          }

          public int getDelay3S() {
            return this.delay3S;
          }

          public LatencyDetailInfo setDelay3S(int delay3S) {
            this.delay3S = delay3S;
            return this;
          }

        }
      }

    }


  }
}
