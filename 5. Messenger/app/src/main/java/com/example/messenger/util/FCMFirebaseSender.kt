package com.example.messenger.util

import android.app.Activity
import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.messenger.R
import org.json.JSONException
import org.json.JSONObject


class FcmFirebaseSender(
    var userFcmToken: String,
    var title: String,
    var body: String,
    mContext: Context,
    mActivity: Activity
) {
    var mContext: Context
    var mActivity: Activity
    private lateinit var requestQueue: RequestQueue
    private val postUrl = "https://fcm.googleapis.com/fcm/send"
    private val fcmServerKey =
        "AAAAPqxTgZk:APA91bFyP4vtUdo1vk2eOF76xBfv2dkERjf9gCanw8MliNi98n8H-hU7dWXu3quQihSHHkCj92DiimNpa_BxpL5MZnfuXft6nnxvrsqcSkDRqLPHdICJmtS12yCOQXuSJBkhLu5EGtHL"

    init {
        this.mContext = mContext
        this.mActivity = mActivity
    }

    fun sendNotifications() {
        requestQueue = Volley.newRequestQueue(mActivity)
        val mainObj = JSONObject()
        try {
            mainObj.put("to", userFcmToken)
            val notiObject = JSONObject()
            notiObject.put("title", title)
            notiObject.put("body", body)
            notiObject.put("icon", R.drawable.ic_baseline_message_24) // enter icon that exists in drawable only
            mainObj.put("notification", notiObject)

            val request: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST,
                postUrl,
                mainObj,
                Response.Listener<JSONObject?> {
                    // code run is got response
                },
                Response.ErrorListener {
                    // code run is got error
                }) {

                override fun getHeaders(): MutableMap<String, String> {
                    val header: MutableMap<String, String> = HashMap()
                    header["content-type"] = "application/json"
                    header["authorization"] = "key=$fcmServerKey"
                    return header
                }
            }
            requestQueue.add(request)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}