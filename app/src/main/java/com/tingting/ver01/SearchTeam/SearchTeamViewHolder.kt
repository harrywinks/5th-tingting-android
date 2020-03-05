package com.tingting.ver01.SearchTeam

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.tingting.ver01.BR
import com.tingting.ver01.View.Main.MainActivity
import com.tingting.ver01.model.team.lookTeamList.TeamResponse
import com.tingting.ver01.viewModel.SearchTeamFragmentViewModel
import kotlinx.android.synthetic.main.current_matching_team_item4.view.*

//데이터를 쓰는 것은 Databinding 과 v
class SearchTeamViewHolder constructor(
    private val databinding: ViewDataBinding,
    val searchTeamFragmentViewModel: SearchTeamFragmentViewModel
) : RecyclerView.ViewHolder(databinding.root) {

    //inflate
    val regieon = itemView?.teamRegion
    val teamInfo = itemView?.teamInfo4
    val arrowToDetail = itemView?.arrowToDetail

    val img_m4First = itemView?.img_m4First
    val img_m4Second = itemView?.img_m4Sec
    val img_m4Thrid = itemView?.img_m4Third
    val img_m4Fourth = itemView?.img_m4Fourth


    fun setup(item: TeamResponse.Data.Team) {
        databinding.setVariable(BR.searchTeamItem, item)
        databinding.executePendingBindings()

        for(i in 0..item.teamMembersInfo.size-1){
            when(i){
                0-> MainActivity.glide.setImage(img_m4First.context,
                    MainActivity.glide.DecryptUrl(item.teamMembersInfo.get(0).thumbnail),img_m4First)

                1-> MainActivity.glide.setImage(img_m4Second.context,
                    MainActivity.glide.DecryptUrl(item.teamMembersInfo.get(1).thumbnail),img_m4Second)

                2-> MainActivity.glide.setImage(img_m4Thrid.context,
                    MainActivity.glide.DecryptUrl(item.teamMembersInfo.get(2).thumbnail),img_m4Thrid)

                3->  MainActivity.glide.setImage(img_m4Fourth.context,
                    MainActivity.glide.DecryptUrl(item.teamMembersInfo.get(3).thumbnail),img_m4Fourth)
            }
        }

        when(item.max_member_number){
            2-> {img_m4Thrid.visibility = View.GONE
                img_m4Fourth.visibility = View.GONE
            }

            3-> img_m4Fourth.visibility = View.GONE

        }


    }

}