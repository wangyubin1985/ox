package ox.oxmall.util;

/**
 * 描述:
 *
 * @author think
 * @create 2019-04-21 22:57
 */
public enum  TradeTypeEnum {
    WeiXinH5("MWEB", "微信h5"),
    WeiXinApp("APP", "微信app支付");

    private String code;
    private String name;

    TradeTypeEnum(String code , String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
