package com.example.bookingservice

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookingservice.data.api.AuthApi
import com.example.bookingservice.data.api.BookingApi
import com.example.bookingservice.data.api.BuildingApi
import com.example.bookingservice.data.api.RoomApi
import com.example.bookingservice.data.repository.AuthRepository
import com.example.bookingservice.data.repository.BookingRepository
import com.example.bookingservice.data.repository.BuildingRepository
import com.example.bookingservice.data.repository.RoomRepository
import com.example.bookingservice.ui.booking.BookingListScreen
import com.example.bookingservice.ui.booking.CreateBookingScreen
import com.example.bookingservice.ui.building.BuildingListScreen
import com.example.bookingservice.ui.login.LoginScreen
import com.example.bookingservice.ui.registration.RegistrationScreen
import com.example.bookingservice.ui.room.RoomListScreen
import com.example.bookingservice.ui.theme.BookingServiceTheme
import com.example.bookingservice.viewmodel.AuthViewModel
import com.example.bookingservice.viewmodel.BookingViewModel
import com.example.bookingservice.viewmodel.BuildingViewModel
import com.example.bookingservice.viewmodel.RoomViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Настройка OkHttpClient с интерсептором для JWT-токена
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val token = getSharedPreferences("auth", Context.MODE_PRIVATE)
                    .getString("jwt_token", null)
                val request = chain.request().newBuilder()
                    .apply {
                        if (token != null) {
                            addHeader("Authorization", "Bearer $token")
                        }
                    }
                    .build()
                chain.proceed(request)
            }
            .build()

        // Настройка Retrofit для локального бэкенда
        val retrofit = Retrofit.Builder()
            .baseUrl("http://158.160.137.7:8080/api/") // Локальный URL для эмулятора
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Создание API-сервисов
        val authApi = retrofit.create(AuthApi::class.java)
        val bookingApi = retrofit.create(BookingApi::class.java)
        val buildingApi = retrofit.create(BuildingApi::class.java)
        val roomApi = retrofit.create(RoomApi::class.java)

        // Создание репозиториев
        val authRepository = AuthRepository(authApi, this)
        val bookingRepository = BookingRepository(bookingApi)
        val buildingRepository = BuildingRepository(buildingApi)
        val roomRepository = RoomRepository(roomApi)

        setContent {
            BookingServiceTheme {
                AppNavigation(
                    authRepository = authRepository,
                    bookingRepository = bookingRepository,
                    buildingRepository = buildingRepository,
                    roomRepository = roomRepository
                )
            }
        }
    }
}

@Composable
fun AppNavigation(
    authRepository: AuthRepository,
    bookingRepository: BookingRepository,
    buildingRepository: BuildingRepository,
    roomRepository: RoomRepository
) {
    val navController = rememberNavController()
    val authViewModel = AuthViewModel(authRepository)

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { userId -> navController.navigate("bookings/$userId") },
                onRegisterClick = { navController.navigate("registration") }
            )
        }
        composable("registration") {
            RegistrationScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { navController.popBackStack() },
                onBackToLoginClick = { navController.popBackStack() }
            )
        }
        composable("buildings") {
            BuildingListScreen(
                viewModel = BuildingViewModel(buildingRepository),
                onBuildingClick = { buildingId -> navController.navigate("rooms/$buildingId") }
            )
        }
        composable("rooms/{buildingId}") { backStackEntry ->
            val buildingId = backStackEntry.arguments?.getString("buildingId") ?: ""
            RoomListScreen(
                buildingId = buildingId,
                viewModel = RoomViewModel(roomRepository),
                onRoomClick = { roomId -> navController.navigate("createBooking/$roomId") }
            )
        }
        composable("bookings/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            BookingListScreen(
                userId = userId,
                viewModel = BookingViewModel(bookingRepository),
                authViewModel = authViewModel,
                onCreateBookingClick = { navController.navigate("buildings") },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("createBooking/{roomId}") { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
            CreateBookingScreen(
                roomId = roomId,
                viewModel = BookingViewModel(bookingRepository),
                onBookingSuccess = { navController.popBackStack() }
            )
        }
    }
}