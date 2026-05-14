package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.remote.tracing.EventAttribute.setValue;

public class CardDeliveryTest {
    @BeforeEach
            void setup () { Selenide.open("http://localhost:9999");}

    public String generateDate(int days) {
        return LocalDate.now().
                plusDays(days).
                format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    public void testCompletedSuccessfully() {
        $( "[data-test-id='city'] input").setValue("Санкт-Петербург");

        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(generateDate(3));
        $("[data-test-id='name'] input").setValue("Анна Петрова");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .should(Condition.visible, Duration.ofSeconds(15))
                .should(Condition.text("Встреча успешно забронирована на " + generateDate(3)));
    }

    @Test
    public void testDeliveryToTheSelectedCityIsUnavailable() {
        $( "[data-test-id='city'] input").setValue("СанктПетербург");

        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(generateDate(3));
        $("[data-test-id='name'] input").setValue("Анна Петрова");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .should(Condition.visible, Duration.ofSeconds(15))
                .should(Condition.text( "Доставка в выбранный город недоступна"));
    }

    @Test
    public void testNameAndSurnameAreIncorrectАн() {
        $( "[data-test-id='city'] input").setValue("Санкт-Петербург");

        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(generateDate(3));
        $("[data-test-id='name'] input").setValue("Anna Петрова");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .should(Condition.visible, Duration.ofSeconds(15))
                .should(Condition.text( "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void testPhoneNumberIsIncorrect() {
        $( "[data-test-id='city'] input").setValue("Санкт-Петербург");

        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(generateDate(3));
        $("[data-test-id='name'] input").setValue("Анна Петрова");
        $("[data-test-id='phone'] input").setValue("+7999999999");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .should(Condition.visible, Duration.ofSeconds(15))
                .should(Condition.text( "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void testNoConsentCheckbox() {
        $( "[data-test-id='city'] input").setValue("Санкт-Петербург");

        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(generateDate(3));
        $("[data-test-id='name'] input").setValue("Анна Петрова");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("button.button").click();
        $(".notification__content")
                .should(Condition.visible, Duration.ofSeconds(15))
                .should(Condition.text( "Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    public void testFieldIsRequired() {
        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(generateDate(3));
        $("[data-test-id='name'] input").setValue("Анна Петрова");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .should(Condition.visible, Duration.ofSeconds(15))
                .should(Condition.text( "Поле обязательно для заполнения"));
    }

    @Test
    public void testOrderForThisDateIsNotPossible() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(generateDate(0));
        $("[data-test-id='name'] input").setValue("Анна Петрова");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .should(Condition.visible, Duration.ofSeconds(15))
                .should(Condition.text( "Заказ на выбранную дату невозможен"));
    }
    @Test
    public void testDateEnteredIncorrectly() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='name'] input").setValue("Анна Петрова");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .should(Condition.visible, Duration.ofSeconds(15))
                .should(Condition.text( "Неверно введена дата"));
    }

    @Test
    public void testNoFirstAndLastName() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(generateDate(3));
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .should(Condition.visible, Duration.ofSeconds(15))
                .should(Condition.text( "Поле обязательно для заполнения"));
    }

    @Test
    public void testNoNumber() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE)
                .setValue(generateDate(3));
        $("[data-test-id='name'] input").setValue("Анна Петрова");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .should(Condition.visible, Duration.ofSeconds(15))
                .should(Condition.text( "Поле обязательно для заполнения"));
    }
}
