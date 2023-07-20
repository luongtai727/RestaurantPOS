package com.example.restaurantpos.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.restaurantpos.R
import com.example.restaurantpos.databinding.ActivityMainReceptionistBinding

class MainReceptionistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainReceptionistBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainReceptionistBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /** Navigation */

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_receptionist) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_main_staff)

        // Use bundle from Login to separate
        if (intent.getIntExtra("NavigateByRole", 1) == 1) {
            graph.setStartDestination(R.id.tableFragment)
        } else {
            graph.setStartDestination(R.id.kitchenFragment)
        }

        /*        if (isTrue){
                    graph.startDestinationId = R.id.kitchenFragment
                }else {
                    graph.startDestination = R.id.OtherDetailsFragment
                }*/

        val navController = navHostFragment.navController
        navController.setGraph(graph, intent.extras)


        /*        val navHostFragment = supportFragmentManager
                    .findFragmentById(R.id.nav_host_receptionist) as NavHostFragment
                navController = navHostFragment.navController*/

        /*
                val navGraph = navController.graph
                navGraph.setStartDestination(R.id.kitchenFragment)
                navController.graph = navGraph*/
    }


    /*
        // Khoi tao Menu
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.sub_menu, menu)
            */
    /*
            menu 1: Directory containing menu.xml
            menu 2: menu.xml <== Chi can thay doi nay de update SUB-MENU
            menu 3: Parameter from onCreateOptionsMenu(...)
             *//*

        return true
    }

    // Xy ly su kien <== item in Menu. Use when() duyet all items in menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuExit -> finish()
            R.id.menuHome -> Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
            R.id.menuSearch -> Toast.makeText(this, "Search", Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }
*/

}