package com.indra.actions;

import com.indra.models.DataExcelModels;
import com.indra.pages.PortabilityPrepaidPage;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.List;

public class PortabilityPrepaidActions extends PortabilityPrepaidPage {
    public PortabilityPrepaidActions(WebDriver driver) {
        super(driver);
    }

    DataExcelModels dataExcelModels = new DataExcelModels();
    DatabasePortInActions databasePortInActions = new DatabasePortInActions();
    UninstallCBSServicesActions servicesActions = new UninstallCBSServicesActions();

    public void makePortabilityRequestAndDB(String msisdnPort, String msiPort) throws SQLException {
        validateLinesBd(msisdnPort,msiPort);
        consultSingleScreen(msisdnPort);
        validateTransctionBd(msisdnPort);

        switchToDefaultContent();
        initialRuteRequestPortability();
        makePortabilityRequest(msisdnPort);

        validateTransctionBd(msisdnPort);
        aceptNitBd(msisdnPort);
        validateTransctionBd(msisdnPort);

        MatcherAssert.assertThat("el status es PIN_REQUEST_ACEPTADO",
                validateTransctionBd(msisdnPort),Matchers.equalTo("PIN_REQUEST_ACEPTADO"));
       // consultNipBd();
    }


    public void initialRute(){
        switchToDefaultContent();
        clickSale();
        clickActivation();
        clickPagoAplazos();
        clickActivator();
        switchToIframe();
        getListaDesplegable().waitUntilVisible();
        clickListArrow();
        clickPortabilityPrepaid();
    }

    public void customerInformation(String vendedor,String cliente)  {
        enter(vendedor).into(getIdAsesor());
        getListaTipoIdentificacion().click();
        getTipoCC().click();
        enter(cliente).into(getNumIdentificacion());
        enter("2000").into(getAnioExpedicion());
        getBtncontinuar().click();
    }

    public void activationInformation(String msisdnPorting,String msisdn, String imsi) throws SQLException {

        String nip = msisdnPorting.substring(5,10);
        //consultNipBd();
         clickCurrentTelephony();
        clicktypeTelephony();
        // generacion aleatorio
        writeNIP(nip(nip));
        writeMsisdnToPorting(msisdnPorting);
        clickCheckScheduledDate();
        clickInputCalendar();
        selectNextBusinessDayFromCalendar();
        writeMsisdn(msisdn);
        writeImsi(imsi);
        clickTypeOfSale();
        clickTypeOfSaleOnlySim();
        windowsScrolldown();
        clickBtnContinue();

        //cambiar diad el calendario
        WebElement faildDayCalendar = getDriver().findElement(By.id("ActivacionesForm:idFechaActivacionPortabilidadMessage"));
        if(faildDayCalendar.isDisplayed()){
            clickInputCalendar();
            selectNextBusinessDayFromCalendarHoliday();
            windowsScrolldown();
            clickBtnContinue();
            getContinueTarife().waitUntilVisible();
            getContinueTarife().click();
            waitABit(2000);
        }

        getContinueTarife().waitUntilVisible();
        getContinueTarife().click();
        waitABit(2000);

    }

    public  void demographicInformation(){
        getPaymentDepar().click();
        getDeparment().click();
        getPaymentCity().click();
        getCity().click();
        //getValidate().click();
        enter("3222345678").into(getPhone());
        getContinueActivationDemo().click();
        waitABit(10000);
        WebElement confirm2 = getDriver().findElement(By.xpath("//input[contains(@id,'popupConfirmacionDatos:confirmarDatos')]"));
        confirm2.click();
        //getConfirm().click();

        getActivationDetails().waitUntilPresent();

        WebElement title = getDriver().findElement(By.className("tituloPagina"));
        MatcherAssert.assertThat("La activacion fue exitosa",title.getText(), Matchers.equalTo("ACTIVACION EXITOSA"));
    }

    public void clickSale(){
        getVenta().click();
    }

    public void clickActivation(){
        getActivacion().click();
    }

    public void clickPagoAplazos(){
        getPagoAplazos().click();
    }

    public void clickActivator(){
        getActivador().click();
    }

    public void switchToIframe(){
        WebElement iframe = getDriver().findElement(By.id("iframe"));
        getDriver().switchTo().frame(iframe);
    }

    public void switchToDefaultContent(){
        getDriver().switchTo().defaultContent();
    }

    public void clickListArrow(){
        getListaDesplegable().click();

    }

    public void clickPortabilityPrepaid(){
        getPortabilidadPrepago().click();

    }

    public void clickCurrentTelephony(){
        getListaDesplegableTipoTelefonia().click();

    }

    public void clicktypeTelephony(){
        getTipoPrepago().click();

    }

    public void writeNIP(String NIP){
        enter(NIP).into(getInputNip());
    }

    public void writeMsisdnToPorting(String msisdnPorting){
        enter(msisdnPorting).into(getInputMsisdnAportar());
    }

    public void writeMsisdn(String msisdn){
        enter(msisdn).into(getInputMsisdn());
    }

    public void writeImsi(String imsi){
        enter(imsi).into(getInputImsi());
    }

