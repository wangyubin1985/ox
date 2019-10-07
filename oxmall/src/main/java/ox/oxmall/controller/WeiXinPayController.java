package ox.oxmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ox.oxmall.service.WeiXinPayService;

/**
 * 描述:
 *
 * @author think
 * @create 2019-04-21 17:38
 */

public class WeiXinPayController {
    @Autowired
    private WeiXinPayService weiXinPayService;


    @RequestMapping(value = "/h5pay", method = RequestMethod.GET)
    public String h5pay(){
        String url = weiXinPayService.weixinPayH5();
        return "";
    }
}
