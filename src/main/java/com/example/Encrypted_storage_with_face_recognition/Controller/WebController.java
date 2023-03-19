package com.example.Encrypted_storage_with_face_recognition.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/")
public class WebController {

    @RequestMapping(value = "/{page}")
    public String page(@PathVariable String page) {
        return page;
    }

    @RequestMapping(value = "")
    public ModelAndView index() {
        ModelAndView mav =new ModelAndView("index.html");
        return mav;
    }

}
