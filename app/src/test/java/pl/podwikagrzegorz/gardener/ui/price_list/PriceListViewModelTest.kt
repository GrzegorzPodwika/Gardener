package pl.podwikagrzegorz.gardener.ui.price_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.hamcrest.Matchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import pl.podwikagrzegorz.gardener.getOrAwaitValue

class PriceListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

/*    @Test
    fun addNewNote_sets() {
        val priceListViewModel = mock(PriceListViewModel::class.java)
        val previousSizeOfList = priceListViewModel.priceList.value?.size ?: 0

        priceListViewModel.addNote(NoteRealm())

        val value = priceListViewModel.priceList.getOrAwaitValue()
        val latestSize = value.size
        assertEquals((previousSizeOfList + 1), `is`(latestSize))

    }*/
}