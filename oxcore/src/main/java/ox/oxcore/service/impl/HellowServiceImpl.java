package ox.oxcore.service.impl;

import com.oxapi.spi.HellowService;

/**
 * 描述:
 *
 * @author think
 * @create 2019-06-02 15:03
 */
public class HellowServiceImpl implements HellowService {

    @Override
    public void sayHellow(String str) {
        System.out.println("hellow , " + str );
    }
}