    public void clickTypeOfSale(){
        getListaDesplegableTipoVenta().click();
    }

    public void clickTypeOfSaleOnlySim(){
        getTipoSimSola().click();
    }

    public void clickCheckScheduledDate(){
        getCheckFechaCalendarizada().click();
    }

    public void clickInputCalendar(){
        getInputcalendario().click();
    }

    public void selectNextBusinessDayFromCalendar(){
        int currentDay=0;// se usa para indicar cuando llego al dia actual del calendario
        int click = 0;
        List<WebElement> dias = getDriver().findElements(By.xpath("//td[@onmouseover]")); // almacena todos los dias presentes del calendario
        // ciclo para recorrer la lista de dias del calendario
        for(WebElement dia :dias){
            //si el contador currentDay es igual 1 y es el dia seguiente le hace click (solo selecciona los dias habiles)
            //rf-cal-c-cnt-overflow rf-cal-c rf-cal-sel
            if(currentDay==1 && dia.getAttribute("class").equals("rf-cal-c-cnt-overflow rf-cal-c rf-cal-btn") && click == 0)
            {
                //System.out.println("selecciono este día "+ dia.getText());
                dia.click();
                break;
            }
            // si el dia del calendario es igual al dia presente hace el contador currentDay igual a 1.
            if(dia.getAttribute("class").equals("rf-cal-c-cnt-overflow rf-cal-c rf-cal-today rf-cal-btn")){
                currentDay=1;
            }
        }
    }

    public void selectNextBusinessDayFromCalendarHoliday(){
        int currentDay=0;// se usa para indicar cuando llego al dia actual del calendario

        List<WebElement> dias = getDriver().findElements(By.xpath("//td[@onmouseover]")); // almacena todos los dias presentes del calendario
        // ciclo para recorrer la lista de dias del calendario
        for(WebElement dia :dias){
            //si el contador currentDay es igual 1 y es el dia seguiente le hace click (solo selecciona los dias habiles)
            //rf-cal-c-cnt-overflow rf-cal-c rf-cal-sel
            if(currentDay==1 && dia.getAttribute("class").equals("rf-cal-c-cnt-overflow rf-cal-c rf-cal-btn"))
            {
                //System.out.println("selecciono este día "+ dia.getText());
                dia.click();
                break;
            }
            // si el dia del calendario es igual al dia presente hace el contador currentDay igual a 1.
            if(dia.getAttribute("class").equals("rf-cal-c-cnt-overflow rf-cal-c rf-cal-sel")){
                currentDay=1;
            }
        }
    }



    public void clickBtnContinue(){
        WebElement continuar = getDriver().findElement(By.name("ActivacionesForm:btnContinuarActivacionVenta"));
        continuar.click();
    }

    public void windowsScrolldown(){
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollBy(0,520)");
    }


    public void validateLinesBd(String msisdn,String msi) throws SQLException {
        databasePortInActions.cleanLinesMsisdn(msisdn);
        databasePortInActions.cleanLinesMsi(msi);
    }

    public String validateTransctionBd(String msisdn) throws SQLException {
        return databasePortInActions.executePortabilityTransactionStatus(msisdn);
    }

    public void aceptNitBd(String msisdn) throws SQLException {
        databasePortInActions.executePortabilityNip(msisdn);
    }

    public int consultNipBd(String nip) throws SQLException {
        String resultNip = databasePortInActions.executeSelectNip(nip);
        //System.out.println(databasePortInActions.executeSelectNip(nip).length());
        return resultNip.length();
    }

    public void executePortabilityReceptBd(String msisdn) throws SQLException {
        databasePortInActions.executePortabilityRecept(msisdn);
    }

    public void executeUpdatePortIdBd(String msisdn) throws SQLException {
        databasePortInActions.executeUpdatePortId(portId(msisdn), msisdn);
    }

    public void executePortIdBd(String msisdn) throws SQLException {
        databasePortInActions.executePortId(portId(msisdn));
    }

    public List<String> executePortabilityTransactionBd(String msisdn) throws SQLException {
        return databasePortInActions.executePortabilityTransaction(msisdn);
    }

    public void executeWindowPortabilityBd(String msisdn) throws SQLException {
        databasePortInActions.executeWindowPortability(msisdn);
    }

    public void consultSingleScreen(String msisdn){
        getDriver().switchTo().defaultContent();
        getConsult().click();
        getConsultPos().click();
        getConsultIntegral().click();
        getCosultaPantallaUnica().click();
        WebElement iframe = getDriver().findElement(By.id("iframe"));
        getDriver().switchTo().frame(iframe);
        enter(msisdn).into(getMsisdn2());
        getSearchButton().click();
        getGeneralCustomerInformation().waitUntilPresent();
        WebElement plan = getDriver().findElement(By.id("j_id15:j_id29"));
        String plantx = plan.getText().replace("ó","o");
        MatcherAssert.assertThat("la informacion",
                plantx, Matchers.containsString("No se encontr") );
    }

