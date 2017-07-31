package com.monkey01.springbootstart.controller;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by feiweiwei on 17/7/31.
 */
//@RestController注解能够使项目支持Rest
@RestController
@SpringBootApplication
@RequestMapping(value = "/springboot")
public class UserController {

    @RequestMapping(value = "/getUserByGet", method = RequestMethod.GET)
    String getUserByGet(@RequestParam(value = "userName") String userName){
        return "Hello " + userName;
    }


}
