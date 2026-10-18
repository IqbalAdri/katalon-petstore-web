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

/*
 * TC08 - Checkout tanpa login [NEGATIVE]
 * Variables : categoryId, productId, itemId (default value di tab Variables)
 */

'Precondition: buka halaman utama tanpa login dan tambahkan item ke cart'
CustomKeywords.'petstore.CommonKeywords.openHomePage'()

WebUI.verifyElementPresent(findTestObject('Page_Header/link_SignIn'), GlobalVariable.timeout)

CustomKeywords.'petstore.CartKeywords.addItemToCart'(categoryId, productId, itemId)

'Step 1: klik Proceed to Checkout'
WebUI.click(findTestObject('Page_Cart/btn_ProceedToCheckout'))

'Expected: checkout ditolak dengan pesan wajib login dan user diarahkan ke halaman Sign In'
WebUI.verifyElementVisible(findTestObject('Page_Header/txt_Message'))

WebUI.verifyMatch(WebUI.getText(findTestObject('Page_Header/txt_Message')), '^You must sign on before attempting to check out.*',
    true)

WebUI.verifyElementPresent(findTestObject('Page_Login/btn_Login'), GlobalVariable.timeout)

WebUI.verifyElementNotPresent(findTestObject('Page_Checkout/btn_Continue'), GlobalVariable.timeout)

WebUI.closeBrowser()
