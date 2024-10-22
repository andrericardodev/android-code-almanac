package dev.andrericardo.base_layout_xml_navigation_bottomnavigation_topappbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dev.andrericardo.base_layout_xml_navigation_bottomnavigation_topappbar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Gerencia a navegação do app e as transições entre fragments
    private lateinit var navController: NavController

    // Configura o comportamento da barra superior (TopAppBar) em resposta à navegação
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recupera o NavHostFragment responsável por hospedar e gerenciar a navegação entre fragments
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment

        // Inicializa o NavController a partir do NavHostFragment
        navController = navHostFragment.navController

        // Conecta o BottomNavigationView ao NavController para controlar a navegação entre fragments
        binding.bottomNavMain.setupWithNavController(navController)

        // Define os destinos de nível superior (aqueles que não exibem o botão de "voltar")
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.screenBFragment, R.id.screenCFragment)
        )

        // Configura a TopAppBar para funcionar com o NavController e o AppBarConfiguration
        binding.topAppbarMain.setupWithNavController(navController, appBarConfiguration)

        // Adiciona um listener para mudar o ícone da TopAppBar dependendo do destino atual
        navController.addOnDestinationChangedListener { _, destination, _ ->

            // Verifica se o destino atual é um destino de nível superior
            val isTopLevelDestination =
                appBarConfiguration.topLevelDestinations.contains(destination.id)

            // Se não for um destino de nível superior, exibe o ícone de "voltar"
            if (!isTopLevelDestination) binding.topAppbarMain.setNavigationIcon(R.drawable.ic_arrow_back)
        }

    }
}
