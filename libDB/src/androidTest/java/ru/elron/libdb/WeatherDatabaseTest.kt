package ru.elron.libdb

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.elron.libdb.favorite.FavoriteDao
import ru.elron.libdb.favorite.FavoriteEntity

@RunWith(AndroidJUnit4::class)
class WeatherDatabaseTest {
    lateinit var database: WeatherDatabase
    lateinit var dao: FavoriteDao

    @Before
    fun beforeTest() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = WeatherDatabase.getInstance(appContext)
        dao = database.favoriteDao()
        dao.deleteAll()
    }

    @Test
    fun getList_isCorrect() {
        var list = dao.getList()
        Assert.assertNotNull(list)
        Assert.assertEquals(0, list.size)

        val test_1 = FavoriteEntity(city = "city_1", date = "1", temperature = "1", data = "")
        val test_2 = FavoriteEntity(city = "city_17", date = "2", temperature = "3", data = "")

        dao.setCity(test_1)
        dao.setCity(test_2)

        Assert.assertEquals(2, dao.getSize())
        list = dao.getList()
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

        val result = dao.getCityOrNull(id)
        Assert.assertNull(result)

        val test_1 = FavoriteEntity(
            id = id,
            city = "city_1",
            date = "1",
            temperature = "1",
            data = ""
        )
        dao.setCity(test_1)

        val result_2 = dao.getCityOrNull(id)
        Assert.assertNotNull(result_2)
        Assert.assertEquals(test_1.city, result_2!!.city)
        Assert.assertEquals(test_1.date, result_2.date)
        Assert.assertEquals(test_1.temperature, result_2.temperature)
    }

    @Test
    fun getCountByCityId_isCorrect() {
        val id = 1L
        var count = dao.getCountByCityId(id)
        Assert.assertEquals(0, count)

        val test_1 = FavoriteEntity(
            id = id,
            city = "city_1",
            date = "1",
            temperature = "1",
            data = ""
        )
        dao.setCity(test_1)

        count = dao.getCountByCityId(id)
        Assert.assertEquals(1, count)
    }
}
