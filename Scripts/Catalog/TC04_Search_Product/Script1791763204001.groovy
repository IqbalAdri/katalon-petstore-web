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
 * TC04 - Pencarian produk [POSITIVE + NEGATIVE][DDT]
 * Data      : Data Files/SearchProductData (Data/search_product_data.csv) - di-binding di Test Suite
 * Variables : scenario, keyword, expectedResult (FOUND | NOT_FOUND | EMPTY_KEYWORD), expectedProducts (dipisah ';')
 */

CustomKeywords.'petstore.CommonKeywords.logScenario'('TC04', scenario)

'Precondition: buka halaman utama JPetStore'
CustomKeywords.'petstore.CommonKeywords.openHomePage'()

'Siapkan keyword: token {EMPTY} pada data diganti string kosong'
String searchKeyword = CustomKeywords.'petstore.CommonKeywords.resolveDataToken'(keyword)

'Step 1: input keyword pada kolom search'
WebUI.setText(findTestObject('Page_Header/input_SearchKeyword'), searchKeyword)

'Step 2: klik tombol Search'
WebUI.click(findTestObject('Page_Header/btn_Search'))

if (expectedResult == 'FOUND') {
    'Expected (positive): semua produk yang diharapkan tampil & jumlah hasil sesuai'
    List<String> products = expectedProducts.split(';').collect({ it.trim() })

    for (String productName : products) {
        WebUI.verifyElementPresent(findTestObject('Page_Search/txt_ResultProductName', [('productName') : productName]),
            GlobalVariable.timeout)
    }

    int resultCount = WebUI.findWebElements(findTestObject('Page_Search/txt_AllResultProductNames'), GlobalVariable.timeout).size()

    WebUI.verifyEqual(resultCount, products.size())
} else if (expectedResult == 'NOT_FOUND') {
    'Expected (negative): halaman hasil terbuka tetapi tidak ada produk yang ditemukan'
    WebUI.verifyElementPresent(findTestObject('Page_Search/link_ReturnToMainMenu'), GlobalVariable.timeout)

    WebUI.verifyElementNotPresent(findTestObject('Page_Search/txt_AllResultProductNames'), GlobalVariable.timeout)
} else if (expectedResult == 'EMPTY_KEYWORD') {
    'Expected (negative): muncul pesan validasi keyword kosong'
    WebUI.verifyElementVisible(findTestObject('Page_Header/txt_Message'))

    WebUI.verifyElementText(findTestObject('Page_Header/txt_Message'), 'Please enter a keyword to search for, then press the search button.')
} else {
    KeywordUtil.markFailed("expectedResult tidak dikenal pada data file: '$expectedResult'")
}

WebUI.closeBrowser()
