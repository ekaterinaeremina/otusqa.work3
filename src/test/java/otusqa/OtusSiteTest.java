package otusqa;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.aeonbits.owner.ConfigFactory;

import java.util.Iterator;
import java.util.Set;

//@Listeners(ExecutionListener.class)
public class OtusSiteTest extends BaseTest{

    private static final Logger log = Logger.getLogger(OtusSiteTest.class);
    private static MyConfig config = ConfigFactory.create(MyConfig.class);


    @Test (description = "")
    public void verifyMainPageURLTest()
    {
        log.info("Run verifyMainPageURLTest");
        String otusUrl = config.url();
        driver.get(otusUrl);
        log.info("Go to " + otusUrl);
        Assert.assertEquals(driver.getCurrentUrl(), config.url());
        log.info("Passed verifyMainPageURLTest!");
    }

    @Test
    public void fieldAboutMyself(){
        WebDriverWait wait = new WebDriverWait(driver,10);
        String otusUrl = config.url();
        driver.get(otusUrl);
        log.info("Go to " + otusUrl);
        LogIn(wait);
        driver.get("https://otus.ru/lk/biography/personal/");
        log.info("Go to LK");

        By locInputName = By.xpath("(//input[@id='id_fname'])");
        inputTextToElementWithWaitVisibility(wait, locInputName,config.newName());
        log.info("Input Name");
        By locDateOfBirth = By.xpath("(//input[@name='date_of_birth'])");
        inputTextToElementWithWaitVisibility(wait, locDateOfBirth,config.newBirthDay());
        log.info("Input date of birth");
        driver.findElement(By.xpath("(//button[contains(text(), 'Сохранить и продолжить')])")).click();
        log.info("Save change");

        Actions action = new Actions(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//p[@class='header2-menu__item-text header2-menu__item-text__username'])")));
        WebElement elem = driver.findElement(By.xpath("(//p[@class='header2-menu__item-text header2-menu__item-text__username'])"));
        action.moveToElement(elem);
        action.perform();

        By locExit = By.xpath("(//a[@title='Выход'])");
        findElementWithWaitVisibility(wait, locExit).click();
        log.info("LogOut");

        LogIn(wait);
        driver.get("https://otus.ru/lk/biography/personal/");
        log.info("Go to LK");

        WebElement fieldName = findElementWithWaitVisibility(wait, locInputName);
        String name = fieldName.getAttribute("value");
        log.info("Get FirstName: " + name);
        Assert.assertEquals(name, config.newName());
        log.info("FirstName is Correct");

        WebElement fieldDateOfBirthday = findElementWithWaitVisibility(wait, locDateOfBirth);
        String birthDay = fieldDateOfBirthday.getAttribute("value");
        log.info("Get BirthDay: "+ birthDay);
        Assert.assertEquals(birthDay, config.newBirthDay());
        log.info("BirthDay is Correct");
    }

    private void LogIn(WebDriverWait wait)
    {
        By locEnterOrRegistration = By.cssSelector("button[data-modal-id]");
        findElementWithWaitVisibility(wait, locEnterOrRegistration).click();
        log.info("Click to 'Вход и регистрация'");
        By locEmail = By.xpath("(//input[@type=\"text\"])[1]");
        inputTextToElementWithWaitVisibility(wait, locEmail, "ykyeremi@mts.ru");
        log.info("Input email'");
        By locPassword = By.xpath("(//input[@type=\"password\"])");
        inputTextToElementWithWaitVisibility(wait, locPassword, "");
        log.info("Input password'");
        By locEnter = By.xpath("(//button[contains(text(), 'Войти')])[1]");
        findElementWithWaitVisibility(wait, locEnter).submit();
        log.info("Click to 'Войти'");
        wait.until(CookiesContainsAuthTokenExpires(driver));
        log.info("LogIn");
    }

    public static ExpectedCondition<Boolean> CookiesContainsAuthTokenExpires(final WebDriver driver) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                Set<Cookie> cookies = driver.manage().getCookies();
                for (Iterator<Cookie> it = cookies.iterator(); it.hasNext(); ) {
                    Cookie c = it.next();
                    if (c.getName().equals("auth_token_expires")) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
}