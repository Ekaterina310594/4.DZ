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

}
