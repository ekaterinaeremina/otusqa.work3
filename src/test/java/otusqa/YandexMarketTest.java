package otusqa;

import org.aeonbits.owner.ConfigFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

public class YandexMarketTest extends BaseTest{

    private static final Logger log = Logger.getLogger(YandexMarketTest.class);
    private static MyConfig config = ConfigFactory.create(MyConfig.class);

    @Test
    public void CompareOSOfXiaomiAndRedmi() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        String ya = "https://yandex.ru";
        driver.get(ya);
        log.info("Go to " + ya);

        By locYaMarket = By.xpath("(//a[@data-id='market'])");
        findElementWithWaitVisibility(wait, locYaMarket).click();
        log.info("Go to yandex Market");

        By locAllCategories = By.xpath("(//div[@class='PXL2nleaah'])");
        findElementWithWaitVisibility(wait, locAllCategories).click();
        log.info("Go to Все катеории");

        By locElectronic = By.linkText("Электроника");
        findElementWithWaitVisibility(wait, locElectronic).click();
        log.info("Go to Электроника");

        By locShowAll = By.xpath("(//span[@class='_2qvOOvezty _2x2zBaVN-3 _9qbcyI_fyS gmQcKpnhWV'])");
        findElementWithWaitVisibility(wait, locShowAll).click();
        log.info("Click to Показать все");

        By locMobilePhone = By.linkText("Мобильные телефоны");
        findElementWithWaitVisibility(wait,locMobilePhone).click();
        log.info("Go to Мобильные телефоны");

        By locBrand = By.xpath("(//fieldset[@data-autotest-id='7893318'])");
        WebElement brand = findElementWithWaitVisibility(wait, locBrand);
        log.info("Find brands filter");

        By locShowAllBrands = By.xpath("(//button[@class='i-a11y-reset-button _2Wg9rE1HzR'])");
        findElementWithWaitVisibility(wait, locShowAllBrands).click();
        log.info("Click show all brands");

        By locInputBrand = By.xpath("(//input[@class='_1JYTt02WxW'])");
        inputTextToElementWithWaitVisibility(brand, wait,locInputBrand,"Xiaomi");
        log.info("Input xiaomi to brand flter");

        By locXiaomi = By.linkText("Xiaomi");
        findElementWithWaitVisibility(brand, wait,locXiaomi).click();
        log.info("Select Xiaomi brand");

        By locSortByPrice = By.linkText("по цене");
        findElementWithWaitVisibility(wait, locSortByPrice).click();
        log.info("Sort by price");

        // Переделать
        //By locProductsGrid = By.xpath("(//div[@class='n-filter-applied-results__content preloadable i-bem preloadable_js_inited'])");
        /*wait.until(SortEnd(wait, locProductsGrid));
        log.info("Sort completed");*/

        By locLoad = By.xpath("(//div[contains(@class, 'n-snippet-cell2 i-bem b-spy-events b-zone b-spy-visible shop-history')])");
        wait.until(ExpectedConditions.visibilityOfElementLocated(locLoad));

        By locProducts = By.xpath("(//div[@class='n-filter-applied-results__content preloadable i-bem preloadable_js_inited']/div[1]/div)");
        List<WebElement> productsAtOnce = driver.findElements(locProducts);
        int nAll = productsAtOnce.size(); // количество смартфонов отображающееся за раз
        log.info("Show "+ nAll + " phones at once");


        WebElement firstXiaomi=null; // Первый в списке смартфон Xiaomi не Redmi
        WebElement firstRedmi=null; // Первый в списке смартфон Redmi
        String title = ""; // Название смартфона
        int nPhone = 1; // итератор по смартфонам
        int nShowYet = 1; // итератор по блокам "Показать еще"

        // Поиск первого сматрфона не Redmi
        while (!title.contains("Смартфон") || title.contains("Redmi")) {
            // Если не найдено на первой странице, жмем "Показать еще"
            if (nPhone>nAll)
            {
                By locShowYet = By.xpath("(//a[contains(@class, 'button button_size_m button_theme_pseudo i-bem button_js_inited')])");
                findElementWithWaitVisibility(wait, locShowYet).click();
                nShowYet++;
                nPhone=1;
            }
            By locCurrentPhone = By.xpath("(//div[@class='n-filter-applied-results__content preloadable i-bem preloadable_js_inited']/div["+nShowYet+"]/div["+nPhone+"])");
            firstXiaomi = findElementWithWaitVisibility(wait,locCurrentPhone);
            List<WebElement> tagsA = firstXiaomi.findElements(By.tagName("a"));
            for (WebElement el : tagsA) {
                if (el.getAttribute("title").contains("Смартфон") )
                    title = el.getAttribute("title");
            }
            nPhone++;
        }
        log.info("First Xiaomi: "+ title);
        int NXiaomi = nPhone-1;
        int NShowYetXiaomi = nShowYet;
        nPhone = 1;
        nShowYet = 1;

