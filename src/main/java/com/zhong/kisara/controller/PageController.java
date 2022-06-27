package com.zhong.kisara.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zhonghanbo
 */
@Controller
public class PageController {

    /**
     * 主页 / 总览
     *
     * @param model 模型
     * @return {@link String}
     */
    @GetMapping(value = {"/index"})
    public String index(Model model) {
        return "/index";
    }

    /**
     * 自定义表
     *
     * @param model 模型
     * @return {@link String}
     */
    @GetMapping(value = {"/custom_table"})
    public String custom_table(Model model) {
        return "/custom_table";
    }

    /**
     * 连接连接页面
     *
     * @return {@link String}
     */
    @GetMapping(value = {"/connection"})
    public String connection() {

        return "/connection";
    }


    /**
     * 测试 页面
     *
     * @return {@link String}
     */
    @GetMapping(value = {"/test"})
    public String test() {
        return "/test";
    }
}
