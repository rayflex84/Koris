package com.torepofficiel.carem.koris.com.torepofficiel.carem.custom_volley

/**
 * Created by macbook on 12/09/17.
 */

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.android.volley.toolbox.HttpHeaderParser
import org.json.JSONException
import org.json.JSONObject

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class CustomJSonObjectRequest : Request<JSONObject> {

    private var listener: Listener<JSONObject>? = null
    private var params: Map<String, String>? = null

    constructor(url: String, params: Map<String, String>,
                reponseListener: Listener<JSONObject>, errorListener: ErrorListener) : super(Request.Method.GET, url, errorListener) {
        this.listener = reponseListener
        this.params = params
    }

    constructor(method: Int, url: String, params: Map<String, String>,
                reponseListener: Listener<JSONObject>, errorListener: ErrorListener) : super(method, url, errorListener) {
        this.listener = reponseListener
        this.params = params
    }

    @Throws(com.android.volley.AuthFailureError::class)
    override fun getParams(): Map<String, String>? {
        return params
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
        try {
            val jsonString = String(response.data,
                    (HttpHeaderParser.parseCharset(response.headers)) as Charset)
            return Response.success(JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            return Response.error(ParseError(e))
        } catch (je: JSONException) {
            return Response.error(ParseError(je))
        }

    }

    override fun deliverResponse(response: JSONObject) {
        // TODO Auto-generated method stub
        listener!!.onResponse(response)
    }
}