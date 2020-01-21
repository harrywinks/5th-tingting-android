package com.example.tintint_jw.Model.Team.LookMyTeamInfoDetail
import androidx.annotation.Keep


@Keep
data class LookMyTeamInfoDetailResponse(
          val `data`: Data = Data()
) {
          @Keep
          data class Data(
              val teamInfo: TeamInfo = TeamInfo(),
              val teamMember: List<TeamMember> = listOf()
          ) {
                    @Keep
                    data class TeamInfo(
                              val chat_address: String = "", // kakao/soeijfsif/
                              val gender: Int = 0, // 1
                              val intro: String = "", // 저희팀은귀엽습니다.
                              val is_verified: Int = 0, // 1
                              val max_member_number: Int = 0, // 2
                              val name: String = "", // 여자팀1
                              val owner_id: Int = 0 // 43
                    )

                    @Keep
                    data class TeamMember(
                              val id: Int = 0, // 43
                              val name: String = "", // 틴틴
                              val thumbnail: String = "" // d2323223fffv.png
                    )
          }
}