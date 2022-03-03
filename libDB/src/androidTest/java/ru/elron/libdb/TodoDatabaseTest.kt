package ru.elron.libdb

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoDatabaseTest {
    lateinit var database: TodoDatabase
    lateinit var dao: TodoDao

    @Before
    fun beforeTest() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = TodoDatabase.getInstance(appContext)
        dao = database.todoDao()
        dao.deleteAll()
    }

    @Test
    fun getList_isCorrect() {
        var list = dao.getList()
        Assert.assertNotNull(list)
        Assert.assertEquals(0, list.size)

        val test_1 = TodoEntity(text = "text1", checked = true)
        val test_2 = TodoEntity(text = "text2", checked = true)

        dao.setTodo(test_1)
        dao.setTodo(test_2)

        Assert.assertEquals(2, dao.getSize())
        list = dao.getList()
        Assert.assertNotNull(list)
        Assert.assertEquals(2, list.size)

        with(list[0]) {
            Assert.assertEquals(test_1.text, text)
            Assert.assertEquals(test_1.checked, checked)
        }
        with(list[1]) {
            Assert.assertEquals(test_2.text, text)
            Assert.assertEquals(test_2.checked, checked)
        }
    }

    @Test
    fun getTodo_isCorrect() {
        val id = 1L

        val result = dao.getTodoOrNull(id)
        Assert.assertNull(result)

        val test_1 = TodoEntity(id = id, text = "text1", checked = true)
        dao.setTodo(test_1)

        val result_2 = dao.getTodoOrNull(id)
        Assert.assertNotNull(result_2)
        Assert.assertEquals(test_1.text, result_2!!.text)
        Assert.assertEquals(test_1.checked, result_2.checked)
    }

    @Test
    fun setTextById_isCorrect() {
        val list = dao.getList()
        Assert.assertNotNull(list)
        Assert.assertEquals(0, list.size)

        val id = 1L
        val text = "text_1"
        val test_1 = TodoEntity(id = id, text = text, checked = true)
        dao.setTodo(test_1)

        val test_2 = dao.getTodoOrNull(id)
        Assert.assertNotNull(test_2)
        Assert.assertEquals(test_1.text, test_2?.text)

        val text_2 = "text_2"
        dao.setTextById(test_2!!.id, text_2)

        val test_3 = dao.getTodoOrNull(test_2.id)
        Assert.assertNotNull(test_3)
        Assert.assertEquals(text_2, test_3?.text)
    }

    @Test
    fun delete_isCorrect() {
        val id = 1L
        val test_1 = TodoEntity(id = id, text = "text1", checked = true)
        dao.delete(test_1)
        dao.setTodo(test_1)

        val result = dao.getTodoOrNull(id)
        Assert.assertNotNull(result)

        dao.delete(result!!)
        val result_2 = dao.getTodoOrNull(id)
        Assert.assertNull(result_2)
    }

    @Test
    fun delete_by_id_isCorrect() {
        val id = 1L
        val test_1 = TodoEntity(id = id, text = "text1", checked = true)
        dao.delete(id)

        dao.setTodo(test_1)
        val result = dao.getTodoOrNull(id)
        Assert.assertNotNull(result)

        dao.delete(result!!.id)
        val result_2 = dao.getTodoOrNull(id)
        Assert.assertNull(result_2)
    }

    @Test
    fun deleteAll_isCorrect() {
        var size = dao.getSize()
        Assert.assertEquals(0, size)

        val id = 1L
        val test_1 = TodoEntity(id = id, text = "text1", checked = true)
        dao.setTodo(test_1)

        size = dao.getSize()
        Assert.assertEquals(1, size)

        dao.deleteAll()

        size = dao.getSize()
        Assert.assertEquals(0, size)
    }
}
