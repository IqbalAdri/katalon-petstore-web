package petstore

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

/**
 * Keyword umum JPetStore: membuka aplikasi dan autentikasi.
 * Semua URL, credential, dan timeout diambil dari Global Variable (Profile) - tidak ada hardcode.
 */
class CommonKeywords {

	/**
	 * Membuka browser, maximize window, lalu navigasi ke halaman utama (GlobalVariable.baseUrl).
	 */
	@Keyword
	def openHomePage() {
		WebUI.openBrowser('')
		WebUI.maximizeWindow()
		WebUI.navigateToUrl(GlobalVariable.baseUrl)
		WebUI.waitForPageLoad(GlobalVariable.timeout)
		WebUI.verifyElementPresent(findTestObject('Page_Header/link_Cart'), GlobalVariable.timeout)
	}

	/**
	 * Login melalui menu Sign In.
	 * @param username username yang diinput
	 * @param password password yang diinput
	 */
	@Keyword
	def login(String username, String password) {
		WebUI.click(findTestObject('Page_Header/link_SignIn'))
		WebUI.waitForElementVisible(findTestObject('Page_Login/input_Username'), GlobalVariable.timeout)
		WebUI.setText(findTestObject('Page_Login/input_Username'), username)
		WebUI.setText(findTestObject('Page_Login/input_Password'), password)
		WebUI.click(findTestObject('Page_Login/btn_Login'))
	}

	/**
	 * Verifikasi user sudah login: menu Sign Out tampil dan teks Welcome sesuai.
	 * @param firstName first name akun; kosong/null = cukup verifikasi format "Welcome ...!"
	 */
	@Keyword
	def verifyLoggedIn(String firstName) {
		WebUI.verifyElementPresent(findTestObject('Page_Header/link_SignOut'), GlobalVariable.timeout)
		String welcomeText = WebUI.getText(findTestObject('Page_Home/txt_WelcomeContent'))
		if (firstName) {
			WebUI.verifyEqual(welcomeText, "Welcome ${firstName}!".toString())
		} else {
			WebUI.verifyMatch(welcomeText, '^Welcome .+!$', true)
		}
	}

	/**
	 * Logout lalu verifikasi menu Sign In kembali tampil.
	 */
	@Keyword
	def logout() {
		WebUI.click(findTestObject('Page_Header/link_SignOut'))
		WebUI.verifyElementPresent(findTestObject('Page_Header/link_SignIn'), GlobalVariable.timeout)
	}

	/**
	 * Mengganti token pada test data:
	 *   {GLOBAL_USERNAME} -> GlobalVariable.username
	 *   {GLOBAL_PASSWORD} -> GlobalVariable.password
	 *   {EMPTY}           -> string kosong
	 * Token {EMPTY} diperlukan karena Katalon mengganti sel CSV yang kosong dengan default value variabel test case.
	 */
	@Keyword
	String resolveDataToken(String value) {
		if (value == null || value.trim() == '{EMPTY}') {
			return ''
		}
		String resolved = value
				.replace('{GLOBAL_USERNAME}', GlobalVariable.username.toString())
				.replace('{GLOBAL_PASSWORD}', GlobalVariable.password.toString())
		return resolved
	}

	/**
	 * Precondition suite: memastikan akun pada Global Variable bisa dipakai login.
	 * Jika login ditolak (mis. database server demo di-reset), akun diregistrasi ulang
	 * dengan username & password yang sama. Tidak pernah menggagalkan suite - hanya memberi warning.
	 */
	@Keyword
	def ensureAccountRegistered(String username, String password) {
		FailureHandling optional = FailureHandling.OPTIONAL
		try {
			WebUI.openBrowser('', optional)
			WebUI.navigateToUrl(GlobalVariable.baseUrl, optional)
			if (loginQuietly(username, password)) {
				KeywordUtil.logInfo("Akun '${username}' valid dan siap dipakai test.")
				return
			}

			KeywordUtil.markWarning("Akun '${username}' tidak bisa login di server demo. Registrasi ulang dengan credential dari Global Variable...")
			Map<String, String> data = new DataGenerator().generateAccountData()
			data.username = username
			data.password = password

			WebUI.click(findTestObject('Page_Login/link_RegisterNow'), optional)
			WebUI.setText(findTestObject('Page_Register/input_UserId'), data.username, optional)
			WebUI.setText(findTestObject('Page_Register/input_NewPassword'), data.password, optional)
			WebUI.setText(findTestObject('Page_Register/input_RepeatPassword'), data.password, optional)
			WebUI.setText(findTestObject('Page_Register/input_FirstName'), data.firstName, optional)
			WebUI.setText(findTestObject('Page_Register/input_LastName'), data.lastName, optional)
			WebUI.setText(findTestObject('Page_Register/input_Email'), data.email, optional)
			WebUI.setText(findTestObject('Page_Register/input_Phone'), data.phone, optional)
			WebUI.setText(findTestObject('Page_Register/input_Address1'), data.address1, optional)
			WebUI.setText(findTestObject('Page_Register/input_Address2'), data.address2, optional)
			WebUI.setText(findTestObject('Page_Register/input_City'), data.city, optional)
			WebUI.setText(findTestObject('Page_Register/input_State'), data.state, optional)
			WebUI.setText(findTestObject('Page_Register/input_Zip'), data.zip, optional)
			WebUI.setText(findTestObject('Page_Register/input_Country'), data.country, optional)
			WebUI.click(findTestObject('Page_Register/btn_SaveAccount'), optional)

			if (loginQuietly(username, password)) {
				KeywordUtil.logInfo("Akun '${username}' berhasil diregistrasi ulang.")
			} else {
				KeywordUtil.markWarning("Akun '${username}' tetap tidak bisa login (kemungkinan username sudah dipakai dengan password lain). " +
						"Ganti username/password di Profiles > default.")
			}
		} finally {
			WebUI.closeBrowser(optional)
		}
	}

	/**
	 * Login tanpa menggagalkan test; mengembalikan true jika menu Sign Out tampil setelah login.
	 */
	private boolean loginQuietly(String username, String password) {
		FailureHandling optional = FailureHandling.OPTIONAL
		WebUI.click(findTestObject('Page_Header/link_SignIn'), optional)
		WebUI.setText(findTestObject('Page_Login/input_Username'), username, optional)
		WebUI.setText(findTestObject('Page_Login/input_Password'), password, optional)
		WebUI.click(findTestObject('Page_Login/btn_Login'), optional)
		boolean loggedIn = WebUI.waitForElementPresent(findTestObject('Page_Header/link_SignOut'), 3, optional)
		if (loggedIn) {
			WebUI.click(findTestObject('Page_Header/link_SignOut'), optional)
		}
		return loggedIn
	}

	/**
	 * Menulis informasi skenario DDT ke log eksekusi (tampil di report).
	 */
	@Keyword
	def logScenario(String testName, String scenario) {
		KeywordUtil.logInfo("[${testName}] Skenario: ${scenario}")
	}
}
