package com.indra.actions;

import com.indra.pages.CambioPosPrePage;
import net.serenitybdd.core.Serenity;
import org.openqa.selenium.*;

import java.awt.*;

public class CambioPosPreActions extends CambioPosPrePage {
    public CambioPosPreActions(WebDriver driver) {
        super(driver);
    }

    public void takeScreenShot(int wait){
        waitABit(wait);
        getDriver().switchTo().defaultContent();
        Serenity.takeScreenshot();
        WebElement iframe = getDriver().findElement(By.id("iframe"));
        getDriver().switchTo().frame(iframe);
    }

    public void initialRute(){
        postSaleClick();
        transactionClick();
        lineModificationClick();
        ContractAssignmentClick();
    }

    public void executeContractAssignment(String phonenumber, String idClient) throws InterruptedException, AWTException {
        switchToIframe();
        writePhoneNumber(phonenumber);
        writeReasonForChange();
        writeVendorNumber();
        waitABit(2000);
        getMotivo().click();
        getMotivo1().click();
        getReasonCause().click();
        getReasonCause1().click();
        getDetail().click();
        getDetail1().click();
        getPlan().click();
        getPlanPre().click();

        takeScreenShot(10);

        waitABit(2000);
        getDriver().findElement(By.id("PlansChangePreForm:bntPlanChange")).click();
        waitABit(2000);
        alertAcept();
        waitABit(35000);
        getMensajes().waitUntilPresent();

        takeScreenShot(500);

        WebElement msgs = getDriver().findElement(By.xpath("//form[@id='PlanschangeForm']//table"));
        System.out.println(msgs.getText());

        System.out.println(getMensajes().getText());
    }

    public void postSaleClick(){
        getPostSale().click();
    }

    public void transactionClick(){
        getTransaction().click();
    }

    public void lineModificationClick(){
        getLineModification().click();
    }

    public void ContractAssignmentClick(){
        getContractAssignment().click();
    }

    public void writePhoneNumber(String phonumber){
        enter(phonumber).into(getPhoneNumber());
        getPhoneNumber().sendKeys(Keys.TAB);
    }

    public void switchToIframe(){
        WebElement iframe = getDriver().findElement(By.id("iframe"));
        getDriver().switchTo().frame(iframe);
    }

    public void writeVendorNumber(){
        enter("10960370").into(getVendedor());
    }


    public void writeReasonForChange(){
        enter("Prueba Cesion Automatizada QA").into(getReasonChange());
    }

    public void alertAcept(){
        Alert alert = getDriver().switchTo().alert();
        alert.accept();
    }

}
