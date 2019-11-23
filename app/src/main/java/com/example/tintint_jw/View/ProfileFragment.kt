package com.example.tintint_jw.TeamInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tintint_jw.ProfileResponseRequest.ProfileResponseReAdapter
import com.example.tintint_jw.ProfileResponseRequest.ProfileResponseReData
import com.example.tintint_jw.ProfileTeamInfo.ProfileTeamInfoData
import com.example.tintint_jw.ProfileTeamInfo.ProflieTeamInfoAdapter
import com.example.tintint_jw.R
import com.example.tintint_jw.View.ProfileDetailFragment
import com.example.tintint_jw.View.TeamInfo
import kotlinx.android.synthetic.main.fragment_team_info.view.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.view.*


class ProfileFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.profile_fragment, null)
        // move to detail profile fragment
        view.newteamEditProfileTV.setOnClickListener(){
            activity!!.supportFragmentManager.beginTransaction().
                add(R.id.mainFragment,ProfileDetailFragment()).commit()
        }


        //this is code for teamList
        //this is testcode.
        var teamList = arrayListOf<ProfileTeamInfoData>()
        teamList.add(ProfileTeamInfoData("불금불금",false))
        teamList.add(ProfileTeamInfoData("안귀요미들",false))
        teamList.add(ProfileTeamInfoData("안귀요미들",false))
        teamList.add(ProfileTeamInfoData("안귀요미들",false))

        //when connect with server use this code.
       /* for(i in 0..teamList.size){
            teamList.add(ProfileTeamInfoData(teamList.get(i).name,teamList.get(i).IsNews))
        }
        */

        val PTadapter = ProflieTeamInfoAdapter(activity!!.applicationContext,teamList){
            it-> activity!!.supportFragmentManager.beginTransaction().add(R.id.mainFragment, TeamInfo()).commit()
            //teamName 넘김 --> 서버에서 teamName이랑 일치하는 정보 받아온 후 화면에 띄워줌.

        }

        val deco = ProfileTeamInfoMargin(5)
        view.newteamRecyclerView1.addItemDecoration(deco)
        view.newteamRecyclerView1.adapter = PTadapter

        val lm = LinearLayoutManager(activity!!.applicationContext)
        view.newteamRecyclerView1.layoutManager = lm
        view.newteamRecyclerView1.setHasFixedSize(true)


        //this is code for Request Answer.

        var requestData  = arrayListOf<ProfileResponseReData>()

        requestData.add(ProfileResponseReData("불금불금"))
        requestData.add(ProfileResponseReData("안귀요미들"))
        requestData.add(ProfileResponseReData("안귀요미들"))
        requestData.add(ProfileResponseReData("안귀요미들"))

        val Readapter = ProfileResponseReAdapter(activity!!.applicationContext,requestData)

        view.newteamRecyclerView2.addItemDecoration(deco)
        view.newteamRecyclerView2.adapter = Readapter
        val lm2 = LinearLayoutManager(activity!!.applicationContext)
        view.newteamRecyclerView2.layoutManager = lm2
        view.newteamRecyclerView2.setHasFixedSize(true)

        return view
    }

}