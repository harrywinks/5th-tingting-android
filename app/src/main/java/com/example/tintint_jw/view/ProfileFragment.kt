package com.example.tintint_jw.TeamInfo

import GetProfileResponse
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tintint_jw.ApplyTeamInfo.ApplyTeamInfoActivity
import com.example.tintint_jw.Model.ModelProfile
import com.example.tintint_jw.Model.ProfileCallBack
import com.example.tintint_jw.ProfileResponseRequest.ProfileResponseReAdapter
import com.example.tintint_jw.ProfileResponseRequest.ProfileResponseReData
import com.example.tintint_jw.ProfileTeamInfo.ProfileTeamInfoData
import com.example.tintint_jw.ProfileTeamInfo.ProflieTeamInfoAdapter
import com.example.tintint_jw.R
import com.example.tintint_jw.SharedPreference.App
import com.example.tintint_jw.view.ProfileDetailActivity
import com.example.tintint_jw.view.SettingsActivity
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class ProfileFragment : Fragment(){

    var model : ModelProfile = ModelProfile(activity)
    var teamList = arrayListOf<ProfileTeamInfoData>()
    lateinit var MyTeamAdapter: ProflieTeamInfoAdapter
    lateinit var Readapter :ProfileResponseReAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        App.prefs.myautoLogin= "true"


        val view = inflater.inflate(R.layout.profile_fragment, null)

        // settings
        view.settings.setOnClickListener {
            val intentSetting = Intent(activity, SettingsActivity::class.java)
            activity!!.startActivity(intentSetting)
        }

        // move to detail profile fragment
        view.ProfileEdit.setOnClickListener(){

            val intent = Intent(activity, ProfileDetailActivity::class.java)
            activity!!.startActivity(intent)
        }

        var myTeamdata : List<GetProfileResponse.Data.MyTeam> = listOf()

       teamList = arrayListOf<ProfileTeamInfoData>()


        model.getProfile(App.prefs.myToken.toString(), object : ProfileCallBack{
            override fun onSuccess(
                name: String,
                birth: String,
                height: String,
                thumnail: String,
                gender: String,
                data :List<GetProfileResponse.Data.MyTeam>) {

                myTeamdata = data


                val scope = CoroutineScope(Dispatchers.Main
                )
                runBlocking {
                    scope.launch {
                        for(i in 0..myTeamdata.size-1){
                            teamList.add(ProfileTeamInfoData(myTeamdata.get(i).name,myTeamdata.get(i).id,false))
                            MyTeamAdapter.notifyDataSetChanged()
                        }
                            myTeamMessageView(teamList,alertNoTeam)
                    }
                }

                //this is code for teamList
                //this is testcode.

                NickName_View.setText(name+" 님")
                Glide.with(this@ProfileFragment).load(thumnail).apply(RequestOptions.circleCropTransform()).into(view.newteamProfileImg)

            }

        })



        //newteamProfileImg.setImageResource(R.drawable.haein)
        /*Glide.with(view)
            .load(R.drawable.haein)
            .apply(RequestOptions.circleCropTransform())
            .into(view.newteamProfileImg)*/

        //when connect with server use this code.
       /* for(i in 0..teamList.size){
            teamList.add(ProfileTeamInfoData(teamList.get(i).name,teamList.get(i).IsNews))
        }
        */

        var intent2 = Intent(activity,TeamInfoActivity::class.java)

         MyTeamAdapter = ProflieTeamInfoAdapter(activity!!.applicationContext,teamList){

            data -> startActivity(intent2)

            //teamName 넘김 --> 서버에서 teamName이랑 일치하는 정보 받아온 후 화면에 띄워줌.
        }

        val deco = ProfileTeamInfoMargin(5)
        view.newteamRecyclerView1.addItemDecoration(deco)
        view.newteamRecyclerView1.adapter = MyTeamAdapter

        val lm = LinearLayoutManager(activity!!.applicationContext)
        view.newteamRecyclerView1.layoutManager = lm
        view.newteamRecyclerView1.setHasFixedSize(true)


        //this is code for Request Answer.

        var requestData  = arrayListOf<ProfileResponseReData>()





        var intent = Intent(activity,ApplyTeamInfoActivity::class.java)

         Readapter = ProfileResponseReAdapter(activity!!.applicationContext,requestData){

            data-> startActivity(intent)

        }

        view.newteamRecyclerView2.addItemDecoration(deco)
        view.newteamRecyclerView2.adapter = Readapter

        val lm2 = LinearLayoutManager(activity!!.applicationContext)
        view.newteamRecyclerView2.layoutManager = lm2
        view.newteamRecyclerView2.setHasFixedSize(true)

        return view
    }

    fun myTeamMessageView(lsize : List<Any>, view:TextView){
        Log.d("myTeamMessageView",teamList.size.toString())

        if(lsize.size!=0){
            Log.d("myTeamMessageView","myTeamMessageView실행됨")

            view.visibility=View.INVISIBLE

        }
    }


}