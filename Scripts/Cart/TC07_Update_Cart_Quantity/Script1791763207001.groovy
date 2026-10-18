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
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

/*
 * TC07 - Update quantity item di cart [POSITIVE + NEGATIVE][DDT]
 * Data      : Data Files/CartQuantityData (Data/cart_quantity_data.csv) - di-binding di Test Suite
 * Variables : scenario, categoryId, productId, itemId, listPrice, quantity, expectedResult, expectedQuantity
 *   UPDATED   -> quantity valid, total = harga x quantity
 *   REMOVED   -> quantity 0 / negatif, item terhapus dari cart
 *   UNCHANGED -> quantity non-numerik diabaikan, quantity tetap
 */

CustomKeywords.'petstore.CommonKeywords.logScenario'('TC07', scenario)

'Precondition: buka halaman utama dan tambahkan 1 item ke cart'
CustomKeywords.'petstore.CommonKeywords.openHomePage'()

CustomKeywords.'petstore.CartKeywords.addItemToCart'(categoryId, productId, itemId)

'Step 1: ubah quantity sesuai data'
WebUI.setText(findTestObject('Page_Cart/input_Quantity', [('itemId') : itemId]), quantity.toString())

'Step 2: klik Update Cart'
WebUI.click(findTestObject('Page_Cart/btn_UpdateCart'))

if (expectedResult == 'REMOVED') {
    'Expected (negative): quantity < 1 menghapus item, cart kosong dan Sub Total $0.00'
    WebUI.verifyElementPresent(findTestObject('Page_Cart/txt_EmptyCart'), GlobalVariable.timeout)

    WebUI.verifyElementNotPresent(findTestObject('Page_Cart/link_CartItemId', [('itemId') : itemId]), GlobalVariable.timeout)

    CustomKeywords.'petstore.CartKeywords.verifyPrice'(WebUI.getText(findTestObject('Page_Cart/txt_SubTotal')), BigDecimal.ZERO)
} else if ((expectedResult == 'UPDATED') || (expectedResult == 'UNCHANGED')) {
    'Expected: quantity sesuai data dan total dihitung ulang = harga x quantity'
    WebUI.verifyElementAttributeValue(findTestObject('Page_Cart/input_Quantity', [('itemId') : itemId]), 'value', expectedQuantity.toString(),
        GlobalVariable.timeout)

    BigDecimal expectedTotal = CustomKeywords.'petstore.CartKeywords.parsePrice'(listPrice) * new BigDecimal(expectedQuantity.toString())

    CustomKeywords.'petstore.CartKeywords.verifyPrice'(WebUI.getText(findTestObject('Page_Cart/txt_TotalCost', [('itemId') : itemId])),
        expectedTotal)

    CustomKeywords.'petstore.CartKeywords.verifyPrice'(WebUI.getText(findTestObject('Page_Cart/txt_SubTotal')), expectedTotal)
} else {
    KeywordUtil.markFailed("expectedResult tidak dikenal pada data file: '$expectedResult'")
}

WebUI.closeBrowser()
