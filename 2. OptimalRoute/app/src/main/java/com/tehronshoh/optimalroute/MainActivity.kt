package com.tehronshoh.optimalroute

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.ContextCompat
import com.tehronshoh.optimalroute.databinding.MainViewBinding
import com.tehronshoh.optimalroute.ui.theme.OptimalRouteTheme
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.image.ImageProvider
import kotlin.math.abs

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var mapKitUtil by remember {
                mutableStateOf<MapKitUtil?>(null)
            }


            OptimalRouteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    AndroidViewBinding(factory = MainViewBinding::inflate) {
                        mapKitUtil = MapKitUtil(yandexMapView)
                        composeView.setContent {
                            var listOfPlaces by remember {
                                mutableStateOf(listOf<Point>())
                            }

                            val point = Point(
                                38.57935204500182, 68.79011909252935
                            )

                            var isPointChoosing by remember {
                                mutableStateOf(false)
                            }

                            val mapListener = object : InputListener {
                                override fun onMapTap(p0: com.yandex.mapkit.map.Map, p1: Point) {
                                    Log.d("TAG_MAP", "onMapTap: $point")
                                }

                                override fun onMapLongTap(
                                    map: com.yandex.mapkit.map.Map, point: Point
                                ) {
                                    if (isPointChoosing) {
                                        isPointChoosing = false
                                        listOfPlaces =
                                            listOfPlaces.toMutableList().also { it.add(point) }
                                                .toList()
                                    }
                                }
                            }

                            val listener = MapObjectTapListener { m, p ->
                                listOfPlaces = listOfPlaces.toMutableList().also {
                                    it.removeIf { point ->
                                        (abs(p.latitude - point.latitude) < 0.01) && (abs(p.longitude - point.longitude) < 0.01)
                                    }
                                }.toList()

                                true
                            }

                            yandexMapView.map.addInputListener(mapListener)

                            mapKitUtil?.apply {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Button(
                                        onClick = {
                                            listOfPlaces = listOf()
                                            clear()
                                        }, modifier = Modifier.padding(end = 12.dp)
                                    ) {
                                        Text("R")
                                    }

                                    val modifier = if (isPointChoosing) Modifier
                                        .alpha(0.5f)
                                        .weight(1f) else Modifier.weight(1f)

                                    Button(
                                        onClick = {
                                            isPointChoosing = !isPointChoosing
                                        }, modifier = modifier
                                    ) {
                                        Text("+")
                                    }
                                }
                                addPlaces(
                                    listOfPlaces, ImageProvider.fromBitmap(
                                        getBitmapFromVectorDrawable(R.drawable.baseline_add_location_24)
                                    ), listener
                                )


                                if (listOfPlaces.size > 1) submitRequestForDrawingRoute(
                                    listOfPlaces
                                )
                            }
                            DisposableEffect(Unit) {
                                Log.d("TAG_MAP", "MapScreen: MapKit Started!")

                                mapKitUtil?.apply {
                                    initialize(
                                        context = this@MainActivity,
                                        cameraPosition = CameraPosition(
                                            Point(
                                                point.latitude, point.longitude
                                            ), 8f, 0f, 0f
                                        ),
                                        animation = Animation(Animation.Type.SMOOTH, 0.3f)
                                    )
                                }

                                trafficVisibility.setOnClickListener {
                                    mapKitUtil?.changeTrafficVisibility()
                                }

                                mapKitUtil?.start()
                                onDispose {
                                    mapKitUtil?.stop()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Context.getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
    val drawable = ContextCompat.getDrawable(this, drawableId) ?: return null

    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    ) ?: return null

    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}