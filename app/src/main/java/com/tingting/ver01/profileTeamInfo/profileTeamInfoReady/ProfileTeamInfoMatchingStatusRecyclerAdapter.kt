package com.tingting.ver01.profileTeamInfo.profileTeamInfoReady

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tingting.ver01.ApplyTeamInfo.ApplyTeamInfoActivity
import com.tingting.ver01.databinding.ProfileTeamInfoItemBinding
import com.tingting.ver01.model.CodeCallBack
import com.tingting.ver01.model.ModelMatching
import com.tingting.ver01.model.team.lookMyTeamInfoDetail.LookMyTeamInfoDetailResponse
import com.tingting.ver01.teamInfo.ProfileTeamInfoMatchingStatusHolder
import com.tingting.ver01.view.Main.MainActivity
import com.tingting.ver01.viewModel.ProfileTeamInfoViewModel

class ProfileTeamInfoMatchingStatusRecyclerAdapter(private val profileTeamInfoViewModel: ProfileTeamInfoViewModel, val myTeamId:Int)
    : RecyclerView.Adapter<ProfileTeamInfoMatchingStatusHolder>(){

    var teamList : List<LookMyTeamInfoDetailResponse.Data.TeamMatching> = emptyList()
    lateinit var view : ViewGroup

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileTeamInfoMatchingStatusHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ProfileTeamInfoItemBinding.inflate(inflater,parent,false)
        view = parent

        return ProfileTeamInfoMatchingStatusHolder(
            dataBinding,
            profileTeamInfoViewModel
        )
    }

    override fun getItemCount(): Int {
        return teamList.size
    }

    override fun onBindViewHolder(holder: ProfileTeamInfoMatchingStatusHolder, position: Int) {
            holder.setUp(teamList[position])

        for(i in 0..teamList[position].sendTeam.membersInfo.size -1){
            when(i){
                1-> MainActivity.glide.setImage(holder.img1.context, MainActivity.glide.DecryptUrl(
                   teamList[position].sendTeam.membersInfo.get(i).thumbnail
                ),holder.img1)
                2-> MainActivity.glide.setImage(holder.img2.context, MainActivity.glide.DecryptUrl(
                    teamList[position].sendTeam.membersInfo.get(i).thumbnail
                ),holder.img2)
                3-> MainActivity.glide.setImage(holder.img3.context, MainActivity.glide.DecryptUrl(
                    teamList[position].sendTeam.membersInfo.get(i).thumbnail
                ),holder.img3)
                4-> MainActivity.glide.setImage(holder.img4.context, MainActivity.glide.DecryptUrl(
                    teamList[position].sendTeam.membersInfo.get(i).thumbnail
                ),holder.img4)
            }
        }

        when(teamList[position].sendTeam.membersInfo.size){
            2->{
                holder.img3.visibility = View.GONE
                holder.img4.visibility = View.GONE
            }
            3-> holder.img4.visibility = View.GONE
        }


        holder.itemView.setOnClickListener {
            //상대 프로필 보여주는 api 콜
            val intent = Intent(view.context, ApplyTeamInfoActivity::class.java)
            //myTeamId
            //상대 팀 ID
            intent.putExtra("myTeamId",myTeamId)
            intent.putExtra("matchingTeamId", teamList[position].id)
            view.context.startActivity(intent)
        }

        holder.okBtn.setOnClickListener {
            // 수락 하는 api 콜
            ModelMatching.getInstance().receiveHeart(teamList[position].id, object : CodeCallBack {

                override fun onSuccess(code: String, value: String) {
                    if(code.equals("201")){
                        Toast.makeText(view.context.applicationContext,"수락 되었습니다.", Toast.LENGTH_LONG).show()

                    }else if(code.equals("400")){
                        Toast.makeText(view.context.applicationContext,"매칭 정보가 없습니다!", Toast.LENGTH_LONG).show()

                    }else if(code.equals("403")){
                        Toast.makeText(view.context.applicationContext,"팀에 속해있지 않습니다!", Toast.LENGTH_LONG).show()

                    }else{
                        Toast.makeText(view.context.applicationContext,"매칭 수락하기 실패", Toast.LENGTH_LONG).show()

                    }
                }
            })

        }

        holder.cancelBtn.setOnClickListener {
            //취소하는 api 콜
        }

    }

    //데이터 업데이트
    fun updateData(teamData: LookMyTeamInfoDetailResponse){
        this.teamList = teamData.data.teamMatchings
        notifyDataSetChanged()
    }
}