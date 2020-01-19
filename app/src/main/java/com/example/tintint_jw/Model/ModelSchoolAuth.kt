package com.example.tintint_jw.Model

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.tintint_jw.Model.Auth.School.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModelSchoolAuth(val context: Context) {

    fun schoolAuth(name: String, email: String, id : IdCallBack) {

        val schoolAuthRequest = SchoolAuthRequest(name, email)
        val call = RetrofitGenerator.create().SchoolAuth(schoolAuthRequest)

        call.enqueue(object : Callback<SchoolAuthResponse> {
            override fun onFailure(call: Call<SchoolAuthResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "이미 가입된 이메일이거나 가입이 불가능한 이메일입니다.", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<SchoolAuthResponse>,
                response: Response<SchoolAuthResponse>
            ) {
                Log.d("School Auth response", response.toString())
                var a: SchoolAuthResponse? = response.body()
                id.onSuccess(a!!.data.message)
                Log.d("School Auth response", response.message().toString())


            }
        })

    }

    fun schoolAuthComplete(email: String, id:IdCallBack) {
        val schoolAuthCompleteRequest = SchoolCompleteRequest(email)
        val call = RetrofitGenerator.create().SchoolAuthComplete(email)

        call.enqueue(object : Callback<SchoolCompleteResponse> {
            override fun onFailure(call: Call<SchoolCompleteResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "인증이 필요한 이메일입니다.", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<SchoolCompleteResponse>,
                response: Response<SchoolCompleteResponse>
            ) {
                Log.d("Complete response", response.toString())
                var a: SchoolCompleteResponse? = response.body()
                try{
                    id.onSuccess(a!!.data.message)
                }catch(e: KotlinNullPointerException){

                }
                Log.d("Complete response2", response.message().toString())
            }
        })
    }

}