package com.tingting.ver01.Matching

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tingting.ver01.Model.Matching.ShowAllCandidateListResponse
import com.tingting.ver01.Model.ModelMatching
import com.tingting.ver01.Model.TeamDataCallback
import com.tingting.ver01.R
import com.tingting.ver01.SearchTeam.PaginationScrollListener
import com.tingting.ver01.SharedPreference.App
import com.tingting.ver01.View.MainActivity
import kotlinx.android.synthetic.main.fragment_matching_main.*
import kotlinx.android.synthetic.main.fragment_matching_main.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView
import uk.co.deanwild.materialshowcaseview.shape.OvalShape


class MatchingFragment : Fragment() {

    val model : ModelMatching = ModelMatching(activity)
    val recyclerview = null
    var teamList = arrayListOf<TeamData>()
    var isLastPage = false
    var isLoading = false
    var myTeamId:Int = 0
    var currTeam:String = ""
    var isFirstSelected = true
    var myTeamNumber =0
    var listOptions : ArrayList<String> = ArrayList()
    lateinit var teamContainer:RelativeLayout
    lateinit var myTeam : List<ShowAllCandidateListResponse.Data.MyTeam>
    lateinit var matchingTeam: List<ShowAllCandidateListResponse.Data.Matching>

    lateinit var adapter : MatchingAdapter
    lateinit var spinnerAdapter:FilterAdapter

