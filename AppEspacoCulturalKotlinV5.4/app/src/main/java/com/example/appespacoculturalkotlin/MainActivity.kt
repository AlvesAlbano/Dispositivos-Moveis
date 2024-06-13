package com.example.appespacoculturalkotlin

import android.os.Bundle
import android.content.Intent
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.appespacoculturalkotlin.adapter.AdapterViewPager
import com.example.appespacoculturalkotlin.fragment.FragmentHome
import com.example.appespacoculturalkotlin.fragment.FragmentHour
import com.example.appespacoculturalkotlin.fragment.FragmentView
import com.example.appespacoculturalkotlin.telas_adm.AdmsairActivity
import com.google.android.material.navigation.NavigationView
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : FragmentActivity() {
    private lateinit var pagerMain: ViewPager2
    private val fragmentList = arrayListOf(
        FragmentHome(),
        FragmentView(),
        FragmentHour()
    )

    private lateinit var bottomNav: AnimatedBottomBar
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        pagerMain = findViewById(R.id.pagerMain)
        bottomNav = findViewById(R.id.bottomNav)

        val adapterViewPager = AdapterViewPager(this, fragmentList)
        pagerMain.adapter = adapterViewPager

        bottomNav.setupWithViewPager2(pagerMain)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_admin -> {
                    val intent = if (Adm.admValor) {
                        Intent(this, AdmsairActivity::class.java)
                    } else {
                        Intent(this, AdministradorActivity::class.java)
                    }
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.END) // Mudei para END
                }
                R.id.nav_sobre -> {
                    val intent = Intent(this, SobreActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.END) // Mudei para END
                }
                R.id.nav_sair -> {
                    drawerLayout.closeDrawer(GravityCompat.END) // Mudei para END
                    finishAffinity()
                }
            }
            true
        }

        bottomNav.setOnTabInterceptListener(object : AnimatedBottomBar.OnTabInterceptListener {
            override fun onTabIntercepted(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ): Boolean {
                val tabTitle = newTab.title.toString()
                when (tabTitle) {
                    "HOME", "VISUALIZAR" -> drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    "HORÁRIOS", "MAIS" -> {
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                        if (tabTitle == "MAIS") {
                            openDrawer()
                        }
                    }
                }
                return true
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun openDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.openDrawer(GravityCompat.END) // Mudei para END
        }
    }
}
