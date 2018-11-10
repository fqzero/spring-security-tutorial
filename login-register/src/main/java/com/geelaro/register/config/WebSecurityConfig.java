package com.geelaro.register.config;

import com.geelaro.register.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomUserDetailService userDetailService;

    /**
     * 密码加密
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * auth的方式
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  //csrf不可用
                .authorizeRequests()
                .antMatchers("/static/**","/css/**").permitAll() //访问允许静态文件
                .antMatchers("/","/register").permitAll() //允许访问首页和注册页
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").failureUrl("/login?error")//指定登录页和登录失败页
                .defaultSuccessUrl("/userInfo") //登录成功跳转页
                .usernameParameter("name")
                .passwordParameter("passWd")
                .and()
                .logout().logoutSuccessUrl("/login").permitAll() //退出登录跳转页
                .and()
                .rememberMe() //remember me
                .tokenRepository(tokenRepository()) //存储
                .userDetailsService(userDetailService)
                .tokenValiditySeconds(24*60*60);//token有效期24h
    }


    public PersistentTokenRepository tokenRepository(){
        //存储内存，不推荐
//        InMemoryTokenRepositoryImpl memory =new InMemoryTokenRepositoryImpl();
//        return memory;
        /** 存档到数据库中 **/
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(this.dataSource);
        return db;
    }
}
