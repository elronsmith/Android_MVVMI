package ru.elron.libdb

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.elron.libdb.cache.CacheDao
import ru.elron.libdb.cache.CacheEntity
import ru.elron.libdb.favorite.FavoriteDao
import ru.elron.libdb.favorite.FavoriteEntity

@RunWith(AndroidJUnit4::class)
class WeatherDatabaseTest {
    lateinit var database: WeatherDatabase
    lateinit var favoriteDao: FavoriteDao
    lateinit var cacheDao: CacheDao

    @Before
    fun beforeTest() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = WeatherDatabase.getInstance(appContext)
        favoriteDao = database.favoriteDao()
        favoriteDao.deleteAll()
        cacheDao = database.cacheDao()
        cacheDao.deleteAll()
    }

    @Test
    fun getList_isCorrect() {
        var list = favoriteDao.getList()
        Assert.assertNotNull(list)
        Assert.assertEquals(0, list.size)

        val test_1 = FavoriteEntity(city = "city_1", date = "1", temperature = "1", data = "")
        val test_2 = FavoriteEntity(city = "city_17", date = "2", temperature = "3", data = "")

        favoriteDao.setCity(test_1)
        favoriteDao.setCity(test_2)

        Assert.assertEquals(2, favoriteDao.getSize())
        list = favoriteDao.getList()
        Assert.assertNotNull(list)
        Assert.assertEquals(2, list.size)

        with(list[0]) {
            Assert.assertEquals(test_1.city, city)
            Assert.assertEquals(test_1.date, date)
            Assert.assertEquals(test_1.temperature, temperature)
        }
        with(list[1]) {
            Assert.assertEquals(test_2.city, city)
            Assert.assertEquals(test_2.date, date)
            Assert.assertEquals(test_2.temperature, temperature)
        }
    }

    @Test
    fun getCityOrNull_isCorrect() {
        val id = 1L

        val result = favoriteDao.getCityOrNull(id)
        Assert.assertNull(result)

        val test_1 = FavoriteEntity(
            id = id,
            city = "city_1",
            date = "1",
            temperature = "1",
            data = ""
        )
        favoriteDao.setCity(test_1)

        val result_2 = favoriteDao.getCityOrNull(id)
        Assert.assertNotNull(result_2)
        Assert.assertEquals(test_1.city, result_2!!.city)
        Assert.assertEquals(test_1.date, result_2.date)
        Assert.assertEquals(test_1.temperature, result_2.temperature)
    }

    @Test
    fun getCountByCityId_isCorrect() {
        val id = 1L
        var count = favoriteDao.getCountByCityId(id)
        Assert.assertEquals(0, count)

        val test_1 = FavoriteEntity(
            id = id,
            city = "city_1",
            date = "1",
            temperature = "1",
            data = ""
        )
        favoriteDao.setCity(test_1)

        count = favoriteDao.getCountByCityId(id)
        Assert.assertEquals(1, count)
    }

    @Test
    fun getOldCityList_isCorrect() {
        var list = cacheDao.getList()
        Assert.assertNotNull(list)
        Assert.assertEquals(0, list.size)

        val test_1 = CacheEntity(
            id = 1,
            city = "city_1",
            date = "1",
            dt = 1000L,
            temperature = "1",
            data = ""
        )
        cacheDao.setCity(test_1)

        list = cacheDao.getOldCityList(0, 100)
        Assert.assertNotNull(list)
        Assert.assertEquals(0, list.size)

        list = cacheDao.getOldCityList(500, 1500)
        Assert.assertNotNull(list)
        Assert.assertEquals(1, list.size)

        list = cacheDao.getOldCityList(1500, 2000)
        Assert.assertNotNull(list)
        Assert.assertEquals(0, list.size)
    }

    @Test
    fun getDateList_isCorrect() {
        val list = cacheDao.getList()
        Assert.assertNotNull(list)
        Assert.assertEquals(0, list.size)

        val test_1 = CacheEntity(
            id = 1,
            city = "city_1",
            date = "1",
            dt = 1000L,
            temperature = "1",
            data = ""
        )
        cacheDao.setCity(test_1)

        val dateList = cacheDao.getDateList()
        Assert.assertNotNull(dateList)
        Assert.assertEquals(1, dateList.size)
        Assert.assertEquals(test_1.dt, dateList[0])
    }

    @Test
    fun getCityByDtOrNull_isCorrect() {
        val list = cacheDao.getList()
        Assert.assertNotNull(list)
        Assert.assertEquals(0, list.size)

        val test_1 = CacheEntity(
            id = 1,
            city = "city_1",
            date = "1",
            dt = 1000L,
            temperature = "1",
            data = ""
        )
        cacheDao.setCity(test_1)

        val test_2 = cacheDao.getCityByDtOrNull(test_1.dt)
        Assert.assertNotNull(test_2)
        Assert.assertEquals(test_1.dt, test_2!!.dt)
    }

    @Test
    fun getCityByNameOrNull_isCorrect() {
        val list = cacheDao.getList()
        Assert.assertNotNull(list)
        Assert.assertEquals(0, list.size)

        val test_1 = CacheEntity(
            id = 1,
            city = "city_1",
            date = "1",
            dt = 1000L,
            temperature = "1",
            data = ""
        )
        cacheDao.setCity(test_1)

        val test_2 = cacheDao.getCityByNameOrNull(test_1.city)
        Assert.assertNotNull(test_2)
        Assert.assertEquals(test_1.city, test_2!!.city)
    }
}