    public void initialRuteRequestPortability(){
        clickPresale();
        cLickNumberPortability();
        clickRequests();
        clickRequestsNip();
        switchToIframe();
    }
    public void makePortabilityRequest(String msisdn){
        getNumero().waitUntilVisible();
        writePhoneNumberForPortability(msisdn);
        clickBtnRequest();
        WebElement soliNip = getDriver().findElement(By.id("frmSlctdPin:j_id42"));
        //MatcherAssert.assertThat("solicitud nip exitosa",
        //        soliNip.getText(), Matchers.containsString("Las solicitudes se procesaron") );

    }

    public void clickPresale(){
        getPreventa().click();
    }

    public void cLickNumberPortability(){
        getPortabilidadNumerica().click();
    }


    public void clickRequests(){
        getSolicitudes().click();
    }

    public void clickRequestsNip(){
        getSolicitudDeNIP().click();
    }


    public void writePhoneNumberForPortability(String msisdn){
        enter(msisdn).into(getNumero());
    }

    public void clickBtnRequest(){
        getBtnSolicitar().click();
    }

    public void portabilityRequestSoapUI(String msisdn) throws SQLException {
        String response = servicesActions.portabilidad(executePortabilityTransactionBd(msisdn).get(0),
                executePortabilityTransactionBd(msisdn).get(1),
                executePortabilityTransactionBd(msisdn).get(2),
                executePortabilityTransactionBd(msisdn).get(3),
                executePortabilityTransactionBd(msisdn).get(4),
                dataExcelModels.getPortabilitySoapUI());


        //System.out.println("Response--->"+response);
        MatcherAssert.assertThat("la respuesta del servicio es O",
                servicesActions.extractResponseInformation(response,"return"),Matchers.equalTo("0"));
       }

    public void validateLineTemporal(String msisdn) throws SQLException {
        consultSingleScreen1(msisdn);
        validateTransctionBd(msisdn);
    }

    public void consultSingleScreen1(String msisdn){
        getDriver().switchTo().defaultContent();
        getConsult().click();
        getConsultPos().click();
        getConsultIntegral().click();
        getCosultaPantallaUnica().click();
        WebElement iframe = getDriver().findElement(By.id("iframe"));
        getDriver().switchTo().frame(iframe);
        enter(msisdn).into(getMsisdn2());
        getSearchButton().click();
        waitABit(1000);
        getGeneralCustomerInformation().waitUntilPresent();

        WebElement plan = getDriver().findElement(By.id("j_id135:j_id157"));
        MatcherAssert.assertThat("el plan es prepago",
                plan.getText(),Matchers.containsString("Plan Tigo Prepago") );

        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollBy(0,520)");

        getHlrImpre().click();
        waitABit(2000);
        getHlr().click();


        WebElement hrl = getDriver().findElement(By.xpath("//*[@id='j_id461:j_id465']"));
        MatcherAssert.assertThat("el hrl es ",
                hrl.getText(),Matchers.containsString("Operation is successful") );
    }

    public void preWindow(String msisdn) throws SQLException {
        executePortabilityReceptBd(msisdn);
        validateTransctionBd(msisdn);
        executePortIdBd(msisdn);
        executeUpdatePortIdBd(msisdn);
        executePortIdBd(msisdn);
    }

    public void validateLineTemporal1(String msisdn) throws SQLException {
        consultSingleScreen2(msisdn);
        validateTransctionBd(msisdn);
    }

    public void consultSingleScreen2(String msisdn){
        getDriver().switchTo().defaultContent();
        getConsult().click();
        getConsultPos().click();
        getConsultIntegral().click();
        getCosultaPantallaUnica().click();
        WebElement iframe = getDriver().findElement(By.id("iframe"));
        getDriver().switchTo().frame(iframe);
        //enter("3016875893").into(getMsisdn2());
        enter(msisdn).into(getMsisdn2());
        getSearchButton().click();
        waitABit(1000);
        getGeneralCustomerInformation().waitUntilPresent();
        WebElement plan = getDriver().findElement(By.id("j_id135:j_id157"));

        MatcherAssert.assertThat("el plan es pospago",
                plan.getText(),Matchers.containsString("Pospago 5.") );

        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollBy(0,420)"); //Scroll vertically down by 1000 pixels
        getHlr().click();
        WebElement hrl = getDriver().findElement(By.id("j_id473:j_id477"));
        MatcherAssert.assertThat("el hrl es ",
                plan.getText(),Matchers.containsString("Operation is successful") );
    }

    public String portId(String msisdn){
        String nip = msisdn.substring(5,10);
        // logica si esta sumar 1 al nip
        // nip = String.valueOf(Integer.valueOf(nip) + 1);
        // repertir consulta
        String portId= "000022011082401"+nip;
        return portId;
    }

    public void window(String msisdn) throws SQLException {
        executeWindowPortabilityBd(msisdn);
        portabilityRequestSoapUI(msisdn);
        executeWindowPortabilityBd(msisdn);
    }

    public String nip(String nip) throws SQLException {

        while(consultNipBd(nip) > 0){
            nip = String.valueOf(Integer.valueOf(nip) + 1);
            consultNipBd(nip);
        }
        return nip;
    }

}

