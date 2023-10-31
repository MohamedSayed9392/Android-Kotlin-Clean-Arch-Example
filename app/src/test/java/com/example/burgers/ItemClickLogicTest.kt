package com.example.burgers

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.burgers.data.datasource.api.BurgersApi
import com.example.burgers.data.datasource.dao.AssetsBurgersDao
import com.example.burgers.data.datasource.db.AssetsBurgersDatabase
import com.example.burgers.data.repository.AssetsBurgerRepositoryImpl
import com.example.burgers.domain.model.AssetsBurger
import com.example.burgers.domain.model.RemoteBurger
import com.example.burgers.domain.repository.AssetsBurgerRepository
import com.example.burgers.helpers.TestHelpers
import com.example.burgers.utils.Utils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"], application = Application::class)
class ItemClickLogicTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: BurgersApi
    private lateinit var db: AssetsBurgersDatabase
    private lateinit var dao: AssetsBurgersDao
    private lateinit var repository: AssetsBurgerRepository

    @Before
    fun init() {
        service = TestHelpers.getTestRetrofitApiService(isConnected = false)
        db = TestHelpers.getTestDB()
        dao = db.burgersDao

        repository = AssetsBurgerRepositoryImpl(dao,service)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testThatUsersCanClickOnHttpsItemsOnly() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()

        for(i in 0..3){
            dao.insertBurger(TestHelpers.getWithoutHttpTestDBItem())
            dao.insertBurger(TestHelpers.getHttpTestDBItem())
        }

        val dbBurgersList = repository.getAllBurgersDB().first()
        val remoteBurgersList = repository.getBurgersRemote().first()

        val shuffledList = Utils.getShuffledListOfLists(dbBurgersList,remoteBurgersList)

        assert(shuffledList.all { burger ->
            val link = if(burger is AssetsBurger) burger.link else if(burger is RemoteBurger) burger.link else ""

            if(link.startsWith("https")){
                return@all Utils.openHttpsUrl(context,link)
            }else{
                return@all !Utils.openHttpsUrl(context,link)
            }
        })
    }

    @After
    fun tearDown(){
        db.close()
    }
}