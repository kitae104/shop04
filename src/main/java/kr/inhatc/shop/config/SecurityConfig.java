package kr.inhatc.shop.config;

import jakarta.servlet.http.HttpSession;
import kr.inhatc.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 기본 로그인 사용하기
        //http.formLogin(Customizer.withDefaults());
        //http.logout(Customizer.withDefaults());

//        // 로그인 처리하기
//        http.formLogin(form -> form
//                .loginPage("/member/login")
//                .defaultSuccessUrl("/")
//                .failureUrl("/member/login/error")
//                .usernameParameter("email")
//                .passwordParameter("password")
//                .permitAll());
//
//        http.logout(Customizer.withDefaults());
//
//        // 각 페이지에 대한 접근 권한 설정
//        http.authorizeHttpRequests(request -> request
//                .requestMatchers("/css/**").permitAll()
//                .requestMatchers("/", "/member/**", "/item/**", "/images/**").permitAll()
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .anyRequest().authenticated());
//
//        // 권한 없는 경우에 대한 예외 처리
//        http.exceptionHandling(exception -> exception
//                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        // 로그인 설정
        http
                .formLogin(form -> form
                        .loginPage("/member/login")  // 사용자 정의 로그인 페이지
                        .defaultSuccessUrl("/")       // 로그인 성공 후 이동할 페이지
                        .failureUrl("/member/login/error")  // 로그인 실패 시 이동할 페이지
                        .usernameParameter("email")   // 이메일 필드
                        .passwordParameter("password")// 비밀번호 필드
                        .permitAll()                  // 로그인 페이지는 누구나 접근 가능
                )
                .logout(Customizer.withDefaults()); // 기본 로그아웃 설정

        // 페이지 접근 권한 설정
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/css/**", "/", "/member/**", "/item/**", "/images/**").permitAll()  // 공용 자원에 대한 접근 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // ADMIN 권한을 가진 사용자만 접근 가능
                        .anyRequest().authenticated()  // 그 외의 요청은 인증 필요
                );

        // 권한 예외 처리 설정(인증 필요한 페이지에 권한 없는 사용자가 접근했을 때)
        http
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())  // 권한 없는 접근 시 예외 처리
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
