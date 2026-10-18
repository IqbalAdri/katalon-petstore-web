import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

/**
 * Listener global untuk logging eksekusi dan cleanup browser.
 */
class PetStoreTestListener {

	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		KeywordUtil.logInfo("=== START TEST SUITE: ${testSuiteContext.getTestSuiteId()} | baseUrl: ${GlobalVariable.baseUrl} ===")
	}

	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		KeywordUtil.logInfo(">>> START: ${testCaseContext.getTestCaseId()}")
	}

	/**
	 * Jika test case gagal di tengah jalan, script tidak sempat menjalankan closeBrowser().
	 * Listener menutup browser agar iterasi / test case berikutnya mulai dari kondisi bersih.
	 */
	@AfterTestCase
	def afterTestCase(TestCaseContext testCaseContext) {
		String status = testCaseContext.getTestCaseStatus()
		KeywordUtil.logInfo("<<< FINISH: ${testCaseContext.getTestCaseId()} -> ${status}")
		if (status != 'PASSED') {
			WebUI.closeBrowser(FailureHandling.OPTIONAL)
		}
	}

	@AfterTestSuite
	def afterTestSuite(TestSuiteContext testSuiteContext) {
		KeywordUtil.logInfo("=== FINISH TEST SUITE: ${testSuiteContext.getTestSuiteId()} ===")
	}
}
