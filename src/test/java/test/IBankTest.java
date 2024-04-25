package test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static data.DataGenerator.Registration.getRegisteredUser;
import static data.DataGenerator.Registration.getUser;
import static data.DataGenerator.getRandomLogin;
import static data.DataGenerator.getRandomPassword;

public class IBankTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldLoginRegisteredUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $$("button").find(Condition.exactText("Продолжить")).click();
        $("h2").shouldBe(Condition.appear, Duration.ofSeconds(15));
        $("h2").shouldHave(Condition.exactText("Личный кабинет"));
    }

    @Test
    void shouldNotLoginNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $$("button").find(Condition.exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldBe(Condition.appear, Duration.ofSeconds(15));
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $$("button").find(Condition.exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldBe(Condition.appear, Duration.ofSeconds(15));
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    void shouldNotLoginIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $$("button").find(Condition.exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldBe(Condition.appear, Duration.ofSeconds(15));
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $$("button").find(Condition.exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldBe(Condition.appear, Duration.ofSeconds(15));
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }
}
