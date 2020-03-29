package com.tingting.ver01.searchTeam

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tingting.ver01.BR
import com.tingting.ver01.R
import com.tingting.ver01.databinding.ActivitySearchTeamInfoBinding
import com.tingting.ver01.model.ModelTeam
import com.tingting.ver01.model.team.lookIndivisualTeam.IndivisualTeamResponse
import com.tingting.ver01.searchTeam.searchTeamMemberInfo.SearchTeamMemberInfoAdapter
import com.tingting.ver01.teamInfo.TeamInfoRecyclerViewMargin
import com.tingting.ver01.viewModel.TeamInfoActivityViewModel
import kotlinx.android.synthetic.main.activity_search_team_info.*
import kotlinx.android.synthetic.main.dialog_view.view.*

class SearchTeamInfo : AppCompatActivity() {

    lateinit var teamMemberAdapter: SearchTeamMemberInfoAdapter
    lateinit var dataBinding: ActivitySearchTeamInfoBinding

    var bossId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_team_info)

        dataBinding.viewmodel =
            ViewModelProviders.of(this).get(TeamInfoActivityViewModel::class.java)

        // 처리
        val size = resources.getDimensionPixelSize(R.dimen.wide_size)

        bossId = intent.getIntExtra("teamBossId", 0)
        var teamPassword = intent.getStringExtra("teamPassword")

        dataBinding.viewmodel?.fetchData(bossId)

        dataBinding.lifecycleOwner = this

        // back button event
        dataBinding.back.setOnClickListener {
            finish()
        }
        //Edit Team info button click

        //init screen
        dataBinding.teamJoin.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.dialog_team_info, null)

            val mbuilder = builder.setView(dialogView).show()

            dialogView.dialogOK.setOnClickListener {

                ModelTeam.getInstance().JoinTeam(bossId, "") { isSuccess: Boolean, response: Int? ->
                    finish()
                }

            }

            dialogView.dialogCancel.setOnClickListener {
                mbuilder.dismiss()
            }

        }

        setObserver()
        setApdater()

    }




    fun setObserver() {
        dataBinding.viewmodel?.teamData?.observe(this, Observer {
            teamMemberAdapter.update(it)
            initview(it)
        })
    }

    fun setApdater() {
        var data = dataBinding.viewmodel

        if (data != null) {
            val deco = TeamInfoRecyclerViewMargin(10)
            teamRecyclerView.addItemDecoration(deco)

            teamMemberAdapter = SearchTeamMemberInfoAdapter(dataBinding.viewmodel!!, this)
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            teamRecyclerView.layoutManager = layoutManager
            teamRecyclerView.adapter = teamMemberAdapter
        }
    }

    fun initview(item : IndivisualTeamResponse) {
        dataBinding.setVariable(BR.teaminfoItem,item)
        dataBinding.executePendingBindings()

        var gender = item.data.teamInfo.gender
        var maxNum = item.data.teamInfo.max_member_number

        when (gender) {
            0 -> dataBinding.genderInfo.text = "남자"
            1 -> dataBinding.genderInfo.text = "여자"
        }

        dataBinding.numberInfo.text = maxNum.toString() + ":" + maxNum.toString()

    }
}

