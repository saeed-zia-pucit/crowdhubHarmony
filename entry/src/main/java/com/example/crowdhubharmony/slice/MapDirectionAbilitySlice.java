package com.example.crowdhubharmony.slice;

import com.example.crowdhubharmony.ResourceTable;
import com.example.crowdhubharmony.api.NetworkRequestManager;
import com.huawei.hms.maps.harmony.CameraUpdate;
import com.huawei.hms.maps.harmony.CameraUpdateFactory;
import com.huawei.hms.maps.harmony.HuaweiMap;
import com.huawei.hms.maps.harmony.MapView;
import com.huawei.hms.maps.harmony.model.*;
import com.kongzue.dialog.util.Log;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.utils.Color;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MapDirectionAbilitySlice extends AbilitySlice {

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
        super.setUIContent(ResourceTable.Layout_ability_map_direction);
        getDrivingRouteResult();
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
            LatLng latLng1 = new LatLng(40.701464, 16.950620);
            LatLng latLng2 = new LatLng(44.063907, 62.765220);
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
        } catch (Exception e) {
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
            renderRoute(mPaths, mLatLngBounds);
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
}
