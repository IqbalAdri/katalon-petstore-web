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
 * TC05 - Browse produk berdasarkan kategori [POSITIVE][DDT]
 * Data      : Data Files/CategoryData (Data/category_data.csv) - di-binding di Test Suite
 * Variables : categoryId, categoryName, productId, productName
 */

'Precondition: buka halaman utama JPetStore'
CustomKeywords.'petstore.CommonKeywords.openHomePage'()

'Step 1: klik ikon kategori pada sidebar'
WebUI.click(findTestObject('Page_Home/link_SidebarCategory', [('categoryId') : categoryId]))

'Expected: halaman kategori terbuka dengan judul dan URL yang sesuai'
WebUI.verifyElementText(findTestObject('Page_Category/txt_CategoryTitle'), categoryName)

WebUI.verifyMatch(WebUI.getUrl(), ('.*categoryId=' + categoryId) + '.*', true)

'Expected: produk yang diharapkan tampil di daftar kategori'
WebUI.verifyElementText(findTestObject('Page_Category/txt_ProductName', [('productId') : productId]), productName)

'Step 2: klik Product ID untuk membuka detail produk'
WebUI.click(findTestObject('Page_Category/link_ProductId', [('productId') : productId]))

'Expected: halaman detail produk terbuka dengan nama produk yang benar'
WebUI.verifyElementText(findTestObject('Page_Product/txt_ProductTitle'), productName)

WebUI.verifyMatch(WebUI.getUrl(), ('.*productId=' + productId) + '.*', true)

WebUI.closeBrowser()
