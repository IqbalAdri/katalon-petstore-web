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
 * TC09 - Checkout order sampai selesai [POSITIVE][E2E]
 * Credential dari Global Variable, item dari Variables (categoryId, productId, itemId).
 * Data pembayaran memakai nilai default (fake) yang sudah diisi oleh aplikasi demo.
 */

'Precondition: buka halaman utama dan login dengan credential dari Global Variable'
CustomKeywords.'petstore.CommonKeywords.openHomePage'()

CustomKeywords.'petstore.CommonKeywords.login'(GlobalVariable.username, GlobalVariable.password)

CustomKeywords.'petstore.CommonKeywords.verifyLoggedIn'('')

'Step 1: tambahkan item ke cart'
CustomKeywords.'petstore.CartKeywords.addItemToCart'(categoryId, productId, itemId)

'Step 2: klik Proceed to Checkout'
WebUI.click(findTestObject('Page_Cart/btn_ProceedToCheckout'))

'Expected: form Payment Details & Billing Address tampil'
WebUI.verifyElementPresent(findTestObject('Page_Checkout/select_CardType'), GlobalVariable.timeout)

WebUI.verifyElementPresent(findTestObject('Page_Checkout/input_CardNumber'), GlobalVariable.timeout)

WebUI.verifyElementPresent(findTestObject('Page_Checkout/input_ExpiryDate'), GlobalVariable.timeout)

'Step 3: klik Continue'
WebUI.click(findTestObject('Page_Checkout/btn_Continue'))

'Step 4: klik Confirm pada halaman konfirmasi order'
WebUI.verifyElementPresent(findTestObject('Page_Checkout/btn_Confirm'), GlobalVariable.timeout)

WebUI.click(findTestObject('Page_Checkout/btn_Confirm'))

'Expected: order berhasil dibuat dan item tercantum di detail order'
WebUI.verifyMatch(WebUI.getText(findTestObject('Page_Header/txt_Message')), '^Thank you, your order has been submitted.*',
    true)

WebUI.verifyElementPresent(findTestObject('Page_Checkout/link_OrderItemId', [('itemId') : itemId]), GlobalVariable.timeout)

'Step 5: logout'
CustomKeywords.'petstore.CommonKeywords.logout'()

WebUI.closeBrowser()
