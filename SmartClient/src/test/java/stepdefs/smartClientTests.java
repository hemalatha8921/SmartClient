package stepdefs;

import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;


public class smartClientTests extends commonMethods {

    @Test(priority = 0)
    public static void smartClient_Tile_Filtering() throws Exception {
        webDriver.get(Prop.getProperty("tileFilteringURL"));
        waitForPageToLoad();
        commonMethods.clickElement(OrProp.getProperty("btn_AcceptCookies"));
        commonMethods.clickElement(OrProp.getProperty("txt_Searchbox"));
        commonMethods.sendText(OrProp.getProperty("txt_Searchbox"), "a");
        WebElement ele_Scroll = commonMethods.getWebElement(OrProp.getProperty("bar_Scroller"));
        Actions action = new Actions(webDriver);
        action.dragAndDropBy(ele_Scroll, 24, 0).perform();
        commonMethods.clickElement(OrProp.getProperty("chkbx_Ascending"));
        commonMethods.clickElement(OrProp.getProperty("pl_Frame"));
        commonMethods.clickElement(OrProp.getProperty("pl_Value"));
        List<WebElement> list_Animal_Images = commonMethods.getWebElements(OrProp.getProperty("list_Animal_Images"));
        if (list_Animal_Images.size() > 12) {
            Assert.assertTrue(true, "Images are morethan 12");
        } else {
            Assert.assertTrue(false, "Images are lessthan 12");
        }
    }

    @Test(priority = 1)
    public static void smartClient_Dropdown_Grid_Category() throws Exception {
        String expectedItem = "Exercise";
        String expectedUnit = "Ea";
        double expectedMinCost = 1.1;

//     Navigate to URL
        webDriver.get(Prop.getProperty("dropdownGridURL"));
        waitForPageToLoad();
//     Expand Items dropdown
        commonMethods.clickElement(OrProp.getProperty("list_Item"));
        List<WebElement> currentVisibleOptions = getDropDownElements();
        int currPosition = 0;
//        Iterate to find expected Item
        while (currPosition < currentVisibleOptions.size()) {
            WebElement currentRow = currentVisibleOptions.get(currPosition);
            if (!currentRow.isDisplayed()) {
//                 Scroll to option(hidden option) and update current visible item list and position.
                scrollToElement(currentRow);
                currentVisibleOptions = getDropDownElements();
                currPosition = 0;
            }

            List<WebElement> itemValues = commonMethods.getWebElements(OrProp.getProperty("list_Values") + "[" + (currPosition + 1) + "]//td//div");
            String itemName = itemValues.get(0).getText();
            String itemUnit = itemValues.get(1).getText();
            String itemCost = itemValues.get(2).getText();

            if (itemName.toLowerCase().contains(expectedItem.toLowerCase()) &&
                    itemUnit.toLowerCase().equals(expectedUnit.toLowerCase()) &&
                    Double.valueOf(itemCost) >= expectedMinCost) {
//                Click on expected Item
                currentRow.click();
                break;
            }
            currPosition++;
        }
    }

