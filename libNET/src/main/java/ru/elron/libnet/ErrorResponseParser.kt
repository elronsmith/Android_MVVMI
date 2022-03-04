package ru.elron.libnet

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import ru.elron.libnet.model.ErrorResponse
import java.io.IOException

class ErrorResponseParser(private val retrofit: Retrofit) {
    fun parseErrorResponseOrNull(response: Response<*>): ErrorResponse? {
        val converter: Converter<ResponseBody, ErrorResponse> = retrofit.responseBodyConverter(
            ErrorResponse::class.java,
            arrayOfNulls<Annotation>(0)
        )

        val errorResponse: ErrorResponse? = try {
            val body = response.errorBody()
            if (body != null) converter.convert(body) else null
        } catch (e: IOException) {
            null
        }

        return errorResponse
    }
}