        // Поиск первого сматрфона Redmi
        while (!title.contains("Смартфон") || !title.contains("Redmi")) {
            // Если не найдено на первой странице, жмем "Показать еще"
            if (nPhone>nAll)
            {
                By locShowYet = By.xpath("(//a[contains(@class, 'button button_size_m button_theme_pseudo i-bem button_js_inited')])");
                findElementWithWaitVisibility(wait, locShowYet).click();
                nShowYet++;
                nPhone=1;
            }
            By locCurrentPhone = By.xpath("(//div[@class='n-filter-applied-results__content preloadable i-bem preloadable_js_inited']/div["+nShowYet+"]/div["+nPhone+"])");
            firstRedmi = findElementWithWaitVisibility(wait,locCurrentPhone);
            List<WebElement> tagsA = firstRedmi.findElements(By.tagName("a"));
            for (WebElement el : tagsA) {
                if (el.getAttribute("title").contains("Смартфон") )
                    title = el.getAttribute("title");
            }
            nPhone++;
        }
        int NRedmi = nPhone-1;
        int NShowYetRedmi = nShowYet;
        log.info("Fisrt Redmi: "+ title);

        By locAddToCompareRedmi = By.xpath("(//div[@class='n-filter-applied-results__content preloadable i-bem preloadable_js_inited']/div["+NShowYetRedmi+"]/div["+NRedmi+"]/div[1]/div[1]/div[1]/div[1]/i[1])");
        addElementToCompare(firstRedmi, wait, locAddToCompareRedmi);
        log.info("Add redmi to compare");

        By locPopUpInformer = By.xpath("(//div[@class='popup-informer__content'])");
        By locPopUpInformerClose = By.xpath("(//div[@class='popup-informer__content']/div[4])");
        findElementWithWaitVisibility(wait, locPopUpInformer,locPopUpInformerClose).click();
        log.info("Close popup informer");

        By locAddToCompareXiaomi = By.xpath("(//div[@class='n-filter-applied-results__content preloadable i-bem preloadable_js_inited']/div["+NShowYetXiaomi+"]/div["+NXiaomi+"]/div[1]/div[1]/div[1]/div[1]/i[1])");
        addElementToCompare(firstXiaomi, wait, locAddToCompareXiaomi);
        log.info("Add xiaomi to compare");

        findElementWithWaitVisibility(wait, locPopUpInformer,locPopUpInformerClose).click();
        log.info("Close popup informer");

        By locCompare = By.xpath("(//a[@href='/compare?track=head'])");
        findElementWithWaitVisibility(wait, locCompare).click();
        log.info("Go to compare");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='n-compare-content__line i-bem n-compare-content__line_js_inited'])")));
        List<WebElement> elementsToCompare = driver.findElements(By.xpath("//div[@class='n-compare-content__line i-bem n-compare-content__line_js_inited']/div"));
        int countElementsToCompare = elementsToCompare.size();
        log.info("Count of elements to compare = " + countElementsToCompare);
        Assert.assertEquals(countElementsToCompare, 2, "В списке товаров не 2 позиции");

        driver.findElement(By.xpath("(//span[contains(text(), 'все характеристики')])")).click();
        log.info("Click to Все характеристики");

        By locBlockCompare = By.xpath("(//div[@class='n-compare-table i-bem n-compare-table_js_inited'])");
        By locBlockParams = By.xpath("(//div[@class='n-compare-table i-bem n-compare-table_js_inited']/div[6])");
        WebElement params = findElementWithWaitVisibility(wait, locBlockCompare, locBlockParams);

        List<WebElement> os = params.findElements(By.xpath("(//div[contains(text(), 'Операционная система')])"));
        boolean OSisExist = os.get(0).isDisplayed();
        log.info("OS visible is " + OSisExist);
        Assert.assertEquals(OSisExist, true, "В списке характеристик не появилась позиция \"Операционная система\"");

        driver.findElement(By.xpath("(//span[contains(text(), 'различающиеся характеристики')])")).click();
        log.info("Click to различающиеся характеристики");

        params = findElementWithWaitVisibility(wait, locBlockCompare, locBlockParams);

        os = params.findElements(By.xpath("(//div[contains(text(), 'Операционная система')])"));

        OSisExist = os.get(0).isDisplayed();
        log.info("OS visible is " + OSisExist);
        Assert.assertEquals(OSisExist, false, "В списке характеристик появилась позиция \"Операционная система\"");
    }

    private void addElementToCompare(WebElement element, WebDriverWait wait, By by)
    {
        Actions action = new Actions(driver);
        action.moveToElement(element);
        action.perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        element.findElement(by).click();
    }

private ExpectedCondition<Boolean> SortEnd(final WebDriverWait wait, final By by) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                WebElement elem = findElementWithWaitVisibility(wait,by);
                String style = elem.getAttribute("style");
                if (style.equals("height: auto;"))
                    return true;
                else
                    return false;
            }
        };
    }
}
