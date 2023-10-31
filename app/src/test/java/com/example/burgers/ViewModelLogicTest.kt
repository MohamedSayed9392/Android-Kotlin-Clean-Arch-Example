package com.example.burgers

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
class ViewModelLogicTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: BurgersApi
    private lateinit var db: AssetsBurgersDatabase
    private lateinit var dao: AssetsBurgersDao
    private lateinit var repository: AssetsBurgerRepository

    @Before
    fun init() {
        db = TestHelpers.getTestDB()
        dao = db.burgersDao
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testBurgersListIsShuffledFromDBandRemote() = runTest {

        service = TestHelpers.getTestRetrofitApiService(isConnected = true)
        repository = AssetsBurgerRepositoryImpl(dao,service)

        for(i in 0..3){
            dao.insertBurger(TestHelpers.getHttpsTestDBItem())
        }

        val dbBurgersList = repository.getAllBurgersDB().first()
        val remoteBurgersList = repository.getBurgersRemote().first()

        val normalList = Utils.getNormalListOfLists(dbBurgersList,remoteBurgersList)
        val shuffledList = Utils.getShuffledListOfLists(dbBurgersList,remoteBurgersList)

        //Assert that the shuffled list contains items from DB and items from RemoteBurger
        assert(shuffledList.any { it is AssetsBurger } && shuffledList.any { it is RemoteBurger })
        //Assert that the shuffled list size equal the normal list with all db items and remote items
        assert(shuffledList.size == normalList.size)
        //Assert that the shuffled list order not equal the normal list order and it's shuffled randomly
        assert(shuffledList != normalList)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testInCaseNoInternerGetItemsFromDBOnly() = runTest {
        service = TestHelpers.getTestRetrofitApiService(isConnected = false)
        repository = AssetsBurgerRepositoryImpl(dao,service)

        for(i in 0..3){
            dao.insertBurger(TestHelpers.getHttpsTestDBItem())
        }

        val dbBurgersList = repository.getAllBurgersDB().first()
        val remoteBurgersList = repository.getBurgersRemote().first()

        val shuffledList = Utils.getShuffledListOfLists(dbBurgersList,remoteBurgersList)

        assert(shuffledList.all { it is AssetsBurger })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testThatLinksNotStartWithHttpPrefixedWithHttps() = runTest {
        service = TestHelpers.getTestRetrofitApiService(isConnected = true)
        repository = AssetsBurgerRepositoryImpl(dao,service)

        dao.insertBurger(TestHelpers.getWithoutHttpTestDBItem())
        dao.insertBurger(TestHelpers.getHttpTestDBItem())
        dao.insertBurger(TestHelpers.getHttpsTestDBItem())

        val dbBurgersList = repository.getAllBurgersDB().first()
        val remoteBurgersList = repository.getBurgersRemote().first()

        val shuffledList = Utils.getShuffledListOfLists(dbBurgersList,remoteBurgersList)

        assert(shuffledList.all {
            (it is AssetsBurger && it.link.startsWith("http")) ||
                    (it is RemoteBurger && it.link.startsWith("http"))
        })
    }

    @After
    fun tearDown(){
        db.close()
    }
}