    val SHOWCASE_ID:String = "tooltip_matching"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_matching_main, null)

        var teamSpinner = view.filter

        adapter = activity?.let { MatchingAdapter(it, teamList as MutableList<TeamData>) }!!


        if(isFirstSelected){
            isFirstSelected = false;
            model.lookTeamList(App.prefs.myToken.toString(), currTeam, object : TeamDataCallback{
                override fun showAllCandidateList(data: ShowAllCandidateListResponse?) {
                    matchingTeam = data!!.data.matchingList
                    myTeam = data!!.data.myTeamList

                    try {

                        teamList.clear()

                        //myTeamNumber = myTeam.get(0).max_member_number


                        val scope = CoroutineScope(Dispatchers.Main)
                        runBlocking {
                            scope.launch {
                                Log.i("myTeam", data.data.myTeamList.size.toString())
                                if(myTeam.isNullOrEmpty()) {
                                    view.currentTeam.text = "소속된 팀이 없습니다."
                                    view.currentTeamsub.visibility = View.GONE

                                }else{
                                    myTeamNumber = myTeam.get(0).max_member_number
                                    for(i in 0..myTeam.size-1) {
                                        listOptions.add(myTeam.get(i).name)
                                    }
                                    view.currentTeam?.text = myTeam.get(0).name
                                    view.currentTeamsub.visibility = View.VISIBLE
                                    myTeamId = myTeam.get(0).id

                                    for (i in 0..matchingTeam.size-1){

                                        if(matchingTeam.get(i).max_member_number==1 && myTeamNumber==1){
                                            teamList.add(TeamData(matchingTeam.get(i).membersInfo.get(0).thumbnail, matchingTeam.get(i).place, matchingTeam.get(i).name,matchingTeam.get(i).max_member_number,matchingTeam.get(i).id))

                                        }else if (matchingTeam.get(i).max_member_number ==2&& myTeamNumber==2 ){
                                            teamList.add(TeamData(matchingTeam.get(i).membersInfo.get(0).thumbnail, matchingTeam.get(i).membersInfo.get(1).thumbnail, matchingTeam.get(i).place, matchingTeam.get(i).name,matchingTeam.get(i).max_member_number,matchingTeam.get(i).id))

                                        }else if(matchingTeam.get(i).max_member_number==3 && myTeamNumber==3){
                                            teamList.add(TeamData(matchingTeam.get(i).membersInfo.get(0).thumbnail,matchingTeam.get(i).membersInfo.get(1).thumbnail,matchingTeam.get(i).membersInfo.get(2).thumbnail, matchingTeam.get(i).place, matchingTeam.get(i).name,matchingTeam.get(i).max_member_number,matchingTeam.get(i).id))

                                        }else if (matchingTeam.get(i).max_member_number==4 && myTeamNumber==4){
                                            teamList.add(TeamData(matchingTeam.get(i).membersInfo.get(0).thumbnail,matchingTeam.get(i).membersInfo.get(1).thumbnail,matchingTeam.get(i).membersInfo.get(2).thumbnail,matchingTeam.get(i).membersInfo.get(3).thumbnail, matchingTeam.get(i).place, matchingTeam.get(i).name,matchingTeam.get(i).max_member_number,matchingTeam.get(i).id))
                                        }

                                    }
                                }

                                spinnerAdapter = com.tingting.ver01.Matching.FilterAdapter(activity!!.applicationContext, listOptions)
                                teamSpinner.adapter = spinnerAdapter
                            }

                        }

                        adapter.notifyDataSetChanged()

                        // 팀 스피너

                    }  catch (e : Exception){
                        e.printStackTrace()
                    }

                }

            })

        }

        teamContainer = view.findViewById(R.id.myTeam)
        //init data

        teamSpinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{


            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("Spinner",position.toString());
                loadTeamList(position)
            }
        })
        var adapter2  = activity?.applicationContext?.let { ArrayAdapter(it,R.layout.spinner_filter_dropdown,R.id.spinnerText,listOptions) } as SpinnerAdapter

        adapter.notifyDataSetChanged()

        adapter.matchingclick = object  : MatchingAdapter.MatchingClick{

            override fun Onclick(view: View, position: Int) {
                //val intent = Intent(activity, com.example.tintint_jw.Matching.MatchingDetail::class.java)
                val intent = Intent(activity, MatchingApplyTeamInfo::class.java)
                intent.putExtra("MatchingTeamId", teamList.get(position).teamID)
                intent.putExtra("MyTeamId", myTeamId)

                activity!!.startActivity(intent)

            }
        }

        view.searchMatching.adapter = adapter

        val layoutManager = LinearLayoutManager(this.context)
        view.searchMatching.layoutManager = layoutManager
        view.searchMatching.setHasFixedSize(true)


        view.searchMatching.addOnScrollListener(object:PaginationScrollListener(LinearLayoutManager(view.context)){
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                var visibleItemCount = view.searchMatching.layoutManager!!.childCount
                var totalItemCount = view.searchMatching.layoutManager!!.itemCount
                var first : LinearLayoutManager = view.searchMatching.layoutManager as LinearLayoutManager
                var firstPosition = first.findFirstVisibleItemPosition()


                if (!isLoading && !isLastPage) {

                    if ((visibleItemCount + firstPosition >= totalItemCount) && (firstPosition >= 0)) {
                        loadMoreItems()
                    }
                }
                super.onScrolled(recyclerView, dx, dy)

            }

            override fun loadMoreItems() {

            }
        }
        )

        view.matchingSwipe.setOnRefreshListener{

            var update = Runnable {
                //데이터 셋을 불러옴
                Toast.makeText(view.context,"데이터를 불러왔습니다.", Toast.LENGTH_LONG).show()
            }
            matchingSwipe.isRefreshing = false
        }

        presentShowcaseView()

        return view
    }

    fun presentShowcaseView(){
        MaterialShowcaseView.Builder(activity)
            .setTarget(teamContainer)
            .setShape(object :OvalShape(){
                override fun setAdjustToTarget(adjustToTarget: Boolean) {


                }
            })
            .setMaskColour(resources.getColor(R.color.tooltip_mask))
            .setDismissText("닫기")
            .setDismissTextColor(Color.parseColor("#EAEAEA"))
            .setContentText("매칭을 신청할 때 속한 팀을 다양하게 바꿔보세요!")
            .setContentTextColor(Color.parseColor("#EAEAEA"))
            .setDelay(100)
            .singleUse(SHOWCASE_ID)
            .show()
    }
    fun loadTeamList(index :Int ){
        teamList.clear()
        listOptions.clear()

        model.lookTeamList(App.prefs.myToken.toString(), currTeam, object : TeamDataCallback{
            override fun showAllCandidateList(data: ShowAllCandidateListResponse?) {
                matchingTeam = data!!.data.matchingList
                myTeam = data!!.data.myTeamList

                try {

                    teamList.clear()

                    myTeamNumber = myTeam.get(index).max_member_number


                    val scope = CoroutineScope(Dispatchers.Main)
                    for(i in 0..myTeam.size-1) {
                        listOptions.add(myTeam.get(i).name)
                    }
                    /*Array<String>(myTeam.size,{i ->""})
                    */

                    runBlocking {
                        scope.launch {
                            try{
                            for (i in 0..matchingTeam.size-1){

                                if(matchingTeam.get(i).max_member_number==1 && myTeamNumber==1){
                                    teamList.add(TeamData(matchingTeam.get(i).membersInfo.get(0).thumbnail, matchingTeam.get(i).place, matchingTeam.get(i).name,matchingTeam.get(i).max_member_number,matchingTeam.get(i).id))

                                }else if (matchingTeam.get(i).max_member_number ==2&& myTeamNumber==2 ){
                                    teamList.add(TeamData(matchingTeam.get(i).membersInfo.get(0).thumbnail, matchingTeam.get(i).membersInfo.get(1).thumbnail, matchingTeam.get(i).place, matchingTeam.get(i).name,matchingTeam.get(i).max_member_number,matchingTeam.get(i).id))

                                }else if(matchingTeam.get(i).max_member_number==3 && myTeamNumber==3){
                                    teamList.add(TeamData(matchingTeam.get(i).membersInfo.get(0).thumbnail,matchingTeam.get(i).membersInfo.get(1).thumbnail,matchingTeam.get(i).membersInfo.get(2).thumbnail, matchingTeam.get(i).place, matchingTeam.get(i).name,matchingTeam.get(i).max_member_number,matchingTeam.get(i).id))

                                }else if (matchingTeam.get(i).max_member_number==4 && myTeamNumber==4){
                                    teamList.add(TeamData(matchingTeam.get(i).membersInfo.get(0).thumbnail,matchingTeam.get(i).membersInfo.get(1).thumbnail,matchingTeam.get(i).membersInfo.get(2).thumbnail,matchingTeam.get(i).membersInfo.get(3).thumbnail, matchingTeam.get(i).place, matchingTeam.get(i).name,matchingTeam.get(i).max_member_number,matchingTeam.get(i).id))
                                }
                            }
                            }catch (e : Exception){
                                e.printStackTrace()
                            }

                            if(myTeam.size == 0) {
                                currentTeam.text = "소속된 팀이 없습니다."
                                currentTeamsub.visibility = View.GONE

                            }else{
                                currentTeam?.text = myTeam.get(index).name
                                myTeamId = myTeam.get(index).id
                            }


                        }

                    }
                    adapter.notifyDataSetChanged()

                    // 팀 스피너

                }  catch (e : Exception){

                }

            }

        })

    }

    override fun onResume() {
        super.onResume()
        MainActivity.allowRefreshMatching=true
        MainActivity.allowRefreshSearch=false
        MainActivity.allowRefreshProfile=false
    }



}