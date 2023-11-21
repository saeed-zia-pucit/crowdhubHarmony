package com.example.crowdhubharmony.slice;

import com.example.crowdhubharmony.api.NetworkRequestManager;
import com.huawei.hms.maps.harmony.*;
import com.huawei.hms.maps.harmony.model.*;
import com.kongzue.dialog.util.Log;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.PositionLayout;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.bundle.IBundleManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MapAbilitySlice extends AbilitySlice {
    private static final HiLogLabel HILOG_LABEL = new HiLogLabel(0, 0, "MainAbility");

    private LatLngBounds mLatLngBounds;
    private HuaweiMap mHuaweiMap;
    private MapView mMapView;

    private Marker mMarkerOrigin;

    private Marker mMarkerDestination;
    private List<Polyline> mPolylines = new ArrayList<>();
    private List<List<LatLng>> mPaths = new ArrayList<>();

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        CommonContext.setContext(this);

        // Initialize MapView Object.
        mMapView = new MapView(this);

        // Creating a MapView
        mMapView.onCreate();

        // Obtains the HuaweiMap object.
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(HuaweiMap huaweiMap) {
                mHuaweiMap = huaweiMap;

                // If mHuaweiMap is null, the program stops running.
                if (null == mHuaweiMap) {
                    return;
                }
                requestPermission();

                // CameraUpdate cameraUpdate = buildCameraUpdate();
                //  mHuaweiMap.moveCamera(cameraUpdate);

                // Sets the minimum preferred zoom level. The value ranges from 3 to 20.
                mHuaweiMap.setMinZoomPreference(3);
                // Sets the maximum preferred zoom level. The value ranges from 3 to 20.
                mHuaweiMap.setMaxZoomPreference(14);
                // Reset maximum and minimum zoom levels
                mHuaweiMap.resetMinMaxZoomPreference();
            }
        });

        // Create a layout.
        ComponentContainer.LayoutConfig config = new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT);
        PositionLayout myLayout = new PositionLayout(this);
        myLayout.setLayoutConfig(config);
        ShapeElement element = new ShapeElement();
        element.setShape(ShapeElement.RECTANGLE);
        element.setRgbColor(new RgbColor(255, 255, 255));

        // Load the MapView object.
        myLayout.addComponent(mMapView);
        super.setUIContent(myLayout);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    public void getDrivingRouteResult() {
        removePolylines();
        try {
            LatLng latLng1 = new LatLng(54.216608, -4.66529);
            LatLng latLng2 = new LatLng(54.209673, -4.64002);

        NetworkRequestManager.getDrivingRoutePlanningResult(latLng1, latLng2,
                new NetworkRequestManager.OnNetworkListener() {
                    @Override
                    public void requestSuccess(String result) {
                        generateRoute(result);
                    }

                    @Override
                    public void requestFail(String errorMsg) {
                        errorMsg.toString();
//                        Message msg = Message.obtain();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("errorMsg", errorMsg);
//                        msg.what = 1;
//                        msg.setData(bundle);
//                        mHandler.sendMessage(msg);
                    }
                });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void generateRoute(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray routes = jsonObject.optJSONArray("routes");
            if (null == routes || routes.length() == 0) {
                return;
            }
            JSONObject route = routes.getJSONObject(0);

            // get route bounds
            JSONObject bounds = route.optJSONObject("bounds");
            if (null != bounds && bounds.has("southwest") && bounds.has("northeast")) {
                JSONObject southwest = bounds.optJSONObject("southwest");
                JSONObject northeast = bounds.optJSONObject("northeast");
                LatLng sw = new LatLng(southwest.optDouble("lat"), southwest.optDouble("lng"));
                LatLng ne = new LatLng(northeast.optDouble("lat"), northeast.optDouble("lng"));
                mLatLngBounds = new LatLngBounds(sw, ne);
            }

            // get paths
            JSONArray paths = route.optJSONArray("paths");
            for (int i = 0; i < paths.length(); i++) {
                JSONObject path = paths.optJSONObject(i);
                List<LatLng> mPath = new ArrayList<>();

                JSONArray steps = path.optJSONArray("steps");
                for (int j = 0; j < steps.length(); j++) {
                    JSONObject step = steps.optJSONObject(j);

                    JSONArray polyline = step.optJSONArray("polyline");
                    for (int k = 0; k < polyline.length(); k++) {
                        if (j > 0 && k == 0) {
                            continue;
                        }
                        JSONObject line = polyline.getJSONObject(k);
                        double lat = line.optDouble("lat");
                        double lng = line.optDouble("lng");
                        LatLng latLng = new LatLng(lat, lng);
                        mPath.add(latLng);
                    }
                }
                mPaths.add(i, mPath);
            }
            renderRoute(mPaths,mLatLngBounds);
          //  mHandler.sendEmptyMessage(0);

        } catch (JSONException e) {
            Log.e("TAG", "JSONException" + e.toString());
        }
    }
    private void renderRoute(List<List<LatLng>> paths, LatLngBounds latLngBounds) {
        if (null == paths || paths.size() <= 0 || paths.get(0).size() <= 0) {
            return;
        }

        for (int i = 0; i < paths.size(); i++) {
            List<LatLng> path = paths.get(i);
            PolylineOptions options = new PolylineOptions().color(Color.BLUE.getValue()).width(5);
            for (LatLng latLng : path) {
                options.add(latLng);
            }

            Polyline polyline = mHuaweiMap.addPolyline(options);
            mPolylines.add(i, polyline);
        }

        addOriginMarker(paths.get(0).get(0));
        addDestinationMarker(paths.get(0).get(paths.get(0).size() - 1));

        if (null != latLngBounds) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, 5);
            mHuaweiMap.moveCamera(cameraUpdate);
        } else {
            mHuaweiMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paths.get(0).get(0), 13));
        }

    }
    private void addOriginMarker(LatLng latLng) {
        if (null != mMarkerOrigin) {
            mMarkerOrigin.remove();
        }
        mMarkerOrigin = mHuaweiMap.addMarker(new MarkerOptions().position(latLng)
                .anchorMarker(0.5f, 0.9f)
                // .anchorMarker(0.5f, 0.9f)
                .title("Origin")
                .snippet(latLng.toString()));
    }

    private void addDestinationMarker(LatLng latLng) {
        if (null != mMarkerDestination) {
            mMarkerDestination.remove();
        }
        mMarkerDestination = mHuaweiMap.addMarker(
                new MarkerOptions().position(latLng).anchorMarker(0.5f, 0.9f).title("Destination").snippet(latLng.toString()));
    }

    private void removePolylines() {
        for (Polyline polyline : mPolylines) {
            polyline.remove();
        }

        mPolylines.clear();
        mPaths.clear();
        mLatLngBounds = null;
    }
    private void requestPermission() {
        if (verifySelfPermission("ohos.permission.LOCATION") != IBundleManager.PERMISSION_GRANTED) {
            HiLog.info(HILOG_LABEL, "Permission denied");
            // The application has not been granted the permission.
            if (canRequestPermission("ohos.permission.LOCATION")) {
                // Check whether permission authorization can be implemented via a dialog box (at initial request or when the user has not chosen the option of "don't ask again" after rejecting a previous request).
                requestPermissionsFromUser(
                        new String[]{"ohos.permission.LOCATION"}, 1);
            } else {
                // Display the reason why the application requests the permission and prompt the user to grant the permission.
                HiLog.info(HILOG_LABEL, "permission denied");
            }
        } else {

            getDrivingRouteResult();

        }

    }
}
