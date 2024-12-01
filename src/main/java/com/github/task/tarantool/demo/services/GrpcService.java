package com.github.task.tarantool.demo.services;


import com.github.task.tarantool.demo.BasicObject;
import com.github.task.tarantool.demo.BasicServiceGrpc;
import com.github.task.tarantool.demo.DeleteObjectRequest;
import com.github.task.tarantool.demo.GetObjectRequest;
import com.github.task.tarantool.demo.GetObjectResponse;
import com.github.task.tarantool.demo.GetRangeObjectsRequest;
import com.github.task.tarantool.demo.ObjectCountResponse;
import com.github.task.tarantool.demo.PutObjectRequest;
import com.github.task.tarantool.demo.exceptions.NotFoundException;
import com.github.task.tarantool.demo.model.rest.KVObject;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

@net.devh.boot.grpc.server.service.GrpcService
class GrpcService extends BasicServiceGrpc.BasicServiceImplBase {

    private final TarantoolStorageService storageService;

    public GrpcService(TarantoolStorageService storageService) {
        this.storageService = storageService;
    }

    private static final Logger logger = Logger.getLogger(GrpcService.class.getName());

    @Override
    public void putObject(PutObjectRequest request, StreamObserver<BasicObject> responseObserver) {
        KVObject object = storageService.getKVObjectByKey(request.getKey());
        KVObject created;
        if (object != null) {
            created = storageService.updateKVObject(request.getKey(), request.getValue());
        } else {
            created = storageService.createKVObject(request.getKey(), request.getValue());
        }
        responseObserver.onNext(BasicObject.newBuilder().setKey(created.getKey()).setValue(created.getValue()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getObject(GetObjectRequest request, StreamObserver<GetObjectResponse> responseObserver) {
        KVObject object = storageService.getKVObjectByKey(request.getKey());
        if (object==null) {
            responseObserver.onNext(GetObjectResponse.newBuilder().setValue(object.getValue()).build());
            responseObserver.onCompleted();
        } else {
            throw new NotFoundException("Object with key: "+request.getKey()+" not found");
        }

    }

    @Override
    public void deleteObject(DeleteObjectRequest request, StreamObserver<Empty> responseObserver) {
        boolean updated = storageService.deleteKVObject(request.getKey());
        if (updated) {
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } else {
            throw new NotFoundException("Object with key: "+request.getKey()+" not found");
        }

    }

    @Override
    public void getRange(GetRangeObjectsRequest request, StreamObserver<BasicObject> responseObserver) {
        //not implemented
        responseObserver.onNext(BasicObject.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void getCount(Empty request, StreamObserver<ObjectCountResponse> responseObserver) {
        Integer count = storageService.getObjectsCount();
        responseObserver.onNext(ObjectCountResponse.newBuilder().setCount(count).build());
        responseObserver.onCompleted();
    }
}
