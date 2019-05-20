package com.example.moviedbclean.domain.mapper

import android.util.Log
import com.example.moviedbclean.domain.response.ErrorDomainModel
import com.example.moviedbclean.domain.response.ErrorStatus
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * attempts to parse http response body and get error data from it
 *
 * @param body retrofit response body
 * @return returns an instance of [ErrorDomainModel] with parsed data or NOT_DEFINED status
 */
fun ResponseBody.getHttpError(): ErrorDomainModel {
    return try {
        // use response body to get error detail
        val result = string()
        Log.d("getHttpError", "getErrorMessage() called with: errorBody = [$result]")
        val json = Gson().fromJson(result, JsonObject::class.java)
        ErrorDomainModel(
            json.toString(),
            400,
            ErrorStatus.BAD_RESPONSE
        )
    } catch (e: Throwable) {
        e.printStackTrace()
        ErrorDomainModel(ErrorStatus.NOT_DEFINED)
    }
}

fun Throwable.mapToErrorDomainModel(): ErrorDomainModel {
    val errorModel: ErrorDomainModel? = when (this) {

        // if throwable is an instance of HttpException
        // then attempt to parse error data from response body
        is HttpException -> {
            // handle UNAUTHORIZED situation (when token expired)
            if (code() == 401) {
                ErrorDomainModel(ErrorStatus.UNAUTHORIZED)
            } else {
                response().errorBody()?.getHttpError()
            }
        }

        // handle api call timeout error
        is SocketTimeoutException -> {
            ErrorDomainModel(
                "TIME OUT!!",
                0,
                ErrorStatus.TIMEOUT
            )
        }

        // handle connection error
        is IOException -> {
            ErrorDomainModel(
                "CHECK CONNECTION",
                0,
                ErrorStatus.NO_CONNECTION
            )
        }

        is UnknownHostException -> {
            ErrorDomainModel(
                "CHECK CONNECTION",
                0,
                ErrorStatus.NO_CONNECTION
            )
        }
        else -> null
    }
    return errorModel!!
}