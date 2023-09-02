package kr.inhatc.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/member")
public class MemberController {

    @GetMapping(value = "/new")
    public String memberForm(Model model) {

        return "member/memberForm";
    }
}
