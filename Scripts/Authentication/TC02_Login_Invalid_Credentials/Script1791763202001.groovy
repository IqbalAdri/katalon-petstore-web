import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

CustomKeywords.'petstore.CommonKeywords.logScenario'('TC02', scenario)

'Siapkan data login: ganti token {GLOBAL_USERNAME}/{GLOBAL_PASSWORD} dengan Global Variable dan {EMPTY} dengan string kosong'
String inputUsername = CustomKeywords.'petstore.CommonKeywords.resolveDataToken'(username)

String inputPassword = CustomKeywords.'petstore.CommonKeywords.resolveDataToken'(password)

String message = CustomKeywords.'petstore.CommonKeywords.resolveDataToken'(expectedMessage)

'Precondition: buka halaman utama JPetStore'
CustomKeywords.'petstore.CommonKeywords.openHomePage'()

'Step 1: klik menu Sign In'
WebUI.click(findTestObject('Page_Header/link_SignIn'))

'Step 2: input username & password tidak valid'
WebUI.setText(findTestObject('Page_Login/input_Username'), inputUsername)

WebUI.setText(findTestObject('Page_Login/input_Password'), inputPassword)

'Step 3: klik tombol Login'
WebUI.click(findTestObject('Page_Login/btn_Login'))

'Expected: login ditolak - user tetap di halaman Sign In dan tidak ada menu Sign Out'
WebUI.verifyElementPresent(findTestObject('Page_Login/btn_Login'), GlobalVariable.timeout)

WebUI.verifyElementNotPresent(findTestObject('Page_Header/link_SignOut'), GlobalVariable.timeout)

WebUI.verifyElementPresent(findTestObject('Page_Header/link_SignIn'), GlobalVariable.timeout)

'Expected: pesan error sesuai data (jika diisi)'
if (message.trim() != '') {
    WebUI.verifyElementVisible(findTestObject('Page_Header/txt_Message'))

    String actualMessage = WebUI.getText(findTestObject('Page_Header/txt_Message'))

    WebUI.verifyMatch(actualMessage, ('.*' + java.util.regex.Pattern.quote(message.trim())) + '.*', true)
}

WebUI.closeBrowser()
