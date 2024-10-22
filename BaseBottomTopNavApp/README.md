## Checklist para implementar o componente navigation do jetpack em um aplicativo Android utilizando, BottomNavigation e TopAppBar com layout XML

## Checklist

1. Adicionar Dependências Necessárias
2. Criar os fragments referentes a navegação do BottomNavigation
3. Criar um NavGraph para cada fragment e já configurar a string do Label.
4. Criar o NavGraph dos NavGraph de cada Fragment
5. Criar o arquivo de menu
6. Criar a Interface com FragmentContainerView e BottomNavigationView
7. Configurar a navegação na Activity
8. Configurar o TopAppBar
---

## Detalhes
### Criar um NavGraph para cada fragment e já configurar a string do Label.
Exemplo `res/navigation/home.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/home"
    app:startDestination="@id/homeFragment">

    <fragment
        android:id="@+id/homeFragment"
        android:name="dev.andrericardo.movieseriesbase.presentation.home.HomeFragment"
        android:label="@string/title_screen_home"
        tools:layout="@layout/fragment_home" />
</navigation>
```

### Criar o NavGraph dos NavGraph de cada Fragment
Exemplo `res/navigation/main_nav.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/main_nav"
    app:startDestination="@id/home">

    <include app:graph="@navigation/home" />
    <include app:graph="@navigation/search" />
    <include app:graph="@navigation/favorites" />
    <include app:graph="@navigation/profile" />
</navigation>
```


### Criar o arquivo de menu
Exemplo `res/menu/bottom_navigation_menu.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/home"
        android:icon="@drawable/ic_home"
        android:title="@string/title_screen_home" />

    <item
        android:id="@+id/screen_b"
        android:icon="@drawable/ic_star"
        android:title="@string/title_screen_b" />

    <item
        android:id="@+id/screen_c"
        android:icon="@drawable/ic_settings"
        android:title="@string/title_screen_c" />
</menu>
```

### 3. Criar a Interface com AppBarLayout, FragmentContainerView e BottomNavigationView
Exemplo `res/menu/bottom_navigation_menu.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <com.google.android.material.appbar.AppBarLayout
        android:id="@+id/top_appbar_layout_main"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <com.google.android.material.appbar.MaterialToolbar
            android:id="@+id/top_appbar_main"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:minHeight="?attr/actionBarSize" />

    </com.google.android.material.appbar.AppBarLayout>

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host_container"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:defaultNavHost="true"
        app:layout_constraintBottom_toTopOf="@id/bottom_nav_main"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/top_appbar_layout_main"
        app:navGraph="@navigation/nav_main" />

    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottom_nav_main"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:menu="@menu/bottom_navigation_menu" />

</androidx.constraintlayout.widget.ConstraintLayout>
```


### Configurar a navegação na Activity
Exemplo `MainActivity.kt`

```kt
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

}

```

### Configurar a TopAppBar
Exemplo `MainActivity.kt`

```kt
class MainActivity : AppCompatActivity() {
    //...
    override fun onCreate(savedInstanceState: Bundle?) {
        //...
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
```
