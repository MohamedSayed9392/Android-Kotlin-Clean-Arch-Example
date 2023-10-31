package com.example.burgers

import android.app.Application
import com.example.burgers.data.datasource.dao.AssetsBurgersDao
import com.example.burgers.data.datasource.db.AssetsBurgersDatabase
import com.example.burgers.helpers.TestHelpers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"], application = Application::class)
class AssetsBurgersDaoTest {
    private lateinit var db: AssetsBurgersDatabase
    private lateinit var dao: AssetsBurgersDao

    @Before
    fun init() {
        db = TestHelpers.getTestDB()
        dao = db.burgersDao
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testQueryReturnItemsWithHttpsLinksOnly() = runTest {

        val httpTestItem = TestHelpers.getHttpTestDBItem()
        val httpsTestItem = TestHelpers.getHttpsTestDBItem()

        dao.insertBurger(httpTestItem)
        dao.insertBurger(httpsTestItem)

        var allItemsWithHttpsLinks = true
        dao.getAllBurgers().first().forEach { burger ->
            if (!burger.link.startsWith("https")) {
                allItemsWithHttpsLinks = false
            }
        }

        assertTrue(allItemsWithHttpsLinks)
    }

    @After
    fun tearDown(){
        db.close()
    }
}