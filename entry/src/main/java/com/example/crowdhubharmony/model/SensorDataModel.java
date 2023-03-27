package com.example.crowdhubharmony.model;


import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public final class SensorDataModel {
    private final int itemImg;
    @NotNull
    private final String itemValue;

    public final int getItemImg() {
        return this.itemImg;
    }

    @NotNull
    public final String getItemValue() {
        return this.itemValue;
    }

    public SensorDataModel(int itemImg, @NotNull String itemValue) {
        super();
        this.itemImg = itemImg;
        this.itemValue = itemValue;
    }

    @NotNull
    public final SensorDataModel copy(int itemImg, @NotNull String itemValue) {
        Intrinsics.checkNotNullParameter(itemValue, "itemValue");
        return new SensorDataModel(itemImg, itemValue);
    }

}

