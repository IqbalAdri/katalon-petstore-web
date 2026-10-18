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
 * TC03 - Registrasi akun baru [POSITIVE][DYNAMIC DATA]
 * Data akun di-generate otomatis oleh custom keyword petstore.DataGenerator,
 * username selalu unik sehingga test bisa dijalankan berulang kali.
 */

'Generate data akun baru (dynamic data)'
Map account = CustomKeywords.'petstore.DataGenerator.generateAccountData'()

'Precondition: buka halaman utama JPetStore'
CustomKeywords.'petstore.CommonKeywords.openHomePage'()

'Step 1: buka form registrasi melalui Sign In > Register Now!'
WebUI.click(findTestObject('Page_Header/link_SignIn'))

WebUI.click(findTestObject('Page_Login/link_RegisterNow'))

WebUI.verifyElementVisible(findTestObject('Page_Register/btn_SaveAccount'))

'Step 2: isi User Information'
WebUI.setText(findTestObject('Page_Register/input_UserId'), account.username)

WebUI.setText(findTestObject('Page_Register/input_NewPassword'), account.password)

WebUI.setText(findTestObject('Page_Register/input_RepeatPassword'), account.password)

'Step 3: isi Account Information'
WebUI.setText(findTestObject('Page_Register/input_FirstName'), account.firstName)

WebUI.setText(findTestObject('Page_Register/input_LastName'), account.lastName)

WebUI.setText(findTestObject('Page_Register/input_Email'), account.email)

WebUI.setText(findTestObject('Page_Register/input_Phone'), account.phone)

WebUI.setText(findTestObject('Page_Register/input_Address1'), account.address1)

WebUI.setText(findTestObject('Page_Register/input_Address2'), account.address2)

WebUI.setText(findTestObject('Page_Register/input_City'), account.city)

WebUI.setText(findTestObject('Page_Register/input_State'), account.state)

WebUI.setText(findTestObject('Page_Register/input_Zip'), account.zip)

WebUI.setText(findTestObject('Page_Register/input_Country'), account.country)

'Step 4: isi Profile Information'
WebUI.selectOptionByValue(findTestObject('Page_Register/select_LanguagePreference'), account.language, false)

WebUI.selectOptionByValue(findTestObject('Page_Register/select_FavouriteCategory'), account.favouriteCategory, false)

WebUI.check(findTestObject('Page_Register/chk_EnableMyList'))

WebUI.check(findTestObject('Page_Register/chk_EnableMyBanner'))

WebUI.verifyElementChecked(findTestObject('Page_Register/chk_EnableMyList'), GlobalVariable.timeout)

'Step 5: simpan akun'
WebUI.click(findTestObject('Page_Register/btn_SaveAccount'))

'Expected: registrasi berhasil - user diarahkan kembali ke halaman utama (catalog)'
WebUI.verifyElementPresent(findTestObject('Page_Home/link_SidebarCategory', [('categoryId') : account.favouriteCategory]),
    GlobalVariable.timeout)

WebUI.verifyMatch(WebUI.getUrl(), '.*/actions/Catalog\\.action.*', true)

'Step 6: login menggunakan akun yang baru dibuat (JPetStore tidak menampilkan status login otomatis setelah registrasi)'
CustomKeywords.'petstore.CommonKeywords.login'(account.username, account.password)

'Expected: akun baru tersimpan, bisa dipakai login, dan nama yang tampil sesuai data registrasi'
CustomKeywords.'petstore.CommonKeywords.verifyLoggedIn'(account.firstName)

'Step 7: logout'
CustomKeywords.'petstore.CommonKeywords.logout'()

WebUI.closeBrowser()
