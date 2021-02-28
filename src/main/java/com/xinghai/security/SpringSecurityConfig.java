package com.xinghai.security;

import com.xinghai.security.authentication.MyAuthenctiationFailureHandler;
import com.xinghai.security.authentication.MyAuthenticationSuccessHandler;
import com.xinghai.security.authentication.MyLogoutSuccessHandler;
import com.xinghai.security.exception.RestAuthenticationAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private MyAuthenctiationFailureHandler myAuthenctiationFailureHandler;

    @Autowired
    private RestAuthenticationAccessDeniedHandler restAuthenticationAccessDeniedHandler;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
       return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //使用BCrypt加密方式
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭请求伪造
        http.csrf().disable();

        //解决X-Frame-Options to deny问题
        http.headers().frameOptions().sameOrigin();

        //任何请求都需要验证
        http.authorizeRequests()
                //设置哪些静态文件不需要拦截
                .antMatchers(
                        "/login.html",
                        "/my/**",
                        "/treetable-lay/**",
                        "/xadmin/**",
                        "/ztree/**",
                        "/js/**",
                        "/webuploader/**",
                        "/static/**"
                )
                .permitAll()
                .anyRequest()
                .authenticated();

        http.formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                //登陆成功处理
                .successHandler(myAuthenticationSuccessHandler)
                //登陆失败处理
                .failureHandler(myAuthenctiationFailureHandler)
                //登出
                .and().logout().permitAll().invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(myLogoutSuccessHandler);

        //异常处理
        http.exceptionHandling().accessDeniedHandler(restAuthenticationAccessDeniedHandler);


    }
}
