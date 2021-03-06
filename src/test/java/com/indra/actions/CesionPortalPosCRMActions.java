package com.indra.actions;

import com.indra.models.DataExcelModels;
import com.indra.pages.CesionPortalCRMPage;
import com.jcraft.jsch.JSchException;
import net.serenitybdd.core.Serenity;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.awt.*;

public class CesionPortalPosCRMActions extends CesionPortalCRMPage {
    public CesionPortalPosCRMActions(WebDriver driver) {
        super(driver);
    }

    SshConnetions sshConnetions = new SshConnetions();
    DataExcelModels excelModels = new DataExcelModels();
    ReadFileCSV readFileCSV = new ReadFileCSV();

    public void takeScreenShot(int wait){
        waitABit(wait);
        getDriver().switchTo().defaultContent();
        Serenity.takeScreenshot();
        switchToIframe();
    }

    public void initialRute(){
        postSaleClick();
        transactionClick();
        lineModificationClick();
        ContractAssignmentClick();
    }

    public void executeContractAssignment(String phonenumber, String idClient,String planNumber) throws InterruptedException, AWTException, JSchException {
        switchToIframe();
        waitABit(2000);
        writePhoneNumber(phonenumber);
        waitABit(1000);

        getClaveAsesor().waitUntilEnabled();
        getClaveAsesor().waitUntilClickable();
        getClaveAsesor().click();

        getVendedor().waitUntilPresent();
        selectAnnualRenewal();
        writeNewClientNumber(idClient);

        takeScreenShot(10);

        adviserKeyGeneration();
        waitABit(1000);
        writeAdviserKey();

        consultClick();
        getEmail().waitUntilPresent();
        writeVendorNumber();
        writeReasonForChange();
        writeObsevations();

        takeScreenShot(10);

        selectBillingDepartment();
        selectBillingCity();
        addressBillingClick();
        selectVia();
        writeAddress();
        writeAddress2();
        writeWithAddress();
        bntAceptClick();
        writeEmail();
        selectPlan(planNumber);

        takeScreenShot(10);

        btnChangeContractClick();
        waitABit(1000);
        alertAcept();
        waitABit(50000);
        getMensajes().waitUntilPresent();

        takeScreenShot(10);

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

    public void selectAnnualRenewal(){
        Select dropDownAnnualRenewal= new Select(getDriver().findElement(By.id("cesionContratoForm:InfoAnnualRenewal:planField")));
        dropDownAnnualRenewal.selectByVisibleText("SI");
    }

    public void switchToIframe(){
        WebElement iframe = getDriver().findElement(By.id("iframe"));
        getDriver().switchTo().frame(iframe);
    }

    public void writeVendorNumber(){
        enter("10960370").into(getVendedor());
    }

    public void writeNewClientNumber(String idClient){
        enter(idClient).into(getNewSudId());
    }

    public void consultClick(){
        getBtnConsultar().click();
    }

    public void writeReasonForChange(){
        enter("Prueba Cesion Automatizada QA").into(getMotivo());
    }

    public void writeObsevations(){
        enter("Prueba Cesion Automatizada QA").into(getObservation());
    }

    public void selectBillingDepartment(){
        Select dropDownBillingDepartment= new Select(getDriver().findElement(By.xpath("//select[@name='cesionContratoForm:deptoField:j_id202']")));
        dropDownBillingDepartment.selectByValue("6");
    }

    public void selectBillingCity(){
        waitABit(1000);
        Select dropDownBillingDepartment= new Select(getDriver().findElement(By.xpath("//select[@name='cesionContratoForm:ciudadField:j_id216']")));
        dropDownBillingDepartment.selectByValue("1241");
    }

    public void addressBillingClick(){
        getDireccionFacturacion().click();
    }

    public void selectVia(){
        Select dropDownVia= new Select(getDriver().findElement(By.xpath("//div[@class='tigo_address_block_locate']/select")));
        dropDownVia.selectByValue("Avenida");
    }

    public void writeAddress(){

        enter("45").into(getCallenumero());
    }

    public void writeAddress2(){
        enter("16").into(getCallenumero2());
    }

    public void writeWithAddress(){
        enter("36").into(getWithAddress());
    }

    public void bntAceptClick(){
        getBtnAceptar().click();
    }

    public void writeEmail(){
        enter("pruebaAutomatizadaQA@gmail.com").into(getEmail());
    }

    public void selectPlan(String planNumber){
        Select dropDownPlan= new Select(getDriver().findElement(By.xpath("/html/body/div[1]/form/table/tbody/tr/td[2]/table[3]/tbody/tr[2]/td/table/tbody/tr/td/select")));
        dropDownPlan.selectByValue(planNumber);
    }

    public void btnChangeContractClick(){
        waitABit(1000);
        Actions actions = new Actions(getDriver());

        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollBy(0,420)"); //Scroll vertically down by 1000 pixels

        WebElement inventoryAllocation = getDriver().findElement(By.xpath("//*[@id='cesionContratoForm:bntPlanChange']"));
        actions.moveToElement(inventoryAllocation).click().build().perform();
    }

    public void alertAcept(){
        Alert alert = getDriver().switchTo().alert();
        alert.accept();
    }

    public void writeAdviserKey(){
        enter(readFileCSV.getToken()).into(getCajonClaveAsesor());
        getCajonClaveAsesor().sendKeys(Keys.TAB);
        waitABit(1000);
    }

    public void adviserKeyGeneration() throws JSchException {
        sshConnetions.connectionSSH(excelModels.getHostSSH(),excelModels.getUserSSh(),excelModels.getPasswordSSH());
    }

}
