package com.example.tintint_jw

import com.example.tintint_jw.SearchTeam.SearchTeamData
import com.example.tintint_jw.TeamInfo.TeamInfoDetailActivity
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        var a = SearchTeamData(1,"123",3);

        assertEquals(a.img1,1)

    }
}
