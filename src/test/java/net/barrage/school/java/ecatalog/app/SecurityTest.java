package net.barrage.school.java.ecatalog.app;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("fake")
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    // Use https://jwt.io/ to check this bearer token
    public static final String BEARER = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ1cm46dG95b3U6YXBpIiwic3ViIjoiM2QxYzljZjktYWI0ZC00MjcwLWJiMjYtMjYxOTI5N2UyNTg0IiwiaXNzIjoidXJuOmJhcnJhZ2U6dXNlciIsImlhdCI6MTcwMTAzMDQzOCwiZXhwIjoxNzAxMDM0MDM4LCJiLXJvbGVzIjoiUk9MRV9NRVJDSEFOVF9NQU5BR0VSLFJPTEVfU1lTVEVNX0FETUlOIn0.xtdlBZ4Zc80_EFd-eB7L4Du60K-QIx7PysRl8xYgYks";

    @Autowired
    MockMvc mockMvc;

    @Test
    @SneakyThrows
    void simple_jwt_auth() {
        mockMvc.perform(get("/e-catalog/api/v1/products")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void simple_basic_auth() {
        mockMvc.perform(get("/e-catalog/api/v1/products")
                        .with(httpBasic("admin", "password")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void simple_session_auth() {
        mockMvc.perform(get("/e-catalog/api/v1/products")
                        .with(user("admin").roles("SYSTEM_ADMIN")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void simple_anonymous_auth_no_role() {
        mockMvc.perform(get("/e-catalog/api/v1/products")
                        .with(anonymous()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void simple_anonymous_auth_allowed() {
        mockMvc.perform(get("/e-catalog/api/v1/merchants")
                        .with(anonymous()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void simple_form_login() {
        MvcResult result = mockMvc.perform(formLogin().user("admin").password("password"))
                .andDo(print())
                .andExpect(status().isFound())
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();

        assert session != null;
        SecurityContext context = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");

        assertThat(context.getAuthentication().getName()).isEqualTo("admin");


        GrantedAuthority expectedAuthority = new SimpleGrantedAuthority("ROLE_SYSTEM_ADMIN");
        assert (context.getAuthentication().getAuthorities().contains(expectedAuthority));

    }
}
