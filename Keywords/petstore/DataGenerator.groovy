package petstore

import java.text.SimpleDateFormat

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil

/**
 * Generator data dinamis untuk test (dynamic data).
 * Setiap eksekusi menghasilkan data berbeda sehingga test registrasi bisa dijalankan berulang kali.
 */
class DataGenerator {

	private static final Random RANDOM = new Random()

	private static final List<String> FIRST_NAMES = ['Andi', 'Budi', 'Citra', 'Dewi', 'Eka', 'Fajar', 'Gita', 'Hadi', 'Intan', 'Joko']
	private static final List<String> LAST_NAMES = ['Pratama', 'Saputra', 'Lestari', 'Wijaya', 'Kusuma', 'Santoso', 'Rahma', 'Hidayat']
	private static final List<String> CITIES = ['Jakarta', 'Bandung', 'Surabaya', 'Yogyakarta', 'Medan', 'Makassar']
	private static final List<String> CATEGORIES = ['FISH', 'DOGS', 'REPTILES', 'CATS', 'BIRDS']

	/**
	 * Username unik berbasis timestamp + angka acak, contoh: qa_261012153045_427
	 */
	@Keyword
	String uniqueUsername(String prefix) {
		String stamp = new SimpleDateFormat('yyMMddHHmmss').format(new Date())
		return "${prefix}_${stamp}_${RANDOM.nextInt(900) + 100}".toString()
	}

	/**
	 * Data lengkap untuk form registrasi akun baru.
	 */
	@Keyword
	Map<String, String> generateAccountData() {
		String username = uniqueUsername('qa')
		String firstName = pick(FIRST_NAMES)
		String lastName = pick(LAST_NAMES)
		Map<String, String> account = [
			username          : username,
			password          : "Pw${RANDOM.nextInt(900000) + 100000}".toString(),
			firstName         : firstName,
			lastName          : lastName,
			email             : "${username}@example.com".toString(),
			phone             : "08${RANDOM.nextInt(900000000) + 100000000}".toString(),
			address1          : "Jl. Automation No. ${RANDOM.nextInt(200) + 1}".toString(),
			address2          : "RT 0${RANDOM.nextInt(9) + 1} / RW 0${RANDOM.nextInt(9) + 1}".toString(),
			city              : pick(CITIES),
			state             : 'ID',
			zip               : String.valueOf(RANDOM.nextInt(90000) + 10000),
			country           : 'Indonesia',
			language          : 'english',
			favouriteCategory : pick(CATEGORIES)
		]
		KeywordUtil.logInfo("Data akun yang di-generate: username=${account.username}, firstName=${account.firstName}, favouriteCategory=${account.favouriteCategory}")
		return account
	}

	private static String pick(List<String> values) {
		return values[RANDOM.nextInt(values.size())]
	}
}
