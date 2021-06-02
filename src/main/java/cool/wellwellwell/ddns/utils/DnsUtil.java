package cool.wellwellwell.ddns.utils;

import cn.hutool.core.util.StrUtil;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeSubDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeSubDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DnsUtil {

    @Autowired
    private IAcsClient client;

    /**
     * 通过域名获取对应的解析
     * @param host 域名地址
     * @return
     */
    public String getIpByHost(String host) {
        DescribeSubDomainRecordsResponse.Record record = getRecodesByHost(host, "A");
        if (record == null) {
            return null;
        }
        return record.getValue();
    }

    /**
     * 修改IP解析记录
     * @param host
     * @param value
     * @return
     */
    public String updateBindIp(String host, String value) {
        DescribeSubDomainRecordsResponse.Record record = getRecodesByHost(host, "A");
        if (record == null) {
            return "当前DNS不存在";
        }
        String secondaryDomain = StringUtil.getSecondaryDomain(host);
        UpdateDomainRecordRequest request = new UpdateDomainRecordRequest();
        request.setRegionId("cn-hangzhou");
        request.setRecordId(record.getRecordId());
        request.setRR(secondaryDomain);
        request.setType("A");
        request.setValue(value);

        try {
            UpdateDomainRecordResponse response = client.getAcsResponse(request);
            return "success";
        } catch (ServerException e) {
            e.printStackTrace();
            return "error";
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
            return e.getErrMsg();
        }
    }

    /**
     * 获取域名解析结果
     * @param host
     * @param type
     * @return
     */
    public DescribeSubDomainRecordsResponse.Record getRecodesByHost(String host, String type) {
        if (StrUtil.hasBlank(host) || StrUtil.hasBlank(type)) {
            return null;
        }

        DescribeSubDomainRecordsRequest request = new DescribeSubDomainRecordsRequest();
        request.setRegionId("cn-hangzhou");
        request.setSubDomain(host);

        DescribeSubDomainRecordsResponse response;

        try {
            response = client.getAcsResponse(request);
            List<DescribeSubDomainRecordsResponse.Record> domainRecords = response.getDomainRecords();
            for (DescribeSubDomainRecordsResponse.Record domainRecord : domainRecords) {
                if (type.toUpperCase().equals(domainRecord.getType())) {
                    return domainRecord;
                }
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

        return null;
    }
}
