package softsuave.tech_matrix.emoji_sketcher.api

import io.reactivex.Single
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import softsuave.tech_matrix.emoji_sketcher.di.CanvasSize
import softsuave.tech_matrix.emoji_sketcher.model.Stroke
import timber.log.Timber
import java.util.Collections.emptyList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmojiDetectionProvider
@Inject constructor(
    private val service: EmojiDetectionService,
    @CanvasSize private val width: Int,
    @CanvasSize private val height: Int
) {

    fun getEmojis(strokes: List<Stroke>): Single<List<String>> {
        val req = EmojiDetectionRequest.Builder()
            .width(width)
            .height(height)
            .setStrokes(convertStrokesToArray(strokes))
            .build()

        return service.detect(req)
            .map(::extractEmojiArrayFromResponse)
    }

    private fun convertStrokesToArray(strokes: List<Stroke>): Array<Array<IntArray>> {
        return strokes.map { stroke ->
            arrayOf(
                stroke.xcoords.toIntArray(),
                stroke.ycoords.toIntArray(),
                IntArray(0)
            )
        }.toTypedArray()
    }

    private fun extractEmojiArrayFromResponse(responseBody: ResponseBody): List<String> {
        val string = responseBody.string()
        val arrayString = "{\"data\": $string }"

        try {
            val json = JSONObject(arrayString)
            val array = json.getJSONArray("data")

            val status = array.getString(0)
            if (status != "SUCCESS") {
                return emptyList()
            }

            val second = array.getJSONArray(1)
            val emojiArray = second.getJSONArray(0).getJSONArray(1)

            return (0 until emojiArray.length()).map { emojiArray.getString(it) }
        } catch (e: JSONException) {
            Timber.e(e, "Error while parsing server response")
        }
        return emptyList()
    }
}

