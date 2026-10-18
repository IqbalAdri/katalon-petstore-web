package petstore

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.util.regex.Matcher

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

/**
 * Keyword untuk alur katalog -> cart dan perhitungan harga.
 */
class CartKeywords {

	/**
	 * Menambahkan item ke cart lewat UI: Quick Link kategori -> Product ID -> Add to Cart.
	 * Berakhir di halaman Shopping Cart dengan item sudah tampil.
	 */
	@Keyword
	def addItemToCart(String categoryId, String productId, String itemId) {
		WebUI.click(findTestObject('Page_Header/link_QuickCategory', [('categoryId') : categoryId]))
		WebUI.click(findTestObject('Page_Category/link_ProductId', [('productId') : productId]))
		WebUI.click(findTestObject('Page_Product/btn_AddToCart', [('itemId') : itemId]))
		WebUI.verifyElementPresent(findTestObject('Page_Cart/link_CartItemId', [('itemId') : itemId]), GlobalVariable.timeout)
	}

	/**
	 * Mengambil nilai harga dari teks, contoh: "Sub Total: $1,234.50" -> 1234.50
	 */
	@Keyword
	BigDecimal parsePrice(String text) {
		Matcher matcher = (text =~ /\$\s*([\d,]+\.\d{2})/)
		if (!matcher.find()) {
			KeywordUtil.markFailedAndStop("Format harga tidak ditemukan pada teks: '${text}'")
		}
		return new BigDecimal(matcher.group(1).replace(',', ''))
	}

	/**
	 * Verifikasi harga pada teks sama dengan nilai yang diharapkan (perbandingan numerik).
	 */
	@Keyword
	def verifyPrice(String actualText, BigDecimal expectedAmount) {
		BigDecimal actualAmount = parsePrice(actualText)
		if (actualAmount.compareTo(expectedAmount) == 0) {
			KeywordUtil.markPassed("Harga sesuai: ${actualAmount} (teks: '${actualText}')")
		} else {
			KeywordUtil.markFailed("Harga tidak sesuai. Expected: ${expectedAmount}, Actual: ${actualAmount} (teks: '${actualText}')")
		}
	}
}
