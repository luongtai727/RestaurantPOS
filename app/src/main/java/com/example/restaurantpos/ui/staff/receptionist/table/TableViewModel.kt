package com.example.restaurantpos.ui.staff.receptionist.table

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.restaurantpos.db.entity.AccountEntity
import com.example.restaurantpos.db.entity.TableEntity
import com.example.restaurantpos.db.roomdb.PosRoomDatabase
import com.example.restaurantpos.util.DatabaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TableViewModel: ViewModel() {

    fun getAllTable() = DatabaseUtil.getAllTable()


    fun addTable(context: Context, table: TableEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            PosRoomDatabase.getInstance(context).tableDAO().addTable(table)
        }
    }
}