package ox.oxmall.service;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ox.oxmall.util.ConfigUrlUtil;
import ox.oxmall.util.HttpUtil;
import ox.oxmall.util.PayCommonUtil;
import ox.oxmall.util.TradeTypeEnum;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 描述:
 *
 * @author think
 * @create 2019-04-21 22:50
 */
@Service
public class WeiXinPayService {

    private Logger logger = LoggerFactory.getLogger(WeiXinPayService.class);

    public String weixinPayH5() {
        SortedMap<Object, Object> paramMap = new TreeMap<>();
        String mweb_url = "";
        try {
            paramMap.put("product_id", "11111111"); //商品id
            paramMap.put("body", "测试"); //商品描述
            paramMap.put("out_trade_no", "11123123123213345435aaa"); //商户订单号
            paramMap.put("trade_type", TradeTypeEnum.WeiXinH5.getCode()); //交易类型
            paramMap.put("total_fee", "1.12"); //总金额
            paramMap.put("spbill_create_ip", "192.168.1.11"); //终端IP

            String sign = PayCommonUtil.createSign("utf-8", paramMap, "wangyubin_wyb");
            paramMap.put("sign", sign); //签名

            paramMap.put("notify_url", "http://ycb.natapp1.cc/"); //回调地址

            JSONObject json = new JSONObject();
            json.put("type","Wap");
            json.put("wap_url","测试");
            json.put("wap_name","http://ycb.natapp1.cc/");
            JSONObject h5Info = new JSONObject();
            h5Info.put("h5_info", json);

            paramMap.put("scene_info", h5Info.toString());
            String requestXML = PayCommonUtil.getRequestXml(paramMap);
            String result = HttpUtil.postData(ConfigUrlUtil.UNIFIEDORDER, requestXML);
            Map map = PayCommonUtil.doXMLParse(result);
            String return_code = (String) map.get("return_code");
            if("SUCCESS".equals(return_code)){
                mweb_url = (String) map.get("mweb_url");
                logger.info("weixinPayH5支付成功,订单号:{} ", paramMap.get("out_trade_no"));
            }else{
                String errCodeDes = (String) map.get("err_code_des");
                logger.info("weixinPayH5支付失败,订单号:{},失败errCodeDes信息:{} ", paramMap.get("out_trade_no"),errCodeDes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mweb_url;
    }
}
