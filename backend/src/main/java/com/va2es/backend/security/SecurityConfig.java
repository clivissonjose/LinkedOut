package com.va2es.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter){
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(java.util.List.of("http://localhost:4200"));
                    corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Rotas Públicas
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        // Ferramenta de Teste (Apenas para desenvolvimento)
                        .requestMatchers(HttpMethod.POST, "/users/change-role").authenticated()

                        // Gestão de Empresas (Company)
                        .requestMatchers(HttpMethod.GET, "/company/list", "/company/search/**").authenticated() // Todos autenticados podem ver
                        .requestMatchers("/company/**").hasAnyRole("ADMIN", "GESTOR") // Ações de escrita para ADMIN e GESTOR

                        // Gestão de Vagas (Vacancy)
                        .requestMatchers(HttpMethod.GET, "/vagas/list", "/vagas/filter", "/vagas/{id}").authenticated() // Todos autenticados podem ver
                        .requestMatchers("/vagas/**").hasAnyRole("ADMIN", "GESTOR") // Ações de escrita para ADMIN e GESTOR

                        // Gestão de Estudantes (Student)
                        .requestMatchers(HttpMethod.POST, "/students/{id}/apply/{vacancyId}").hasRole("STUDENT") // Apenas estudantes se candidatam
                        .requestMatchers(HttpMethod.GET, "/students/{id}/applications").hasRole("STUDENT") // Apenas estudantes veem suas candidaturas
                        .requestMatchers(HttpMethod.POST, "/students").hasAnyRole("USER", "ADMIN") // Usuário comum ou admin criam perfil
                        .requestMatchers(HttpMethod.GET, "/students/**").authenticated() // Todos autenticados podem visualizar perfis (regra geral)
                        .requestMatchers(HttpMethod.PUT, "/students/**").hasAnyRole("STUDENT", "ADMIN") // Apenas o próprio estudante ou admin podem editar
                        .requestMatchers(HttpMethod.DELETE, "/students/**").hasAnyRole("STUDENT", "ADMIN") // Apenas o próprio estudante ou admin podem deletar

                        // Gestão de Usuários (Users)
                        .requestMatchers(HttpMethod.GET, "/users", "/users/me").authenticated() // Todos autenticados podem ver a lista de usuários e seu próprio perfil
                        .requestMatchers(HttpMethod.PUT, "/users/name").authenticated()

                        // Qualquer outra requisição precisa de autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}