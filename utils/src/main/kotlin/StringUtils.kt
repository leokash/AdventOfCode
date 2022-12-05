
import java.math.BigInteger
import java.security.MessageDigest

fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16).padStart(32, '0')

fun String.replace(data: Map<String, String>): String {
    var res = this
    data.forEach { (old, new) ->  res = res.replace(old, new) }
    return res
}
