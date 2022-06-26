package com.zhong.kisara.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// import static com.zhong.kisara.utils.Constants.FIELD_LOGIC;
// import static com.zhong.kisara.utils.Constants.FIELD_TYPE;

/**
 * @author zhonghanbo
 */
@Controller
public class TableController {

    @GetMapping(value = {"/custom_table"})
    public String index(Model model) {
        // model.addAttribute("fieldType", FIELD_TYPE);
        // model.addAttribute("logics", FIELD_LOGIC);
        return "/custom_table";
    }

    @GetMapping(value = {"/connection"})
    public String connection() {

        return "/connection";
    }

    @GetMapping(value = {"/test"})
    public String test() {

        return "/test";
    }
}
