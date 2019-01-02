package com.torepofficiel.carem.koris.com.torepofficiel.carem.custom_volley

/**
 * Created by macbook on 12/09/17.
 */

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.android.volley.toolbox.HttpHeaderParser

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class CustomStringRequest : Request<String> {

    private var listener: Listener<String>? = null
    private var params: Map<String, String>? = null

    constructor(url: String, params: Map<String, String>,
                reponseListener: Listener<String>, errorListener: ErrorListener) : super(Request.Method.GET, url, errorListener) {
        this.listener = reponseListener
        this.params = params
    }

    constructor(method: Int, url: String, params: Map<String, String>,
                reponseListener: Listener<String>, errorListener: ErrorListener) : super(method, url, errorListener) {
        this.listener = reponseListener
        this.params = params
    }

    @Throws(com.android.volley.AuthFailureError::class)
    override fun getParams(): Map<String, String>? {
        return params
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
        var parsed: String
        try {
            parsed = String(response.data, (HttpHeaderParser.parseCharset(response.headers)) as Charset)
        } catch (e: UnsupportedEncodingException) {
            parsed = String(response.data)
        }

        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response))
    }

    override fun deliverResponse(response: String) {
        // TODO Auto-generated method stub
        listener!!.onResponse(response)
    }
}