package io.madcamp.treasurehunterar.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import io.madcamp.treasurehunterar.R

@Composable
fun MapScreen(
    makerNum: Int = 0
) {
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = true,
                zoomControlsEnabled = true,
            )
        )
    }
    var properties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL
            )
        )
    }
    val singapore = LatLng(1.35, 103.87)
    val newyork = LatLng(40.7, -74.0)
    val paris = LatLng(48.9, 2.4)
    val tokyo = LatLng(35.7, 139.7)
    val seoul = LatLng(37.6, 127.0)
    val kairo = LatLng(30.0, 31.2)
    val london = LatLng(51.5, 0.1)
    val buenosaires = LatLng(-34.6, -58.4)
    val chicago = LatLng(41.9, -87.6)
    val beijing = LatLng(39.9, 116.4)
    val los = LatLng(34.1, -118.2)



    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(kaistMarkerList[makerNum].position, 17f)
    }
    Box(Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.matchParentSize(),
            properties = properties,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState
        ) {
            kaistMarkerList.forEach { mapMarker ->
                Marker(
                    state = MarkerState(mapMarker.position),
                    title = mapMarker.title,
                    snippet = mapMarker.snippet
                )
            }
            Marker(
                state = MarkerState(position = singapore),
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
            Marker(
                state = MarkerState(position = newyork),
                title = "Newyork",
                snippet = "Marker in Newyork"
            )
            Marker(
                state = MarkerState(position = tokyo),
                title = "Tokyo",
                snippet = "Marker in Tokyo"
            )
            Marker(
                state = MarkerState(position = seoul),
                title = "Seoul",
                snippet = "Marker in Seoul"
            )
            Marker(
                state = MarkerState(position = kairo),
                title = "Kairo",
                snippet = "Marker in Kairo"
            )
            Marker(
                state = MarkerState(position = paris),
                title = "Paris",
                snippet = "Marker in Paris"
            )
            Marker(
                state = MarkerState(position = london),
                title = "London",
                snippet = "Marker in London"
            )
            Marker(
                state = MarkerState(position = buenosaires),
                title = "Buenos Aires",
                snippet = "Marker in Buenos Aires"
            )
            Marker(
                state = MarkerState(position = chicago),
                title = "Chicago",
                snippet = "Marker in Chicago"
            )
            Marker(
                state = MarkerState(position = beijing),
                title = "Beijing",
                snippet = "Marker in Beijing"
            )
            Marker(
                state = MarkerState(position = los),
                title = "Los Angeles",
                snippet = "Marker in Los Angeles"
            )
            /*if (showKaist) {
                Marker(
                    state = MarkerState(position = kaist),
                    title = "Kaist",
                    snippet = "Marker in Kaist"
                )
            }*/
        }
        FeatureThatRequiresLocationPermission()
        Switch(
            checked = properties.isMyLocationEnabled,
            onCheckedChange = {
                properties = properties.copy(isMyLocationEnabled = it)
            }
        )

    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun FeatureThatRequiresLocationPermission() {

    // Location permission state
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    if (locationPermissionState.status.isGranted) {
        Text("Location permission Granted")
    } else {
        Column {
            val textToShow = if (locationPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "The Location is important for this app. Please grant the permission."
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Location permission required for this feature to be available. " +
                        "Please grant the permission"
            }
            Text(textToShow)
            Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                Text("Request permission")
            }
        }
    }
}

@Preview
@Composable
fun MapScreenPreview() {
    MapScreen()
}

data class MapMarker(
    val position : LatLng,
    val title : String?,
    val snippet : String?,
    val imageResourceId: Int
    )

val kaist = LatLng(36.372218, 127.360436)
val kaist_n1 = LatLng(36.374180, 127.365830)
val daejeon_currency_museum = LatLng(36.377877, 127.370617)
val kaist_kaimaru = LatLng(36.373993, 127.359240)

val kaistMarkerList : List<MapMarker> = listOf(
    MapMarker(
        position = kaist,
        title = "KAIST 본관",
        snippet = "본관 화분 근처에 있을지도??",
        imageResourceId = R.drawable.daejeon
    ),
    MapMarker(
        position = kaist_n1,
        title = "KAIST N1",
        snippet = "N1 1층 14호에서 초록색을 찾아라!!",
        imageResourceId = R.drawable.duck
    ),
    MapMarker(
        position = kaist_kaimaru,
        title = "카이마루",
        snippet = "카이마루 유일의 중국집인 이곳은?",
        imageResourceId = R.drawable.dream
    ),
    MapMarker(
        position = daejeon_currency_museum,
        title = "Museum",
        snippet = null,
        imageResourceId = R.drawable.android
    )
)