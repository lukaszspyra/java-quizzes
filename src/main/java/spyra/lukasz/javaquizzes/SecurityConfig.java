package spyra.lukasz.javaquizzes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import spyra.lukasz.javaquizzes.shared.AvailableRole;

import java.util.Arrays;

/**
 * Configuration of Spring Security
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String[] resources = new String[]{
            "/modern/**", "/css/**", "/images/**", "/js/**", "/plugins/**", "/scss/**", "/fonts/**", "/icons/**"
    };

    private final String[] admins = new String[]{AvailableRole.SUPER_ADMIN.name(), AvailableRole.ADMIN.name()};
    private final String[] users = Arrays.stream(AvailableRole.values())
            .map(AvailableRole::name)
            .toArray(String[]::new);

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(resources).permitAll()
                .antMatchers("/", "/home").permitAll()
                .antMatchers("/demo/**").permitAll()
                .antMatchers("/register").permitAll()

                .antMatchers("/superadmin/**").hasAuthority(AvailableRole.SUPER_ADMIN.name())
                .antMatchers("/admin/**", "/restricted/**").hasAnyAuthority(admins)
                .antMatchers("/user").hasAnyAuthority(users)
                .anyRequest()
                .authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/login?logout");
    }
}
