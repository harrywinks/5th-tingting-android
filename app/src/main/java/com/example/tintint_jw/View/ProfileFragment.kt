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
import com.example.tintint_jw.Matching.MatchingRequestTeamInfo
import com.example.tintint_jw.Model.Profile.ModelProfile
import com.example.tintint_jw.Model.Profile.SentMatchingsCallback
import com.example.tintint_jw.Model.ProfileCallBack
import com.example.tintint_jw.ProfileResponseRequest.ProfileResponseReAdapter
import com.example.tintint_jw.ProfileResponseRequest.ProfileResponseReData
import com.example.tintint_jw.ProfileTeamInfo.ProfileTeamInfoData
import com.example.tintint_jw.ProfileTeamInfo.ProflieTeamInfoAdapter
import com.example.tintint_jw.R
import com.example.tintint_jw.SharedPreference.App
import com.example.tintint_jw.View.ProfileDetailActivity
import com.example.tintint_jw.View.SettingsActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.kakao.usermgmt.StringSet
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception


class ProfileFragment : Fragment() {

    var model: ModelProfile = ModelProfile(activity)
    var teamList = arrayListOf<ProfileTeamInfoData>()
    var requestData  = arrayListOf<ProfileResponseReData>()
    lateinit var MyTeamAdapter: ProflieTeamInfoAdapter
    lateinit var Readapter :ProfileResponseReAdapter
    var receiveTeamId:Int = 0
    var sentmyTeamId:Int = 0
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val bundle = Bundle()
        firebaseAnalytics = FirebaseAnalytics.getInstance(activity!!.applicationContext)
        Log.d("LoginActivityttttt","qwr")
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, App.prefs.mylocal_id.toString())
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, StringSet.name)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        initShared()

        val view = inflater.inflate(R.layout.profile_fragment, null)

        // settings
        view.settings.setOnClickListener {
            val intentSetting = Intent(activity, SettingsActivity::class.java)
            activity!!.startActivity(intentSetting)
        }

        // move to detail profile fragment
        view.ProfileEdit.setOnClickListener() {

            val intent = Intent(activity, ProfileDetailActivity::class.java)
            activity!!.startActivity(intent)
        }

        var myTeamdata: List<GetProfileResponse.Data.MyTeam> = listOf()

        teamList = arrayListOf<ProfileTeamInfoData>()


        //프로파일 기본 init
        model.getProfile(App.prefs.myToken.toString(), object : ProfileCallBack {
            override fun onSuccess(
                name: String,
                birth: String,
                height: String,
                thumnail: String,
                gender: String,
                data: List<GetProfileResponse.Data.MyTeam>
            ) {

                myTeamdata = data

                App.prefs.mygender = gender

                val scope = CoroutineScope(Dispatchers.Main)
                runBlocking {
                    scope.launch {
                        for (i in 0..myTeamdata.size - 1) {
                            teamList.add(
                                ProfileTeamInfoData(
                                    myTeamdata.get(i).name,
                                    myTeamdata.get(i).id,
                                    false
                                )
                            )
                            MyTeamAdapter.notifyDataSetChanged()
                        }
                        try {
                        myTeamMessageView(teamList, alertNoTeam)
                        }catch (e : Exception){
                        e.printStackTrace()
                    }

                    }
                }

                //this is code for teamList
                //this is testcode.

                newteamTeamlistTV.setText(name+"님의 팀")
                NickName_View.setText(name+" 님")
                App.prefs.myname = name
                Glide.with(this@ProfileFragment).load(thumnail).apply(RequestOptions.circleCropTransform()).into(view.newteamProfileImg)

            }

        })

        MyTeamAdapter = ProflieTeamInfoAdapter(activity!!.applicationContext, teamList)

        MyTeamAdapter.itemClick = object : ProflieTeamInfoAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                Log.d("ProfileTeamData", "ProfileTeamFragment실행")
                var intent = Intent(activity, TeamInfoActivity::class.java)
                intent.putExtra("MyTeamId", teamList.get(position).id)
                startActivity(intent)

            }
        }

        //teamName 넘김 --> 서버에서 teamName이랑 일치하는 정보 받아온 후 화면에 띄워줌.


        val deco = ProfileTeamInfoMargin(5)
        view.newteamRecyclerView1.addItemDecoration(deco)
        view.newteamRecyclerView1.adapter = MyTeamAdapter

        val lm = LinearLayoutManager(activity!!.applicationContext)
        view.newteamRecyclerView1.layoutManager = lm
        view.newteamRecyclerView1.setHasFixedSize(true)


        //this is code for Request Answer.


        // 응답요청
        var requestData = arrayListOf<ProfileResponseReData>()

        Readapter = ProfileResponseReAdapter(activity!!.applicationContext,requestData)

        var intent = Intent(activity, ApplyTeamInfoActivity::class.java)

        model.getSentMatchings(App.prefs.myToken.toString(), object :SentMatchingsCallback{
            override fun sentMatchings(data: GetProfileResponse) {
                var coroutineScope:CoroutineScope = CoroutineScope(Dispatchers.Main)
                runBlocking {
                    coroutineScope.launch {
                        try{
                            for(i in 0..data.data.sentMatchings.size-1){
                                requestData.add(ProfileResponseReData(data.data.sentMatchings.get(i).receiveTeam.name))
                                Log.i("requestData",data.data.sentMatchings.get(i).receiveTeam.name)

                            Readapter.notifyDataSetChanged()

                            sentTeamMessageView(requestData, alertNoApply)
                        }
                    }catch (e:Exception){
                        }
                }

            }
        }})

        Readapter.itemClick = object : ProfileResponseReAdapter.ItemClick{
            override fun Onclick(view: View, position: Int) {
                model.getSentMatchings(App.prefs.myToken.toString(), object:SentMatchingsCallback{

                    override fun sentMatchings(data: GetProfileResponse) {
                        var sentmyTeamName = data.data.sentMatchings.get(position).sendTeam.id
                        var receiveTeamName = data.data.sentMatchings.get(position).receiveTeam.id
                        var matchingId = data.data.sentMatchings.get(position).id
                        Log.i("sentmyTeamName", sentmyTeamName.toString())
                        Log.i("receiveTeamName", receiveTeamName.toString())
                        intent.putExtra("MyTeamId", sentmyTeamName)
                        intent.putExtra("MatchingTeamId", receiveTeamName)
                        intent.putExtra("MatchingId", matchingId)

                        startActivity(intent)

                    }
                })
            }

        }

        view.newteamRecyclerView2.addItemDecoration(deco)
        view.newteamRecyclerView2.adapter = Readapter

        val lm2 = LinearLayoutManager(activity!!.applicationContext)
        view.newteamRecyclerView2.layoutManager = lm2
        view.newteamRecyclerView2.setHasFixedSize(true)

        return view
    }

    fun myTeamMessageView(lsize: List<Any>, view: TextView) {
        Log.d("myTeamMessageView", teamList.size.toString())

        if(lsize.size!=0){
            Log.d("myTeamMessageView","myTeamMessageView실행됨")

            view.visibility=View.INVISIBLE

        }
    }

    fun sentTeamMessageView(stsize:List<Any>, view:TextView){

        Log.i("sentTeamSize", requestData.size.toString())
        if(stsize.size!=0){
            view.visibility = View.INVISIBLE
        }
    }

    fun initShared() {
        App.prefs.mythumnail = ""
        App.prefs.mybirth = ""
        App.prefs.myheight = ""
        App.prefs.myauthenticated_address = ""
        App.prefs.myautoLogin = "true"

    }
}
