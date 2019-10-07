package ox.oxmall.util;

/**
 * 描述:
 *
 * @author think
 * @create 2019-04-21 22:43
 */
public class ConfigUrlUtil {

    /**
     * 微信支付接口
     */
    //统一下单
    public static final String UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //查询订单
    public static final String ORDERQUERY = "https://api.mch.weixin.qq.com/pay/orderquery";
    //关闭订单
    public static final String CLOSEORDER = "https://api.mch.weixin.qq.com/pay/closeorder";
    //申请退款
    public static final String REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    //查询退款
    public static final String REFUNDQUERY = "https://api.mch.weixin.qq.com/pay/refundquery";


}
