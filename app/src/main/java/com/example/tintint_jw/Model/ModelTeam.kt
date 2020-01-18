package com.example.tintint_jw.Model

import android.app.Activity
import androidx.room.Update
import com.example.tintint_jw.Model.Team.JoinTeam.JoinTeamRequest
import com.example.tintint_jw.Model.Team.JoinTeam.JoinTeamResponse
import com.example.tintint_jw.Model.Team.LookTeamList.TeamResponse
import com.example.tintint_jw.Model.Team.MakeTeam.MakeTeamRequest
import com.example.tintint_jw.Model.Team.MakeTeam.MakeTeamResponse
import com.example.tintint_jw.Model.Team.MakeTeam.TeamNameResponse
import com.example.tintint_jw.Model.Team.UpdateTeam.UpdateMyTeaminfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModelTeam(val context: Activity) {

    fun makeTeam(
        token: String, password: String, gender: Int, name: String,
        max_member_number: Int, intro: String, chat_address: String
    ) {

        val request =
            MakeTeamRequest(password, gender, name, max_member_number, intro, chat_address)
        val call = RetrofitGenerator.createTeam().makeTeam(token, request)

        call.enqueue(object : retrofit2.Callback<MakeTeamResponse> {
            override fun onFailure(call: Call<MakeTeamResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<MakeTeamResponse>,
                response: Response<MakeTeamResponse>
            ) {

            }

        })
    }

    fun showIndivisualTeamList(token: String, bossid: Int, team: TeamDataCallback) {
        val call = RetrofitGenerator.createTeam().oneTeamInfo(token, bossid)

        call.enqueue(object : retrofit2.Callback<MakeTeamResponse> {

            override fun onFailure(call: Call<MakeTeamResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<MakeTeamResponse>,
                response: Response<MakeTeamResponse>
            ) {
                team.onIndivisualResult(response.body(), 0, 0)
            }

        })
    }

    fun TeamName(n: String, Id: IdCallBack) {

        val call = RetrofitGenerator.createTeam().McheckTeamName(n)

        call.enqueue(object : retrofit2.Callback<TeamNameResponse> {
            override fun onFailure(call: Call<TeamNameResponse>, t: Throwable) {

                return
            }

            override fun onResponse(
                call: Call<TeamNameResponse>,
                response: Response<TeamNameResponse>
            ) {
                var a = response.body()?.data?.message
                if (a.equals("사용 가능한 팀명입니다.")) {
                    Id.onSuccess("t")
                } else {
                    Id.onSuccess("f")
                }
                return
            }
        })
    }

    fun JoinTeam(token: String, teamid: Int) {
        val request = JoinTeamRequest("")
        val call = RetrofitGenerator.createTeam().joinTeam(token, teamid, request)

        call.enqueue(object : retrofit2.Callback<JoinTeamResponse>{
            override fun onFailure(call: Call<JoinTeamResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<JoinTeamResponse>,
                response: Response<JoinTeamResponse>
            ) {

            }
        })

    }

    fun ReviseTeamInfo(token:String, teamId:Int,Password:String,Gender:String,Name:String,Max_member_number:String
    ,Intro:String,Tag_list:String,Chat_address: String){
        val request = UpdateMyTeaminfo(Password,Gender.toInt(),Name,Max_member_number.toInt(),Intro,Tag_list,Chat_address)

        val call = RetrofitGenerator.createTeam().updateMyTeamInfo(token, teamId, request)

        call.enqueue(object : Callback<MakeTeamResponse>{
            override fun onFailure(call: Call<MakeTeamResponse>, t: Throwable) {
              t.printStackTrace()
            }

            override fun onResponse(
                call: Call<MakeTeamResponse>,
                response: Response<MakeTeamResponse>
            ) {

            }
        })
    }
}