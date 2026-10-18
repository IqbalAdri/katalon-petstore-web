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

'Precondition: buka halaman utama JPetStore'
CustomKeywords.'petstore.CommonKeywords.openHomePage'()

'Step 1: klik menu Sign In'
WebUI.click(findTestObject('Page_Header/link_SignIn'))

WebUI.verifyElementVisible(findTestObject('Page_Login/btn_Login'))

'Step 2: input username & password dari Global Variable'
WebUI.setText(findTestObject('Page_Login/input_Username'), GlobalVariable.username)

WebUI.setText(findTestObject('Page_Login/input_Password'), GlobalVariable.password)

'Step 3: klik tombol Login'
WebUI.click(findTestObject('Page_Login/btn_Login'))

'Expected: login berhasil - menu Sign Out tampil, menu Sign In hilang, muncul teks Welcome'
WebUI.verifyElementPresent(findTestObject('Page_Header/link_SignOut'), GlobalVariable.timeout)

WebUI.verifyElementNotPresent(findTestObject('Page_Header/link_SignIn'), GlobalVariable.timeout)

String welcomeText = WebUI.getText(findTestObject('Page_Home/txt_WelcomeContent'))

WebUI.verifyMatch(welcomeText, '^Welcome .+!$', true)

'Step 4: logout dan pastikan session berakhir'
CustomKeywords.'petstore.CommonKeywords.logout'()

WebUI.verifyElementNotPresent(findTestObject('Page_Header/link_SignOut'), GlobalVariable.timeout)

WebUI.closeBrowser()
