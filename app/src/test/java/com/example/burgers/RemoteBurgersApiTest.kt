package com.example.burgers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.burgers.data.datasource.api.BurgersApi
import com.example.burgers.helpers.TestHelpers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RemoteBurgersApiTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: BurgersApi

    @Before
    fun init() {
        service = TestHelpers.getTestRetrofitApiService()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testLinksIsRandomImageLink() = runTest {
        val remoteBurgersList = service.getBurgers()

        var allLinksIsRandomImageLink = true
        remoteBurgersList.body()?.forEach { burger ->
            allLinksIsRandomImageLink = burger.link.isNotBlank() && burger.images.any {
                it.lg == burger.link || it.sm == burger.link
            }
        }

        assert(allLinksIsRandomImageLink)
    }
}