package org.tron.trongeventquery.monitor;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tron.trongeventquery.filter.CommonFilter;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j(topic = "monitorService")
@Component
public class MonitorService {

//  @Autowired
//  private MonitorMetric monitorMetric;

  public MonitorInfo getMonitorInfo() {
    MonitorInfo monitorInfo = new MonitorInfo();
    monitorInfo.setStatus(1);
    monitorInfo.setMsg("success");
    MonitorInfo.DataInfo data = new MonitorInfo.DataInfo();
    setNetInfo(data);
    monitorInfo.setDataInfo(data);

//    return monitorInfo.ToProtoEntity();
    return monitorInfo;
  }

  public void setNetInfo(MonitorInfo.DataInfo data) {
    MonitorInfo.DataInfo.NetInfo netInfo = new MonitorInfo.DataInfo.NetInfo();

    // set api request info
    MonitorInfo.DataInfo.NetInfo.ApiInfo apiInfo = new MonitorInfo.DataInfo.NetInfo.ApiInfo();
    CommonFilter commonFilter=new CommonFilter();
    apiInfo.setTotalCount(commonFilter.getInstance().getTotalCount());
    apiInfo.setTotalFailCount(commonFilter.getInstance().getFailCount());
    List<MonitorInfo.DataInfo.NetInfo.ApiInfo.ApiDetailInfo> apiDetails = new ArrayList<>();
    for(Map.Entry<String, JSONObject> entry: commonFilter.getInstance().getEndpointMap().entrySet()){
      MonitorInfo.DataInfo.NetInfo.ApiInfo.ApiDetailInfo apiDetail =
          new MonitorInfo.DataInfo.NetInfo.ApiInfo.ApiDetailInfo();
      apiDetail.setName(entry.getKey());
      apiDetail.setCount((int)entry.getValue().get(CommonFilter.TOTAL_REQUST));
      apiDetail.setFailCount((int)entry.getValue().get(CommonFilter.FAIL_REQUST));
      apiDetails.add(apiDetail);
    }
    apiInfo.setApiDetailInfo(apiDetails);
    netInfo.setApi(apiInfo);

    data.setNetInfo(netInfo);

  }

}
