package kr.inhatc.shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.inhatc.shop.dto.MemberFormDto;
import kr.inhatc.shop.entity.Member;
import kr.inhatc.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/member")
@Slf4j                      // 로그를 위한 어노테이션
@RequiredArgsConstructor    // final로 선언된 객체를 자동으로 생성해주는 어노테이션
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    /**
     *
     * @param memberFormDto : 사용자가 입력한 회원가입 정보
     * @param bindingResult : 입력값 검증 결과
     * @param model : view에 전달할 데이터
     * @return : 회원가입 성공시 메인페이지로 이동, 실패시 회원가입 페이지로 이동
     */
    @PostMapping(value = "/new")
    public String saveMember(@Valid MemberFormDto memberFormDto,
                             BindingResult bindingResult, Model model) {

        log.info("===============> saveMember 정상 호출 : " + memberFormDto);   // 전달 내용 확인!!

        if(bindingResult.hasErrors()) {     // 입력값 검증 결과 에러가 있을 경우
            return "member/memberForm";     // 회원가입 페이지로 이동
        }

        try{
            Member member = Member.createMember(memberFormDto, passwordEncoder);    // 회원가입 정보를 Member 객체로 변환
            memberService.saveMember(member);                                       // 회원가입 정보를 DB에 저장
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    /**
     * 로그인 페이지로 이동
     * @return : 로그인 페이지
     */
    @GetMapping(value = "/login")
    public String memberLogin() {
        return "member/memberLoginForm";
    }

    /**
     * 로그인 실패시 에러 메시지를 전달하며 로그인 페이지로 이동
     * @param model : view에 전달할 데이터(에러 메시지)
     * @return : 로그인 페이지
     */
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }

    @GetMapping("/logout")
    public String performLogout(HttpServletRequest request, HttpServletResponse response) {
        // .. perform logout
        log.info("===============> logout");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
           new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }
}
