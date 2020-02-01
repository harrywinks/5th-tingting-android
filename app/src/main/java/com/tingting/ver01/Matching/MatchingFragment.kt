package com.tingting.ver01.Matching

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tingting.ver01.Model.Matching.ShowAllCandidateListResponse
import com.tingting.ver01.Model.ModelMatching
import com.tingting.ver01.Model.TeamDataCallback
import com.tingting.ver01.R
import com.tingting.ver01.SearchTeam.PaginationScrollListener
import com.tingting.ver01.SharedPreference.App
import kotlinx.android.synthetic.main.fragment_matching_main.*
import kotlinx.android.synthetic.main.fragment_matching_main.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MatchingFragment : Fragment() {

    val model : ModelMatching = ModelMatching(activity)
    val recyclerview = null
    var teamList = arrayListOf<TeamData>()
    var isLastPage = false
    var isLoading = false
    var myTeamId:Int = 0
    lateinit var myTeam : List<ShowAllCandidateListResponse.Data.MyTeam>
    lateinit var matchingTeam: List<ShowAllCandidateListResponse.Data.Matching>
    lateinit var listOptions : Array<String>
    lateinit var spinnerAdapter:FilterAdapter
    lateinit var teamSpinner : Spinner
    lateinit var currTeam:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_matching_main, null)

        val adapter = MatchingAdapter(activity!!.applicationContext, teamList)

        //init data

        teamSpinner = view.filter

        //intro = model.lookMatchingTeam()
        model.lookTeamList(App.prefs.myToken.toString(),5, object : TeamDataCallback{
            override fun showAllCandidateList(data: ShowAllCandidateListResponse?) {
                matchingTeam = data!!.data.matchingList
                 myTeam = data!!.data.myTeamList
                  try {

                      val scope = CoroutineScope(Dispatchers.Main)
                      listOptions = Array<String>(myTeam.size,{i ->""})

                      runBlocking {
                          scope.launch {
                              for (i in 0..matchingTeam.size - 1){
                                  when(matchingTeam.get(i).membersInfo.size){
                                      1->teamList.add(TeamData(matchingTeam.get(i).membersInfo.get(0).thumbnail, matchingTeam.get(i).place, matchingTeam.get(i).name))
                                      2->teamList.add(TeamData(matchingTeam.get(i).membersInfo.get(0).thumbnail,matchingTeam.get(i).membersInfo.get(1).thumbnail, matchingTeam.get(i).place, matchingTeam.get(i).name))
                                      3->teamList.add(TeamData(matchingTeam.get(i).membersInfo.get(0).thumbnail,matchingTeam.get(i).membersInfo.get(1).thumbnail,matchingTeam.get(i).membersInfo.get(2).thumbnail, matchingTeam.get(i).place, matchingTeam.get(i).name))
                                      4->teamList.add(TeamData(matchingTeam.get(i).membersInfo.get(0).thumbnail,matchingTeam.get(i).membersInfo.get(1).thumbnail,matchingTeam.get(i).membersInfo.get(2).thumbnail,matchingTeam.get(i).membersInfo.get(3).thumbnail, matchingTeam.get(i).place, matchingTeam.get(i).name))

                                  }
                              }
                              for( i in 0..myTeam.size-1){
                                  Log.d("spinnerItemAdd","스피너 아이템 추가")
                                  if(myTeam.size == 0){
                                      currentTeam.setText("소속된 팀이 없습니다.")
                                      currentTeamsub.visibility = View.GONE
                                  }else{
                                      try{
                                          listOptions.set(i,myTeam.get(i).name)
                                          currentTeamsub.visibility = View.VISIBLE
                                          currentTeam.setText(myTeam.get(0).name)
                                          myTeamId = myTeam.get(0).id

                                          spinnerAdapter =  FilterAdapter(context!!, listOptions)
                                          // 팀 스피너
                                          teamSpinner?.adapter = spinnerAdapter

                                      }catch(e: java.lang.Exception){

                                      }
                                  }

                              }




                          }
                      }
                      adapter.notifyDataSetChanged()
                  }  catch (e : Exception){

                  }

            }

        })


        // 필터
        /*view.addFilter.setOnClickListener(){
            val intent = Intent(activity, FilterActivity::class.java)
            activity!!.startActivity(intent)
        }*/



        adapter.notifyDataSetChanged()

        adapter.matchingclick = object  : MatchingAdapter.MatchingClick{

            override fun Onclick(view: View, position: Int) {


                //val intent = Intent(activity, com.example.tintint_jw.Matching.MatchingDetail::class.java)
                val intent = Intent(activity, MatchingApplyTeamInfo::class.java)
                intent.putExtra("MatchingTeamId", matchingTeam.get(position).id)
                intent.putExtra("MyTeamId", myTeamId)
                activity!!.startActivity(intent)

                //activity!!.supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.mainFragment,MatchingDetail()).commit()
            }
        }



        view.searchMatching.adapter = adapter

        val layoutManager = LinearLayoutManager(this.context)
        view.searchMatching.layoutManager = layoutManager
        view.searchMatching.setHasFixedSize(true)



        // 스피너 아이템 이벤트
        teamSpinner?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("SpinnerNameChange","스피너 이름 변경")
                currTeam = parent!!.getItemAtPosition(position).toString()
                currentTeam.setText(currTeam)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }

        })

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

                Log.d("visibleItemCount",visibleItemCount.toString())
                Log.d("totalItemCount",totalItemCount.toString())
                Log.d("firstPosition",firstPosition.toString())

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


        return view
    }

}