    @Test(priority = 2)
    public static void smartClient_Nested_Grid() throws Exception {
        webDriver.get(Prop.getProperty("nestedGridURL"));
        commonMethods.clickElement(OrProp.getProperty("btn_AcceptCookies"));
        String expectedGridItemName = "Correction";
        Integer totalMatches = 0;
        List<WebElement> currentVisibleGridElements = getNestedGridElements();
        int currPosition = 0;
        waitForPageToLoad();
        while (currPosition < currentVisibleGridElements.size()) {
            WebElement currentRow = currentVisibleGridElements.get(currPosition);
            if (!currentRow.isDisplayed()) {
//                 Scroll to option(hidden option) and update current visible item list and position.
                String itemName =currentRow.findElements(By.xpath(".//td//div")).get(1).getAttribute("innerText");
                scrollToElement(currentVisibleGridElements.get(currPosition-1));
                currentVisibleGridElements = getNestedGridElements();
                currPosition = currentItemIndex(currentVisibleGridElements, itemName);
            }
            waitForPageToLoad();
            List<WebElement> itemValues = currentRow.findElements(By.xpath(".//td//div"));
            String itemName = itemValues.get(1).getText();
            if (itemName.toLowerCase().contains(expectedGridItemName.toLowerCase())) {
                itemValues.get(0).click();
                WebElement currentElement = webDriver.findElement(By.xpath("//div[@class='gridBody']//table//tr//div[contains(text(),'"+itemName+"')]"));
                scrollToElement(currentElement);
                currentVisibleGridElements.get(currPosition);
                if (webDriver.findElements(By.xpath("//table//td[contains(text(),'No items to show.')]")).size() > 0) {
//                    Close as there are no entries
                    WebElement closeButton = webDriver.findElement(By.xpath("//table//td[@class='button']//div[contains(text(),'Close')]"));
                    scrollToElement(closeButton);
                    closeButton.click();
                } else {
                    List<WebElement> descriptionElements = webDriver.findElements(By.xpath("//table[@class='listTable']//tr[@role='listitem']//td[3]//div"));
                    int i = 0;
                    for(; i<descriptionElements.size(); i++) {
                        descriptionElements.get(i).click();
                        totalMatches+=1;
                        String description = "" + totalMatches + " " + RandomStringUtils.randomAlphabetic(10);
                        webDriver.switchTo().activeElement().sendKeys(description);
                        System.out.println("itemName" + itemName + "Text entered:" + description);
                        WebElement saveButton = webDriver.findElement(By.xpath("//table//td[@class='button']//div[contains(text(),'Save')]"));
                        saveButton.click();
                        descriptionElements = webDriver.findElements(By.xpath("//table[@class='listTable']//tr[@role='listitem']//td[3]//div"));
                    }
                    WebElement closeButton = webDriver.findElement(By.xpath("//table//td[@class='button']//div[contains(text(),'Close')]"));
                    scrollToElement(closeButton);
                    closeButton.click();
                }
                waitForPageToLoad();
                scrollToElement(webDriver.findElement(By.xpath("//div[@class='gridBody']//table//tr//div[contains(text(),'"+itemName+"')]")));
                currentVisibleGridElements = getNestedGridElements();
            }
            scrollToElement(webDriver.findElement(By.xpath("//div[@class='gridBody']//table//tr//div[contains(text(),'"+itemName+"')]")));
            currentVisibleGridElements = getNestedGridElements();
            currPosition++;
        }
    }

    public static Integer currentItemIndex(List<WebElement> gridItems, String expectedGridName) throws Exception {
        for(int i =0; i<gridItems.size(); i++) {
            String gridName = gridItems.get(i).findElements(By.xpath(".//td//div")).get(1).getAttribute("innerText");
            if (gridName.contains(expectedGridName)) {
                return  i;
            }
        }
        throw new Exception("Grid with named" + expectedGridName + " not found");
    }
    public static String gridItemName(Integer currPosition) throws Exception {
        return commonMethods.getWebElements(OrProp.getProperty("grid_items") + "[" + (currPosition + 1) + "]//td//div").get(1).getAttribute("innerText");
    }

    public static List<WebElement> getNestedGridElements() throws Exception {
        waitForPageToLoad();
        List<WebElement> elements = commonMethods.getWebElements(OrProp.getProperty("grid_items"));
        if (elements.size() == 0) {
            Thread.sleep(4);
            elements = commonMethods.getWebElements(OrProp.getProperty("grid_items"));
        }
        return elements;
    }

    public static List<WebElement> getDropDownElements() throws Exception {
        waitForPageToLoad();
        List<WebElement> elements = commonMethods.getWebElements(OrProp.getProperty("list_Values"));
        if (elements.size() == 0) {
            Thread.sleep(4);
            elements = commonMethods.getWebElements(OrProp.getProperty("list_Values"));
        }
        return elements;
    }

}
