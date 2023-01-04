package com.explosion204.aperture.sandbox3;

import com.explosion204.aperture.sandbox3.controller.StudentsRestController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UriUtilsTest {
  @Test
  void shouldBuildUrlFromString() {
    final UriComponents components = UriComponentsBuilder
            .fromUriString("https://example.org/resources/{resource}")
            .queryParam("q", "{q}")
            .encode()
            .build();

    final String url = components.expand("secret", "1+2").toString();

    assertThat(url).isEqualTo("https://example.org/resources/secret?q=1%2B2");
  }

  @Test
  void shouldBuildUrlFromController() {
    final String url = MvcUriComponentsBuilder
            .fromController(StudentsRestController.class)
            .encode()
            .toUriString();

    assertThat(url).isEqualTo("http://localhost/students");
  }
}
