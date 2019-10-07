package ox.oxmall.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 描述:
 *
 * @author think
 * @create 2019-04-20 22:43
 */

@RestController
public class TestController {

    @ApiOperation(value="测试index_value", notes="测试index_notes")
    @RequestMapping(value = "/index/{id}", method = RequestMethod.GET)
    @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "String", paramType = "path")
    public String index(@PathVariable String id){
        return "index" + (id==null?"":id);
    }

}
