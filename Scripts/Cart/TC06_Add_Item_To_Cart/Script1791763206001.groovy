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
 * TC06 - Tambah item ke cart lalu remove [POSITIVE][DDT]
 * Data      : Data Files/CartItemData (Data/cart_item_data.csv) - di-binding di Test Suite
 * Variables : categoryId, productId, productName, itemId, itemDescription, listPrice
 */

'Precondition: buka halaman utama JPetStore'
CustomKeywords.'petstore.CommonKeywords.openHomePage'()

'Step 1: pilih kategori melalui Quick Links di header'
WebUI.click(findTestObject('Page_Header/link_QuickCategory', [('categoryId') : categoryId]))

'Step 2: buka detail produk'
WebUI.click(findTestObject('Page_Category/link_ProductId', [('productId') : productId]))

WebUI.verifyElementText(findTestObject('Page_Product/txt_ProductTitle'), productName)

'Step 3: klik Add to Cart pada item yang dipilih'
WebUI.click(findTestObject('Page_Product/btn_AddToCart', [('itemId') : itemId]))

'Expected: halaman Shopping Cart terbuka dan berisi item yang benar'
WebUI.verifyElementText(findTestObject('Page_Cart/txt_CartTitle'), 'Shopping Cart')

WebUI.verifyElementPresent(findTestObject('Page_Cart/link_CartItemId', [('itemId') : itemId]), GlobalVariable.timeout)

WebUI.verifyElementText(findTestObject('Page_Cart/txt_ItemDescription', [('itemId') : itemId]), itemDescription)

WebUI.verifyElementAttributeValue(findTestObject('Page_Cart/input_Quantity', [('itemId') : itemId]), 'value', '1', GlobalVariable.timeout)

WebUI.verifyElementText(findTestObject('Page_Cart/txt_ListPrice', [('itemId') : itemId]), listPrice)

WebUI.verifyElementText(findTestObject('Page_Cart/txt_TotalCost', [('itemId') : itemId]), listPrice)

'Expected: Sub Total = harga item (quantity 1)'
BigDecimal expectedPrice = CustomKeywords.'petstore.CartKeywords.parsePrice'(listPrice)

String subTotalText = WebUI.getText(findTestObject('Page_Cart/txt_SubTotal'))

CustomKeywords.'petstore.CartKeywords.verifyPrice'(subTotalText, expectedPrice)

'Step 4: remove item dari cart'
WebUI.click(findTestObject('Page_Cart/btn_Remove', [('itemId') : itemId]))

'Expected: cart kosong dan Sub Total kembali $0.00'
WebUI.verifyElementPresent(findTestObject('Page_Cart/txt_EmptyCart'), GlobalVariable.timeout)

WebUI.verifyElementNotPresent(findTestObject('Page_Cart/link_CartItemId', [('itemId') : itemId]), GlobalVariable.timeout)

CustomKeywords.'petstore.CartKeywords.verifyPrice'(WebUI.getText(findTestObject('Page_Cart/txt_SubTotal')), BigDecimal.ZERO)

WebUI.closeBrowser()
