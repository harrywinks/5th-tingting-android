package com.example.tintint_jw.SearchTeam.RecylcerViewLoad

import androidx.paging.PageKeyedDataSource
import com.example.tintint_jw.Model.RetrofitGenerator
import com.example.tintint_jw.Model.Team.LookTeamList.TeamResponse
import com.example.tintint_jw.SharedPreference.App
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class TeamDataSource : PageKeyedDataSource<String, TeamResponse.Data.Team>(){

    val api =  RetrofitGenerator.createTeam().lookTeamList(App.prefs.myToken.toString())

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, TeamResponse.Data.Team>
    ) {
        api.enqueue(object :retrofit2.Callback<TeamResponse>{
            override fun onFailure(call: Call<TeamResponse>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<TeamResponse>,
                response: Response<TeamResponse>
            ) {
                val listing = response.body()
                val teampPosts = listing?.data?.teamList

                callback.onResult(teampPosts ?: listOf(), "1", "2")
            }
        })
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, TeamResponse.Data.Team>) {
        api.enqueue(object :retrofit2.Callback<TeamResponse>{
            override fun onFailure(call: Call<TeamResponse>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<TeamResponse>,
                response: Response<TeamResponse>
            ) {
                val listing = response.body()
                val teampPosts = listing?.data?.teamList
                callback.onResult(teampPosts ?: listOf(), "1")
            }
        })
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, TeamResponse.Data.Team>) {
        api.enqueue(object :retrofit2.Callback<TeamResponse>{
            override fun onFailure(call: Call<TeamResponse>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<TeamResponse>,
                response: Response<TeamResponse>
            ) {
                val listing = response.body()
                val teampPosts = listing?.data?.teamList
                callback.onResult(teampPosts ?: listOf(), "2")
            }
        })
    }
}