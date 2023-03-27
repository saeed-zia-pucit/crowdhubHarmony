package com.example.crowdhubharmony.model;
public final class UpdateStatusRequestModel {
 private UpdateStatusModel updateStatusModel;

    public UpdateStatusRequestModel(UpdateStatusModel updateStatusModel) {
        super();
        this.updateStatusModel=updateStatusModel;
    }

    public UpdateStatusModel getUpdateStatusModel() {
        return updateStatusModel;
    }

    public void setUpdateStatusModel(UpdateStatusModel updateStatusModel) {
        this.updateStatusModel = updateStatusModel;
    }
}

